
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.options.AriaRole;

import java.beans.Transient;
import java.nio.file.Paths;

public class playwrightTraditional {

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
        void testCaseBookstore() {
                // Home Page
                page.navigate("https://depaul.bncollege.com/");
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
                page.locator(
                                ".facet__list.js-facet-list.js-facet-top-values > li:nth-child(3) > form > label > .facet__list__label > .facet__list__mark > .facet-unchecked > svg")
                                .first().click();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
                page.locator(
                                "#facet-Color > .facet__values > .facet__list > li > form > label > .facet__list__label > .facet__list__mark > .facet-unchecked > svg")
                                .first().click();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
                page.locator(
                                "#facet-price > .facet__values > .facet__list > li > form > label > .facet__list__label > .facet__list__mark > .facet-unchecked > svg")
                                .click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();
                assertThat(page.getByRole(AriaRole.HEADING,
                                new Page.GetByRoleOptions().setName("JBL Quantum True Wireless"))
                                .first().isVisible());
                assertThat(page.getByText("668972707").first().isVisible());
                assertThat(page.getByText("Adaptive noise canceling").first().isVisible());
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();

                // Shopping Cart Page
                assertThat(page.getByRole(AriaRole.HEADING,
                                new Page.GetByRoleOptions().setName("Your Shopping Cart(1 Item)"))
                                .first().isVisible());
                assertThat(page.getByText("JBL Quantum True Wireless").first().isVisible());
                assertThat(page.getByRole(AriaRole.TEXTBOX,
                                new Page.GetByRoleOptions().setName("Quantity, edit and press"))
                                .first().isVisible());
                assertThat(page.getByText("$").first().isVisible());
                page.locator(".sub-check").first().click();
                assertThat(page.getByText("$").nth(1).isVisible());
                assertThat(page.getByText("$3.00", new Page.GetByTextOptions().setExact(true)).first().isVisible());
                assertThat(page.getByText("TBD").first().isVisible());
                assertThat(page.getByText("$167.98").first().isVisible());
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).fill("TEST");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply Promo Code")).click();
                assertThat(page.getByText("The coupon code entered is").first().isVisible());
                page.getByLabel("Proceed To Checkout").click();

                // Create Account Page
                assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Create Account"))
                                .first()
                                .isVisible());
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

                // Contact Information Page
                assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Contact Information"))
                                .first().isVisible());
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)"))
                                .fill("Chris");
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)")).click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)"))
                                .fill("Kedryna");
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)"))
                                .click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)"))
                                .fill("ckedryn2@depaul.edu");
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)"))
                                .click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)"))
                                .fill("1234567890");
                assertThat(page.getByText("$164.98").nth(2).isVisible());
                assertThat(page.getByText("$3.00").nth(3).isVisible());
                assertThat(page.getByText("TBD").nth(2).isVisible());
                assertThat(page.getByText("$167.98").nth(1).isVisible());
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

                // Pickup Information Page
                assertThat(page.getByText("Chris Kedryna").first().isVisible());
                assertThat(page.getByText("ckedryn2@depaul.edu").first().isVisible());
                assertThat(page.getByText("+11234567890").first().isVisible());
                assertThat(page.locator("#bnedPickupPersonForm").getByText("DePaul University Loop Campus").first()
                                .isVisible());
                assertThat(page.locator(".sub-check").first().isChecked());
                assertThat(page.getByText("$164.98").nth(2).isVisible());
                assertThat(page.getByText("$3.00").nth(3).isVisible());
                assertThat(page.getByText("TBD").nth(2).isVisible());
                assertThat(page.getByText("$167.98").nth(1).isVisible());
                assertThat(page.getByRole(AriaRole.LINK,
                                new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).nth(1).isVisible());
                assertThat(page.getByText("$164.98").nth(3).isVisible());
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

                // Payment Information Page
                assertThat(page.getByText("$164.98").nth(2).isVisible());
                assertThat(page.getByText("$3.00").nth(3).isVisible());
                assertThat(page.getByText("$17.22").nth(1).isVisible());
                assertThat(page.getByText("$185.20").nth(1).isVisible());
                assertThat(page.getByRole(AriaRole.LINK,
                                new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).nth(1).isVisible());
                assertThat(page.getByText("$164.98").nth(3).isVisible());
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();

                // Shopping Cart Page
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove product JBL Quantum"))
                                .click();
                assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Your cart is empty"))
                                .first().isVisible());
                page.close();
        }

}
