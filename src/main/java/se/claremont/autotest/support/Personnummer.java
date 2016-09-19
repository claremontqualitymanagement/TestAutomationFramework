package se.claremont.autotest.support;

import java.util.Date;

/**
 * Manages Swedish personnummer entities
 *
 * Created by jordam on 2016-08-18.
 */
class Personnummer {
    int checkDigit;

    /**
     * Create a new personnummer from string
     * @param instring string to parse for Personnummer
     */
    public Personnummer(String instring){
        String date;
        Date date1;
        String fourLastDigits;
        if(instring.contains("-")){
            date1 = SupportMethods.stringToDate(instring.split("-")[0]);
            fourLastDigits = instring.split("-")[instring.split("-").length];
        }else{
            if(instring.length() == 10){
                date1 = SupportMethods.stringToDate( instring.substring(0, 6));
                fourLastDigits = instring.substring(6);
            }
        }
    }

}
