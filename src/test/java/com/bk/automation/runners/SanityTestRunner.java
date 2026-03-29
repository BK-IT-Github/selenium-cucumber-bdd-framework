package com.bk.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Sanity Test Runner.
 * Executes scenarios tagged with @Sanity.
 *
 * <p>Usage:
 * <pre>
 *   mvn test -Dtest=SanityTestRunner
 *   mvn test -P sanity
 * </pre>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.bk.automation.stepdefinitions",
                "com.bk.automation.hooks"
        },
        tags = "@Sanity",
        plugin = {
                "pretty",
                "html:target/reports/cucumber/sanity-report.html",
                "json:target/reports/cucumber/sanity-report.json",
                "junit:target/reports/cucumber/sanity-report.xml",
                "rerun:target/rerun/failed_sanity_scenarios.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        dryRun = false,
        publish = false
)
public class SanityTestRunner {
    // This class is intentionally empty.
    // Cucumber uses annotations to configure the test execution.
}
