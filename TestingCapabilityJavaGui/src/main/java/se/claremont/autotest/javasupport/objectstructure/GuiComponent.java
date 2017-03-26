package se.claremont.autotest.javasupport.objectstructure;

/**
 * This should be merged with GuiElement interface
 *
 * Created by jordam on 2017-02-19.
 */
@SuppressWarnings("WeakerAccess")
public interface GuiComponent {

    String getName();

    Object getRuntimeComponent();

    String getRecognitionDescription();
}
