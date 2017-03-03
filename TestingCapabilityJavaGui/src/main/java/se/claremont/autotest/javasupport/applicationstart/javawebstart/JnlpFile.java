package se.claremont.autotest.javasupport.applicationstart.javawebstart;
        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;
        import se.claremont.autotest.common.logging.LogLevel;
        import se.claremont.autotest.common.support.SupportMethods;
        import se.claremont.autotest.common.testcase.TestCase;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import java.util.ArrayList;

/**
 * Created by p901dqj on 2017-01-17.
 */
public class JnlpFile {

    TestCase testCase;
    String content;
    String applicationEntryPoint;
    String mainJarFile;
    ArrayList<String> jarFiles = new ArrayList<>();
    String fileNameForJnlpSaveFile;

    public JnlpFile(TestCase testCase){
        this.testCase = testCase;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(logLevel.toString() + ":" + message);
        }else{
            testCase.log(logLevel, message);
        }
    }


    void getJnlpContent(String urlToJnlp, String pathToSaveTo){
        fileNameForJnlpSaveFile = pathToSaveTo;
        log(LogLevel.DEBUG, "Getting jnlp file from url '" + urlToJnlp + "'.");
        SslFileDownloader sslFileDownloader = new SslFileDownloader(testCase);
        sslFileDownloader.downloadFileOverSSLAndPotentiallyBadCertificate(urlToJnlp, pathToSaveTo);
        content = SupportMethods.getFileContent(pathToSaveTo);
        log(LogLevel.EXECUTED, "Saved the jnlp file as '" + pathToSaveTo + "'.");
        //log(LogLevel.DEBUG, "Content of jnlp file:"  + System.lineSeparator() + content);
        getMainJarFilePathFromJnlpFileContent();
        getAdditionalJarFilePathsFromJnlpFileContent();
        getApplicationEntryPoint();
    }

    private void getMainJarFilePathFromJnlpFileContent(){
        ArrayList<String> jarFiles = new ArrayList<String>();
        String startString = "<jar href=\"";
        String stopString = "\" main=\"true";
        String[] fileRows = content.split(System.lineSeparator());
        for(String fileRow : fileRows){
            int startPosition = fileRow.indexOf(startString);
            if(startPosition == -1) continue;
            startPosition = startPosition + startString.length();
            int stopPosition = fileRow.substring(startPosition).indexOf(stopString);
            if(stopPosition == -1) continue;
            stopPosition = startPosition + stopPosition;
            mainJarFile = fileRow.substring(startPosition, stopPosition);
        }
    }

    private void getApplicationEntryPoint(){
        //applicationEntryPoint = getMainClassString(fileNameForJnlpSaveFile);
        String[] lines = content.split(System.lineSeparator());
        int lineNumbers = lines.length;
        for(int lineCount = 0; lineCount < lineNumbers ; lineCount++){
            if(lines[lineCount].contains("<application-desc main-class=\"")){
                for(int args = lineCount; args < lineNumbers; args++){
                    //if(arguments.add(getArg(lines[args])));
                }
            }
        }

    }

    public static String getMainClassString(String pathToJnlp){
        String returnString = "";
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(pathToJnlp);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList applicationDescriptions = doc.getElementsByTagName("application-desc");
            for(int i = 0; i < applicationDescriptions.getLength(); i++){
                Node applicationDescription = applicationDescriptions.item(i);
                if (applicationDescription.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) applicationDescription;
                    if(element.getAttribute("main-class") != null);{
                        returnString += element.getAttribute("main-class");
                    }
                    NodeList arguments = element.getElementsByTagName("argument");
                    for(int j = 0; j < arguments.getLength(); j++){
                        returnString += " " + arguments.item(j).getTextContent();
                    }
                }
                returnString += System.lineSeparator();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }


    private void getAdditionalJarFilePathsFromJnlpFileContent(){
        log(LogLevel.DEBUG, "Identifying the jar files in the jnlp file.");
        String startString = "<jar href=\"";
        String stopString = "\" main=\"false";
        String[] fileRows = content.split(System.lineSeparator());
        for(String fileRow : fileRows){
            int startPosition = fileRow.indexOf(startString);
            if(startPosition == -1) continue;
            startPosition = startPosition + startString.length();
            int stopPosition = fileRow.substring(startPosition).indexOf(stopString);
            if(stopPosition == -1) continue;
            stopPosition = startPosition + stopPosition;
            log(LogLevel.DEBUG, "Identified the jar file '" + fileRow.substring(startPosition, stopPosition) + "' in the file row below:" + System.lineSeparator() + fileRow);
            jarFiles.add(fileRow.substring(startPosition, stopPosition));
        }
    }


}
