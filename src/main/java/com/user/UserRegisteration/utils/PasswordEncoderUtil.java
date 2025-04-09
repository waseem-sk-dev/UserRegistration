package com.user.UserRegisteration.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Encrypt Password
    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Verify Password
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
