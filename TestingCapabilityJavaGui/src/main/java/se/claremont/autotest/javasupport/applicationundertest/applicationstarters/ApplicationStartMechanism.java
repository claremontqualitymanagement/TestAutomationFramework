package se.claremont.autotest.javasupport.applicationundertest.applicationstarters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStartMechanism {
    @JsonProperty public String startUrlOrPathToJarFile;
    @JsonProperty public String mainClass;
    @JsonIgnore TestCase testCase;
    @JsonProperty public List<String> arguments = new ArrayList<>();
    @JsonIgnore ClassLoader classLoader;

    private ApplicationStartMechanism(){}

    public ApplicationStartMechanism(TestCase testCase){
        this.testCase = testCase;
    }

    public void run(){
        ApplicationStarter as = new ApplicationStarter(testCase);
        try {
            as.startJar(new URL(startUrlOrPathToJarFile), mainClass, arguments.toArray(new String[0]));
        } catch (MalformedURLException e) {
            System.out.println("Could not start program '" + startUrlOrPathToJarFile + "', with start class '" + mainClass + "' and arguments '" + String.join("', '", arguments) + "'. Error:" + e.toString());
        }
    }

    public String saveToJsonFile(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath), this);
        } catch (IOException e) {
            return e.toString();
        }
        return "ok";
    }

    public static ApplicationStartMechanism readFromJsonFile(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filePath), ApplicationStartMechanism.class);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return new ApplicationStartMechanism(new TestCase());
    }

    String toClassFile(){
        StringBuilder sb = new StringBuilder();
        sb.append("");
        return sb.toString();
    }
}
