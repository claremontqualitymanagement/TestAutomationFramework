package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

/**
 * Created by jordam on 2016-10-02.
 */
public interface PositionBasedGuiElement {
    Integer getLeftPosition();
    Integer getRightPosition();
    Integer getTopPosition();
    Integer getBottomPosition();
    String getTypeName(); // Something like: return this.getClass().toString();
}
