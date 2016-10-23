package se.claremont.autotest.guidriverpluginstructure.swingsupport.robotswinggluecode;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import se.claremont.autotest.common.CliTestRunner;
import se.claremont.autotest.common.LogFolder;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jordam on 2016-10-21.
 */
public class RobotSwingInteractionMethods {
    TestCase testCase;

    public RobotSwingInteractionMethods(TestCase testCase){
        this.testCase = testCase;
    }

    public void captureScreenshot(){
        String filePath = LogFolder.testRunLogFolder + testCase.testName + CliTestRunner.testRun.fileCounter + ".png";
        System.out.println("Saving screenshot of desktop to '" + filePath + "'.");
        CliTestRunner.testRun.fileCounter++;
        try{
            Path file = Paths.get(filePath);
            File fileFolder = new File(filePath);
            fileFolder.getParentFile().mkdirs();
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            if(image != null){
                ImageIO.write(image, "png", new File(filePath));
                testCase.log(LogLevel.DEVIATION_EXTRA_INFO, "Saved screenshot as '" + filePath + "'.");
            }
        }catch (Exception e){
            testCase.log(LogLevel.FRAMEWORK_ERROR, "Could not take screenshot of desktop. Is Robot driver ok? " + e.toString());
        }
    }
}
