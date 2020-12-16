package com.example.tezu;

public class StudentDetails {

    public String Gender,StudentName,RollNumber,Email,Contact,School,Department,Programme,Hostel,ProfilePic;

    public StudentDetails(){

    }

    public StudentDetails(String gender, String studentName, String rollNumber, String email, String contact, String school, String department, String programme, String hostel, String profilePic) {
        Gender = gender;
        StudentName = studentName;
        RollNumber = rollNumber;
        Email = email;
        Contact = contact;
        School = school;
        Department = department;
        Programme = programme;
        Hostel = hostel;
        ProfilePic = profilePic;
    }
}
