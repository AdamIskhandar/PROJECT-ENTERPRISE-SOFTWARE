package com.cliniccare.util;

public class GeneratePassword {

    public static void main(String[] args) {

        String password = "admin123";

        String hashedPassword =
                PasswordUtil.hashPassword(password);

        System.out.println(hashedPassword);
    }
}