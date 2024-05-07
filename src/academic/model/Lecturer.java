package academic.model;

public class Lecturer implements Person {
   // deklarasi
   private String id;
   private String name;
   private String initials;
   private String email;
   private String studyProgram;

   public Lecturer(String id, String name, String initials, String email, String studyProgram) {
       this.id = id;
       this.name = name;
       this.initials = initials;
       this.email = email;
       this.studyProgram = studyProgram;
   }

   @Override
   public String getId() {
       return id;
   }

   @Override
   public String getName() {
       return name;
   }

   @Override
   public void setId(String id) {
       this.id = id;
   }

   @Override
   public void setName(String name) {
       this.name = name;
   }

   public String getInitials() {
       return initials;
   }

   public String getEmail() {
       return email;
   }

   public String getStudyProgram() {
       return studyProgram;
   }
}

