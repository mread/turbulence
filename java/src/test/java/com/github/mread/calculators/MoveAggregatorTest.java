package com.github.mread.calculators;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MoveAggregatorTest {

    @Test
    public void noMove() {
        List<String> noMoveExample = Arrays.asList(
                "a/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(noMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("a/a.java"));
    }

    @Test
    public void singleMove() {
        List<String> simpleMoveExample = Arrays.asList(
                "b/a.java",
                "{a => b}/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("b/a.java"));
    }

    @Test
    public void singleDeepMove() {
        List<String> simpleMoveExample = Arrays.asList(
                "x/b/a.java",
                "x/{a => b}/a.java",
                "x/a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("x/a/a.java"), equalTo("x/b/a.java"));
    }

    @Test
    public void canUseRawPathToGetUltimateName() {
        List<String> simpleMoveExample = Arrays.asList(
                "b/a.java",
                "{a => b}/a.java",
                "a/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(simpleMoveExample);
        assertThat(moveAggregator.getUltimateName("{a => b}/a.java"), equalTo("b/a.java"));
    }

    @Test
    public void doubleMove() {
        List<String> simpleMoveExample = Arrays.asList(
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
        List<String> noMoveExample = Arrays.asList(
                "3\t6\ta/a.java",
                "5\t4\ta/a.java"
                );
        MoveAggregator moveAggregator = new MoveAggregator(noMoveExample);
        assertThat(moveAggregator.getUltimateName("a/a.java"), equalTo("a/a.java"));
    }

}
