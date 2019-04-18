package se.claremont.taf.core.reporting;

/**
 * Used throughout TAF reporting to produce HTML reports with a unified style.
 *
 * Created by jordam on 2016-11-19.
 */
public enum  UxColors {
    LIGHT_GREY("#F2F2F2"),
    MID_GREY("#DAD8D9"),
    DARK_GREY("rgb(104,102,99)"),
    LIGHT_BLUE("rgb(150,192,230)"),
    DARK_BLUE("rgb(119,150,178)"),
    RED("rgb(242,102,100)"),
    GREEN("#2db92d"),
    YELLOW("cornsilk"),
    DARK_YELLOW("darkyellow"),
    ORANGE("orange"),
    WHITE("white"),
    BLACK("black");

    private final String htmlColorCode;

    UxColors(String htmlColorCode){
        this.htmlColorCode = htmlColorCode;
    }

    public String getHtmlColorCode(){
        return this.htmlColorCode;
    }

}
