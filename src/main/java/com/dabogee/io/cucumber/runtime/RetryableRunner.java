package com.dabogee.io.cucumber.runtime;

import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Log4j2
public class RetryableRunner {

    private static final String RERUN_PREFIX = "rerun";
    private static final String PLUGIN_SEPARATOR = ":";

    //private Integer retries = 2;

    public static void main(String[] argv) {
        byte exitStatus = run(argv, Thread.currentThread().getContextClassLoader());
        System.exit(exitStatus);
    }

    /**
     * Launches the Cucumber-JVM command line.
     *
     * @param argv        runtime options. See details in the {@code cucumber.api.cli.Usage.txt} resource.
     * @param classLoader classloader used to load the runtime
     * @return 0 if execution was successful, 1 if it was not (test failures)
     */
    public static byte run(String[] argv, ClassLoader classLoader) {
        RuntimeOptions ro = new RuntimeOptions(Arrays.asList(argv));
        log.info(extractRerunReportPath(ro));
//        ro.getPluginFormatterNames();

        final Runtime runtime = Runtime.builder()
                .withRuntimeOptions(ro)
                .withClassLoader(classLoader)
                .build();

        runtime.run();
        return runtime.exitStatus();
    }

    private static String extractRerunReportPath(RuntimeOptions ro) {
        String path = ro.getPluginFormatterNames()
                .stream()
                .filter(n -> StringUtils.startsWithIgnoreCase(n, RERUN_PREFIX))
                .findFirst()
                .orElse(null);
        return StringUtils.isNotBlank(path) ? StringUtils.substringAfter(path, PLUGIN_SEPARATOR) : null;
    }
}
