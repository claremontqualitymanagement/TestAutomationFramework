package se.claremont.autotest.common.support.tableverification;

/**
 * Used to mark evaluation status of table rows during verifications
 *
 * Created by jordam on 2017-01-28.
 */
public enum TableRowEvaluationStatus {
    UNEVALUATED,
    ONLY_MATCHES,
    NO_MATCH,
    BOTH_MATCHES_AND_NON_MATCHES
}
