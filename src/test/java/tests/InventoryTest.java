package tests;

import base.BaseTest;
import com.google.gson.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.InventoryPage;
import utils.DataDriven;

public class InventoryTest extends BaseTest{


    @Test
    public void verifyInventoryPageElementsAfterLogin() {



            JsonObject data = DataDriven.jsonReader();

            JsonObject valid =
                    data.getAsJsonObject("validLogin");

            String username =
                    valid.get("username").getAsString();

            String password =
                    valid.get("password").getAsString();

            LoginPage loginPage = new LoginPage(driver);

            loginPage.login(username, password);

            InventoryPage inventoryPage =
                    new InventoryPage(driver);

            // Verify URL
            Assert.assertTrue(
                    driver.getCurrentUrl().contains("/inventory.html")
            );

            // Verify browser title
            Assert.assertEquals(
                    inventoryPage.getPageTitle(),
                    "Swag Labs"
            );

            // Verify heading
            Assert.assertTrue(
                    inventoryPage.isHeadingDisplayed()
            );

            // Verify cart icon
            Assert.assertTrue(
                    inventoryPage.isCartDisplayed()
            );

            // Verify product count
            Assert.assertEquals(
                    inventoryPage.getProductsCount(),
                    6
            );
        }
    }
















