/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managementsystem;

/**
 *
 * @author tengk
 */
public class Department 
{
    private String departmentID;
    private String departmentName;
    private String description;
    
    public Department(String departmentID,String departmentName,String description)
    {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.description = description;
    }
    public String getDepartmentID()
    {
        return departmentID;
    }
    public String getDepartmentName()
    {
        return departmentName;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}
