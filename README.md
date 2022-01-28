# e2e Test Cucumber

e2e Automation tests using `Cucumber` and `TestNG` framework.

# End to End tests in local environment
Chrome browse is used for test execution. <br/>
Chrome driver is present in `/src/resources/driver/chromedriver.exe`

## Run Tests
- `mvn clean install -Dbrowser=chrome -Dmode=local -Denvironment=local -DsuiteXmlFile=E2e_test.xml`


## Test results
On build complete, result will be present in:
`../target/results/cucumber-maven-reports/cucumber-html-reports/overview-features.html`
And the output will look like this:  ![Cucumber Result](./src/resources/img/cucumber_result.JPG)
