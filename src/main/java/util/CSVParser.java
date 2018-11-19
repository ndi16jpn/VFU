package util;

import data.StudentData;

import java.util.*;

/**
 * Parsar en csvfil innehållande studener på en kurs enligt formatet:
 * ﻿Personnummer;Namn;c/o;Utdelningsadress;Postort;Telefon/sms;E-post;
 */
public class CSVParser {

    public static List<StudentData> parseStudentCSV(List<String> csvLines){
        String splitBy = ";";
        final int NAME_INDEX = 1;
        final int DOB_INDEX = 0;
        final int EMAIL_INDEX = 6;
        final int PHONE_INDEX = 5;
        List<StudentData> students = new ArrayList<>();
        for (String csvLine : csvLines) {
            if (!isStudentLine(csvLine)) {
                continue;
            }
            String[] studentData = csvLine.trim().split(splitBy);
            students.add(
                    new StudentData(
                            studentData[EMAIL_INDEX],
                            studentData[NAME_INDEX],
                            studentData[DOB_INDEX].substring(0, 4),
                            studentData[PHONE_INDEX]
                    )
            );
        }
        Set<String> emails = new HashSet<>();
        students.removeIf(studentData -> !emails.add(studentData.getEmail()));
        return students;
    }

    private static boolean isStudentLine(String csvLine) {
        try {
            Long.parseLong(csvLine.substring(0, 12));
        } catch (Exception e) {
            return false;
        }

        String splitBy = ";";
        int numOfFields = 7;
        if (csvLine.trim().split(splitBy).length != numOfFields) {
            return false;
        }

        return true;
    }

}
