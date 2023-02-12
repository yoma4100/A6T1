package ru.netology;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class BalanceTest {
    String loginName = "vasya";
    String loginPassword = "qwerty123";
    String verificationCode = "12345";
    String cardReceiveNumber = "1";
    String cardSendNumber = "2";
    String firstCardNumber = "5559 0000 0000 0001";
    String secondCardNumber = "5559 0000 0000 0002";
    int transferAmount=100;

    String stringTransferAmount = Integer.toString(transferAmount);

    @Test
    public void test() {

        open("http://localhost:9999/");

        clearBrowserCookies();
        clearBrowserLocalStorage();

        //START PAGE

        $(".paragraph").shouldHave(text("Мы гарантируем безопасность ваших данных"));
        $("[name='login']").shouldBe(visible);
        $("[name='login']").click();
        $("[name='login']").setValue(loginName);
        $("[name='password']").shouldBe(visible);
        $("[name='password']").click();
        $("[name='password']").setValue(loginPassword);
        $("[data-test-id='action-login']").shouldBe(visible);
        $("[data-test-id='action-login']").click();

        //VERIFICATION PAGE
        $(".paragraph").shouldHave(text("Необходимо подтверждение"));
        $("[name='code']").shouldBe(visible);
        $("[name='code']").click();
        $("[name='code']").setValue(verificationCode);
        $("[data-test-id='action-verify']").shouldBe(visible);
        $("[data-test-id='action-verify']").click();

        //DASHBOARD PAGE
        $(".heading_size_xl").shouldHave(text("Ваши карты"));

        //запоминание изначального баланса карты на которую осуществляется перевод
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";

        String balanceValueReceive = $(".list__item:nth-of-type(" + cardReceiveNumber + ") div").getText();
        int startReceive = balanceValueReceive.indexOf(balanceStart);
        int finishReceive = balanceValueReceive.indexOf(balanceFinish);
        balanceValueReceive = balanceValueReceive.substring(startReceive + balanceStart.length(), finishReceive);
        int startBalanceValueReceive = Integer.parseInt(balanceValueReceive);

        //запоминание изначального баланса карты с которой осуществляется перевод
        String balanceValueSend = $(".list__item:nth-of-type(" + cardSendNumber + ") div").getText();
        int startSend = balanceValueSend.indexOf(balanceStart);
        int finishSend = balanceValueSend.indexOf(balanceFinish);
        balanceValueSend = balanceValueSend.substring(startSend + balanceStart.length(), finishSend);
        int startBalanceValueSend = Integer.parseInt(balanceValueSend);

        //закончили запоминание

        //определение итогового баланса и приведение его к строке

        int balanceAfterReceive = startBalanceValueReceive + transferAmount;
        int balanceAfterSend = startBalanceValueSend - transferAmount;
        String concatenateReceiveBalance = "**** **** **** 0001, баланс: " + balanceAfterReceive + " р." + System.lineSeparator() + "Пополнить";
        String concatenateSendBalance = "**** **** **** 0002, баланс: " + balanceAfterSend + " р." + System.lineSeparator() + "Пополнить";

        //завершение определения и преведния

        $(":nth-of-type(" + cardReceiveNumber + ") [data-test-id='action-deposit']").shouldBe(visible);
        $(":nth-of-type(" + cardReceiveNumber + ") [data-test-id='action-deposit']").click();

        //TRANSFER PAGE
        $(".heading_size_xl").shouldHave(text("Пополнение карты"));
        $("span[data-test-id='amount'] input").shouldBe(visible);
        $("span[data-test-id='amount'] input").click();
        $("span[data-test-id='amount'] input").setValue(stringTransferAmount);
        $("span[data-test-id='from'] input").shouldBe(visible);
        $("span[data-test-id='from'] input").click();
        //TODO заменить Keys.COMMAND на Keys.CONTROL перед пушем
        $("span[data-test-id='from'] input").sendKeys(Keys.chord(Keys.COMMAND + "A"), Keys.BACK_SPACE);
        $("span[data-test-id='from'] input").setValue(secondCardNumber);

        //сравнение карты отображаемой в поле "Куда" с картой, на которую осуществляется перевод

        int startNumber = firstCardNumber.length() - 4;
        int endNumber = firstCardNumber.length();
        String outputNumber = "";

        for (int i = 0; i < endNumber; i++) {
            if (i < startNumber) {
                if (firstCardNumber.charAt(i) == ' ') {
                    outputNumber += " ";
                } else {
                    outputNumber += "*";
                }
            } else {
                outputNumber += firstCardNumber.charAt(i);
            }
        }

        $(":nth-of-type(3) input.input__control[value]").shouldBe(visible);
        $(":nth-of-type(3) input.input__control[value]").shouldHave(attribute("value", outputNumber));

        //окончание сравнения карт

        $("[data-test-id='action-transfer']").shouldBe(visible);
        $("[data-test-id='action-transfer']").click();

        //TODO после разнесения кода по классам страниц не забыть описать кейс
        // с кнопкой отмены (селектор [data-test-id='action-cancel'])
        // с попыткой перевода денег без указания карты
        // с поптыткой перевода денег на несуществующую карту
        // с попыткой перевода отрицательной суммы
        // с попыткой перевода без указания суммы
        // с попыткой перевода всей суммы
        // с попыткой перевода суммы большей доступного остатка

        //DASHBOARD PAGE
        $(".heading_size_xl").shouldHave(text("Ваши карты"));

        //сравнение балансов карт после перевод
        $(".list__item:nth-of-type(" + cardReceiveNumber + ")").shouldHave(text(concatenateReceiveBalance));
        $(".list__item:nth-of-type(" + cardSendNumber + ")").shouldHave(text(concatenateSendBalance));
    }
}
