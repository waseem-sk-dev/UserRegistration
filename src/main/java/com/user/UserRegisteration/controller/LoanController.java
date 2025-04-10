package com.user.UserRegisteration.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.user.UserRegisteration.entity.LoanApplication;
import com.user.UserRegisteration.entity.UserRegistration;
import com.user.UserRegisteration.service.LoanService;



@Controller
public class LoanController {

    @Autowired
    private LoanService loanService;

    // 🌟 GET: Show Loan Application Form
    @GetMapping("/loan/apply")
    public String showLoanApplicationForm(HttpSession session, Model model, RedirectAttributes redirect) {
        UserRegistration user = (UserRegistration) session.getAttribute("loggedInUser");

        if (user == null) {
            redirect.addFlashAttribute("promptMessage", "You must be logged in to apply for a loan.");
            return "redirect:/login";
        }

        model.addAttribute("loanApplication", new LoanApplication());
        return "loan-application"; // JSP page name
    }

    // ✅ POST: Handle Form Submission
    @PostMapping("/apply-loan")
    public String applyLoan(@ModelAttribute @Valid LoanApplication loan,
                            BindingResult result,
                            HttpSession session,
                            RedirectAttributes redirect) {

        if (result.hasErrors() || loan.getLoanAmount() == null || loan.getLoanAmount() <= 0 ||
            loan.getAnnualIncome() == null || loan.getAnnualIncome() <= 0 ||
            loan.getLoanDuration() == null || loan.getLoanDuration() <= 0) {
            redirect.addFlashAttribute("promptMessage", "Please fill all required fields correctly.");
            return "redirect:/loan/apply";
        }

        UserRegistration user = (UserRegistration) session.getAttribute("loggedInUser");

        if (user == null) {
            redirect.addFlashAttribute("promptMessage", "You must be logged in to apply for a loan.");
            return "redirect:/login";
        }

        Long loanId = loanService.applyForLoan(loan, user.getEmail());
       

        redirect.addFlashAttribute("promptMessage", "Loan application submitted successfully. Ref ID: " + loanId);
        return "redirect:/user-dashboard";
    }
    @GetMapping("/user-dashboard")
    public String showUserDashboard(HttpSession session, Model model, RedirectAttributes redirect) {
        UserRegistration user = (UserRegistration) session.getAttribute("loggedInUser");

        if (user == null) {
            redirect.addFlashAttribute("errorMessage", "Please log in first.");
            return "redirect:/login";
        }

        // Fetch loans using email
        List<LoanApplication> userLoans = loanService.getLoansByUserEmail(user.getEmail());
        model.addAttribute("userLoans", userLoans);

        // Optional: Add user info to model for profile display
        model.addAttribute("userProfile", user);

        return "user-dashboard";
    }
    




}
