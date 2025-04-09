package com.user.UserRegisteration.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.UserRegisteration.dao.RegisterDao;
import com.user.UserRegisteration.emailsender.EmailService;
import com.user.UserRegisteration.entity.Status;
import com.user.UserRegisteration.entity.UserRegistration;

@Service
public class AdminService {
	   @Autowired
	    private RegisterDao userRepository;
	   @Autowired
	    private EmailService emailService;
	
	public List<UserRegistration> getAllUsers() {
		return userRepository.getAllUsers();
	}
	public boolean deactivateUser(Long userId) {
	    UserRegistration user = userRepository.findById(userId);

	    if (user != null && user.getStatus() == Status.ACTIVE) {
	        user.setStatus(Status.INACTIVE);
	        userRepository.saveUser(user); // This should return true/false if needed
	        return true;
	    }
	    return false;
	}

    public List<UserRegistration> searchByKeyword(String keyword) {
        return userRepository.searchByKeyword(keyword);
    }


	public boolean activateUser(Long userId) {
	   UserRegistration user = userRepository.findById(userId);
	    if (user != null && user.getStatus()==Status.INACTIVE) {
	        user.setStatus(Status.ACTIVE);
	        userRepository.saveUser(user);
	        return true;
	    }
	    return false;
	}
	public void sendNotificationEmail(String email, String subject, String emailContent) {

	    emailService.sendEmail(email, subject, emailContent);
	}

    public UserRegistration getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }
    
    public boolean updateUser(UserRegistration user) {
    	return userRepository.updateUser(user);
    	}
    public boolean deleteUser(Long userId) {
		return userRepository.deleteUser(userId);
		
	}
}
