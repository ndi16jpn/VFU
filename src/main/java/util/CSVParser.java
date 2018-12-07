package util;

import data.StudentData;

import java.util.*;

/**
 * Parsar en csvfil innehållande studener på en kurs enligt formatet:
 * ﻿Personnummer;Namn;c/o;Utdelningsadress;Postort;Telefon/sms;E-post;
 */
public class CSVParser {

    public static List<StudentData> parseStudentCSV(List<String> csvLines){
        boolean firstLineFound = false;
        int lineMarker = 0;
        while (!firstLineFound) {
            if(csvLines.get(lineMarker).contains("Personnummer") || csvLines.get(lineMarker).contains("personnummer")) {
                firstLineFound = true;
            } else {
                lineMarker ++;
            }
        }
        String splitBy = ";";
        String[] firstLine = csvLines.get(lineMarker).split(splitBy);


        int NAME_INDEX = -1;
        int DOB_INDEX = -1;
        int EMAIL_INDEX = -1;
        int PHONE_INDEX = -1;

        int attributeIndex = 0;
        for (String attribute: firstLine

        ) {
            String attributeString = attribute.replaceAll("\"", "");
            attributeString = attributeString.replaceAll("-", "");
            attributeString = attributeString.toLowerCase();
            if (attributeString.contains("personnummer")) {
                DOB_INDEX = attributeIndex;
            } else if (attributeString.contains("namn")) {
                NAME_INDEX = attributeIndex;
            } else if (attributeString.contains("epost")) {
                EMAIL_INDEX = attributeIndex;
            } else if (attributeString.contains("telefon")) {
                PHONE_INDEX = attributeIndex;
            }
            attributeIndex++;
        }


        List<StudentData> students = new ArrayList<>();
        for (String csvLine : csvLines) {
            csvLine = csvLine.replaceAll("\"","");
            csvLine = csvLine.replaceAll("-","");
            if (!isStudentLine(csvLine, DOB_INDEX)) {
                continue;
            }
            String[] studentData = csvLine.trim().split(splitBy);
            //for (int studentDataIndex = 0; studentDataIndex < studentData.length; studentDataIndex++) {
            //    studentData[studentDataIndex] = studentData[studentDataIndex].replaceAll("\"","");
            //    studentData[studentDataIndex] = studentData[studentDataIndex].replaceAll("-","");
            //}
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

    private static boolean isStudentLine(String csvLine, int DOB_INDEX) {
        try {
            Long.parseLong(csvLine.substring(DOB_INDEX, 12));
        } catch (Exception e) {
            return false;
        }

        //String splitBy = ";";
        //int numOfFields = 7;
        //if (csvLine.trim().split(splitBy).length != numOfFields) {
        //    return false;
        //}

        return true;
    }

}
