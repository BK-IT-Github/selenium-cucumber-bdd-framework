# BK Automation UI WebApp

**Enterprise-grade Selenium BDD Automation Framework** for Healthcare Facility Management application.

---

## 🏗️ Technology Stack

| Component          | Technology                              |
|--------------------|-----------------------------------------|
| Language           | Java 17                                 |
| Automation Tool    | Selenium WebDriver 4.18                 |
| BDD Framework      | Cucumber 7.15                           |
| Test Runner        | JUnit 4.13                              |
| Build Tool         | Maven                                   |
| Design Pattern     | Page Object Model (POM) + Service Layer |
| Data-Driven        | Excel (Apache POI) + JSON (Jackson)     |
| Logging            | Log4j2                                  |
| Reporting          | Cucumber Reports + Extent Spark Reports |
| CI/CD              | Jenkins (Declarative Pipeline)          |
| IDE                | IntelliJ IDEA                           |

---

## 📂 Project Structure

```
Bk.Automation.UI.WebApp/
├── pom.xml
├── Jenkinsfile
├── .gitignore
├── src/
│   ├── main/
│   │   ├── java/com/bk/automation/
│   │   │   ├── core/
│   │   │   │   ├── config/
│   │   │   │   │   ├── ConfigReader.java        # Singleton config management
│   │   │   │   │   └── Environment.java         # Environment enum
│   │   │   │   └── driver/
│   │   │   │       └── DriverFactory.java        # ThreadLocal WebDriver factory
│   │   │   ├── pages/
│   │   │   │   ├── BasePage.java                 # Abstract base page
│   │   │   │   ├── login/
│   │   │   │   │   └── LoginPage.java            # Login page object
│   │   │   │   └── dashboard/
│   │   │   │       └── DashboardPage.java        # Dashboard page object
│   │   │   ├── services/
│   │   │   │   ├── BaseService.java              # Abstract base service
│   │   │   │   ├── login/
│   │   │   │   │   └── LoginService.java         # Login business logic
│   │   │   │   └── dashboard/
│   │   │   │       └── DashboardService.java     # Dashboard business logic
│   │   │   └── utils/
│   │   │       ├── WaitUtil.java                 # Wait strategies
│   │   │       ├── ScreenshotUtil.java           # Screenshot capture
│   │   │       ├── ExcelUtil.java                # Excel data reader
│   │   │       ├── JsonUtil.java                 # JSON data reader
│   │   │       └── CommonUtils.java              # Common Selenium helpers
│   │   └── resources/
│   │       ├── config/
│   │       │   ├── config.properties             # Default configuration
│   │       │   ├── config-qa.properties           # QA environment
│   │       │   ├── config-staging.properties      # Staging environment
│   │       │   └── config-prod.properties         # Production environment
│   │       └── log4j2.xml                        # Log4j2 configuration
│   └── test/
│       ├── java/com/bk/automation/
│       │   ├── hooks/
│       │   │   └── Hooks.java                    # Cucumber lifecycle hooks
│       │   ├── runners/
│       │   │   ├── SmokeTestRunner.java          # @Smoke tag runner
│       │   │   ├── RegressionTestRunner.java     # @Regression tag runner
│       │   │   ├── SanityTestRunner.java         # @Sanity tag runner
│       │   │   └── FailedScenariosRerunRunner.java # Rerun failed tests
│       │   └── stepdefinitions/
│       │       ├── login/
│       │       │   └── LoginStepDefinitions.java
│       │       └── dashboard/
│       │           └── DashboardStepDefinitions.java
│       └── resources/
│           ├── features/
│           │   ├── login/
│           │   │   └── Login.feature             # 9 login scenarios
│           │   └── dashboard/
│           │       └── Dashboard.feature         # 10 dashboard scenarios
│           ├── testdata/
│           │   └── json/
│           │       ├── login_data.json
│           │       └── dashboard_data.json
│           ├── extent.properties                 # Extent Reports config
│           └── extent-config.xml                 # Extent Reports theme
```

---

## 🔄 Framework Architecture Flow

```
Feature File (.feature)
    ↓
Step Definitions (stepdefinitions/)
    ↓
Service Layer (services/)           ← Business logic abstraction
    ↓
Page Objects (pages/)               ← Element locators & page actions
    ↓
DriverFactory (core/driver/)        ← ThreadLocal WebDriver management
    ↓
WebDriver → Browser
```

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.9+
- Chrome / Firefox / Edge browser

### Run Smoke Tests
```bash
mvn clean test -P smoke
```

### Run Regression Tests
```bash
mvn clean test -P regression
```

### Run Sanity Tests
```bash
mvn clean test -P sanity
```

### Run with Specific Browser
```bash
mvn clean test -P smoke -Dbrowser=firefox
```

### Run with Headless Mode
```bash
mvn clean test -P smoke -Dbrowser.headless=true
```

### Run on Specific Environment
```bash
mvn clean test -P smoke -Denvironment=staging
```

### Run Specific Runner
```bash
mvn clean test -Dtest=SmokeTestRunner
```

### Rerun Failed Scenarios
```bash
mvn clean test -Dtest=FailedScenariosRerunRunner
```

### Run with Custom Tags
```bash
mvn clean test -Dcucumber.filter.tags="@Login and @Positive"
```

---

## 📊 Reporting

After test execution, reports are generated at:

| Report Type          | Location                                          |
|----------------------|---------------------------------------------------|
| Cucumber HTML        | `target/reports/cucumber/*-report.html`           |
| Cucumber JSON        | `target/reports/cucumber/*-report.json`           |
| Cucumber JUnit XML   | `target/reports/cucumber/*-report.xml`            |
| Extent Spark Report  | `target/reports/extent/SparkReport.html`          |
| Screenshots          | `target/screenshots/`                             |
| Logs                 | `target/logs/automation.log`                      |

---

## 🏷️ Tagging Strategy

| Tag              | Purpose                          |
|------------------|----------------------------------|
| `@Smoke`         | Quick sanity validation          |
| `@Regression`    | Full regression suite            |
| `@Sanity`        | Basic UI/element validation      |
| `@Positive`      | Valid/happy path scenarios        |
| `@Negative`      | Invalid/error scenarios           |
| `@Login`         | Login module scenarios            |
| `@Dashboard`     | Dashboard module scenarios        |
| `@Module-Login`  | Module-level grouping             |
| `@TC-XXX-NNN`    | Individual test case IDs          |

---

## 🔧 CI/CD (Jenkins)

The framework includes a `Jenkinsfile` with:
- **Parameterized builds** (test suite, browser, environment)
- **Headless browser execution**
- **Failed scenario rerun**
- **Report archival and publishing**
- **Workspace cleanup**

---

## 📋 Best Practices

1. **No Thread.sleep()** — All waits use explicit/fluent wait strategies
2. **ThreadLocal WebDriver** — Safe for parallel execution
3. **Service Layer** — Business logic separated from step definitions
4. **Environment-specific configs** — Easy switching between QA/Staging/Prod
5. **Rerun mechanism** — Automatically retry failed scenarios
6. **Comprehensive logging** — Log4j2 with rolling file appenders
7. **Screenshots on failure** — Embedded in Cucumber & Extent reports
8. **Data-driven testing** — Excel and JSON support
9. **Modular page objects** — One page class per application page
10. **Scalable structure** — Easy to add new modules (500+ test cases)
