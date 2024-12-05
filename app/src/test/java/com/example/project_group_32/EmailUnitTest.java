package com.example.project_group_32;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class EmailUnitTest {

    public static boolean isEmailValid(String email) {
        return Pattern.matches("[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+", email);
    }

    @Test
    public void testEmail() {
        assertTrue(isEmailValid("org@uo.ca"));
        assertFalse(isEmailValid("att@uo"));
    }
}
