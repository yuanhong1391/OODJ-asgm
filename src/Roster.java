/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managementsystem;

/**
 *
 * @author tengk
 */
public class Roster 
{
    private String rosterID;
    private String doctorID;
    private String shiftDate;
    private String shiftType;
    
    public Roster(String roasterID,String doctorID,String shiftDate,String shiftType)
    {
        this.rosterID = rosterID;
        this.doctorID = doctorID;
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
    }
    public String getRosterID()
    {
        return rosterID;
    }
    public String getDoctorID()
    {
        return doctorID;
    }
    public String getShiftDate()
    {
        return shiftDate;
    }
    public String getShiftType()
    {
        return shiftType;
    }
    
    public void setDoctorID(String doctorID)
    {
        this.doctorID = doctorID;
    }
    public void setShiftDate(String shiftDate)
    {
        this.shiftDate = shiftDate;
    }
    public void setShiftType(String shiftType)
    {
        this.shiftType = shiftType;
    }
}
