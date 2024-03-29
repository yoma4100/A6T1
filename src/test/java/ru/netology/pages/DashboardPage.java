package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.text;

import ru.netology.data.DataHelper;

public class DashboardPage {
    final SelenideElement heading = $(".heading_size_xl");
    final SelenideElement depositButton1 = $(":nth-of-type(1) [data-test-id='action-deposit']");
    final SelenideElement depositButton2 = $(":nth-of-type(2) [data-test-id='action-deposit']");

    public DashboardPage() {
        heading.shouldHave(text(DataHelper.getDashboardPageHeader()));
    }

    public int getCardBalance(String cardNumber) {
        return DataHelper.getCardBalance(cardNumber);
    }

    public TransferPage depositToCard1() {
        depositButton1.click();
        return new TransferPage();
    }

    public TransferPage depositToCard2() {
        depositButton2.click();
        return new TransferPage();
    }
}