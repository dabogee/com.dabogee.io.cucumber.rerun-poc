package com.dabogee.io.cucumber.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class RetryableRunnerTest {

    @DataProvider
    Object[][] parseRetriesCountDataProvider() {
        return new Object[][] {
            new Object[] {Arrays.asList("--retries:3"), 3},
            new Object[] {Arrays.asList("--retries:"), 1},
            new Object[] {Arrays.asList("--retries"), 1},
            new Object[] {Arrays.asList("--retries:a"), 1}
        };
    }

    @Test(dataProvider = "parseRetriesCountDataProvider")
    public void testParseRetriesCount(List<String> args, Integer retries) {
        assertThat(RetryableRunner.parseRetriesCount(args)).isEqualTo(retries);
    }
}
