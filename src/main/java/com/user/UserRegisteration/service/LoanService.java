package com.user.UserRegisteration.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.UserRegisteration.dao.LoanDao;
import com.user.UserRegisteration.emailsender.EmailService;
import com.user.UserRegisteration.entity.LoanApplication;
import com.user.UserRegisteration.entity.LoanStatus;
import com.user.UserRegisteration.entity.UserRegistration;

@Service
public class LoanService {
	@Autowired
	LoanDao loanRepository;
    @Autowired
    private EmailService emailService;
    
	public Long applyForLoan(LoanApplication loan, String userEmail) {
	    UserRegistration user = loanRepository.findByEmail(userEmail);
	    loan.setStatus(LoanStatus.PENDING);
	    loan.setUser(user);
	    loanRepository.save(loan);
	    // Email notification
        String subject = "Loan Application Submitted Successfully";
        String body = "Dear " + user.getFullName() + ",\n\n" +
                      "Your loan application for ₹" + loan.getLoanAmount() +
                      " (" + loan.getLoanType() + ") has been submitted successfully. " +
                      "We will review it and get back to you shortly.\n\n" +
                      "Regards,\nLoan Processing Team";

        emailService.sendEmail(user.getEmail(), subject, body);

	    return loan.getId();  // Returns Loan ID
	}

	 public List<LoanApplication> getLoansByUserEmail(String email) {
	        return loanRepository.getLoansByUserEmail(email);
	    }
	 public void sendApprovalEmail(Long loanId) {
		    LoanApplication loan = loanRepository.findById(loanId);
		    String subject = "Your Loan Application has been Approved!";
		    String body = "Dear " + loan.getUser().getFullName() +
		                  ", your loan application for ₹" + loan.getLoanAmount() +
		                  " has been approved. Please check your dashboard for further details.";
		    emailService.sendEmail(loan.getUser().getEmail(), subject, body);
		}

		public void sendRejectionEmail(Long loanId, String reason) {
		    LoanApplication loan = loanRepository.findById(loanId);
		    String subject = "Your Loan Application has been Rejected";
		    String body = "Dear " + loan.getUser().getFullName() +
		                  ", we regret to inform you that your loan application has been rejected.\n" +
		                  "Reason: " + reason + ".\nPlease contact support for more details.";
		    emailService.sendEmail(loan.getUser().getEmail(), subject, body);
		}
		@Transactional	
		public boolean approveLoan(Long loanId) {
			return loanRepository.approveLoan(loanId);
			
		}

	    public boolean rejectLoan(Long loanId, String reason) {
	        return loanRepository.rejectLoan(loanId, reason);
	    }

		public List<LoanApplication> getAllLoanApplications() {
	 return loanRepository.getAllLoanApplications();
		}

		public LoanApplication getLoanById(Long loanId) {
	return loanRepository.getLoanById(loanId);
		}

		public List<LoanApplication> searchLoans(String keyword) {
			return loanRepository.searchLoans(keyword);
		}

}
