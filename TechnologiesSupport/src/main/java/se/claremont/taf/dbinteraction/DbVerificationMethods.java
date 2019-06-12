package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.VerificationMethods;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.tableverification.CellMatchingType;
import se.claremont.taf.core.support.tableverification.TableData;
import se.claremont.taf.core.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

public class DbVerificationMethods extends VerificationMethods {

    public DbResponse response;

    public DbVerificationMethods(TestCase testCase, DbResponse dbResponse) {
        super(testCase);
        this.response = dbResponse;
    }

    public DbVerificationMethods responseTime(int maximumApprovedResponseTimeInMilliseconds){
        if(response == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Cannot check response time with null response.");
            return this;
        }
        if(response.responseTimeInMilliseconds > maximumApprovedResponseTimeInMilliseconds){
            testCase.log(LogLevel.VERIFICATION_FAILED, "DB response time threshold was " + maximumApprovedResponseTimeInMilliseconds + " ms, but response time was " + response.responseTimeInMilliseconds + " ms.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "DB response time threshold was " + maximumApprovedResponseTimeInMilliseconds + " ms, and response time was " + response.responseTimeInMilliseconds + " ms.");
        }
        return this;
    }

    public DbVerificationMethods columnCount(int expectedNrOfColumns){
        int actualColumnCount = SqlInteractionManager.getColumnCountFromResultSet(response.resultSet);
        if(actualColumnCount != expectedNrOfColumns){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected " + expectedNrOfColumns + " columns, but found " + actualColumnCount + ". Data:" + System.lineSeparator() + response.resultDataTable.toString());
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "ResultSet column count was " + expectedNrOfColumns + " as expected.");
        }
        return this;
    }

    public DbVerificationMethods recordExist(String columnNameColonValueSemicolonSeparatedString, CellMatchingType cellMatchingType){
        response.resultDataTable.verifyRowExist(columnNameColonValueSemicolonSeparatedString, cellMatchingType);
        return this;
    }

    public DbVerificationMethods recordDoesNotExist(String columnNameColonValueSemicolonSeparatedString, CellMatchingType cellMatchingType){
        response.resultDataTable.rowDoesNotExist(columnNameColonValueSemicolonSeparatedString, cellMatchingType);
        return this;
    }
}
