package database;

import data.StudentData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import organisations.Municipality;
import organisations.Region;
import roles.Admin;
import roles.Student;
import roles.VFUSamordnare;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DbContentTest {

    private DatabaseSelector databaseSelector;
    private DatabaseInserter databaseInserter;
    private DatabaseDeleter databaseDeleter;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        String testDbPath = Paths.get(tmpFolder.getRoot().getAbsolutePath(), "testdb.sqlite").toString();
        Database db = DatabaseHandler.getDatabase(testDbPath);
        databaseSelector = db.getSelector();
        databaseInserter = db.getInserter();
        databaseDeleter = db.getDeleter();
    }

    @Test
    public void getAdmin() throws Exception {
        Admin admin = databaseSelector.getAdmin();
        assertEquals("Vfuadmin", admin.getEmail());
        assertEquals("Vfuadmin", admin.getName());
        assertEquals("1000:868bb0bec884e0ba69d16fbb4a64b95788ad919cada7d60b:9d6378fca5fb77b6a281a584052ed43a19710076cec3e911", admin.getHashedPassword());
    }

    @Test
    public void insertAndSelectRegions() throws Exception {

        databaseInserter.addRegion(new Region("Södertälje2"));
        databaseInserter.addRegion(new Region("Gävle2"));

        List<String> regions = databaseSelector.getAllRegionNames();
        assertEquals(databaseSelector.getAllRegionNames().size(), regions.size());
        assertEquals("Gävle2", regions.get(2));
        assertEquals("Södertälje2", regions.get(6));
    }

    @Test
    public void regionExists() throws Exception {
        assertFalse(databaseSelector.regionExists("Gävle2"));

        databaseInserter.addRegion(new Region("Gävle2"));
        databaseInserter.addRegion(new Region("Södertälje2"));

        assertTrue(databaseSelector.regionExists("Gävle2"));
        assertTrue(databaseSelector.regionExists("Södertälje2"));
    }

    @Test
    public void getAllMunicipalitiesForRegion() throws Exception {
        Region r1 = new Region("Gävle2");
        databaseInserter.addRegion(r1);
        databaseInserter.addMunicipality(new Municipality("Söderhamn2", Arrays.asList(r1)));
        databaseInserter.addMunicipality(new Municipality("Stockholm2", Arrays.asList(r1)));

        List<String> munis1 = databaseSelector.getAllMunicipalitiesForRegion(r1.getName());
        assertEquals(2, munis1.size());
        assertEquals("Stockholm2", munis1.get(0));
        assertEquals("Söderhamn2", munis1.get(1));

        Region r2 = new Region("Södertälje2");
        databaseInserter.addRegion(r2);
        databaseInserter.addMunicipality(new Municipality("Växjö2", Arrays.asList(r2)));

        List<String> munis2 = databaseSelector.getAllMunicipalitiesForRegion(r2.getName());
        assertEquals(1, munis2.size());
        assertEquals("Växjö2", munis2.get(0));
    }

    @Test
    public void municipalityExists() throws Exception {
        assertFalse(databaseSelector.municipalityExists("Gävle2"));

        databaseInserter.addRegion(new Region("Gävle2"));
        databaseInserter.addMunicipality(
                new Municipality("Sandviken2", Arrays.asList(new Region("Gävle2")))
        );

        assertTrue(databaseSelector.municipalityExists("Sandviken2"));
    }

    @Test
    public void deleteRegionAndMuniContent() throws Exception {
        databaseInserter.addRegion(new Region("Gävle2"));
        assertTrue(databaseSelector.regionExists("Gävle2"));

        databaseInserter.addMunicipality(new Municipality("Söderhamn4", Arrays.asList(new Region("Gävle2"))));
        databaseInserter.addMunicipality(new Municipality("Söderhamn2", Arrays.asList(new Region("Gävle2"))));
        databaseInserter.addMunicipality(new Municipality("Söderhamn3", Arrays.asList(new Region("Gävle2"))));
        assertTrue(databaseSelector.municipalityExists("Söderhamn4"));
        assertTrue(databaseSelector.municipalityExists("Söderhamn2"));
        assertTrue(databaseSelector.municipalityExists("Söderhamn3"));

        databaseDeleter.deleteRegionAndMuniContent(new Region("Gävle2"));
        assertFalse(databaseSelector.regionExists("Gävle2"));
        assertFalse(databaseSelector.municipalityExists("Söderhamn4"));
        assertFalse(databaseSelector.municipalityExists("Söderhamn2"));
        assertFalse(databaseSelector.municipalityExists("Söderhamn3"));
    }

    @Test
    public void deleteMuniContent() throws Exception {
        databaseInserter.addRegion(new Region("Gävle2"));
        databaseInserter.addMunicipality(new Municipality("Sandviken2", Arrays.asList(new Region("Gävle2"))));
        assertTrue(databaseSelector.municipalityExists("Sandviken2"));

        databaseDeleter.deleteMuniContent("Sandviken2");
        assertFalse(databaseSelector.municipalityExists("Sandviken2"));
    }

    @Test
    public void addStudentFirstPage() throws Exception {
        assertNull(databaseSelector.getStudent("student@student.hig.se"));

        databaseInserter.addSingleStudent(
                new StudentData("student@student.hig.se", "name", "dob", "telnum")
        );
        databaseInserter.addRegion(new Region("Gävle2"));
        databaseInserter.addStudentFirstPage(
                "student@student.hig.se",
                new Region("Gävle2"),
                "Personal letter 123\n\nhej vad kul"
        );

        Student student = databaseSelector.getStudent("student@student.hig.se");
        assertEquals("student@student.hig.se", student.getEmail());
        assertEquals("Gävle2", student.getRegion().getName());
        assertEquals("Personal letter 123\n\nhej vad kul", student.getPersonalLetter());
    }

    @Test
    public void deleteVfuSam() throws Exception {
        String name = "name";
        String email = "kalle@vfu.se";
        String phone = "";
        Region region = new Region("Gävle");
        String hashedPassword = "";

        assertNull(databaseSelector.getVFUSamordnare(email));
        databaseInserter.addVFUSamordnare(new VFUSamordnare(name, email, phone, region, hashedPassword));
        assertNotNull(databaseSelector.getVFUSamordnare(email));

        databaseDeleter.deleteVFUSamContent(email);
        assertNull(databaseSelector.getVFUSamordnare(email));
    }

}
