package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.TransferPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    private DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        clearBrowserCookies();
        clearBrowserLocalStorage();

        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin();
        dashboardPage = verificationPage.validVerify();
    }

    @AfterEach
    void closeUp() {
        Configuration.holdBrowserOpen = false;
    }


    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        int amount = DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromFirstToSecondCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - amount;
        int balance2ndCardExp = balance2ndCardStart + amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        int amount = DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromSecondToFirstCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + amount;
        int balance2ndCardExp = balance2ndCardStart - amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
        // данный тест должен приводить к появлению какого-нибудь информера о невозможности перевода суммы
        // на карту отправителя, как следствие, падению, но у нас есть баг
        // https://github.com/yoma4100/A6T1/issues/1
    void shouldTransferMoneyFromFirstToFirstCard() {
        int amount = DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromFirstToFirstCard(amount);

        //TODO удалить часть кода ниже после починки бага https://github.com/yoma4100/A6T1/issues/1
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
        // данный тест должен приводить к появлению какого-нибудь информера о невозможности перевода суммы
        // на карту отправителя, как следствие, падению, но у нас есть баг
        // https://github.com/yoma4100/A6T1/issues/1
    void shouldTransferMoneyFromSecondToSecondCard() {
        int amount = DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromSecondToSecondCard(amount);

        //TODO удалить часть кода ниже после починки бага https://github.com/yoma4100/A6T1/issues/1
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferMoneyFromNonExistToSecondCard() {
        int amount = DataHelper.getTransferAmount();
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromNonExistToSecondCard(amount);
    }

    @Test
    void shouldTransferMoneyFromNonExistToFirstCard() {
        int amount = DataHelper.getTransferAmount();
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromNonExistToFirstCard(amount);
    }

    @Test
    void shouldTransferAllMoneyFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromFirstToSecondCard(balance1stCardStart);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart + balance1stCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferAllMoneyFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromSecondToFirstCard(balance2ndCardStart);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + balance2ndCardStart;
        int balance2ndCardExp = balance2ndCardStart - balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
        // данный тест должен приводить к появлению какого-нибудь информера о невозможности перевода суммы
        // большей доступного остатка и, как следствие, падению, но у нас есть баг
        // https://github.com/yoma4100/A6T1/issues/2
    void shouldTransferMoreThanAmountFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int amount = balance1stCardStart + 1;
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromFirstToSecondCard(amount);

        //TODO удалить часть кода ниже после починки бага https://github.com/yoma4100/A6T1/issues/2
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - amount;
        int balance2ndCardExp = balance2ndCardStart + amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
        // данный тест должен приводить к появлению какого-нибудь информера о невозможности перевода суммы
        // большей доступного остатка и, как следствие, падению, но у нас есть баг
        // https://github.com/yoma4100/A6T1/issues/2
    void shouldTransferMoreThanAmountFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int amount = balance2ndCardStart + 1;
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromSecondToFirstCard(amount);

        //TODO удалить часть кода ниже после починки бага https://github.com/yoma4100/A6T1/issues/2
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + amount;
        int balance2ndCardExp = balance2ndCardStart - amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferNegativeAmountFromFirstToSecondCard() {
        int amount = -DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromFirstToSecondCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + amount;
        int balance2ndCardExp = balance2ndCardStart - amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferNegativeAmountFromSecondToFirstCard() {
        int amount = -DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromSecondToFirstCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - amount;
        int balance2ndCardExp = balance2ndCardStart + amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferZeroFromFirstToSecondCard() {
        int amount = 0;
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferMoneyFromFirstToSecondCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - amount;
        int balance2ndCardExp = balance2ndCardStart + amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferZeroFromSecondToFirstCard() {
        int amount = 0;
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferMoneyFromSecondToFirstCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + amount;
        int balance2ndCardExp = balance2ndCardStart - amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
        // данный тест должен приводить к появлению какого-нибудь информера о невозможности перевода суммы
        // без указания суммы, как следствие, падению, но у нас есть баг
        // https://github.com/yoma4100/A6T1/issues/4
    void shouldTransferEmptyAmountFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.transferEmptyAmountFirstToSecondCard();

        //TODO удалить часть кода ниже после починки бага https://github.com/yoma4100/A6T1/issues/4
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
        // данный тест должен приводить к появлению какого-нибудь информера о невозможности перевода суммы
        // без указания суммы, как следствие, падению, но у нас есть баг
        // https://github.com/yoma4100/A6T1/issues/4
    void shouldTransferEmptyAmountFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.transferEmptyAmountFromSecondToFirstCard();

        //TODO удалить часть кода ниже после починки бага https://github.com/yoma4100/A6T1/issues/4
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldCancelTransferFromFirstToSecondCard() {
        int amount = DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading();
        transferPage.cancelTransferFromFirstToSecondCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldCancelTransferFromSecondToFirstCard() {
        int amount = DataHelper.getTransferAmount();
        int balance1stCardStart = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading();
        transferPage.cancelTransferFromSecondToFirstCard(amount);
        dashboardPage = new DashboardPage();
        int balance1stCardFact = dashboardPage.getCardBalance(DataHelper.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(DataHelper.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }
}