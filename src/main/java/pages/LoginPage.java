package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class LoginPage {

    WebDriver driver;

    // Locators
    By usernameField = By.id("user-name");
    By passwordField = By.id("password");
    By loginButton = By.id("login-button");
    By errorMessage = By.cssSelector("[data-test='error']");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Enter username
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    // Enter password
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    // Click login
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Login method
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Login without password
    public void loginWithoutPassword(String username) {
        enterUsername(username);
        clickLogin();
    }

    // Get error message
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    // Check error visibility
    public boolean isErrorDisplayed() {
        return driver.findElement(errorMessage).isDisplayed();
    }
}

