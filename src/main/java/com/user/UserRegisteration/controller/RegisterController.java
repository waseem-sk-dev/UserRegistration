package com.user.UserRegisteration.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.user.UserRegisteration.dto.PasswordResetDTO;
import com.user.UserRegisteration.entity.RawPassword;
import com.user.UserRegisteration.entity.UserRegistration;
import com.user.UserRegisteration.service.LoanService;
import com.user.UserRegisteration.service.RegisterService;

@Controller
public class RegisterController {


	   @Autowired
	    private RegisterService service;
	   @Autowired
	    private LoanService loanService;
    

    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistration());
        return "register";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/register")
    public String handleUserRegistration(
            @Validated(RawPassword.class) @ModelAttribute UserRegistration user,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) return "register";

        try {
            if (service.getUserByEmail(user.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("error", "Email is already registered. Please log in.");
                return "redirect:/register";
            }

            String resultMsg = service.registerUser(user);
            if (resultMsg.contains("success")) {
                service.sendWelcomeEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName());
                redirectAttributes.addFlashAttribute("promptMessage", "Registration successful! Please log in.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("promptMessage", "An error occurred during registration.");
                return "redirect:/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("promptMessage", "Error during registration.");
            return "redirect:/register";
        }
    }

    @PostMapping("/perform-login")
    public String loginUser(@RequestParam String usernameOrEmail,
                            @RequestParam String rawPassword,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        UserRegistration user = service.getUserByEmailOrUsername(usernameOrEmail);
        if (user == null) {
            redirectAttributes.addFlashAttribute("promptMessage", "User not found.");
            return "redirect:/login";
        }

        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && "INACTIVE".equalsIgnoreCase(user.getStatus().name())) {
            redirectAttributes.addFlashAttribute("promptMessage", "Your account is locked. Contact support.");
            return "redirect:/login";
        }

        if (service.validatePassword(rawPassword, user.getPassword())) {
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                service.resetFailedAttempts(user.getEmail());
            }

            session.setAttribute("loggedInUser", user);
            session.setAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            session.setAttribute("role", user.getRole());

            redirectAttributes.addFlashAttribute("promptMessage", "Login successful. Welcome " + user.getFirstName());
            return "ADMIN".equalsIgnoreCase(user.getRole()) ? "redirect:/admin/dashboard" : "redirect:/user-dashboard";
        } else {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                redirectAttributes.addFlashAttribute("promptMessage", "Invalid credentials.");
            } else {
                int attemptsLeft = service.increaseFailedAttempts(user.getEmail());
                if (attemptsLeft <= 0) {
                    service.lockUser(user.getEmail());
                    redirectAttributes.addFlashAttribute("promptMessage", "Account locked after 3 failed attempts.");
                } else {
                    redirectAttributes.addFlashAttribute("promptMessage", "Invalid credentials. Attempts left: " + attemptsLeft);
                }
            }
            return "redirect:/login";
        }
    }

 
    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                         RedirectAttributes redirectAttributes) {
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        redirectAttributes.addFlashAttribute("promptMessage", "Logged out successfully.");
        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String emailOrUsername, Model model) {
        UserRegistration user = service.getUserByEmailOrUsername(emailOrUsername);
        if (user != null) {
            service.generateResetToken(user);
            model.addAttribute("message", "Reset link has been sent (check console).");
        } else {
            model.addAttribute("error", "User not found.");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
        if (!service.isTokenValid(token)) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/login";
        }
        model.addAttribute("token", token);
        model.addAttribute("passwordResetDTO", new PasswordResetDTO());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handlePasswordReset(@ModelAttribute PasswordResetDTO dto, Model model) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", dto.getToken());
            return "reset-password";
        }

        if (!service.isValidPassword(dto.getNewPassword())) {
            model.addAttribute("error", "Password does not meet security requirements.");
            model.addAttribute("token", dto.getToken());
            return "reset-password";
        }

        UserRegistration user = service.getUserByResetToken(dto.getToken());
        if (user == null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "login";
        }

        service.updatePassword(user, dto.getNewPassword());
        model.addAttribute("promptMessage", "Password updated successfully. Please log in.");
        return "login";
    }

    @GetMapping("/viewUser")
    public String viewUser(@RequestParam Long userId, Model model) {
        UserRegistration user = service.getUserById(userId);
        if (user != null) {
            model.addAttribute("selectedUser", user);
            return "view-user";
        } else {
            model.addAttribute("promptMessage", "User not found!");
            return "redirect:/user-dashboard";
        }
    }

    @GetMapping("/editUserForm")
    public String editUserForm(@RequestParam Long userId, Model model) {
        UserRegistration user = service.getUserById(userId);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute UserRegistration user, RedirectAttributes redirect) {
        try {
            boolean isUpdated = service.updateUser(user);
            if (isUpdated) {
            	service.sendNotificationEmail(user.getEmail(), "Account Update Notification",
                        "Dear " + user.getFirstName() + ", your account details have been updated. " +
                        "If you did not request this change, please contact support immediately.");
                redirect.addFlashAttribute("promptMessage", "User updated successfully!");
            } else {
                redirect.addFlashAttribute("promptMessage", "User update failed.");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("promptMessage", "An error occurred while updating the user.");
            e.printStackTrace();
        }
        return "redirect:/user-dashboard";
    }

    
        

}
