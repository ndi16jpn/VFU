package util;

import data.StudentData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CSVParserTest {

    private List<String> csvLines;

    @Before
    public void setUp() throws Exception {
        csvLines = new ArrayList<>();
        csvLines.add("Personnummer;Namn;c/o;Utdelningsadress;Postort;Telefon/sms;E-post;");
        csvLines.add("458394829201;Anka, Kalle;;SUPERGATAN 6;34756 STOCKHOLM;0701234567;abc12def@student.hig.se;");
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
    }

    @Test
    public void parseStudentCSVSize() throws Exception {
        List<StudentData> studentDataList = CSVParser.parseStudentCSV(csvLines);

        assertEquals(2, studentDataList.size());
    }

    @Test
    public void parseStudentCSVSize2() throws Exception {
        List<String> csvLinesNoStudents = new ArrayList<>();
        csvLinesNoStudents.add("Personnummer;Namn;c/o;Utdelningsadress;Postort;Telefon/sms;E-post;\n");

        List<StudentData> studentDataList = CSVParser.parseStudentCSV(csvLinesNoStudents);

        assertEquals(0, studentDataList.size());
    }

    @Test
    public void parseStudentCSVValues() throws Exception {
        List<StudentData> studentDataList = CSVParser.parseStudentCSV(csvLines);

        StudentData student1 = studentDataList.get(0);
        assertEquals("4583", student1.getDob());
        assertEquals("Anka, Kalle", student1.getName());
        assertEquals("0701234567", student1.getPhoneNumber());
        assertEquals("abc12def@student.hig.se", student1.getEmail());

        StudentData student2 = studentDataList.get(1);
        assertEquals("4768", student2.getDob());
        assertEquals("Svensson, Nils", student2.getName());
        assertEquals("0735464190", student2.getPhoneNumber());
        assertEquals("def16oks@student.hig.se", student2.getEmail());
    }

    @Test
    public void parseStudentCSVDuplicate() throws Exception {
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
        csvLines.add("476839391839;Svensson, Nils;;MÅNGATAN 19;79161 GÄVLE;0735464190;def16oks@student.hig.se;");
        csvLines.add("458394829201;Anka, Kalle;;SUPERGATAN 6;34756 STOCKHOLM;0701234567;abc12def@student.hig.se;");
        csvLines.add("458394829201;Anka, Kalle;;SUPERGATAN 6;34756 STOCKHOLM;0701234567;abc12def@student.hig.se;");
        csvLines.add("458394829201;Anka, Kalle;;SUPERGATAN 6;34756 STOCKHOLM;0701234567;abc12def@student.hig.se;");
        csvLines.add("458394829201;Anka, Kalle;;SUPERGATAN 6;34756 STOCKHOLM;0701234567;abc12def@student.hig.se;");

        List<StudentData> studentDataList = CSVParser.parseStudentCSV(csvLines);

        assertEquals(2, studentDataList.size());
    }

}
