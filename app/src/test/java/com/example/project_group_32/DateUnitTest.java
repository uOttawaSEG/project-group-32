package com.example.project_group_32;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUnitTest {

    @Test
    public void testDate() {
        Date date = new GregorianCalendar(2024, Calendar.DECEMBER, 4, 10, 0).getTime();

        assertEquals(date, OrganizerActivity.convertDate("2024-DEC-04 10:00"));
    }
}
