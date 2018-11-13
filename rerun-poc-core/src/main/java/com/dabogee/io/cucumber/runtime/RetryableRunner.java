package com.dabogee.io.cucumber.runtime;

import static com.google.common.base.Preconditions.checkNotNull;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import static java.lang.String.format;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class RetryableRunner {

    private static final String RETRIES_PREFIX = "--retries";
    private static final String RERUN_PREFIX = "rerun";
    private static final String PLUGIN_SEPARATOR = ":";
    private static final Byte IS_OK = 0x0;
    private static final Byte IS_ERROR = 0x1;


    //private Integer retries = 2;

    public static void main(String[] argv) {
        byte exitStatus = run(Arrays.asList(argv), Thread.currentThread().getContextClassLoader());
        System.exit(exitStatus);
    }

    /**
     * Launches the Cucumber-JVM command line.
     *
     * @param args        runtime options. See details in the {@code cucumber.api.cli.Usage.txt} resource.
     * @param classLoader classloader used to load the runtime
     * @return 0 if execution was successful, 1 if it was not (test failures)
     */
    public static byte run(List<String> args, ClassLoader classLoader) {
        Integer retriesCount = parseRetriesCount(args);
        RuntimeOptions ro = new RuntimeOptions(removeRetriesFromArgs(args));
        String originRerunReportPath = extractRerunReportPath(ro);
        List<String> originFeaturePaths = ro.getFeaturePaths();

        Byte status = IS_ERROR;

        for (int i = 0; i < retriesCount && !status.equals(IS_OK); i++) {
            final Runtime runtime = Runtime.builder()
                    .withRuntimeOptions(ro)
                    .withClassLoader(classLoader)
                    .build();

            runtime.run();

            File rerunReportCopy = copyRerunReportToTempFile(originRerunReportPath);
            if (rerunReportCopy != null) {
                ro.getFeaturePaths().clear();
                ro.getFeaturePaths().addAll(originFeaturePaths);
                ro.getFeaturePaths().add(format("@%s", rerunReportCopy.getAbsolutePath()));
            }
            status = runtime.exitStatus();
        }
        return status;
    }

    private static File createRerunReportCopy() {
        try {
            File out = File.createTempFile("rerun-copy", ".txt");
            out.deleteOnExit();
            return out;
        }
        catch (IOException e) {
            return null;
        }
    }

    private static File copyRerunReportToTempFile(String rerunReportPath) {
        try {
            File rerunReportCopy = createRerunReportCopy();
            checkNotNull(rerunReportCopy);
            FileUtils.copyFile(new File(rerunReportPath), rerunReportCopy);
            return rerunReportCopy;
        }
        catch (NullPointerException | IOException e) {
            return null;
        }
    }

    private static String extractRerunReportPath(RuntimeOptions ro) {
        String path = ro.getPluginFormatterNames()
                .stream()
                .filter(n -> StringUtils.startsWithIgnoreCase(n, RERUN_PREFIX))
                .findFirst()
                .orElse(null);
        return StringUtils.isNotBlank(path) ? StringUtils.substringAfter(path, PLUGIN_SEPARATOR) : null;
    }

    static Integer parseRetriesCount(List<String> args) {
        String count = args.stream().filter(a -> a.startsWith(RETRIES_PREFIX))
                .map(a -> StringUtils.substringAfter(a, ":"))
                .findFirst().orElse(null);
        try {
            return Integer.parseInt(count);
        }
        catch (NullPointerException | NumberFormatException e) {
            return 1;
        }
    }

    private static List<String> removeRetriesFromArgs(List<String> args) {
        return args.stream().filter(a -> !a.startsWith(RETRIES_PREFIX)).collect(Collectors.toList());
    }
}
