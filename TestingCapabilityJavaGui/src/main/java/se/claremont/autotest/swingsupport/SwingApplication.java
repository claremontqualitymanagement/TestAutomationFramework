package se.claremont.autotest.swingsupport;


import org.fest.swing.fixture.FrameFixture;

import java.awt.*;

/**
 * Created by jordam on 2016-11-01.
 */
public class SwingApplication {
    private Frame frame;
    private FrameFixture frameFixture;
    private double timeoutInMiliseconds = 10000;

    public SwingApplication(Frame frame){
        this.frame = frame;
        frame.setVisible(true);
        double startTime = System.currentTimeMillis();
        while (!frame.isVisible() && System.currentTimeMillis() - startTime < timeoutInMiliseconds){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {

            }
        }
        this.frameFixture = new FrameFixture(frame);
    }
}
