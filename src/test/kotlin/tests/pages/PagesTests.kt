package tests.pages

import base.BaseTest
import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.options.AriaRole
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

@Tag("smoke")
@DisplayName("Открытие страниц сайта")
@Timeout(value = 60, unit = TimeUnit.SECONDS)
class PagesTests : BaseTest() {

    @TestFactory
    @DisplayName("Каждая страница открывается и показывает свой заголовок")
    fun pagesHaveHeadings(): List<DynamicTest> {
        val pages = mapOf(
            "/checkboxes" to "Checkboxes",
            "/dropdown" to "Dropdown List",
            "/login" to "Login Page"
        )

        return pages.map { (path, heading) ->
            DynamicTest.dynamicTest("$path → «$heading»") {
                page.navigate(path)
                assertThat(
                    page.getByRole(AriaRole.HEADING, Page.GetByRoleOptions().setName(heading))
                ).isVisible()
            }
        }
    }
}
