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
    
    
    
}
