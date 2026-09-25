package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author tengk
 */
public class Manager 
{
    private String managerID;
    private String managerName;
    private String Email;
    private String Phone;
    
    public Manager(String managerID,String managername,String email,String phone)
    {
        this.managerID=managerID;
        this.managerName=managerName;
        this.Email=Email;
        this.Phone=Phone;
    }
    
    public String getID()
    {
        return managerID;
    }
    
    public String getName()
    {
        return managerName;
    }
    public String getEmail()
    {
        return Email;
    }
    public String getPhone()
    {
        return Phone;
    }
    public void setName(String managerName)
    {
        this.managerName=managerName;
    }
    public void setEmail(String Email)
    {
        this.Email=Email;
    }
    public void setPhone(String Phone)
    {
        this.Phone=Phone;
    }
}
