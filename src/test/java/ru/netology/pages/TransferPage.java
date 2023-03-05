package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferPage {

    final SelenideElement heading = $(".heading_size_xl");
    final SelenideElement amountField = $("span[data-test-id='amount'] input");
    final SelenideElement fromField = $("span[data-test-id='from'] input");
    final SelenideElement toField = $(":nth-of-type(3) input.input__control");
    final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    final SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    final SelenideElement errorElement = $("[data-test-id='error-notification']");
    final SelenideElement errorElementText = $("[data-test-id='error-notification'] div[class='notification__title']");

    public void checkHeading() {
        heading.shouldHave(text(DataHelper.getTransferPageHeader()));
    }

    public void setAmount(String amount) {
        amountField.click();
        amountField.setValue(amount);
    }

    public void setFromCard(String cardNumber) {
        fromField.click();
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

    public void transferMoney(int amount, String fromCardNumber, String toCardNumber) {
        setAmount(String.valueOf(amount));
        setFromCard(fromCardNumber);
        DataHelper.checkToCard(toField, toCardNumber);

        //TODO дополнить проверки после починки бага https://github.com/yoma4100/A6T1/issues/1
        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toField, toCardNumber)));

        clickTransferButton();

        if (errorElement.isDisplayed()) {
            assertEquals("Ошибка", errorElementText.getText().trim());
        }
    }

    public void transferMoneyFromFirstToSecondCard(int amount) {
        transferMoney(amount, DataHelper.getFirstCardNumber(), DataHelper.getSecondCardNumber());
    }

    public void transferMoneyFromSecondToFirstCard(int amount) {
        transferMoney(amount, DataHelper.getSecondCardNumber(), DataHelper.getFirstCardNumber());
    }

    public void transferMoneyFromFirstToFirstCard(int amount) {
        transferMoney(amount, DataHelper.getFirstCardNumber(), DataHelper.getFirstCardNumber());
    }

    public void transferMoneyFromSecondToSecondCard(int amount) {
        transferMoney(amount, DataHelper.getSecondCardNumber(), DataHelper.getSecondCardNumber());
    }

    public void transferMoneyFromNonExistToSecondCard(int amount) {
        transferMoney(amount, DataHelper.getNonExistCardNumber(), DataHelper.getSecondCardNumber());
    }

    public void transferMoneyFromNonExistToFirstCard(int amount) {
        transferMoney(amount, DataHelper.getNonExistCardNumber(), DataHelper.getFirstCardNumber());
    }

    public void transferEmptyAmountFirstToSecondCard() {
        setFromCard(DataHelper.getFirstCardNumber());
        DataHelper.checkToCard(toField, DataHelper.getSecondCardNumber());

        //TODO дополнить проверки после починки бага https://github.com/yoma4100/A6T1/issues/4
        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toField, DataHelper.getSecondCardNumber())));

        clickTransferButton();
    }

    public void transferEmptyAmountFromSecondToFirstCard() {
        setFromCard(DataHelper.getSecondCardNumber());
        DataHelper.checkToCard(toField, DataHelper.getFirstCardNumber());

        //TODO дополнить проверки после починки бага https://github.com/yoma4100/A6T1/issues/4
        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toField, DataHelper.getFirstCardNumber())));

        clickTransferButton();
    }

    public void cancelTransferFromFirstToSecondCard(int amount) {
        setAmount(String.valueOf(amount));
        setFromCard(DataHelper.getFirstCardNumber());
        DataHelper.checkToCard(toField, DataHelper.getSecondCardNumber());

        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toField, DataHelper.getSecondCardNumber())));

        clickCancelButton();
    }

    public void cancelTransferFromSecondToFirstCard(int amount) {
        setAmount(String.valueOf(amount));
        setFromCard(DataHelper.getSecondCardNumber());
        DataHelper.checkToCard(toField, DataHelper.getFirstCardNumber());

        toField.shouldBe(visible);
        toField.shouldHave(value(DataHelper.checkToCard(toField, DataHelper.getFirstCardNumber())));

        clickCancelButton();
    }
}