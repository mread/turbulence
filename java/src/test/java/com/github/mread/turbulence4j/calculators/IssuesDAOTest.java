package com.github.mread.turbulence4j.calculators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IssuesDAOTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private IssuesDAO issuesDAO;

    @Before
    public void setup() {
        issuesDAO = new IssuesDAO();
    }

    @Test
    public void callingSetTypeWithoutFirstCreatingAnIssueCausesError() {
        exception.expect(RuntimeException.class);
        issuesDAO.setType("abc");
    }

    @Test
    public void callingAddReleaseWithoutFirstCreatingAnIssueCausesError() {
        exception.expect(RuntimeException.class);
        issuesDAO.addRelease("1.0");
    }

    @Test
    public void canSetTypeAfterFirstCreatingAnIssue() {
        issuesDAO.createIssue();
        issuesDAO.setType("defect");
    }

    @Test
    public void persistingAnIssueResetsTheCurrentIssueToNull() {
        issuesDAO.createIssue();
        issuesDAO.persistIssue();
        exception.expect(RuntimeException.class);
        issuesDAO.setType("this should cause an error");
    }

    @Test
    public void shouldntAllowTwoCreatesWithoutAPersist() {
        issuesDAO.createIssue();
        exception.expect(RuntimeException.class);
        issuesDAO.createIssue();
    }
}
