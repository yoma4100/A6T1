package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    final SelenideElement heading = $(".paragraph");
    final SelenideElement loginField = $("[name='login']");
    final SelenideElement passwordField = $("[name='password']");
    final SelenideElement loginButton = $("[data-test-id='action-login']");

    public LoginPage() {
        heading.shouldHave(text(DataHelper.getLoginPageHeader()));
        loginField.shouldBe(visible);
    }

    public VerificationPage validLogin() {
        String login = DataHelper.getValidLogin();
        String password = DataHelper.getValidPassword();
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
        return new VerificationPage();
    }
}