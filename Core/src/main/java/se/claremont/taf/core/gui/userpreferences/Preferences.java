package se.claremont.taf.core.gui.userpreferences;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Preferences {
    @JsonProperty private Map<String, Object> prefs = new HashMap<>();

    public Preferences(){}

    public void add(String propertyName, Object property){
        prefs.put(propertyName, property);
    }

    public Object get(String propertyName){
        if(!prefs.containsKey(propertyName))return null;
        return prefs.get(propertyName);
    }

    public void loadFromFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(new File(path), Preferences.class);
        } catch (IOException e) {
            System.out.println("Could not read user preferences from file '" + path + "'. Error: " + e.getMessage());
        }
    }

    public void saveToFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path), this);
        } catch (IOException e) {
            System.out.println("Could not write user preferences to file '" + path + "'. Error: " + e.getMessage());
        }
    }

    public static String getPreferencesFile(){
        return "C:\\Temp\\taf.preferences";

    }

}
