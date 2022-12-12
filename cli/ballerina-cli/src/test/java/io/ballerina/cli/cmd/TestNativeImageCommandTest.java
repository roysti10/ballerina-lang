package io.ballerina.cli.cmd;

import io.ballerina.projects.ProjectEnvironmentBuilder;
import io.ballerina.projects.environment.Environment;
import io.ballerina.projects.environment.EnvironmentBuilder;
import io.ballerina.projects.util.ProjectConstants;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


import static io.ballerina.projects.util.ProjectConstants.DIST_CACHE_DIRECTORY;

/**
 * Test Native Image command tests.
 *
 * @since 2.3.0
 */
public class TestNativeImageCommandTest extends BaseCommandTest {
    private Path testResources;
    private Path testDistCacheDirectory;
    ProjectEnvironmentBuilder projectEnvironmentBuilder;

    @BeforeClass
    public void setup() throws IOException {
        super.setup();
        try {
            this.testResources = super.tmpDir.resolve("test-cmd-test-resources");
            Path testBuildDirectory = Paths.get("build").toAbsolutePath();
            this.testDistCacheDirectory = testBuildDirectory.resolve(DIST_CACHE_DIRECTORY);
            Path customUserHome = Paths.get("build", "user-home");
            Environment environment = EnvironmentBuilder.getBuilder().setUserHome(customUserHome).build();
            projectEnvironmentBuilder = ProjectEnvironmentBuilder.getBuilder(environment);
            URI testResourcesURI = Objects.requireNonNull(
                    getClass().getClassLoader().getResource("test-resources")).toURI();
            Files.walkFileTree(Paths.get(testResourcesURI), new TestCommandTest.Copy(Paths.get(testResourcesURI),
                    this.testResources));
        } catch (URISyntaxException e) {
            Assert.fail("error loading resources");
        }
    }

    @Test(description = "Test a valid ballerina project")
    public void testNativeImageTests() throws IOException {
        Path projectPath = this.testResources.resolve("validProjectWithTests");
        System.setProperty(ProjectConstants.USER_DIR, projectPath.toString());
        TestCommand testCommand = new TestCommand(projectPath, printStream, printStream, false, true);
        new CommandLine(testCommand).parse();
        testCommand.execute();
        String buildLog = readOutput(true);
        Assert.assertTrue(buildLog.contains("1 passing"));
    }

// TODO: Fix test cases that are failing due to "Caused by: java.util.zip.ZipException: ZIP file can't be opened as a
//  file system because entry "/resources/$anon/./0/openapi-spec.yaml" has a '.' or '..' element in its name" error.

//    @Test(description = "Test a valid ballerina file")
//    public void testTestBalFile() throws IOException {
//        Path validBalFilePath = this.testResources.resolve("valid-test-bal-file").resolve("sample_tests.bal");
//        System.setProperty(ProjectConstants.USER_DIR, this.testResources.resolve("valid-test-bal-file").toString());
//        TestCommand testCommand = new TestCommand(validBalFilePath, printStream, printStream, false, true);
//        new CommandLine(testCommand).parseArgs(validBalFilePath.toString());
//        testCommand.execute();
//        String buildLog = readOutput(true);
//        Assert.assertTrue(buildLog.contains("1 passing"));
//    }
//
//    @Test(description = "Test a valid ballerina file with periods in the file name")
//    public void testTestBalFileWithPeriods() throws IOException {
//        Path validBalFilePath = this.testResources.resolve("valid-test-bal-file").resolve("sample.tests.bal");
//
//        System.setProperty(ProjectConstants.USER_DIR, this.testResources.resolve("valid-test-bal-file").toString());
//        TestCommand testCommand = new TestCommand(validBalFilePath, printStream, printStream, false, true);
//        new CommandLine(testCommand).parseArgs(validBalFilePath.toString());
//        testCommand.execute();
//        String buildLog = readOutput(true);
//        Assert.assertTrue(buildLog.contains("1 passing"));
//    }
}
