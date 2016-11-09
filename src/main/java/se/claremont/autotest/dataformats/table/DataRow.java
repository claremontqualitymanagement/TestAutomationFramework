package se.claremont.autotest.dataformats.table;

import java.util.*;

/**
 * Created by jordam on 2016-11-09.
 */
class DataRow {
    public Map<String, String> dataCells;
    public int rowNumber;

    public DataRow(List<String> headLines, String[] dataCells, int rowNumber) {
        this.rowNumber = rowNumber;
        this.dataCells = new LinkedHashMap<>();
        if (dataCells != null && headLines != null && dataCells.length >= headLines.size())
        {
            for (int cellNumber = 0; cellNumber < headLines.size(); cellNumber++)
            {
                this.dataCells.put(headLines.get(cellNumber), dataCells[cellNumber].trim());
            }
        }
    }

    public @Override String toString() {
        List<String> dataRowString = new ArrayList<>();
        Set<String> headlines = dataCells.keySet();
        for (String headline : headlines) {
            dataRowString.add(dataCells.get(headline));
        }
        return String.join(";", dataRowString);
    }

    public String value(String headline) {
        String returnValue = "";
        try {
            returnValue = dataCells.get(headline);
        } catch (Exception e) {
            returnValue = "--- VARNING: No headline '" + headline + "' found for row --- " + e.getMessage();
        }
        return returnValue;
    }
}
