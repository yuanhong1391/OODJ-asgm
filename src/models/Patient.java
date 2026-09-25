/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Tei Yuan Hong
 */
public class Patient extends User{
    //Patient specific attributes
    private String bloodType;
    private String emergencyContact;
    private String medicalHistory;
    
    //Create function
    public Patient(String id, String username, String password, String name, String phone, String email, String bloodType, String emergencyContact, String medicalHistory) 
    {
        super(id, username, password, name, phone, email, "Patient");
        this.bloodType = bloodType;
        this.emergencyContact = emergencyContact;
        this.medicalHistory = medicalHistory;
    }
    
    @Override
    public String toTxtRecord() {
        return String.join (",", 
                getID(),
                getUsername(),
                getPassword(),
                getName(),
                getPhone(),
                getEmail(),
                getRole(),
                bloodType,
                emergencyContact,
                medicalHistory);
        
    }
    
    //rewrite update profile, allow patients update emergency contact number
    public void updateProfile(String name, String phone, String email, String emergencyContact) 
    {
        super.updateProfile(name, phone, email);
        this.emergencyContact = emergencyContact;
    }
    
    public void bookAppointment (String doctorId, String dateTime) 
    {
        
    }
    
    public void cancelAppoinment(String appointmentId) 
    {
        
    }
    
    public void submitFeedback(String doctorId, int rating, String comment) 
    {
        
    }
    
    //Patients specific get and set
    public String getBloodType() {
    return bloodType;}
    
    public String getEmergencyContact() {
    return emergencyContact;}
    
    public String getMedicalHistory() {
    return medicalHistory;}
    
    
    public void setBloodType(String bloodType) {
    this.bloodType = bloodType;}
    
    public void setEmergencyContact(String emergencyContact){
    this.emergencyContact = emergencyContact;}
    
    public void setMedicalHistory(String medicalHistory) {
    this.medicalHistory = medicalHistory;}
    
}