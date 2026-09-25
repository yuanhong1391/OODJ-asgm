/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Tei Yuan Hong
 */
public abstract class User {
    // basic details for user
    private String id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String role;
    
    // add function
    public User(String id, String username, String password, String name, String phone, String email, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }
    
    //get user details
    public String getUserDetails() {
        return String.format("ID: %s | Name: %s | Role: %s | Phone: %s | Email: %s", id, name, role, phone, email);
    }
    
    //update user detail
    public void updateProfile(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    
    //abstarct method: convert into single line in txt file
    
    public abstract String toTxtRecord();
    
    //get user specific details (password, name and so on)
    
    public String getID() {
    return id;
    }
    
    public String getUsername() {
    return username;}
    
    public String getName() {
    return name;}
    
    public String getPassword() {
    return password;}
    
    public String getPhone() {
    return phone;}
    
    public String getEmail() {
    return email;}
    
    public String getRole() {
    return role;}
    
    //set user specific details 
    
    public void setID (String id) {
    this.id = id;}
    
    public void setUsername (String username) {
    this.username = username;}
    
    public void setName (String name) {
    this.name = name;}
    
    public void setPhone (String phone) {
    this.phone = phone;}
    
    public void setEmail (String email) {
    this.email = email;}
    
    public void setRole (String role) {
    this.role = role;}
}
