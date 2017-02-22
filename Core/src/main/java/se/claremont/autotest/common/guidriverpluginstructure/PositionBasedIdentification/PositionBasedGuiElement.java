package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

/**
 * Created by jordam on 2016-10-02.
 */
public interface PositionBasedGuiElement {
    int getLeftPosition();
    int getRightPosition();
    int getTopPosition();
    int getBottomPosition();
    String getTypeName(); // Something like: return this.getClass().toString();
}
