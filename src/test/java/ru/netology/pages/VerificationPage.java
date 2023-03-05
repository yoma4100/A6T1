package ru.netology.pages;

import ru.netology.data.DataHelper;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    final SelenideElement heading = $(".paragraph");
    final SelenideElement codeField = $("[name='code']");
    final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        heading.shouldHave(text(DataHelper.getVerificationPageHeader()));
        codeField.shouldBe(visible);
    }

    public DashboardPage validVerify() {
        String verificationCode = DataHelper.getVerificationCode();
        codeField.setValue(verificationCode);
        verifyButton.click();
        return new DashboardPage();
    }
}