package data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Data {
    private Data() {
    }

    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerRu = new Faker(new Locale("ru"));


    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }


    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getValidMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentDate(String pattern) {
        // в поле pattern вводится MM или yy
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getValidYear() {
        // здесь взят рандомный год  от следующего до +4 года
        String currentYear = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")); //следующий год
        String futureExpirationYear = LocalDate.now().plusYears(4).format(DateTimeFormatter.ofPattern("yy"));

        return String.valueOf(faker.number().numberBetween(Integer.parseInt(currentYear), Integer.parseInt(futureExpirationYear)));
    }

    public static String getPastYear() {
        String previousYear = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        return String.valueOf(faker.number().numberBetween(10, Integer.parseInt(previousYear))); // от 10 года до предыдущего года
    }

    public static String getYearPlusSixYears() {
        return LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
    }
    public static String getPreviousDate(String pattern) {
        String previousDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern(pattern));
        return previousDate;
    }


    public static String getValidCVV() {
        return faker.numerify("###");
    }

    public static String getValidCardholder() {
        return faker.name().name();
    }


    public static String getRandomCardNumber() {
        return "3570 3530 8951 8655";
    }

    public static String getRandomDigit() {
        return String.valueOf(faker.number().numberBetween(1, 9));
    }

    public static String getRussianName() {
        return fakerRu.name().name();
    }

}
