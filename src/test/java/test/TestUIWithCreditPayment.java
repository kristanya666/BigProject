package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Data;
import data.SqlHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentCreditForm;

import static com.codeborne.selenide.Selenide.open;

public class TestUIWithCreditPayment {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    /** @AfterAll
    static void tearDown() {
        SqlHelper.cleanDatabases();
    } **/


    @Test
    public void testCreditPaymentWithApprovedCard() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveSuccessNotification();

        var status = SqlHelper.getCreditStatusById();
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    public void testCreditPaymentWithCurrentDate() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getCurrentDate("MM"));
        paymentCreditForm.fillYear(Data.getCurrentDate("yy"));
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveSuccessNotification();
    }

    @Test
    public void testCreditPaymentWithDeclinedCard() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getDeclinedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveErrorNotification();

        var status = SqlHelper.getCreditStatusById();
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    public void testCreditPaymentWithOneDigitInCardField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getRandomDigit());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithNotingInCardField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithZerosInCardField() {
        String zeros = "0000_0000_0000_0000";

        open("http://localhost:8080");


        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();


        paymentCreditForm.fillCardNumber(zeros);
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithPastYear() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getPastYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipOfCardExpiration(); // поле год должно иметь подсказку "Истек срок действия карты"
    }

    @Test
    public void testCreditPaymentWithPreviousMonth() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getPreviousDate("MM"));
        paymentCreditForm.fillYear(Data.getPreviousDate("yy"));
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipOfCardExpiration();
        // поле год должно иметь подсказку "Истёк срок действия карты"
    }

    @Test
    public void testCreditPaymentWithThirteenthMonth() {
        String wrongMonth = "13";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(wrongMonth);
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    public void testCreditPaymentWithZeroMonth() {
        String zeroMonth = "00";

        open("http://localhost:8080");


        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(zeroMonth);
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    public void testCreditPaymentWithZeroYear() {
        String zeroYear = "00";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(zeroYear);
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipOfCardExpiration(); // поле год должно иметь подсказку "Истек срок действия карты"
    }

    @Test
    public void testCreditPaymentWithoutDate() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithoutMonth() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithoutYear() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithYearPlusSixYears() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getYearPlusSixYears());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongTermOfCardInYearField();
        // поле год должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    public void testCreditPaymentWithSymbolsInDateField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(symbols);
        paymentCreditForm.fillYear(symbols);
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"

        paymentCreditForm.monthShouldBeEmpty(); //поле месяц должно быть пустым
        paymentCreditForm.yearShouldBeEmpty(); //поле год должно быть пустым
    }

    @Test
    public void testCreditPaymentWithOneDigitInDateField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getRandomDigit());
        paymentCreditForm.fillYear(Data.getRandomDigit());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); //поле месяц должно иметь подсказку "Неверный формат"
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); //поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithRussianTextInCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getRussianName());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithDigitInCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getRandomDigit());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithOneCharInCardholderField() {
        String characterOne = "a";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(characterOne);
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithSymbolsInCardholderField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(symbols);
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithoutCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.clickButton();


        paymentCreditForm.cardholderShouldBeFilledIn(); //поле Имя Владельца должно иметь подсказку "Поле обязательно для заполнения"
    }

    @Test
    public void testCreditPaymentWithZerosInCVCField() {
        String zeros = "000";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(zeros);
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithOneDigitInCVCField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getRandomDigit());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithoutCVCField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testCreditPaymentWithCharactersInCVCField() {
        String text = "Text";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(text);
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
        paymentCreditForm.cvvShouldBeEmpty(); //поле cvv должно быть пустым
    }

    @Test
    public void testCreditPaymentWithSymbolsInCVCField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(symbols);
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
        paymentCreditForm.cvvShouldBeEmpty(); //поле cvv должно быть пустым
    }

    @Test
    public void testCreditPaymentWithEmptyFields() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardField();
        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField();
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField();
        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField();
        paymentCreditForm.cardholderShouldBeFilledIn();
    }

    @Test
    public void testCreditPaymentWithRandomCreditNumber() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getRandomCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldReceiveErrorNotification();
    }


    @Test
    public void testCreditPaymentNotifications() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCreditForm paymentCreditForm = mainPage.selectPaymentByCredit();

        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveOnlySuccessNotification(); // должно быть видно только одно уведомление об успешной оплате
    }
}

