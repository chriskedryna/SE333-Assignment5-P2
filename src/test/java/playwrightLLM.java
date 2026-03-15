import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;

public class playwrightLLM {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext((new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void testEarbudsPurchaseWorkflow() {

        // === Home Page: Search for earbuds ===
        page.navigate("https://depaul.bncollege.com/");
        // The search textbox is hidden behind a toggle button; open it first
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("search").setExact(true)).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");

        // Apply Brand filter: JBL
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.locator("label").filter(new Locator.FilterOptions().setHasText("JBL")).first().click();

        // Apply Color filter: Black
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
        page.locator("label").filter(new Locator.FilterOptions().setHasText("Color Black")).first().click();

        // Apply Price filter: Over $50
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
        page.locator("label").filter(new Locator.FilterOptions().setHasText("Price Over $50")).first().click();

        // Click product link (product name uses "- Black" with a space before the dash)
        page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black"))
                .first().click();

        // === Product Page: Assert product name, SKU, and description ===
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black")))
                .isVisible();
        assertThat(page.getByText("668972707").first()).isVisible();
        assertThat(page.getByText("Adaptive noise cancelling").first()).isVisible();

        // Add one to cart
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();

        // Assert 1 item in cart badge (aria-label on the cart container after adding)
        assertThat(page.locator("[aria-label='Open cart menu Cart has 1 item(s).']")).isVisible();

        // Click on cart
        page.locator("[aria-label='Open cart menu Cart has 1 item(s).'] a").click();

        // === Shopping Cart Page ===
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Your Shopping Cart(1 Item)"))).isVisible();
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black")))
                .isVisible();
        assertThat(page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions()
                        .setName("Quantity, edit and press enter to update the quantity")))
                .hasValue("1");
        assertThat(page.getByText("$164.98").first()).isVisible();

        // Select FAST In-Store Pickup
        page.getByText("FAST In-Store Pickup").first().click();

        // Assert sidebar: subtotal matches earbuds price, handling $3, taxes TBD,
        // estimated total
        assertThat(page.getByText("$164.98").first()).isVisible();
        assertThat(page.getByText("$3.00").first()).isVisible();
        assertThat(page.getByText("TBD").first()).isVisible();
        assertThat(page.getByText("$167.98").first()).isVisible();

        // Enter promo code TEST, click APPLY, assert rejection message
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).fill("TEST");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply Promo Code")).click();
        assertThat(page.getByText("The coupon code entered is not valid.").first()).isVisible();

        // Proceed To Checkout (two buttons exist on this page; use the first)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout"))
                .first().click();

        // === Create Account / Login Page ===
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Create Account"))).isVisible();

        // Proceed as Guest
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

        // === Contact Information Page ===
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Contact Information"))).isVisible();

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)"))
                .fill("Chris");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)"))
                .fill("Kedryna");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)"))
                .fill("ckedryn2@depaul.edu");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)"))
                .fill("1234567890");

        // Assert sidebar totals are visible (labels on this page: "Order Subtotal",
        // "Total")
        assertThat(page.getByText("Order Subtotal").first()).isVisible();
        assertThat(page.getByText("$164.98").first()).isVisible();
        assertThat(page.getByText("$3.00").first()).isVisible();
        assertThat(page.getByText("$167.98").first()).isVisible();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

        // === Pickup Information (same Shipping & Pick Up page, contact section
        // collapsed) ===
        // Assert contact information is correct
        assertThat(page.getByText("Chris Kedryna").first()).isVisible();
        assertThat(page.getByText("ckedryn2@depaul.edu").first()).isVisible();
        assertThat(page.getByText("+11234567890").first()).isVisible();
        // Assert pickup location is DePaul University Loop Campus & SAIC
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Pickup Location"))).isVisible();
        assertThat(page.getByText("DePaul University Loop Campus & SAIC").first()).isVisible();
        // Assert pickup person "I'll pick them up" radio is checked
        assertThat(page.getByRole(AriaRole.RADIO,
                new Page.GetByRoleOptions().setName("I\u2019ll pick them up"))).isChecked();
        // Assert sidebar: subtotal, handling, taxes TBD, total
        assertThat(page.getByText("Order Subtotal").first()).isVisible();
        assertThat(page.getByText("$164.98").first()).isVisible();
        assertThat(page.getByText("$3.00").first()).isVisible();
        assertThat(page.getByText("TBD").first()).isVisible();
        assertThat(page.getByText("$167.98").first()).isVisible();
        // Assert pickup item link and price in sidebar
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions()
                        .setName("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black"))
                .first()).isVisible();
        assertThat(page.getByText("$164.98").first()).isVisible();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

        // === Payment Information Page ===
        // Assert sidebar: subtotal $164.98, handling $3.00, taxes $17.22, total $185.20
        assertThat(page.getByText("Order Subtotal").first()).isVisible();
        assertThat(page.getByText("$164.98").first()).isVisible();
        assertThat(page.getByText("$3.00").first()).isVisible();
        assertThat(page.getByText("$17.22").first()).isVisible();
        assertThat(page.getByText("$185.20").first()).isVisible();
        // Assert pickup item link and price in sidebar
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions()
                        .setName("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black"))
                .first()).isVisible();
        assertThat(page.getByText("$164.98").first()).isVisible();

        // Click BACK TO CART (upper-right "Back to cart" link in the checkout header)
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();

        // === Return to Cart: Delete product and assert empty cart ===
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove product JBL Quantum"))
                .click();
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Your cart is empty"))).isVisible();

        page.close();
    }
}
