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
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
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
        if (token == null) return null;
        try (Session session = factory.openSession()) {
            Criteria criteria = session.createCriteria(UserRegistration.class);
            criteria.add(Restrictions.eq("resetToken", token));
            return (UserRegistration) criteria.uniqueResult();
        }
    }

    public void updateFailedAttempts(int newAttempts, String email) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(UserRegistration.class);
            criteria.add(Restrictions.eq("email", email));
            UserRegistration user = (UserRegistration) criteria.uniqueResult();
            if (user != null) {
                user.setFailedAttempts(newAttempts);
                session.update(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void lockUser(String email) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(UserRegistration.class);
            criteria.add(Restrictions.eq("email", email));
            UserRegistration user = (UserRegistration) criteria.uniqueResult();
            if (user != null) {
                user.setStatus(Status.INACTIVE);
                session.update(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public List<UserRegistration> searchByKeyword(String keyword) {
        try (Session session = factory.openSession()) {
            Criteria criteria = session.createCriteria(UserRegistration.class);

            Criterion firstNameMatch = Restrictions.ilike("firstName", "%" + keyword + "%");
            Criterion lastNameMatch = Restrictions.ilike("lastName", "%" + keyword + "%");
            Criterion emailMatch = Restrictions.ilike("email", "%" + keyword + "%");
            Criterion mobileMatch = Restrictions.ilike("mobile", "%" + keyword + "%");
            Criterion usernameMatch = Restrictions.ilike("username", "%" + keyword + "%");

            Criterion statusMatch = null;
            try {
                Status statusEnum = Status.valueOf(keyword.toUpperCase());
                statusMatch = Restrictions.eq("status", statusEnum);
            } catch (IllegalArgumentException ignored) {}

            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(firstNameMatch);
            disjunction.add(lastNameMatch);
            disjunction.add(emailMatch);
            disjunction.add(mobileMatch);
            disjunction.add(usernameMatch);
            if (statusMatch != null) disjunction.add(statusMatch);

            criteria.add(disjunction);
            @SuppressWarnings("unchecked")
            List<UserRegistration> resultList = criteria.list();
            return resultList;
        }
    }

    public UserRegistration getUserById(Long userId) {
        try (Session session = factory.openSession()) {
            Criteria criteria = session.createCriteria(UserRegistration.class);
            criteria.add(Restrictions.eq("id", userId));
            return (UserRegistration) criteria.uniqueResult();
        }
    }

    public List<UserRegistration> getAllUsers() {
        try (Session session = factory.openSession()) {
            Criteria criteria = session.createCriteria(UserRegistration.class);
            @SuppressWarnings("unchecked")
            List<UserRegistration> list = criteria.list();
            return list;
        }
    }

    public boolean updateUser(UserRegistration user) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            UserRegistration existingUser = session.get(UserRegistration.class, user.getId());

            if (existingUser == null) {
                return false;
            }

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setMobile(user.getMobile());
            existingUser.setEmail(user.getEmail());
            existingUser.setLocation(user.getLocation());

            session.update(existingUser);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(Long userId) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
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
        }
    }

    public boolean deactivateUser(Long userId) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(UserRegistration.class);
            criteria.add(Restrictions.eq("id", userId));
            UserRegistration user = (UserRegistration) criteria.uniqueResult();
            if (user != null) {
                user.setStatus(Status.INACTIVE);
                session.update(user);
                tx.commit();
                return true;
            } else {
                System.out.println("User not found with ID: " + userId);
                return false;
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public UserRegistration findById(Long userId) {
        try (Session session = factory.openSession()) {
            Criteria criteria = session.createCriteria(UserRegistration.class);
            criteria.add(Restrictions.eq("id", userId));
            return (UserRegistration) criteria.uniqueResult();
        }
    }
}
