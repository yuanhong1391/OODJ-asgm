/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Tei Yuan Hong
 */
public class Doctor extends User{
    //doctor specific attributes
    private String specialization;
    private String roomNumber;
    
    //create function for doctor
    public Doctor(String id, String username, String password, String name, String phone, String email, String specialization, String roomNumber) 
    {
        super(id, username, password, name, phone, email, "Doctor");
        this.specialization = specialization;
        this.roomNumber = roomNumber;
    }
    
    //Override user abstarct
    @Override
    public String toTxtRecord() {
    return String.join(",",
            getID(),
            getUsername(),
            getPassword(),
            getName(),
            getPhone(),
            getEmail(),
            getRole(),
            specialization,
            roomNumber
            );
    }
    
    // Method log patient vital signs and write consultation notes
    public void logVitals(String patientID, String vitals, String notes) 
    {
        //create a specific id for every vital
        String vitalId = services.FileHelper.generateNextId("V","data/vitals.txt");
        String today = java.time.LocalDate.now().toString();
        
        //format of virtals.txt
        String record = String.join(",", vitalId, patientID, getID(), today, vitals, notes);
        
        services.FileHelper.appendLine("data/vitals.txt", record);
    }
    
    // Method issue digital medication prescriptions
    public void issuePrescription(String patientID, String medicine, String dosage)
    {
        String prescriptionId = services.FileHelper.generateNextId("PR", "data/prescription.txt");
        String today = java.time.LocalTime.now().toString();
        String status = "Pending";
        
        String record = String.join(",", prescriptionId, getID(), today ,medicine, dosage, status);
        
        services.FileHelper.appendLine("data/prescriptions.txt", record);
    }
    
    // Method issue requests to admin 
    public void requestsTest(String patientID, String testType) 
    {
        String testId = services.FileHelper.generateNextId("T", "data/lab_tests.txt");
        String today = java.time.LocalDate.now().toString();
        String result = "none";
        String status = "Requested";
        
        String record = String.join(",", testId, patientID, getID(), today, testType, result, status);
        services.FileHelper.appendLine("data/lab_tests.txt", record);
    }
    
    //Get and set for doctor specific attibutes
    public String getSpecialization() 
    {
        return specialization;
    }
    
    public String getRoomNumber() 
    {
        return roomNumber;
    }
    
    public void setSpecialization(String specialization) 
    {
        this.specialization = specialization;
    }
    
    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }
    
}   

