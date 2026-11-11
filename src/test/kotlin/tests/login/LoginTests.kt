package tests.login

import base.BaseTest
import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.options.AriaRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@Tag("login")
@DisplayName("Страница логина")
class LoginTests : BaseTest() {

    @BeforeEach
    fun openLoginPage() {
        page.navigate("/login")
    }

    private fun login(username: String, password: String) {
        page.getByLabel("Username").fill(username)
        page.getByLabel("Password").fill(password)
        page.getByRole(AriaRole.BUTTON, Page.GetByRoleOptions().setName("Login")).click()
    }

    @Test
    @Tag("smoke")
    @DisplayName("Успешный вход с правильным логином и паролем")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    fun successfulLogin() {
        login("tomsmith", "SuperSecretPassword!")

        assertAll(
            { assertThat(page).hasURL(Pattern.compile(".*/secure")) },
            { assertThat(page.locator("#flash")).containsText("You logged into a secure area!") },
            { assertThat(page.getByRole(AriaRole.LINK, Page.GetByRoleOptions().setName("Logout"))).isVisible() }
        )
    }

    @ParameterizedTest(name = "{index}: логин={0}, пароль={1}")
    @CsvSource(
        "wronguser, SuperSecretPassword!, Your username is invalid!",
        "tomsmith,  wrongpassword,        Your password is invalid!",
        "'',        '',                   Your username is invalid!"
    )
    @DisplayName("Вход с неверными данными показывает ошибку")
    fun invalidLogin(username: String, password: String, expectedError: String) {
        login(username, password)

        assertAll(
            { assertThat(page).hasURL(Pattern.compile(".*/login")) },
            { assertThat(page.locator("#flash")).containsText(expectedError) }
        )
    }

    @Test
    @Disabled("Временно отключён: восстановление пароля на тестовом сайте работает нестабильно")
    @DisplayName("Восстановление пароля")
    fun forgotPassword() {
        page.navigate("/forgot_password")
    }

    @Nested
    @DisplayName("После успешного входа")
    inner class AfterLogin {

        @BeforeEach
        fun loginFirst() {
            login("tomsmith", "SuperSecretPassword!")
        }

        @Test
        @Tag("smoke")
        @DisplayName("Кнопка Logout возвращает на страницу логина")
        fun logout() {
            page.getByRole(AriaRole.LINK, Page.GetByRoleOptions().setName("Logout")).click()

            assertAll(
                { assertThat(page).hasURL(Pattern.compile(".*/login")) },
                { assertThat(page.locator("#flash")).containsText("You logged out of the secure area!") }
            )
        }
    }
}
