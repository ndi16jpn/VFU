package matching;

import database.*;
import organisations.Place;

import roles.Student;

import java.util.*;

/**
 * En klass som hanterar matchningen av studenter och platser
 */
public class Match {
    /**
     * Matchar en student till en plats i databasen
     * @param student
     * @param place
     * @return
     */
    public MatchStudentPlace manualMatch(Student student, Place place){
            return new MatchStudentPlace(student,place);

    }
    private List<Place> unMatchedPlaces(List<Place> placeList){

        List<Place> unMatchedPlaces = new ArrayList<>();
        for(Place p: placeList){
            if (p.getStudent()==null){
                unMatchedPlaces.add(p);
            }
        }


        return unMatchedPlaces;
    }
     private List<Student> getUnmatchedStudents(List<Student> studentList){
         List<Place> placeList = new ArrayList<>();
         try {
             placeList = DatabaseHandler.getDatabase().getSelector().getAllPlaces();
         } catch (DatabaseException e) {
             e.printStackTrace();
         }

         placeList.removeAll(unMatchedPlaces(placeList));
         List<Student> unMatchedStudentList= new ArrayList<>(studentList);
         for(Place place: placeList){
             for(int i=0;i<unMatchedStudentList.size();i++) {
                 if (place.getStudent().getEmail().equals(unMatchedStudentList.get(i).getEmail())){
                     unMatchedStudentList.remove(i);
                 }
             }
         }
         List<Student> students = new ArrayList<>();
         for(Student student: unMatchedStudentList){
             if(student.getChoice_1().getId()!=0){
                 students.add(student);

             }
         }


         return students;

    }


    /**
     * Gör en automatisk matchning för en lista med platser och en lista med studenter
     * @param placeList
     * @param studentList
     * @return
     */
    public List<MatchStudentPlace> getMatchEachPlace(List<Place> placeList, List<Student> studentList){
        List<MatchStudentPlace>matchList = new ArrayList<>();
        List<Place> unmatchedPlaceList = unMatchedPlaces(placeList);
        List<Student> unmatchedStudentList = getUnmatchedStudents(studentList);
        if(unmatchedStudentList.size()>0){
            for(Place place: unmatchedPlaceList){
                if(place.getStudent()==null) {
                    List<Student> newStudentList = new ArrayList<>();
                    for (Student student : unmatchedStudentList) {
                        if(student.getChoice_1().getId()==place.getUnit().getId()){
                            newStudentList.add(student);
                        }
                    }
                    if(!newStudentList.isEmpty()){
                        int randomNumber = new Random().nextInt(newStudentList.size());
                        matchList.add(new MatchStudentPlace(newStudentList.get(randomNumber),place));
                        place.setStudent(newStudentList.get(randomNumber));
                        unmatchedStudentList.remove(place.getStudent());
                    }
                }
            }

            for(Place place: unmatchedPlaceList){
                if(place.getStudent()==null) {

                    List<Student> newStudentList = new ArrayList<>();
                    for (Student student : unmatchedStudentList) {
                        if(student.getChoice_2().getId()==place.getUnit().getId()){
                            newStudentList.add(student);
                        }
                    }
                    if(!newStudentList.isEmpty()){
                        int randomNumber = new Random().nextInt(newStudentList.size());
                        matchList.add(new MatchStudentPlace(newStudentList.get(randomNumber),place));
                        place.setStudent(newStudentList.get(randomNumber));
                        unmatchedStudentList.remove(place.getStudent());
                    }
                }
            }

            for(Place place: unmatchedPlaceList){
                if(place.getStudent()==null) {

                    List<Student> newStudentList = new ArrayList<>();
                    for (Student student : unmatchedStudentList) {
                        if(student.getChoice_3().getId()==place.getUnit().getId()){
                            newStudentList.add(student);
                        }
                    }
                    if(!newStudentList.isEmpty()){
                        int randomNumber = new Random().nextInt(newStudentList.size());
                        matchList.add(new MatchStudentPlace(newStudentList.get(randomNumber),place));
                        place.setStudent(newStudentList.get(randomNumber));
                        unmatchedStudentList.remove(place.getStudent());
                    }
                }
            }


            for(Place place: unmatchedPlaceList){
                if(place.getStudent()==null) {

                    List<Student> newStudentList = new ArrayList<>();
                    for (Student student : unmatchedStudentList) {
                        if(student.getChoice_4().getId()==place.getUnit().getId()){
                            newStudentList.add(student);
                        }
                    }
                    if(!newStudentList.isEmpty()){
                        int randomNumber = new Random().nextInt(newStudentList.size());
                        matchList.add(new MatchStudentPlace(newStudentList.get(randomNumber),place));
                        place.setStudent(newStudentList.get(randomNumber));
                        unmatchedStudentList.remove(place.getStudent());
                    }
                }
            }


            for(Place place: unmatchedPlaceList){
                if(place.getStudent()==null) {

                    List<Student> newStudentList = new ArrayList<>();
                    for (Student student : unmatchedStudentList) {
                        if(student.getChoice_5().getId()==place.getUnit().getId()){
                            newStudentList.add(student);
                        }
                    }
                    if(!newStudentList.isEmpty()){
                        int randomNumber = new Random().nextInt(newStudentList.size());
                        matchList.add(new MatchStudentPlace(newStudentList.get(randomNumber),place));
                        place.setStudent(newStudentList.get(randomNumber));
                        unmatchedStudentList.remove(place.getStudent());
                    }
                }
            }

            for(Place place: unmatchedPlaceList){
                if(place.getStudent()==null) {

                    List<Student> newStudentList = new ArrayList<>();
                    for (Student student : unmatchedStudentList) {
                        if(student.getRegion().getName().equals(place.getUnit().getMunicipality().getRegion().getName())&& student.getChoice_1().getId()!=0){
                            newStudentList.add(student);
                        }
                    }
                    if(!newStudentList.isEmpty()){
                        int randomNumber = new Random().nextInt(newStudentList.size());
                        matchList.add(new MatchStudentPlace(newStudentList.get(randomNumber),place));
                        place.setStudent(newStudentList.get(randomNumber));
                        unmatchedStudentList.remove(place.getStudent());
                    }
                }
            }
        }
        return matchList;
    }
}
