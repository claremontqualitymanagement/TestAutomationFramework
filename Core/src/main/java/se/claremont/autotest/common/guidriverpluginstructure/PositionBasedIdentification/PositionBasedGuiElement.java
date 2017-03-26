package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

/**
 * An interface for any type of GUI element used for position based identification, regardless of technology
 *
 * Created by jordam on 2016-10-02.
 */
public interface PositionBasedGuiElement {
    Integer getLeftPosition();
    Integer getRightPosition();
    Integer getTopPosition();
    Integer getBottomPosition();
    String getTypeName(); // Something like: return this.getClass().toString();
}
