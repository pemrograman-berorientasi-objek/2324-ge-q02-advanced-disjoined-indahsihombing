package academic.model;

/**
 * @author NIM Nama
 * @author NIM Nama
 */
public class Student {

     //deklarasi
     private String id;
     private String name;
     private int year;
     private String major;
 
     public Student(String id, String name, int year, String major) {
         this.id = id;
         this.name = name;
         this.year = year;
         this.major = major;
     }
 
     public String getId() {
         return id;
     }
 
     public String getName() {
         return name;
     }
 
     public int getYear() {
         return year;
     }
 
     public String getMajor() {
         return major;
     }
 
 }