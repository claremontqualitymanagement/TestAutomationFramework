package se.claremont.autotest.dataformats.table;

/**
 * Created by jordam on 2016-11-09.
 */
public class EvaluatedCell {
    public String cellData;
    public CellMatchStatus cellMatchStatus = CellMatchStatus.UNEVALUATED;

    public EvaluatedCell(String dataValue, CellMatchStatus cellMatchStatus) {
        cellData = dataValue;
        this.cellMatchStatus = cellMatchStatus;
    }
}

