package com.user.UserRegisteration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.user.UserRegisteration.dao.RegisterDao;
import com.user.UserRegisteration.emailsender.EmailService;
import com.user.UserRegisteration.entity.UserRegistration;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class RegisterService {

    @Autowired
    private RegisterDao userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public String registerUser(UserRegistration user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean isRegistered = userRepository.saveUser(user);
        if (isRegistered) {
            sendWelcomeEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName());
        }
        return isRegistered ? "successfully registered user" : "Error, can not register user";
    }

    public UserRegistration getUserByEmailOrUsername(String input) {
        UserRegistration user = getUserByEmail(input);
        return (user != null) ? user : getUserByUsername(input);
    }

    public UserRegistration getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserRegistration getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return rawPassword != null && encodedPassword != null && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public boolean isValidPassword(String newPassword) {
        if (newPassword == null) return false;
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return newPassword.matches(pattern);
    }

    public void generateResetToken(UserRegistration user) {
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.saveUser(user);
        System.out.println("Reset Link: http://localhost:8080/reset-password?token=" + token);
    }

    public UserRegistration getUserByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    public boolean isTokenValid(String token) {
        UserRegistration user = getUserByResetToken(token);
        return user != null && user.getResetToken() != null;
    }

    public void updatePassword(UserRegistration user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.saveUser(user);
        String fullName = user.getFirstName() + " " + user.getLastName();
        sendPasswordUpdatedEmail(user.getEmail(), fullName);
    }

    public void sendWelcomeEmail(String email, String fullName) {
        String subject = "Welcome to Our Platform, " + fullName + "!";
        String emailContent = String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; color: #333;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                        <h2 style="color: #2196F3;">Welcome to Wasim Infotech, %s!</h2>
                        <p>We're excited to have you on board. 🚀</p>
                        <p>At <strong>Wasim Infotech</strong>, we are committed to delivering cutting-edge tech solutions and providing you with the best user experience possible.</p>
                        <p>You can now log in to your account and begin exploring all that our platform has to offer.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="https://wasiminfotech.com/login" style="background-color: #2196F3; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px;">Login to Your Account</a>
                        </div>
                        <p>If you need any assistance, feel free to contact our support team at 
                        <a href="mailto:support@wasiminfotech.com">support@wasiminfotech.com</a>.</p>
                        <p style="margin-top: 40px;">Cheers,</p>
                        <p><strong>Waseem Shaikh</strong><br>Founder & CEO<br>Wasim Infotech</p>
                    </div>
                </body>
            </html>
            """, fullName);

        try {
            emailService.sendEmail(email, subject, emailContent);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }

    public void sendPasswordUpdatedEmail(String email, String fullName) {
        String subject = "Your Password Has Been Updated";
        String emailContent = String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                        <h2 style="color: #4CAF50;">Hello %s,</h2>
                        <p>Your password was successfully updated.</p>
                        <p>If you did not make this change, please contact our support team immediately at 
                        <a href="mailto:support@wasiminfotech.com">support@wasiminfotech.com</a>.</p>
                        <p>Thank you for using <strong>Wasim Infotech</strong>.</p>
                        <p style="margin-top: 40px;">Regards,</p>
                        <p><strong>Waseem Shaikh</strong><br>Founder & CEO<br>Wasim Infotech</p>
                    </div>
                </body>
            </html>
            """, fullName);

        try {
            emailService.sendEmail(email, subject, emailContent);
        } catch (Exception e) {
            System.err.println("Failed to send password update email: " + e.getMessage());
        }
    }
    @Transactional
    public int increaseFailedAttempts(String email) {
        UserRegistration user = getUserByEmail(email);
        if (user != null) {
            int newAttempts = user.getFailedAttempts() + 1;
            userRepository.updateFailedAttempts(newAttempts, email);

            int remainingAttempts = 3 - newAttempts;
            return remainingAttempts;
        }
        return -1; // user not found
    }

    @Transactional
    public void resetFailedAttempts(String email) {
    	userRepository.updateFailedAttempts(0, email);
    }

    public UserRegistration getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }
    public boolean updateUser(UserRegistration user) {
    	return userRepository.updateUser(user);
    	}
	public void sendNotificationEmail(String email, String subject, String emailContent) {
		
	    emailService.sendEmail(email, subject, emailContent);
	}
    @Transactional
    public void lockUser(String email) {
    	userRepository.lockUser(email);
    }

}
