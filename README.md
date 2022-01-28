# campus-test-e2e

# End to End tests in local environment
End2end tests use Chrome browser to execute the tests.
Chrome driver is present in `/src/resources/driver/chromedriver.exe`

## Run Testsdd .
- `mvn clean install -Dbrowser=chrome -Dmode=local -Denvironment=local -DsuiteXmlFile=E2e_test.xml`


## Test results
On build complete, result will be present in:
`../target/results/cucumber-maven-reports/cucumber-html-reports/overview-features.html`
And the output will look like this:  ![Cucumber Result](./src/resources/img/cucumber_result.png)
"# e2e-test-google" 
