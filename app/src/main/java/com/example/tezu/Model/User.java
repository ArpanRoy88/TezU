package com.example.tezu.Model;

public class User {
    private String Contact;
    private String Department;
    private String Email;
    private String Gender;
    private String Hostel;
    private String Programme;
    private String RollNumber;
    private String School;
    private String StudentName;
    private String ProfilePic;


    public User(String contact, String department, String email, String gender, String hostel, String programme, String rollNumber, String school, String studentName, String profilePic) {
        Contact = contact;
        Department = department;
        Email = email;
        Gender = gender;
        Hostel = hostel;
        Programme = programme;
        RollNumber = rollNumber;
        School = school;
        StudentName = studentName;
        ProfilePic = profilePic;
    }

    public User(){
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getHostel() {
        return Hostel;
    }

    public void setHostel(String hostel) {
        Hostel = hostel;
    }

    public String getProgramme() {
        return Programme;
    }

    public void setProgramme(String programme) {
        Programme = programme;
    }

    public String getRollNumber() {
        return RollNumber;
    }

    public void setRollNumber(String rollNumber) {
        RollNumber = rollNumber;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
}

