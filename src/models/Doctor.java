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
    return String.join("，",
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
    public void logVitaks(String patientID, String vitals) 
    {
        
    }
    
    // Method issue digital medication prescriptions
    public void issuePrescription(String patientID, String medicine)
    {
        
    }
    
    // Method issue requests to admin 
    public void requestsTest(String patientID, String testType) 
    {
        
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

