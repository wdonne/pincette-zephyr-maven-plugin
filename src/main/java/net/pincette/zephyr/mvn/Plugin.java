package net.pincette.zephyr.mvn;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static net.pincette.util.Collections.set;

import java.io.File;
import net.pincette.zephyr.squad.JUnit;
import net.pincette.zephyr.squad.Uploader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "zephyr-squad")
public class Plugin extends AbstractMojo {
  @Parameter private String[] components;
  @Parameter private String[] epics;

  @Parameter(required = true)
  private String jiraEndpoint;

  @Parameter(required = true, property = "jira.password")
  private String password;

  @Parameter(required = true)
  private String project;

  @Parameter(
      required = true,
      defaultValue = "${project.build.directory}/surefire-reports",
      readonly = true)
  private File reportsDirectory;

  @Parameter(required = true, property = "jira.username")
  private String username;

  @Parameter(required = true)
  private String version;

  @Parameter(required = true)
  private String zephyrEndpoint;

  @Override
  public void execute() {
    final File[] files = getFiles();
    final Uploader uploader =
        new Uploader()
            .withProject(project)
            .withVersion(version)
            .withEpics(epics != null ? set(epics) : null)
            .withComponents(components != null ? set(components) : null)
            .withJiraEndpoint(jiraEndpoint)
            .withZephyrEndpoint(zephyrEndpoint)
            .withUsername(username)
            .withPassword(password);

    getLog().info("Uploading " + stream(files).map(File::getAbsolutePath).collect(joining(", ")));
    uploader.upload(JUnit.loadTestcases(files)).toCompletableFuture().join();
  }

  private File[] getFiles() {
    return reportsDirectory.listFiles(f -> f.getName().endsWith(".xml"));
  }
}
