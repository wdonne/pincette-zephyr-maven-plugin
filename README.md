# Zephyr Squad Uploader Maven Plugin

This is a plugin for the [Zephyr Squad Uploader](https://github.com/wdonne/pincette-zephyr-squad) library. Its only Maven goal is ```zephyr-squad```. It picks up JUnit test result files from ```surefire-reports``` and sends them to Zephyr Squad as test executions in a test cycle.

The configuration looks like this:

```
<plugin>
  <groupId>net.pincette</groupId>
  <artifactId>pincette-zephyr-maven-plugin</artifactId>
  <version>1.0</version>
  <configuration>
    <project>My project</project>
    <version>1.0</version>
    <jiraEndpoint>https://xxxxxx/jira/rest/api/2</jiraEndpoint>
    <zephyrEndpoint>https://xxxxxx/jira/rest/zapi/latest</zephyrEndpoint>
    <!--
    <username>xxxxxxx</username>
    <password>xxxxxxx</password>
    -->
    <components>
      <param>mycomponent</param>
    </components>
  </configuration>
</plugin>
```

|Property|Mandatory|Description|
|---|
|components|No|All the Zephyr test issues that are related to one of the components are considered. This is ignored when ```epics``` is also set.|
|epics|No|All the Zephyr test issues that are related to one of the epics are considered. Test issues are related when they are related to story issues that are part of the epic.|
|jiraEndpoint|Yes|The URL that refers to the JIRA REST API.|
|password|No|The JIRA password. It is better not to put it here in clear text. Alternatively you can use the system property ```jira.password```.|
|project|Yes|The JIRA project name.|
|reportsDirectory|No|The directory where the JUnit test reports are expected. The default value is ```${project.build.directory}/surefire-reports```./|
|username|No|The JIRA username. Alternatively you can use the system property ```jira.username```.|
|version|No|The JIRA project version. When it is not set the test cycle will go under ```Unscheduled```.|
|zephyrEndpoint|Yes|The URL that refers to the Zephyr REST API on the JIRA server.|

Run it as follows:

```
> mvn pincette-zephyr:zephyr-squad
```

If you are using a CI-tool, then it is best to run this after the build, because when tests fail the goal won't be run. You can also run it in the post integration test phase.