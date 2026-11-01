package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ---------- Locators ----------
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartItemNames = By.cssSelector("[data-test='cart-list'] [data-test='inventory-item-name']");
    private final By checkoutBtn = By.cssSelector("[data-test='checkout']");
    private final By firstName = By.cssSelector("[data-test='firstName']");
    private final By lastName = By.cssSelector("[data-test='lastName']");
    private final By postalCode = By.cssSelector("[data-test='postalCode']");
    private final By continueBtn = By.cssSelector("[data-test='continue']");
    private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
    private final By continueShoppingBtn = By.cssSelector("[data-test='continue-shopping']");
    private final By menuBtn = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    private final By linkedin = By.cssSelector("[data-test='social-linkedin']");
    private final By facebook = By.cssSelector("[data-test='social-facebook']");
    private final By twitter = By.cssSelector("footer a[href*='twitter.com'], footer a[href*='x.com']");
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ---------- Inventory helpers (by product name) ----------
    private By productCard(String name) {
        return By.xpath("//div[@data-test='inventory-item-name' and normalize-space()='" + name
                + "']/ancestor::div[@data-test='inventory-item']");
    }

    public void addProductToCart(String name) {
        driver.findElement(productCard(name)).findElement(By.tagName("button")).click();
    }

    public String getInventoryButtonText(String name) {
        return driver.findElement(productCard(name)).findElement(By.tagName("button")).getText().trim();
    }

    public double getInventoryPrice(String name) {
        String text = driver.findElement(productCard(name))
                .findElement(By.cssSelector("[data-test='inventory-item-price']")).getText();
        return parsePrice(text);
    }

    // ---------- Cart actions ----------
    public void openCart() {
        driver.findElement(cartLink).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    public List<String> getCartItemNames() {
        return driver.findElements(cartItemNames).stream()
                .map(WebElement::getText).map(String::trim).collect(Collectors.toList());
    }

    public int getCartItemsCount() {
        return driver.findElements(cartItemNames).size();
    }

    public void removeFromCart(String name) {
        String id = name.toLowerCase().replace(" ", "-");
        driver.findElement(By.cssSelector("[data-test='remove-" + id + "']")).click();
    }

    public void continueShopping() {
        driver.findElement(continueShoppingBtn).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    public void clickCheckout() {
        driver.findElement(checkoutBtn).click();
    }

    public void fillCheckoutInfoAndContinue(String first, String last, String zip) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).sendKeys(first);
        driver.findElement(lastName).sendKeys(last);
        driver.findElement(postalCode).sendKeys(zip);
        driver.findElement(continueBtn).click();
    }

    public double getCheckoutItemTotal() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel)).getText();
        // "Item total: $75.97"
        return parsePrice(text.substring(text.indexOf('$')));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void logout() {
        driver.findElement(menuBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/"));
    }

    // ---------- Social links (open in a new tab) ----------
    public String clickLinkedInAndGetUrl() { return clickSocialAndGetUrl(linkedin); }
    public String clickFacebookAndGetUrl() { return clickSocialAndGetUrl(facebook); }
    public String clickTwitterAndGetUrl()  { return clickSocialAndGetUrl(twitter); }

    private String clickSocialAndGetUrl(By locator) {
        String original = driver.getWindowHandle();
        Set<String> before = driver.getWindowHandles();

        WebElement icon = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", icon);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);

        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(d -> d.getWindowHandles().size() > before.size());

        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        handles.removeAll(before);
        driver.switchTo().window(handles.get(0));

        String url = driver.getCurrentUrl();
        driver.close();
        driver.switchTo().window(original);
        return url;
    }

    // ---------- Util ----------
    private double parsePrice(String text) {
        return Double.parseDouble(text.replace("$", "").trim());
    }
}