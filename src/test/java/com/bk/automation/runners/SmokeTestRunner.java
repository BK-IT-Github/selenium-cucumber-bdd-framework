package com.bk.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Smoke Test Runner.
 * Executes only scenarios tagged with @Smoke.
 *
 * <p>Usage:
 * <pre>
 *   mvn test -Dtest=SmokeTestRunner
 *   mvn test -P smoke
 * </pre>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.bk.automation.stepdefinitions",
                "com.bk.automation.hooks"
        },
        tags = "@Smoke",
        plugin = {
                "pretty",
                "html:target/reports/cucumber/smoke-report.html",
                "json:target/reports/cucumber/smoke-report.json",
                "junit:target/reports/cucumber/smoke-report.xml",
                "rerun:target/rerun/failed_smoke_scenarios.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        dryRun = false,
        publish = false
)
public class SmokeTestRunner {
    // This class is intentionally empty.
    // Cucumber uses annotations to configure the test execution.
}
