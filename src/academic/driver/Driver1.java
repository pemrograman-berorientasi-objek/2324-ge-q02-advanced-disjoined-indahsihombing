package academic.driver;

import academic.model.*;
import java.util.*;

public class Driver1 {

    // Static nested class for GPA calculation utility
    static class GPAUtility {
        static double calculateGPA(Map<String, String> grades, Map<String, Integer> credits) {
            double totalGradePoints = 0;
            int totalCredits = 0;

            for (String courseCode : grades.keySet()) {
                String grade = grades.get(courseCode);
                int credit = credits.get(courseCode);

                double gradePoints;
                switch (grade) {
                    case "A":
                        gradePoints = 4.0;
                        break;
                    case "AB":
                        gradePoints = 3.5;
                        break;
                    case "B":
                        gradePoints = 3.0;
                        break;
                    case "BC":
                        gradePoints = 2.5;
                        break;
                    case "C":
                        gradePoints = 2.0;
                        break;
                    case "D":
                        gradePoints = 1.0;
                        break;
                    default:
                        gradePoints = 0.0;
                        break;
                }
                totalGradePoints += gradePoints * credit;
                totalCredits += credit;
            }

            return totalCredits == 0 ? 0 : totalGradePoints / totalCredits;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Lecturer> lecturers = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        ArrayList<Enrollment> enrollmentGrade = new ArrayList<>();
        ArrayList<Students_detail> studentDetail = new ArrayList<>();

        ArrayList<CourseOpening> courseOpenings = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            String[] parts = input.split("#");

            if (parts[0].equals("lecturer-add")) {
                Lecturer lecturer = new Lecturer(parts[1], parts[2], parts[3], parts[4], parts[5]);
                lecturers.add(lecturer);
            } else if (parts[0].equals("student-add")) {
                Student student = new Student(parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
                students.add(student);
                // Add student details
                Students_detail studentDetail1 = new Students_detail(student.getId(), student.getName(), student.getYear(), student.getMajor(), 0, 0);
                studentDetail.add(studentDetail1);
            } else if (parts[0].equals("course-add")) {
                Course course = new Course(parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
                courses.add(course);
            } else if (parts[0].equals("enrollment-add")) {
                Enrollment enrollment = new SimpleEnrollment(parts[1], parts[2], parts[3], parts[4], "None");
                enrollments.add(enrollment);
            } else if (parts[0].equals("enrollment-grade")) {
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getCourseCode().equals(parts[1]) && enrollment.getStudentId().equals(parts[2]) && enrollment.getAcademicYear().equals(parts[3]) && enrollment.getSemester().equals(parts[4])) {
                        enrollment.setGrade(parts[5]);
                        enrollment.setRemedial(parts[5]);
                        enrollmentGrade.add(enrollment); // Add to enrollmentGrade list
                        break;
                    }
                }
            } else if (parts[0].equals("student-details")) {
                String studentId = parts[1];
                Student student = null;
                for (Student students1 : students) {
                    if (students1.getId().equals(studentId)) {
                        student = students1;
                        break;
                    }
                }
                if (student == null) {
                    continue;
                }
            
                Map<String, String> lastGrade = new HashMap<>();
                Map<String, Integer> lastCredit = new HashMap<>();
                
                // Iterate through enrollments including remedial grades
                for (Enrollment enrollment : enrollmentGrade) {
                    if (enrollment.getStudentId().equals(studentId)) {
                        String courseCode = enrollment.getCourseCode();
                        String grade = enrollment.getRemedial().equals("None") ? enrollment.getGrade() : enrollment.getRemedial(); // Use remedial grade if exists
                        lastGrade.put(courseCode, grade);
                        lastCredit.put(courseCode, courses.stream().filter(c -> c.getCode().equals(courseCode)).findFirst().orElseThrow().getCredits());
                    }
                }
            
                // Calculate GPA using static nested class GPAUtility
                double gpa = GPAUtility.calculateGPA(lastGrade, lastCredit);

                // Calculate total credits
                int totalCredits = lastCredit.values().stream().mapToInt(Integer::intValue).sum();

                // Create Students_detail object and add to list
                Students_detail studentDetail1 = new Students_detail(student.getId(), student.getName(), student.getYear(), student.getMajor(), (float) gpa, totalCredits);
                studentDetail.add(studentDetail1);



                // Print students detail
                System.out.println(String.format("%s|%s|%s|%s|%.2f|%d", studentDetail1.getId(), studentDetail1.getName(), studentDetail1.getYear(), studentDetail1.getMajor(), studentDetail1.getGpa(), studentDetail1.getcredit()));
            
            } else if (parts[0].equals("enrollment-remedial")) {
                String courseCode = parts[1];
                String studentId = parts[2];
                String academicYear = parts[3];
                String semester = parts[4];
                String remedialGrade = parts[5];
            
                // Cek apakah kursus dan siswa valid
                boolean validCourse = courses.stream().anyMatch(course -> course.getCode().equals(courseCode));
                boolean validStudent = students.stream().anyMatch(student -> student.getId().equals(studentId));
                if (!validCourse || !validStudent) {
                    continue;
                }
            
                // Cek apakah ada nilai sebelumnya untuk kursus dan siswa yang sama
                boolean hasPreviousGrade = enrollments.stream().anyMatch(enrollment ->
                        enrollment.getCourseCode().equals(courseCode) &&
                        enrollment.getStudentId().equals(studentId) &&
                        enrollment.getAcademicYear().equals(academicYear) &&
                        enrollment.getSemester().equals(semester)
                );
                if (!hasPreviousGrade) {
                    continue;
                }
            
                // Tambahkan nilai remedial
                Enrollment remedialEnrollment = new SimpleEnrollment(courseCode, studentId, academicYear, semester, remedialGrade);
                enrollmentGrade.add(remedialEnrollment);
            
                // Perbarui nilai remedial dan status
                enrollmentGrade.stream()
                        .filter(enrollment ->
                                enrollment.getCourseCode().equals(courseCode) &&
                                enrollment.getStudentId().equals(studentId) &&
                                enrollment.getAcademicYear().equals(academicYear) &&
                                enrollment.getSemester().equals(semester)
                        )
                        .forEach(enrollment -> {
                            if (!enrollment.getRemedial().equals("None")) {
                                enrollment.setRemedial(remedialGrade + "(" + enrollment.getRemedial() + ")");
                            } else {
                                enrollment.setRemedial(remedialGrade);
                            }
                        });
            
            } else if (parts[0].equals("course-open")) {
                String[] lecturerInitials = parts[4].split(",");
                List<Lecturer> courseLecturers = new ArrayList<>();
                for (String initial : lecturerInitials) {
                    for (Lecturer lecturer : lecturers) {
                        if (lecturer.getInitials().equals(initial)) {
                            courseLecturers.add(lecturer);
                            break;
                        }
                    }
                }
                CourseOpening courseOpening = new CourseOpening(parts[1], parts[2], parts[3], courseLecturers);
                courseOpenings.add(courseOpening);
            } else if (parts[0].equals("course-history")) {
                String courseCode = parts[1];
                String codeCourse = null;

                for (Course course : courses) {
                    if (course.getCode().equals(courseCode)) {
                        codeCourse = course.getCode();
                        break;
                    }
                }

                if (codeCourse == null) {
                    continue;
                }
                Collections.sort(courseOpenings, new Comparator<CourseOpening>() {
                    @Override
                    public int compare(CourseOpening co1, CourseOpening co2) {
                        // "odd" semesters come before "even" semesters
                        if (co1.getSemester().equals("odd") && co2.getSemester().equals("even")) {
                            return -1;
                        } else if (co1.getSemester().equals("even") && co2.getSemester().equals("odd")) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

                for (Course course : courses) {
                    if (course.getCode().equals(codeCourse)) {
                        for (CourseOpening courseOpening : courseOpenings) {
                            if (courseOpening.getCourseCode().equals(codeCourse)) {
                                System.out.println(course.getCode() + "|" + course.getName() + "|" + course.getCredits() + "|" + course.getPassingGrade() + "|" + courseOpening.getAcademicYear() + "|" + courseOpening.getSemester() + "|" + getLecturersInfo(courseOpening.getLecturers()));
                                for (Enrollment enrollment : enrollments) {
                                    if (enrollment.getCourseCode().equals(codeCourse) && enrollment.getAcademicYear().equals(courseOpening.getAcademicYear()) && enrollment.getSemester().equals(courseOpening.getSemester())) {
                                        if (enrollment.getRemedial().equals("None")) {
                                            System.out.println(enrollment.getCourseCode() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade());
                                        } else {
                                            System.out.println(enrollment.getCourseCode() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getRemedial());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }  else if (parts[0].equals("student-transcript")) {
                String studentId = parts[1];
                Student student = null;
                for (Student students1 : students) {
                    if (students1.getId().equals(studentId)) {
                        student = students1;
                        break;
                    }
                }
                if (student == null) {
                    continue;
                }
            
                Map<String, String> lastGrade = new HashMap<>();
                Map<String, Integer> lastCredit = new HashMap<>();
                
                Collections.sort(enrollmentGrade, new Comparator<Enrollment>() {
                    @Override
                    public int compare(Enrollment e1, Enrollment e2) {
                        return e1.getAcademicYear().compareTo(e2.getAcademicYear());
                    }
                });
                // Iterate through enrollments including remedial grades
                for (Enrollment enrollment : enrollmentGrade) {
                    // urutkan berdasarkan tahun ajaran
                    if (enrollment.getStudentId().equals(studentId)) {
                        String courseCode = enrollment.getCourseCode(); // digunakan untuk last credit
                        String grade = enrollment.getRemedial().equals("None") ? enrollment.getGrade() : enrollment.getRemedial(); // Use remedial grade if exists
                        lastGrade.put(courseCode, grade);
                        lastCredit.put(courseCode, courses.stream().filter(c -> c.getCode().equals(courseCode)).findFirst().orElseThrow().getCredits());
                        // System.out.println(enrollment.getCourseCode() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + (enrollment.getRemedial().equals("None") ? enrollment.getGrade() : enrollment.getRemedial())+"|"+courses.stream().filter(c -> c.getCode().equals(courseCode)).findFirst().orElseThrow().getCredits()+"----");
                    }
                }

                
            
                // Calculate GPA using static nested class GPAUtility
                double gpa = GPAUtility.calculateGPA(lastGrade, lastCredit);

                // Calculate total credits
                int totalCredits = lastCredit.values().stream().mapToInt(Integer::intValue).sum();

                // Create Students_detail object and add to list
                Students_detail studentDetail1 = new Students_detail(student.getId(), student.getName(), student.getYear(), student.getMajor(), (float) gpa, totalCredits);
                studentDetail.add(studentDetail1);

                // Print students detail
                System.out.println(String.format("%s|%s|%s|%s|%.2f|%d", studentDetail1.getId(), studentDetail1.getName(), studentDetail1.getYear(), studentDetail1.getMajor(), studentDetail1.getGpa(), studentDetail1.getcredit()));
                
                // Create a map to store the latest enrollment for each course
                Map<String, Enrollment> latestEnrollments = new HashMap<>();

                // Iterate through enrollments
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getStudentId().equals(studentId)) {
                        String courseCode = enrollment.getCourseCode();
                        // If this is the first enrollment for this course or if this enrollment is more recent than the one stored
                        if (!latestEnrollments.containsKey(courseCode) || enrollment.getAcademicYear().compareTo(latestEnrollments.get(courseCode).getAcademicYear()) > 0) {
                            latestEnrollments.put(courseCode, enrollment);
                        }
                    }
                }
                // Print latest enrollment for each course
                for (Enrollment enrollment : latestEnrollments.values()) {
                    String grade = enrollment.getRemedial().equals("None") ? enrollment.getGrade() : enrollment.getRemedial();
                    System.out.println(enrollment.getCourseCode() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + grade);
                }

            } else if (parts[0].equals("find-the-best-student")) {
              String year=parts[1];
              String semester=parts[2];
                
              Collections.sort(enrollmentGrade, new Comparator<Enrollment>() {
                @Override
                public int compare(Enrollment e1, Enrollment e2) {
                    return e1.getGrade().compareTo(e2.getGrade());
                }
            });
                
              for( Enrollment enrollment1:enrollments){
                if(year.equals(enrollment1.getAcademicYear())&& semester.equals(enrollment1.getSemester())){
                    enrollment1.getStudentId();

                }
              }  
                
            }else if (input.equals("---")) {


                // Print lecturers
                for (Lecturer lecturer1 : lecturers) {
                    System.out.println(lecturer1.getId() + "|" + lecturer1.getName() + "|" + lecturer1.getInitials() + "|" + lecturer1.getEmail() + "|" + lecturer1.getStudyProgram());
                }
                // Print courses
                for (Course course1 : courses) {
                    System.out.println(course1.getCode() + "|" + course1.getName() + "|" + course1.getCredits() + "|" + course1.getPassingGrade());
                }
                // Print students
                for (Student student1 : students) {
                    System.out.println(student1.getId() + "|" + student1.getName() + "|" + student1.getYear() + "|" + student1.getMajor());
                }
                // Print enrollment grade for each student
                for (Enrollment enrollment : enrollments) {
                    if(enrollment.getRemedial().equals("None"))
                        System.out.println(enrollment.getCourseCode() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade());
                         
                  else {
                    System.out.println(enrollment.getCourseCode() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getRemedial());
    
                        }
                    }
                System.exit(0);
            }
        }
    }


    private static String getLecturersInfo(List<Lecturer> lecturers) {
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < lecturers.size(); i++) {
            Lecturer lecturer = lecturers.get(i);
            info.append(lecturer.getInitials()).append(" (").append(lecturer.getEmail()).append(")");
            if (i < lecturers.size() - 1) {
                info.append(",");
            }
        }
        return info.toString();
    }
}
