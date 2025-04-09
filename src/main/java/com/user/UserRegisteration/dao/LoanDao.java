package com.user.UserRegisteration.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.user.UserRegisteration.entity.LoanApplication;
import com.user.UserRegisteration.entity.LoanStatus;
import com.user.UserRegisteration.entity.UserRegistration;

@Repository
public class LoanDao {
@Autowired
SessionFactory factory;

 
	public UserRegistration findByEmail(String userEmail) {
	Session session = factory.openSession();
	Criteria criteria = session.createCriteria(UserRegistration.class);
	criteria.add(Restrictions.eq("email", userEmail));
	UserRegistration user = (UserRegistration) criteria.uniqueResult();
	return user;
	}


	public void save(LoanApplication loan) {
	    Session session = factory.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        session.save(loan);  // saves loan and links user_id via mapping
	        tx.commit();
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();  // or use logger
	    } finally {
	        session.close();
	    }
	}




	public List<LoanApplication> getLoansByUserEmail(String email) {
	    List<LoanApplication> loanList = new ArrayList<>();

	    try (Session session = factory.openSession()) {
	        // Step 1: Fetch the UserRegistration by email
	        Criteria userCriteria = session.createCriteria(UserRegistration.class);
	        userCriteria.add(Restrictions.eq("email", email));
	        UserRegistration user = (UserRegistration) userCriteria.uniqueResult();

	        // Step 2: If user found, get loans for that user
	        if (user != null) {
	            Criteria loanCriteria = session.createCriteria(LoanApplication.class);
	            loanCriteria.add(Restrictions.eq("user", user)); // using object reference
	            loanList = loanCriteria.list();
	        }

	    } catch (Exception e) {
	        e.printStackTrace(); // Consider using logger
	    }

	    return loanList;
	}

	public LoanApplication findById(Long loanId) {
	    Session session = factory.openSession();
	    LoanApplication loan = null;
	    try {
	        Criteria criteria = session.createCriteria(LoanApplication.class);
	        criteria.add(Restrictions.eq("id", loanId));
	        loan = (LoanApplication) criteria.uniqueResult();
	    } finally {
	        session.close();
	    }
	    return loan;
	}

	public boolean approveLoan(Long loanId) {
	    Session session = factory.openSession();
	    Transaction tx = null;

	    try {
	        tx = session.beginTransaction();

	        Criteria criteria = session.createCriteria(LoanApplication.class);
	        criteria.add(Restrictions.eq("id", loanId));
	        LoanApplication loan = (LoanApplication) criteria.uniqueResult();

	        if (loan != null) {
	            loan.setStatus(LoanStatus.APPROVED); // Set status to APPROVED
	            session.update(loan);
	            tx.commit(); // ✅ commit should be before return
	            return true;
	        }

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace(); // Consider logging instead
	    } finally {
	        session.close();
	    }

	    return false;
	}
	    public boolean rejectLoan(Long loanId, String reason) {
	        Session session = factory.openSession();
	        Transaction tx = null;

	        try {
	            tx = session.beginTransaction();

	            Criteria criteria = session.createCriteria(LoanApplication.class);
	            criteria.add(Restrictions.eq("id", loanId));

	            LoanApplication loan = (LoanApplication) criteria.uniqueResult();

	            if (loan != null) {
	                loan.setStatus(LoanStatus.REJECTED);         // Update status
	                loan.setRejectionReason(reason);             // Save reason
	                session.update(loan);
	                tx.commit();
	                return true;
	            }

	        } catch (Exception e) {
	            if (tx != null) tx.rollback();
	            e.printStackTrace(); // Replace with logger
	        } finally {
	            session.close();
	        }

	        return false;
	    }


		public List<LoanApplication> getAllLoanApplications() {
		Session session = factory.openSession();
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(LoanApplication.class);
		@SuppressWarnings("unchecked")
		List<LoanApplication> list =(List<LoanApplication>) criteria.list();
	return list;
		}


		public LoanApplication getLoanById(Long loanId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(LoanApplication.class);
			criteria.add(Restrictions.eq("id",loanId));
			LoanApplication loan = (LoanApplication)criteria.uniqueResult();
			return loan;
		}


		public List<LoanApplication> searchLoans(String keyword) {
		    Session session = factory.openSession();
		    Criteria criteria = session.createCriteria(LoanApplication.class, "loan");
		    criteria.createAlias("loan.user", "user"); // Join with UserRegistration entity

		    // User-level search criteria
		    Criterion firstNameMatch = Restrictions.ilike("user.firstName", "%" + keyword + "%");
		    Criterion lastNameMatch = Restrictions.ilike("user.lastName", "%" + keyword + "%");
		    Criterion emailMatch = Restrictions.ilike("user.email", "%" + keyword + "%");
		    Criterion mobileMatch = Restrictions.ilike("user.mobile", "%" + keyword + "%");
		    Criterion usernameMatch = Restrictions.ilike("user.username", "%" + keyword + "%");

		    // Loan-level search criteria
		    Criterion loanTypeMatch = Restrictions.ilike("loan.loanType", "%" + keyword + "%");
		    Criterion purposeMatch = Restrictions.ilike("loan.purpose", "%" + keyword + "%");

		    // Attempt to match keyword with LoanStatus enum
		    Criterion statusMatch = null;
		    try {
		        LoanStatus statusEnum = LoanStatus.valueOf(keyword.toUpperCase());
		        statusMatch = Restrictions.eq("loan.status", statusEnum);
		    } catch (IllegalArgumentException e) {
		        // Ignore invalid enum value
		    }

		    // Combine all criteria with OR logic
		    Disjunction disjunction = Restrictions.disjunction();
		    disjunction.add(firstNameMatch);
		    disjunction.add(lastNameMatch);
		    disjunction.add(emailMatch);
		    disjunction.add(mobileMatch);
		    disjunction.add(usernameMatch);
		    disjunction.add(loanTypeMatch);
		    disjunction.add(purposeMatch);
		    if (statusMatch != null) {
		        disjunction.add(statusMatch);
		    }

		    criteria.add(disjunction);

		    @SuppressWarnings("unchecked")
		    List<LoanApplication> resultList = criteria.list();
		    session.close();
		    return resultList;
		}

	}
