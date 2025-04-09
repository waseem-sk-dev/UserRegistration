package com.user.UserRegisteration.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Loan amount is required.")
    @DecimalMin(value = "1000.0", message = "Minimum loan amount must be ₹1000.")
    private Double loanAmount;

    @NotBlank(message = "Loan type is required.")
    private String loanType;

    @NotNull(message = "Loan duration is required.")
    @Min(value = 6, message = "Minimum duration is 6 months.")
    @Max(value = 360, message = "Maximum duration is 360 months.")
    private Integer loanDuration;

    @NotNull(message = "Interest rate is required.")
    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1%.")
    private Double interestRate;

    @NotNull(message = "Annual income is required.")
    @DecimalMin(value = "10000.0", message = "Minimum income must be ₹10,000.")
    private Double annualIncome;

    @NotBlank(message = "Loan purpose is required.")
    @Size(max = 255, message = "Purpose should not exceed 255 characters.")
    private String purpose;

    @Size(max = 500, message = "Rejection reason should not exceed 500 characters.")
    private String rejectionReason;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserRegistration user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Integer getLoanDuration() {
		return loanDuration;
	}

	public void setLoanDuration(Integer loanDuration) {
		this.loanDuration = loanDuration;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public Double getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(Double annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	public UserRegistration getUser() {
		return user;
	}

	public void setUser(UserRegistration user) {
		this.user = user;
	}

    
}
