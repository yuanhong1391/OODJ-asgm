package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author tengk
 */
public class Manager extends User  
{
    
    public Manager(String id,String username,String password,String name, String phone, String email)
    {
        super(id, username, password, name, phone, email, "Manager");
    }
    
    @Override
    public String toTxtRecord() 
    {
        return String.join(",", 
                getID(),
                getUsername(),
                getPassword(),
                getName(),
                getPhone(),
                getEmail(),
                getRole()
                
                );
    }
}
    
    
