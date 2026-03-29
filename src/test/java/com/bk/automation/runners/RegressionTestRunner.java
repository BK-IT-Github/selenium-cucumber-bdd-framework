package com.bk.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Regression Test Runner.
 * Executes scenarios tagged with @Regression.
 *
 * <p>Usage:
 * <pre>
 *   mvn test -Dtest=RegressionTestRunner
 *   mvn test -P regression
 * </pre>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.bk.automation.stepdefinitions",
                "com.bk.automation.hooks"
        },
        tags = "@Regression",
        plugin = {
                "pretty",
                "html:target/reports/cucumber/regression-report.html",
                "json:target/reports/cucumber/regression-report.json",
                "junit:target/reports/cucumber/regression-report.xml",
                "rerun:target/rerun/failed_regression_scenarios.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        dryRun = false,
        publish = false
)
public class RegressionTestRunner {
    // This class is intentionally empty.
    // Cucumber uses annotations to configure the test execution.
}
