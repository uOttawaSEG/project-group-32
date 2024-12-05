package com.example.project_group_32;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NameUnitTest {

    private NameValidate nameValidate;

    @Before
    public void setUp() {
        nameValidate = new NameValidate();
    }

    @Test
    public void testFirstName() {
        assertTrue(nameValidate.isNameValid("Jack"));
        assertFalse(nameValidate.isNameValid("123"));
    }
}