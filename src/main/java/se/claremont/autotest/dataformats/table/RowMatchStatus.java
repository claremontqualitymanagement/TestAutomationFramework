package se.claremont.autotest.dataformats.table;

/**
 * Created by jordam on 2016-11-09.
 */
enum RowMatchStatus {
    ROW_IS_UNEVALUATED,
    ROW_HAS_ONLY_MISMATCHED_DATA,
    ROW_HAS_BOTH_CORRECT_AND_ERRONEUS_DATA,
    ROW_HAS_ONLY_CORRECT_MATCHES
}
