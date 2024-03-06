package com.iprodi08.config;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CucumberReportRunner extends BlockJUnit4ClassRunner {
    @Value("${project.name}")
    private static String projectName;
    @Value("${build.number}")
    private static String buildNumber;
    @Value("${branch.name}")
    private static String branchName;

    public CucumberReportRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        generateReport();
    }

    public static void generateReport() {
        File reportOutputDirectory = new File("target/report/cucumber/");

        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/report/cucumber/cucumber-report.json");

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.setBuildNumber(buildNumber);
        configuration.addClassifications("Build Number", configuration.getBuildNumber());
        configuration.addClassifications("Branch Name", branchName);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }
}
