package com.user.UserRegisteration.dao;

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

import com.user.UserRegisteration.entity.Status;
import com.user.UserRegisteration.entity.UserRegistration;

@Repository
public class RegisterDao {

	@Autowired
	private SessionFactory factory;

	public boolean saveUser(UserRegistration user) {
		Transaction transaction = null;
		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(user);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			return false;
		}
	}

	public UserRegistration findByEmail(String email) {
		try (Session session = factory.openSession()) {
			Criteria criteria = session.createCriteria(UserRegistration.class);
			criteria.add(Restrictions.eq("email", email));
			return (UserRegistration) criteria.uniqueResult();
		}
	}

	public UserRegistration findByUsername(String username) {
		try (Session session = factory.openSession()) {
			Criteria criteria = session.createCriteria(UserRegistration.class);
			criteria.add(Restrictions.eq("username", username));
			return (UserRegistration) criteria.uniqueResult();
		}
	}

	public UserRegistration findByResetToken(String token) {
		if (token == null)
			return null;
		try (Session session = factory.openSession()) {
			Criteria criteria = session.createCriteria(UserRegistration.class);
			criteria.add(Restrictions.eq("resetToken", token));
			return (UserRegistration) criteria.uniqueResult();
		}
	}

	public void updateFailedAttempts(int newAttempts, String email) {
		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(UserRegistration.class);
		criteria.add(Restrictions.eq("email", email));

		UserRegistration user = (UserRegistration) criteria.uniqueResult();

		if (user != null) {
			user.setFailedAttempts(newAttempts);
			session.update(user);
			session.beginTransaction().commit();
		}
	}

	public void lockUser(String email) {
		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(UserRegistration.class);
		criteria.add(Restrictions.eq("email", email));

		UserRegistration user = (UserRegistration) criteria.uniqueResult();

		if (user != null) {
			user.setStatus(Status.INACTIVE);
			;
			session.update(user);
			session.beginTransaction().commit();
		}
	}

	public List<UserRegistration> searchByKeyword(String keyword) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(UserRegistration.class);

		Criterion firstNameMatch = Restrictions.ilike("firstName", "%" + keyword + "%");
		Criterion lastNameMatch = Restrictions.ilike("lastName", "%" + keyword + "%");
		Criterion emailMatch = Restrictions.ilike("email", "%" + keyword + "%");
		Criterion mobileMatch = Restrictions.ilike("mobile", "%" + keyword + "%");
		Criterion usernameMatch = Restrictions.ilike("username", "%" + keyword + "%");

		// Try to parse keyword into enum (case-insensitive)
		Criterion statusMatch = null;
		try {
			Status statusEnum = Status.valueOf(keyword.toUpperCase()); // Adjust based on enum name
			statusMatch = Restrictions.eq("status", statusEnum);
		} catch (IllegalArgumentException e) {
			// Invalid status keyword; ignore this filter
		}

		// Build disjunction (OR)
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(firstNameMatch);
		disjunction.add(lastNameMatch);
		disjunction.add(emailMatch);
		disjunction.add(mobileMatch);
		disjunction.add(usernameMatch);
		if (statusMatch != null) {
			disjunction.add(statusMatch);
		}

		criteria.add(disjunction);

		@SuppressWarnings("unchecked")
		List<UserRegistration> resultList = criteria.list();
		session.close();

		return resultList;
	}


	public UserRegistration getUserById(Long userId) {
	    Session session = factory.openSession();
	    @SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(UserRegistration.class);
	    criteria.add(Restrictions.eq("id", userId));
	    UserRegistration user = (UserRegistration) criteria.uniqueResult();
	    session.close(); 
	    return user;
	}
	public List<UserRegistration> getAllUsers() {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(UserRegistration.class);
		@SuppressWarnings("unchecked")
		List<UserRegistration> list = criteria.list();
		return list;
		
	}

	public boolean updateUser(UserRegistration user) {
	    Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        // 1. Fetch existing user
	        UserRegistration existingUser = session.get(UserRegistration.class, user.getId());

	        if (existingUser == null) {
	            return false; // Or throw an exception
	        }

	        // 2. Update only supplied fields (you can also check for nulls if needed)
	        existingUser.setFirstName(user.getFirstName());
	        existingUser.setLastName(user.getLastName());
	        existingUser.setMobile(user.getMobile());
	        existingUser.setEmail(user.getEmail());
	        existingUser.setLocation(user.getLocation());

	        // Do NOT touch password here, preserve it as-is

	        // 3. Save updated user
	        session.update(existingUser);
	        tx.commit();
	        return true;

	    } catch (Exception e) {
	        tx.rollback();
	        e.printStackTrace();
	        return false;
	    } finally {
	        session.close();
	    }
	}


	public boolean deleteUser(Long userId) {
	    Session session = factory.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        
	        UserRegistration user = session.get(UserRegistration.class, userId);
	        if (user != null) {
	            session.delete(user);
	        }

	        tx.commit();
	        return true;
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return false;
	    } finally {
	        session.close();
	    }
	}

	public boolean deactivateUser(Long userId) {
	    Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    try {
	        UserRegistration user = (UserRegistration) session.createCriteria(UserRegistration.class)
	            .add(Restrictions.eq("id", userId))
	            .uniqueResult();

	        if (user != null) {
	            user.setStatus(Status.INACTIVE);
	            session.saveOrUpdate(user);
	            tx.commit();
	            return true;
	        } else {
	            System.out.println("User not found with ID: " + userId);
	            return false;
	        }
	    } catch (Exception e) {
	        tx.rollback();
	        e.printStackTrace();
	        return false; // 🔧 Missing return handled here
	    } finally {
	        session.close();
	    }
	}

	public UserRegistration findById(Long userId) {
	Session session = factory.openSession();
	Criteria criteria = session.createCriteria(UserRegistration.class);
	criteria.add(Restrictions.eq("id", userId));
	UserRegistration user =(UserRegistration) criteria.uniqueResult();
	return user;
	
	}



}

