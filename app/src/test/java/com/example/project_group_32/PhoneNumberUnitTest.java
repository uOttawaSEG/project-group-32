package com.example.project_group_32;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

public class PhoneNumberUnitTest {

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return Pattern.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", phoneNumber);
    }

    @Test
    public void testPhoneNumber() {
        assertTrue(isPhoneNumberValid("123 456 7890"));
        assertFalse(isPhoneNumberValid("ABC"));
    }

}
