package se.claremont.web.support;

import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

public class TestActions {
    private TestCase testCase;
    private WebInteractionMethods web;
    private NavigationMechanisms nav;

    public TestActions(TestCase testCase) {
        this.testCase = testCase;
        this.web = new WebInteractionMethods(testCase);
        this.nav = new NavigationMechanisms(testCase);
    }

    public WebInteractionMethods getWeb() {
        return web;
    }

    public NavigationMechanisms getNav() {
        return nav;
    }
}
