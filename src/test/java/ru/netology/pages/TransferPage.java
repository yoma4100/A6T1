package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    final SelenideElement heading = $(".heading_size_xl");
    final SelenideElement amountField = $("span[data-test-id='amount'] input");
    final SelenideElement fromField = $("span[data-test-id='from'] input");
    final SelenideElement toField = $(":nth-of-type(3) input.input__control");
    final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    final SelenideElement errorElement = $("[data-test-id='error-notification']");

    public void checkHeading(String transferPageHeader) {
        heading.shouldHave(text(transferPageHeader));
    }

    public void checkError() {
        errorElement.shouldBe(visible);
    }

    public void setAmount(String amount) {
        amountField.setValue(amount);
    }

    public void setFromCard(String cardNumber) {
        fromField.sendKeys(Keys.chord(Keys.CONTROL + "A"), Keys.BACK_SPACE);
        fromField.setValue(cardNumber);
    }

    public void clickTransferButton() {
        transferButton.shouldBe(visible);
        transferButton.click();
    }

    public void clickCancelButton() {
        cancelButton.shouldBe(visible);
        cancelButton.click();
    }

    public void transferMoney(String fromCardNumber, String toCardNumber) {
        setFromCard(fromCardNumber);

        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toCardNumber)));

        clickTransferButton();
    }

    public void transferMoney(int amount, String fromCardNumber, String toCardNumber) {
        setAmount(String.valueOf(amount));
        setFromCard(fromCardNumber);

        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toCardNumber)));

        clickTransferButton();
    }

    public void cancelTransfer(int amount, String fromCardNumber, String toCardNumber) {
        setAmount(String.valueOf(amount));
        setFromCard(fromCardNumber);

        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toCardNumber)));

        clickCancelButton();
    }
}