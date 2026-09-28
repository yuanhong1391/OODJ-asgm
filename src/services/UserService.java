/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.Doctor;
import models.Patient;
import models.User;

/**
 *
 * @author Tei Yuan Hong
 */
public class UserService {
    //Points to user.txt 
    private String filename = "data/users.txt";
    
    //read all user inside the txt file
    public List<User> loadAllUsers() 
    {
        //using arraylist to avoid limit of row
        List<User> userList = new ArrayList<User>();
        File file = new File(filename);
         
        //check file exits or not
        if (!file.exists())
        {
            return userList;
        }
        
        BufferedReader input = null;
        try{
            input = new BufferedReader(new FileReader(filename));
            String line;
            // read every line inside txt.file until content of the line is empty(null)
            while ((line = input.readLine()) != null) 
            {
                //trim used to delete sysbol /n
                line = line.trim();
                //if this line didn't have contect skip (avoid txtfile exits some line double new line)
                if (line.equals("")) {
                continue;}
            
            // using split to split the entire line into data by the , sysbol
            String[] data = line.split(",");
            
            String id = data[0];
            String username = data[1];
            String password = data [2];
            String name = data[3];
            String phone = data [4];
            String email = data [5];
            String role = data [6];
            
            // based on role to adjust the attributes
            // if doctor
            if (role.equalsIgnoreCase("Doctor")) {
            String specialization = data[7];
            String roomNumber = data[8];
            Doctor doc = new Doctor(id, username, password, name, phone, email, specialization, roomNumber);
            userList.add(doc);
            }
            //if patient
            else if (role.equalsIgnoreCase("Patient")) 
            {
                String bloodType = data[7];
                String emergencyContact = data[8];
                String medicalHistory = data[9];
                Patient pat = new Patient(id, username, password, name, phone, email, bloodType, emergencyContact, medicalHistory);
                userList.add(pat);
            }
            }
            input.close();
            } catch (IOException e) {
            e.printStackTrace();}
            return userList;
        } 
    public boolean appendUser (User user) {
        BufferedWriter output = null;
        
        try {
            //FileWriter(filename, true) true means just add new contant and no change previous one
            output = new BufferedWriter (new FileWriter (filename, true));
            
            //reuse method in user class (toTxtRecord)
            output.write(user.toTxtRecord());
            //once finish add a new user, add new line avoid next user in the same line
            output.newLine();
            
            output.close();
            return true;
        } catch (IOException e) {
        e.printStackTrace();
        return false;}
    }
    
    
    public boolean saveAllUsers(List<User> userList) 
    {
        BufferedWriter output = null;
        
        try
        {
            //FileWritter false means empty entire txt file and rewrite it
            output = new BufferedWriter(new FileWriter(filename, false));
            //looping to rewrite the txt file
            for (int i = 0; i < userList.size(); i++) 
            {
                User u = userList.get(i);
                output.write(u.toTxtRecord());
                output.newLine();
            }
            output.close();
            return true;
            
        } catch (IOException e) {
        e.printStackTrace();
        return false;}
    }
    
    public boolean updateUserProfile(String userId, String newName, String newPhone, String newEmail) 
    {
        //load every user data into list
        List<User> list = loadAllUsers();
        boolean found = false;
        
        //loop every line in the list
        for (int i =0; i< list.size(); i++) 
        {
            User u = list.get(i);
            //find which user match the userID
            if (u.getID().equals(userId)) 
            {
                //rewrite the profile 
                u.updateProfile(newName, newPhone, newEmail);
                //change found to true to tell system success find the user 
                found = true;
                break;
                
            }
        }
        
        //if user have been found rewrite all data to save it inside the txt file
        if (found) 
        {
            return saveAllUsers(list);
            
        }
        return false;
    }
    }
