package ru.netology.data;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class DataHelper {
    public static String getLoginPageHeader() {
        return "Мы гарантируем безопасность ваших данных";
    }

    public static String getVerificationPageHeader() {
        return "Необходимо подтверждение";
    }

    public static String getDashboardPageHeader() {
        return "Ваши карты";
    }

    public static String getTransferPageHeader() {
        return "Пополнение карты";
    }

    public static String getValidLogin() {
        return "vasya";
    }

    public static String getValidPassword() {
        return "qwerty123";
    }

    public static String getVerificationCode() {
        return "12345";
    }

    public static String getFirstCardNumber() {
        return "5559 0000 0000 0001";
    }

    public static String getSecondCardNumber() {return "5559 0000 0000 0002";}
    public static String getNonExistCardNumber() {return "5559 0000 0000 0003";}

    public static int getTransferAmount() {
        return 100;
    }

    public static int getCardBalance(String cardNumber) {
        String cardIndex = (cardNumber.charAt(cardNumber.length() - 1) == '1') ? "1" : "2";
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";
        String balanceValue = $(".list__item:nth-of-type(" + cardIndex + ") div").getText();
        int start = balanceValue.indexOf(balanceStart);
        int finish = balanceValue.indexOf(balanceFinish);
        balanceValue = balanceValue.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(balanceValue);
    }

    public static String checkToCard(SelenideElement toField, String expectedCardNumber) {
        int startNumber = expectedCardNumber.length() - 4;
        int endNumber = expectedCardNumber.length();
        String outputNumber = "";

        for (int i = 0; i < endNumber; i++) {
            if (i < startNumber) {
                if (expectedCardNumber.charAt(i) == ' ') {
                    outputNumber += " ";
                } else {
                    outputNumber += "*";
                }
            } else {
                outputNumber += expectedCardNumber.charAt(i);
            }
        }
        return outputNumber;
    }
}