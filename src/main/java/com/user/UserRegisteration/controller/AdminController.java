package com.user.UserRegisteration.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.user.UserRegisteration.entity.LoanApplication;
import com.user.UserRegisteration.entity.UserRegistration;
import com.user.UserRegisteration.service.AdminService;
import com.user.UserRegisteration.service.LoanService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private LoanService loanService;
	   @GetMapping("/dashboard")
	    public String adminDashboard(Model model) {
	        List<UserRegistration> userList = adminService.getAllUsers();
	        model.addAttribute("userList", userList);
	            List<LoanApplication> loanApplications = loanService.getAllLoanApplications();
	            model.addAttribute("loanApplications", loanApplications);
	            return "admin-dashboard";
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

	    @GetMapping("/searchUsers")
	    public String searchUsersByKeyword(@RequestParam String keyword, Model model) {
	        if (keyword == null || keyword.trim().isEmpty()) {
	            model.addAttribute("promptMessage", "Please enter a search keyword.");
	            return "admin-dashboard";
	        }

	        List<UserRegistration> users = adminService.searchByKeyword(keyword.trim());
	        if (users.isEmpty()) {
	            model.addAttribute("promptMessage", "No users found for: " + keyword);
	        } else {
	            model.addAttribute("userList", users);
	            model.addAttribute("searchedKeyword", keyword);
	        }

	        return "searchResults";
	    }

	    @GetMapping("/viewUser")
	    public String viewUser(@RequestParam Long userId, Model model) {
	        UserRegistration user = adminService.getUserById(userId);
	        if (user != null) {
	            model.addAttribute("selectedUser", user);
	            return "view-user";
	        } else {
	            model.addAttribute("promptMessage", "User not found!");
	            return "redirect:/admin/dashboard";
	        }
	    }


	   @PostMapping("/deactivateUser")
	    public String deactivateUser(@RequestParam Long userId, HttpSession session, RedirectAttributes redirect) {
	        String role = (String) session.getAttribute("role");
	        if (role == null || !role.equalsIgnoreCase("admin")) {
	            redirect.addFlashAttribute("promptMessage", "Access denied: Only admins can deactivate users.");
	            return "redirect:/admin/dashboard";
	        }

	        try {
	            boolean isDeactivated = adminService.deactivateUser(userId);
	            if (isDeactivated) {
	                UserRegistration user = adminService.getUserById(userId);
	                adminService.sendNotificationEmail(user.getEmail(), "Account Update Notification",
	                        "Dear " + user.getFirstName() + ", your account has been deactivated. Please contact support for more information.");
	                redirect.addFlashAttribute("promptMessage", "User deactivated!");
	            } else {
	                redirect.addFlashAttribute("promptMessage", "User already inactive.");
	            }
	        } catch (Exception e) {
	            redirect.addFlashAttribute("promptMessage", "Error during deactivation.");
	        }
	        return "redirect:/admin/dashboard";
	    }

	    @PostMapping("/activateUser")
	    public String activateUser(@RequestParam Long userId, HttpSession session, RedirectAttributes redirect) {
	        String role = (String) session.getAttribute("role");
	        if (role == null || !role.equalsIgnoreCase("admin")) {
	            redirect.addFlashAttribute("promptMessage", "Access denied: Only admins can activate users.");
	            return "redirect:/admin/dashboard";
	        }
	        try {
	            boolean isActivated = adminService.activateUser(userId);
	            if (isActivated) {
	                UserRegistration user = adminService.getUserById(userId);
	                adminService.sendNotificationEmail(user.getEmail(), "Account Update Notification",
	                        "Dear " + user.getFirstName() + ", your account has been successfully activated by the admin. You can now log in to the system.");
	                redirect.addFlashAttribute("promptMessage", "User activated!");
	            } else {
	                redirect.addFlashAttribute("promptMessage", "User already active.");
	            }
	        } catch (Exception e) {
	            redirect.addFlashAttribute("promptMessage", "Error during activation.");
	        }
	        return "redirect:/admin/dashboard";
	    }
	    @PostMapping("/updateUser")
	    public String updateUser(@ModelAttribute UserRegistration user, RedirectAttributes redirect) {
	        try {
	            boolean isUpdated = adminService.updateUser(user);
	            if (isUpdated) {
	            	adminService.sendNotificationEmail(user.getEmail(), "Account Update Notification",
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
	        return "redirect:/admin/dashboard";
	    }
	    @GetMapping("/editUserForm")
	    public String editUserForm(@RequestParam Long userId, Model model) {
	        UserRegistration user = adminService.getUserById(userId);
	        model.addAttribute("user", user);
	        return "edit-user";
	    }

	    @PostMapping("/deleteUser")
	    public String deleteUser(@RequestParam Long userId, RedirectAttributes redirect) {
	        UserRegistration user = adminService.getUserById(userId); // Get details before deletion
	        boolean isDeleted = adminService.deleteUser(userId);
	        if (isDeleted) {
	        	adminService.sendNotificationEmail(user.getEmail(), "Account Update Notification",
	                    "Dear " + user.getFirstName() + ", your account has been deleted from the system. If this was a mistake, please contact support immediately.");
	            redirect.addFlashAttribute("promptMessage", "User deleted!");
	        } else {
	            redirect.addFlashAttribute("promptMessage", "Failed to delete user.");
	        }
	        return "redirect:/admin/dashboard";
	    }
	    @GetMapping("/searchLoans")
	    public String searchLoans(@RequestParam(required = false) String keyword, Model model) {
	        List<LoanApplication> loanApplications;

	        if (keyword != null && !keyword.trim().isEmpty()) {
	            loanApplications = loanService.searchLoans(keyword.trim());
	        } else {
	            loanApplications = loanService.getAllLoanApplications(); // fallback method if no keyword
	        }

	        model.addAttribute("loanApplications", loanApplications);
	        model.addAttribute("keyword", keyword); // For showing in search box
	        return "admin-dashboard";
	    }
	    @PostMapping("/approveLoan")
	    public String approveLoan(@RequestParam Long loanId, RedirectAttributes redirectAttributes) {
	        boolean isApproved = loanService.approveLoan(loanId); // Update status
	        
	        if (isApproved) {
	            loanService.sendApprovalEmail(loanId); // Notify user
	            redirectAttributes.addFlashAttribute("message", "Loan ID " + loanId + " has been approved successfully.");
	            redirectAttributes.addFlashAttribute("messageType", "success");
	        } else {
	            redirectAttributes.addFlashAttribute("message", "Failed to approve Loan ID " + loanId + ". Please try again.");
	            redirectAttributes.addFlashAttribute("messageType", "error");
	        }

	        return "redirect:/admin/dashboard";
	    }

	    @PostMapping("/loan/reject")  // Match your JSP form action
	    public String rejectLoan(@RequestParam Long loanId,
	                             @RequestParam String reason,
	                             RedirectAttributes redirectAttributes) {
	        boolean isRejected = loanService.rejectLoan(loanId, reason); // Update status & reason

	        if (isRejected) {
	            loanService.sendRejectionEmail(loanId, reason); // Notify user
	            redirectAttributes.addFlashAttribute("message", "Loan ID " + loanId + " has been rejected.");
	            redirectAttributes.addFlashAttribute("messageType", "error");
	        } else {
	            redirectAttributes.addFlashAttribute("message", "Failed to reject Loan ID " + loanId + ". Please try again.");
	            redirectAttributes.addFlashAttribute("messageType", "error");
	        }

	        return "redirect:/admin/dashboard";  // Redirect to admin loan list after action
	    }

	    @GetMapping("/viewLoanDetails")
	    public String viewLoanDetails(@RequestParam Long loanId, Model model, RedirectAttributes redirectAttributes) {
	        LoanApplication loan = loanService.getLoanById(loanId);

	        if (loan == null) {
	            redirectAttributes.addFlashAttribute("message", "Loan application not found.");
	            redirectAttributes.addFlashAttribute("messageType", "error");
	            return "redirect:/admin/dashboard";
	        }

	        model.addAttribute("loan", loan);
	        return "view-loan-details"; // JSP page to show loan details
	    }
	    @GetMapping("/rejectLoanForm")
	    public String showRejectLoanForm(@RequestParam Long loanId, Model model) {
	        LoanApplication loan = loanService.getLoanById(loanId);
	        model.addAttribute("loan", loan);
	        return "reject-loan-form"; // JSP located at /WEB-INF/views/admin/reject-loan-form.jsp
	    }


	  


}
