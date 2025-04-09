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
    @PostMapping("/admin/approveLoan")
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

        return "redirect:/admin-dashboard";
    }

    @PostMapping("/admin/loan/reject")  // Match your JSP form action
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

        return "redirect:/admin-dashboard";  // Redirect to admin loan list after action
    }

    @GetMapping("/admin/viewLoanDetails")
    public String viewLoanDetails(@RequestParam Long loanId, Model model, RedirectAttributes redirectAttributes) {
        LoanApplication loan = loanService.getLoanById(loanId);

        if (loan == null) {
            redirectAttributes.addFlashAttribute("message", "Loan application not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/admin-dashboard";
        }

        model.addAttribute("loan", loan);
        return "view-loan-details"; // JSP page to show loan details
    }
    @GetMapping("/admin/rejectLoanForm")
    public String showRejectLoanForm(@RequestParam Long loanId, Model model) {
        LoanApplication loan = loanService.getLoanById(loanId);
        model.addAttribute("loan", loan);
        return "reject-loan-form"; // JSP located at /WEB-INF/views/admin/reject-loan-form.jsp
    }





}
