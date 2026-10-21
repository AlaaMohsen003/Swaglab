package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class InventoryPage {
    WebDriver driver;


    By pageHeading = By.className("title");
    By cartIcon = By.className("shopping_cart_link");
    By products = By.className("inventory_item");


    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // Get page title
    public String getPageTitle() {
        return driver.getTitle();
    }

    // Check heading
    public boolean isHeadingDisplayed() {
        return driver.findElement(pageHeading).isDisplayed();
    }

    // Check cart icon
    public boolean isCartDisplayed() {
        return driver.findElement(cartIcon).isDisplayed();
    }

    // Count products
    public int getProductsCount() {
        return driver.findElements(products).size();
    }
}

