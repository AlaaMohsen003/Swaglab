package tests;

import base.BaseTest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class CartTest extends BaseTest {

    private JsonObject data;

    @BeforeMethod // rename to match your BaseTest setup method

    public void init() throws IOException {
        data = new Gson().fromJson(
                new FileReader("src/test/resources/testData.json"), JsonObject.class);

        JsonObject validLogin = data.getAsJsonObject("validLogin");
        new LoginPage(driver).login(
                validLogin.get("username").getAsString(),
                validLogin.get("password").getAsString());
        cartPage = new CartPage(driver);
    }


    private List<String> cartProducts() {
        List<String> list = new ArrayList<>();
        data.getAsJsonArray("cartProducts").forEach(e -> list.add(e.getAsString()));
        return list;
    }

    private void addAllCartProducts() {
        cartProducts().forEach(cartPage::addProductToCart);
    }

    //
    @Test(priority = 1)
    public void verifySocialLinks() {
        Assert.assertTrue(cartPage.clickLinkedInAndGetUrl().contains("linkedin"), "LinkedIn URL");
        Assert.assertTrue(cartPage.clickFacebookAndGetUrl().contains("facebook"), "Facebook URL");
        Assert.assertTrue(cartPage.clickTwitterAndGetUrl().contains("x.com"), "X URL");
    }

    // 2
    @Test(priority = 2)
    public void verifyCartIsEmpty() {
        cartPage.openCart();
        Assert.assertEquals(cartPage.getCartItemsCount(), 0, "Cart should be empty");
    }

    // 3
    @Test(priority = 3)
    public void addThreeProductsAndVerifyOrder() {
        List<String> expected = cartProducts();
        addAllCartProducts();
        cartPage.openCart();
        Assert.assertEquals(cartPage.getCartItemNames(), expected,
                "Cart items/order should match the order they were added");
    }

    // 4
    @Test(priority = 4)
    public void removeOneProduct() {
        List<String> products = cartProducts();
        String toRemove = "Sauce Labs Bolt T-Shirt";
        addAllCartProducts();
        cartPage.openCart();
        cartPage.removeFromCart(toRemove);
        cartPage.continueShopping();

        Assert.assertEquals(cartPage.getInventoryButtonText(toRemove), "Add to cart");
        for (String p : products) {
            if (!p.equals(toRemove)) {
                Assert.assertEquals(cartPage.getInventoryButtonText(p), "Remove", p);
            }
        }
    }

    // 5
    @Test(priority = 5)
    public void verifyCartTotalPrice() {
        List<String> products = cartProducts();

        double expectedSum = 0;
        for (String p : products) {
            expectedSum += cartPage.getInventoryPrice(p);   // read BEFORE adding
        }

        addAllCartProducts();
        cartPage.openCart();
        cartPage.clickCheckout();
        cartPage.fillCheckoutInfoAndContinue("Test", "User", "12345");

        double itemTotal = cartPage.getCheckoutItemTotal();
        Assert.assertEquals(itemTotal, expectedSum, 0.001, "Item total mismatch");
    }

    // 6
    // Real behavior on saucedemo.com (verify manually!): the site does NOT block
    // checkout with an empty cart - it opens checkout-step-one normally.
    @Test(priority = 6)
    public void checkoutWithEmptyCart() {
        cartPage.openCart();
        Assert.assertEquals(cartPage.getCartItemsCount(), 0, "Precondition: cart empty");
        cartPage.clickCheckout();
        Assert.assertTrue(cartPage.getCurrentUrl().contains("checkout-step-one"),
                "Site lets user proceed to checkout step one with an empty cart");
    }

    // 7
    // Real behavior (verify manually!): the cart is kept in browser localStorage,
    // so items are still there after logout/login in the same browser session.

    @Test(priority = 7)
    public void cartStateAfterLogoutLogin() {
        List<String> added = Arrays.asList(
                cartProducts().get(0), cartProducts().get(1));
        added.forEach(cartPage::addProductToCart);

        cartPage.logout();

        JsonObject validLogin = data.getAsJsonObject("validLogin");
        new LoginPage(driver).login(
                validLogin.get("username").getAsString(),
                validLogin.get("password").getAsString());

        cartPage.openCart();
        Assert.assertEquals(cartPage.getCartItemNames(), added,
                "Cart items should persist after logout/login");
    }
}