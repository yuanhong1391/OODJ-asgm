/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import java.util.List;

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
    
    public void bookAppointment (String doctorId, String dateTime, String reason) 
    {
        String appointmentId = services.FileHelper.generateNextId("A", "data/appointments.txt");
        String status = "Pending";
        
        String record = String.join(",", appointmentId, getID(),getName(), doctorId, dateTime, reason, status);
        services.FileHelper.appendLine("data/appointments.txt", record);
    }
    
    public boolean cancelAppoinment(String appointmentId) 
    {
        List<String> appointments = services.FileHelper.readFile("data/appointments.txt");
        boolean updated = false;
        
        for (int i=0; i < appointments.size(); i++) 
        {
            String[] data = appointments.get(i).split(",");
            
            if (data.length >= 6 && data[0].equals(appointmentId)) 
            {
                data[5] = "Cancelled";
                appointments.set(i,String.join(",", data));
                updated = true;
                break;
            }
        }
        
        if (updated) 
        {
            services.FileHelper.writeFile("data/appointments.txt", appointments);
            return updated;
        }
        
        else 
        {
            return updated;
        }
        
    }
    
    public void submitFeedback(String doctorId, int rating, String comment) 
    {
        String feedbackId = services.FileHelper.generateNextId("FB", "data/feedbacks.txt");
        String today = java.time.LocalDate.now().toString();
        
        String record = String.join(",", feedbackId, getID(), doctorId, String.valueOf(rating), comment, today);
        services.FileHelper.appendLine("data/feedbacks.txt", record);
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