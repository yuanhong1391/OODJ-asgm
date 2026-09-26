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
        //using readfile and .size to know currenly txt.file got how ,any case alr
        List<String> lines = readFile(filePath);
        int nextNumber = lines.size() + 1;
        
        //%04d used makesure id at lease have 4, exp 0001
        return String.format("%s%04d", prefix,nextNumber);
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
    
}
