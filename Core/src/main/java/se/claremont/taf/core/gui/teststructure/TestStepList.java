package se.claremont.taf.core.gui.teststructure;

import java.util.ArrayList;
import java.util.List;

public class TestStepList {
    private List<TestStep> testSteps = new ArrayList<>();
    private List<TestStepListChangeListener> changeListeners = new ArrayList<>();

    public TestStepList(){

    }

    public void addChangeListener(TestStepListChangeListener listener){
        changeListeners.add(listener);
    }

    public void removeChangeListener(TestStepListChangeListener listener){
        if(!changeListeners.contains(listener)) return;
        changeListeners.remove(listener);
    }

    public void add(TestStep testStep){
        testSteps.add(testStep);
        for(TestStepListChangeListener listener : changeListeners){
            listener.isAdded(testStep);
        }
    }

    public void remove(TestStep testStep){
        if(testSteps.contains(testStep)){
            for(TestStepListChangeListener listener : changeListeners){
                listener.isRemoved(testStep);
            }
            testSteps.remove(testStep);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Test step list:[").append(System.lineSeparator());
        for(TestStep testStep : testSteps){
            sb.append("[name:'").append(testStep.getName())
                    .append("', type:'")
                    .append(testStep.getTestStepTypeShortName())
                    .append("', action:'")
                    .append(testStep.actionName)
                    .append("', element:'")
                    .append(testStep.elementName)
                    .append("', data:'")
                    .append(testStep.data)
                    .append("', description:'")
                    .append(testStep.getDescription())
                    .append("']")
                    .append(System.lineSeparator());
        }
        sb.append("]");
        return sb.toString();
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public abstract static class TestStepListChangeListener{

        public TestStepListChangeListener(){}

        public abstract void isAdded(TestStep testStep);

        public abstract void isRemoved(TestStep testStep);
    }
}
