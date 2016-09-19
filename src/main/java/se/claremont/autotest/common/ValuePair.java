package se.claremont.autotest.common;

/**
 * Pair of parameter name and parameter value
 *
 * Created by jordam on 2016-08-25.
 */
class ValuePair {
    public final String parameter;
    public String value;

    /**
     * Creating a new value pair from parameter name and string based parameter value
     * @param parameterName The name of the parameter
     * @param parameterValue The value of the parameter
     */
    public ValuePair(String parameterName, String parameterValue){
        this.parameter = parameterName;
        this.value = parameterValue;
    }

    /**
     * Creating a new value pair from parameter name and string based parameter value
     * @param parameterName The name of the parameter
     * @param parameterValue The value of the parameter
     */
    @SuppressWarnings("SameParameterValue")
    public ValuePair(String parameterName, int parameterValue){
        this.parameter = parameterName;
        this.value = Integer.toString(parameterValue);
    }

    /**
     * Gives new value to parameter
     * @param parameterValue New parameter value
     */
    @SuppressWarnings("SameParameterValue")
    public void reassign(String parameterValue){
        this.value = parameterValue;
    }

    public @Override String toString(){
        return "['" + parameter + "' = '" + value + "']";
    }
}
