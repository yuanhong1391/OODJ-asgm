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

/**
 *
 * @author Tei Yuan Hong
 */
public class FileHelper {
    //universal read the file method
    public static List<String> readFile(String filePath) 
    {
        List<String> lines = new ArrayList<String>();
        File file = new File(filePath);
        if (!file.exists())
        {
            return lines;
        }
        
        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                if (!line.equals("")){
                lines.add(line);}
            }
            reader.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return lines;
    }
    
    public static boolean appendLine(String filePath, String record) 
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(record);
            writer.newLine();
            writer.close();
            return true;
            
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static String generateNextId(String prefix, String filePath) 
    {
        List<String> lines = readFile(filePath);
            int max = 0;
            for (String line : lines) {
                String id = line.split(",")[0].trim();
                if (id.toUpperCase().startsWith(prefix.toUpperCase())) {
                    try {
                        int n = Integer.parseInt(id.substring(prefix.length()));
                        if (n > max) max = n;
                    } catch (NumberFormatException e)
                    {
                        continue;
                    }
                }
            }
        return String.format("%s%04d", prefix, max + 1);
    }        
    
    public static boolean writeFile(String filePath, List<String> lines) 
    {
        try (FileWriter fw = new FileWriter(filePath, false))
        {
            for (String line : lines) 
            {
                fw.write(line + System.lineSeparator());
            }
            return true;
        }catch(IOException e) {
                System.err.println("Error writting to file" + filePath + ": " + e.getMessage());
                return false;
                }
    }
    
    public static boolean updateAppointmentContent(String id, int colIndex, String newValue)
    {
        List<String> lines = readFile("data/appointments.txt");
        boolean updated = false;
        
        for (int i = 0; i < lines.size(); i++) 
        {
            String line = lines.get(i);
            String[] data = line.split(",");
            
            if (data[0].trim().equalsIgnoreCase(id.trim())){
            
            data[colIndex] = newValue;
            lines.set(i, String.join(",", data));
            updated = true;
            break;
            }
        }
        
        if (updated) 
        {
            return writeFile("data/appointments.txt", lines);
        }
        return false;
    }
    
    public static String clean(String s) {
    if (s == null) return "";
    return s.replace(",", ";").replace("\r", " ").replace("\n", " ").trim();
}
    
    public static String findNameBasedID(String Id, String role) {
    services.UserService userService = new services.UserService();
    List<models.User> users = userService.loadAllUsers();
    
    for (models.User u : users) {
        if (u.getID().equalsIgnoreCase(Id) && u.getRole().equalsIgnoreCase(role)) {
            return u.getName(); // 
        }
    }
    return null; // 
}
   public static List<String> getRosterSlots(String doctorId) 
   {
       List<String> result = new ArrayList<>();
       java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
       
       String currentDoctor = "";
       String currentDate = "";
       
       for (String line : readFile("data/roster.txt")) 
       {
           if(line.startsWith("RosterID: ")) 
           {
               currentDoctor = "";
               currentDate = "";
           }
           
           else if (line.startsWith("DoctorID:")) 
           {
               currentDoctor = line.substring(9).trim();
           }
           
           else if(line.startsWith("ShiftDate:")) 
           {
               try {
    
               java.time.LocalDate d = java.time.LocalDate.parse(line.substring(10).trim(), fmt);
               if (d.isBefore(java.time.LocalDate.now())) 
               {
                   currentDate = "";
               }
               
               else 
               {
                   currentDate = d.toString();
               }
               
           }catch (java.time.format.DateTimeParseException e) {currentDate = "";}
       }
           else if(line.startsWith("ShiftType:")) 
           {
               if (currentDoctor.equalsIgnoreCase(doctorId) && !currentDate.isEmpty()) {
                String shift = line.substring(10).trim();   
                int bracket = shift.indexOf("(");           
                if (bracket > 0) {
                    shift = shift.substring(0, bracket);    
                }
                String slot = currentDate + " " + shift;    
                if (!result.contains(slot)) {              
                    result.add(slot);
                }
           }
       }
    }
       java.util.Collections.sort(result);
       return result;
    
    }
}
