package se.claremont.autotest.swingsupport.robotswinggluecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jordam on 2016-10-21.
 */
public class RobotSwingInteractionMethods {

    private final static Logger logger = LoggerFactory.getLogger( RobotSwingInteractionMethods.class );
    TestCase testCase;

    public RobotSwingInteractionMethods(TestCase testCase){
        this.testCase = testCase;
    }

    public void captureScreenshot(){
        String filePath = LogFolder.testRunLogFolder + testCase.testName + TestRun.fileCounter + ".png";
        logger.debug( "Saving screenshot of desktop to '" + filePath + "'." );
        TestRun.fileCounter++;
        try{
            Path file = Paths.get(filePath);
            File fileFolder = new File(filePath);
            fileFolder.getParentFile().mkdirs();
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            if(image != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write( image, "png", baos );
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                SupportMethods.saveToFile(imageInByte, filePath);
                //ImageIO.write(image, "png", new File(filePath));
                testCase.log(LogLevel.INFO, "Saved screenshot as '" + filePath + "'.");
            }
        }catch (Exception e){
            testCase.log(LogLevel.FRAMEWORK_ERROR, "Could not take screenshot of desktop. Is Robot driver ok? Is file save to '" + filePath + "' ok? Error: " + e.toString());
        }
    }
}
