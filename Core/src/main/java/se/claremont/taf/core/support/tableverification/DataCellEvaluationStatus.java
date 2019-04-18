package se.claremont.taf.core.support.tableverification;

/**
 * Status of a table cell during its evaluation in a verification
 *
 * Created by jordam on 2017-01-28.
 */
public enum DataCellEvaluationStatus {
    EXACT_MATCH,
    CONTAINS_MATCH,
    REGEX_MATCH,
    NO_MATCH,
    UNEVALUATED
}
