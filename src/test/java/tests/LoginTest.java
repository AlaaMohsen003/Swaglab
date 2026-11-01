package tests;

import base.BaseTest;

import com.google.gson.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DataDriven;
public class LoginTest extends BaseTest {
    @Test
    public void verifySuccessfulLogin() {

        JsonObject data = DataDriven.jsonReader();

        JsonObject valid =
                data.getAsJsonObject("validLogin");

        String username =
                valid.get("username").getAsString();

        String password =
                valid.get("password").getAsString();

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/inventory.html"),
                "User should be redirected to inventory page"
        );
    }

    @Test
    public void verifyInvalidLogin() {

        JsonObject data = DataDriven.jsonReader();

        JsonObject invalid =
                data.getAsJsonObject("invalidLogin");

        String username =
                invalid.get("username").getAsString();

        String password =
                invalid.get("password").getAsString();

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed());

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Username and password do not match")
        );
    }

    @Test
    public void verifyLoginWithoutPassword() {

        JsonObject data = DataDriven.jsonReader();

        JsonObject valid =
                data.getAsJsonObject("validLogin");

        String username =
                valid.get("username").getAsString();

        LoginPage loginPage = new LoginPage(driver);

        loginPage.loginWithoutPassword(username);

        Assert.assertTrue(loginPage.isErrorDisplayed());

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Password is required")
        );
    }
}

