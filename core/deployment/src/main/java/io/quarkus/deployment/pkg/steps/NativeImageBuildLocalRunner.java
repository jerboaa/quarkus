package io.quarkus.deployment.pkg.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

public class NativeImageBuildLocalRunner extends NativeImageBuildRunner {

    private static final String RELEASE_FILE = "release";
    private final String nativeImageExecutable;
    private final File workingDirectory;
    private final File graalVmHome;

    public NativeImageBuildLocalRunner(String nativeImageExecutable, File workingDirectory, File graalVmHome) {
        this.nativeImageExecutable = nativeImageExecutable;
        this.workingDirectory = workingDirectory;
        this.graalVmHome = graalVmHome;
    }

    @Override
    protected String[] getGraalVMVersionCommand(List<String> args) {
        return buildCommand(args);
    }

    @Override
    protected String[] getBuildCommand(List<String> args) {
        return buildCommand(args);
    }

    @Override
    protected void objcopy(String... args) {
        final String[] command = new String[args.length + 1];
        command[0] = "objcopy";
        System.arraycopy(args, 0, command, 1, args.length);
        runCommand(command, null, workingDirectory);
    }

    @Override
    protected boolean objcopyExists() {
        if (!SystemUtils.IS_OS_LINUX) {
            return false;
        }

        // System path
        String systemPath = System.getenv("PATH");
        if (systemPath != null) {
            String[] pathDirs = systemPath.split(File.pathSeparator);
            for (String pathDir : pathDirs) {
                File dir = new File(pathDir);
                if (dir.isDirectory()) {
                    File file = new File(dir, "objcopy");
                    if (file.exists()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private String[] buildCommand(List<String> args) {
        return Stream.concat(Stream.of(nativeImageExecutable), args.stream()).toArray(String[]::new);
    }

    @Override
    protected Map<String, String> getGraalVMReleaseProps() {
        Map<String, String> releaseProps = new HashMap<>();
        Path releaseFile = graalVmHome.toPath().resolve(Paths.get(RELEASE_FILE));
        try (Stream<String> lines = Files.lines(releaseFile)) {
            lines.forEach(line -> {
                String[] tokens = line.split("=", 2); // release file is '=' delimited
                if (tokens.length == 2) {
                    releaseProps.put(tokens[0], tokens[1]);
                }
            });
            return releaseProps;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + releaseFile, e);
        }
    }

}
