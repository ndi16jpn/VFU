package matching;


import data.StudentData;
import matching.Match;
import matching.MatchStudentPlace;
import org.junit.Before;
import org.junit.Test;
import organisations.Municipality;
import organisations.Place;
import organisations.Region;
import organisations.Unit;
import roles.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class MatchTest {
    private List<Student> studentList = new ArrayList<>();
    private List<Place> placeList = new ArrayList<>();
    private Match match = new Match();
    private List<MatchStudentPlace> matchStudentPlaceList = new ArrayList<>();
    private Region regionGavle = new Region("Gävle");
    private Municipality gavle = new Municipality("Gävle");
    private Unit socialtjansten = new Unit(gavle,"Socialtjänsten",5,"hurp");
    private Unit forsakringskassan = new Unit(gavle,"Försäkringskassan",5,"durp");


    @Before
    public void setUp() {
        for(int i=0;i<10;i++) {
            studentList.add(new Student("hej" + i + "@email.com", new StudentData("hej" + i + "@email.com", "Adam" + i, String.valueOf(1990 + i), "07012345" + i), regionGavle, "Hej", socialtjansten, forsakringskassan, null, null, null));
        }
        for(int i = 1; i< studentList.size()+1; i++){
            placeList.add(new Place(i,socialtjansten, false));
            placeList.add(new Place(i+5,forsakringskassan, false));
        }
    }

    @Test
    public void testGetMatchEachPlace(){
        Match match = new Match();
        matchStudentPlaceList = match.getMatchEachPlace(placeList, studentList);


        for(int i=0;i<matchStudentPlaceList.size();i++){
            for(int j=i+1;j<matchStudentPlaceList.size();j++){
                assertNotEquals(matchStudentPlaceList.get(i),matchStudentPlaceList.get(j));
            }
        }
    }
    @Test
    public void testManualMatch(){
        Student student = new Student("fred@hej.se",new StudentData("fred@hej.se","Fred","1990","0701234567"),regionGavle,"hej",socialtjansten,forsakringskassan,null,null,null);
        MatchStudentPlace matchStudentPlace = match.manualMatch(student, placeList.get(0));
        matchStudentPlaceList.add(matchStudentPlace);

        assertNotNull(matchStudentPlace);
        assertEquals(student,matchStudentPlace.getStudent());
        assertNotNull(matchStudentPlaceList.get(0));
        assertEquals("fred@hej.se",matchStudentPlaceList.get(0).getStudent().getEmail());
    }
}