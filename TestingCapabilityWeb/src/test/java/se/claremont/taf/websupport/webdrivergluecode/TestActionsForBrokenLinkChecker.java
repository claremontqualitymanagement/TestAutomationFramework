package se.claremont.taf.websupport.webdrivergluecode;

/**
 * Created by jordam on 2017-03-31.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TestActionsForBrokenLinkChecker {

    public void checkBrokenLinks(WebInteractionMethods web){
        web.reportBrokenLinksOnCurrentPage();
    }
}
