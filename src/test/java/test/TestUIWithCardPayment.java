package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Data;
import data.SqlHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PaymentCardForm;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;


public class TestUIWithCardPayment {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    static void tearDown() {
        SqlHelper.cleanDatabases();
    }

    @Test
    public void testWithApprovedCard() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldReceiveSuccessNotification();

        var status = SqlHelper.getPaymentStatusById();
        Assertions.assertEquals("APPROVED", status);

        var amount = SqlHelper.getPaymentAmountById();
        Assertions.assertEquals("4500000", amount); //45 000 руб здесь указаны в копейках

    }

    @Test
    public void testWithCurrentDate() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getCurrentDate("MM"));
        paymentCardForm.fillYear(Data.getCurrentDate("yy"));
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldReceiveSuccessNotification();
    }

    @Test
    public void testWithDeclinedCard() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getDeclinedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldReceiveErrorNotification();


        var status = SqlHelper.getPaymentStatusById();
        Assertions.assertEquals("DECLINED", status);

    }

    @Test
    public void testWithOneDigitInCardField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getRandomDigit());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithNotingInCardField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithZerosInCardField() {
        open("http://localhost:8080");
        String zeros = "0000_0000_0000_0000";

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(zeros);
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithPastYear() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getPastYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipOfCardExpiration(); // поле год должно иметь подсказку "Истек срок действия карты"
    }

    @Test
    public void testWithPreviousMonth() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getPreviousDate("MM"));
        paymentCardForm.fillYear(Data.getPreviousDate("yy"));
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipOfCardExpiration();
        // поле год должно иметь подсказку "Истёк срок действия карты"
    }

    @Test
    public void testWithThirteenthMonth() {
        String wrongMonth = "13";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(wrongMonth);
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    public void testWithZeroMonth() {
        String zeroMonth = "00";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(zeroMonth);
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    public void testWithZeroYear() {
        String zeroYear = "00";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(zeroYear);
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipOfCardExpiration(); // поле год должно иметь подсказку "Истек срок действия карты"
    }

    @Test
    public void testWithoutDate() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();


        paymentCardForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
        paymentCardForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithoutMonth() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithoutYear() {

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithYearPlusSixYears() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getYearPlusSixYears());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongTermOfCardInYearField();
        // поле год должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    public void testWithSymbolsInDateField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(symbols);
        paymentCardForm.fillYear(symbols);
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
        paymentCardForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"

        paymentCardForm.monthShouldBeEmpty(); //поле месяц должно быть пустым
        paymentCardForm.yearShouldBeEmpty(); //поле год должно быть пустым
    }

    @Test
    public void testWithOneDigitInDateField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getRandomDigit());
        paymentCardForm.fillYear(Data.getRandomDigit());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInMonthField(); //поле месяц должно иметь подсказку "Неверный формат"
        paymentCardForm.shouldHaveTooltipWrongFormatInYearField(); //поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithRussianTextInCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getRussianName());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithDigitInCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getRandomDigit());
        paymentCardForm.clickButton();


        paymentCardForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithOneCharInCardholderField() {
        String characterOne = "a";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(characterOne);
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithSymbolsInCardholderField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(symbols);
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithoutCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.clickButton();


        paymentCardForm.cardholderShouldBeFilledIn(); //поле Имя Владельца должно иметь подсказку "Поле обязательно для заполнения"
    }

    @Test
    public void testWithZerosInCVCField() {
        String zeros = "000";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(zeros);
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();


        paymentCardForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithOneDigitInCVCField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getRandomDigit());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithoutCVCField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    public void testWithCharactersInCVCField() {
        String text = "Text";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(text);
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();


        paymentCardForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
        paymentCardForm.cvvShouldBeEmpty(); //поле cvv должно быть пустым
    }

    @Test
    public void testWithSymbolsInCVCField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(symbols);
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
        paymentCardForm.cvvShouldBeEmpty(); //поле cvv должно быть пустым
    }

    @Test
    public void testWithEmptyFields() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField();
        paymentCardForm.shouldHaveTooltipWrongFormatInMonthField();
        paymentCardForm.shouldHaveTooltipWrongFormatInYearField();
        paymentCardForm.shouldHaveTooltipWrongFormatInCVVField();
        paymentCardForm.cardholderShouldBeFilledIn();
    }

    @Test
    public void testWithRandomCreditNumber() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getRandomCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldReceiveErrorNotification();
    }


    @Test
    public void testNotifications() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        PaymentCardForm paymentCardForm = mainPage.selectPaymentByCard();

        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldReceiveOnlySuccessNotification(); // должно быть видно только одно уведомление об успешной оплате
    }


}
