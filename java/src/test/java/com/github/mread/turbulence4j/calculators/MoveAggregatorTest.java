package com.github.mread.turbulence4j.calculators;

import static com.github.mread.turbulence4j.calculators.MoveAggregator.oldName;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MoveAggregatorTest {

    @Test
    public void noMove() {
        List<String> noMoveExample = asList(
                "a/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(noMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("a/a.java"));
    }

    @Test
    public void simpleFileRename() {
        List<String> simpleFileRenameExample = asList(
                "mread/turbulence4j/Turbulence4j.java",
                "mread/turbulence4j/{CommandLine.java => Turbulence4j.java}",
                "mread/turbulence4j/CommandLine.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleFileRenameExample);
        assertThat(moveAggregator.getUltimateName("mread/turbulence4j/CommandLine.java"),
                equalTo("mread/turbulence4j/Turbulence4j.java"));
    }

    @Test
    public void singleFileMove() {
        List<String> simpleMoveExample = asList(
                "template/g.java",
                "g.java => template/g.java",
                "g.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("g.java"), equalTo("template/g.java"));
    }

    @Test
    public void moveThenRecreateIgnoresMove() {
        List<String> simpleMoveExample = asList(
                "turbulence4j/CommandLine.java",
                "turbulence4j/Turbulence4j.java",
                "turbulence4j/{CommandLine.java => Turbulence4j.java}",
                "turbulence4j/CommandLine.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("turbulence4j/Turbulence4j.java"),
                equalTo("turbulence4j/Turbulence4j.java"));
        assertThat(moveAggregator.getUltimateName("turbulence4j/CommandLine.java"),
                equalTo("turbulence4j/CommandLine.java"));
    }

    @Test
    public void singleDirectoryMove() {
        List<String> simpleMoveExample = asList(
                "b/a.java",
                "{a => b}/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("b/a.java"));
    }

    @Test
    public void singleDeepDirectoryMove() {
        List<String> simpleMoveExample = asList(
                "x/b/a.java",
                "x/{a => b}/a.java",
                "x/a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("x/a/a.java"), equalTo("x/b/a.java"));
    }

    @Test
    public void canUseRawPathToGetUltimateName() {
        List<String> simpleMoveExample = asList(
                "b/a.java",
                "{a => b}/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("{a => b}/a.java"), equalTo("b/a.java"));
    }

    @Test
    public void doubleMove() {
        List<String> simpleMoveExample = asList(
                "{b => c}/a.java",
                "b/a.java",
                "{a => b}/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("c/a.java"));
    }

    @Test
    public void ignoresLeadingDigits() {
        List<String> noMoveExample = asList(
                "3\t6\ta/a.java",
                "5\t4\ta/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(noMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("a/a.java"));
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfWeTryToGetAnUnrecognisdName() {
        MoveAggregator moveAggregator = new MoveAggregator(Collections.<String> emptyList());
        moveAggregator.getUltimateName("file/we/never/saw.java");
    }

    @Test
    public void canDecypherOldName() {
        assertThat(oldName("github/mread/{ => turbulence4j}/calculators/ChurnCalculator.java"),
                equalTo("github/mread/calculators/ChurnCalculator.java"));
    }
}
