package javaapplication1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mega
 */
public class FilePrinter {
    
    public static void main(String [] args) {
        FilePrinter fp = new FilePrinter();
        String content = fp.getFileContents("https://raw.githubusercontent.com/egis/handbook/master/Tech-Stack.md");
        System.out.print(content);
    }
    
    public String getFileContents(String fileDir){
        String content = "";
        
        FileReader fr = null; 
        BufferedReader br = null;
        try {
            
            URL url = new URL(fileDir); 
            
            String line;
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FilePrinter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FilePrinter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                Logger.getLogger(FilePrinter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return getJSONFormat(content);
    }
    
    private String getJSONFormat(String input){
        
        String content = "";
        String [] sections = input.split("##");
        String [] jsonSections = new String [sections.length];
        
        boolean checkHeadersNext = false;
        List<String> headerData = new ArrayList<>();
        
        boolean checkDataNext = false;
        
        for (int x = 0; x < sections.length; x++){
            
            if (sections[x].trim().isEmpty())
                continue;
            
            String [] items = sections[x].split("\n");
            String jsonElements = "[";
            for (int a = 0; a < items.length; a++){
                //Get rid of tabling and readability spaces
                if (items[a].contains("-----") || items[a].trim().isEmpty()){
                    continue;
                }
                
                String [] elements = items[a].split("\\|");
                if (elements.length == 1){
                    String section = elements[0].replaceAll("\\*", "");
                    section = section.replaceAll("\"", "\\\"");
                    jsonSections[x] = "{\"" + section.trim() + "\":" ;
                    checkHeadersNext = true;
                    checkDataNext = false;
                    continue;
                }
                
                if(checkHeadersNext){
                    headerData = new ArrayList<>();
                    for (String element : elements){
                        element = element.replaceAll("\\*", "");
                        element = element.replaceAll("\"", "\\\"");
                        headerData.add(element);
                    }
                    checkHeadersNext = false;
                    checkDataNext = true;
                    continue;
                }
                String json = "{";
                for (int y = 0; y < elements.length; y++){
                    if (y < headerData.size()){
                        elements[y] = elements[y].replaceAll("\"", "\\\"");
                        json += "\"" + headerData.get(y).trim() + "\":\"" + elements[y].trim() + "\"";
                        if ((y+1) < elements.length)
                            json += ",";
                    }
                }
                json += "}";
                jsonElements += json;
                if ((a+1) < items.length)
                    jsonElements += ",";
                
            }
            
            jsonElements += "]";
            jsonSections[x] += jsonElements += "}";
            content += jsonSections[x];
            if ((x+1) < jsonSections.length)
                content += ",";
        }
        return "["+content+"]";
    }
    
}
