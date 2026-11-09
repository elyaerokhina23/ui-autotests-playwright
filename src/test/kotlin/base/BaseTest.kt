package base

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

open class BaseTest {

    companion object {
        lateinit var playwright: Playwright
        lateinit var browser: Browser

        @JvmStatic
        @BeforeAll
        fun launchBrowser() {
            playwright = Playwright.create()
            browser = playwright.chromium().launch(
                BrowserType.LaunchOptions().setHeadless(false)
            )
        }

        @JvmStatic
        @AfterAll
        fun closeBrowser() {
            browser.close()
            playwright.close()
        }
    }

    lateinit var context: BrowserContext
    lateinit var page: Page

    @BeforeEach
    fun openPage() {
        context = browser.newContext(
            Browser.NewContextOptions().setBaseURL("https://the-internet.herokuapp.com")
        )
        page = context.newPage()
    }

    @AfterEach
    fun closePage() {
        context.close()
    }
}
