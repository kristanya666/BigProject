package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlHelper {
    static String url = System.getProperty("db.url");
    static String login = System.getProperty("db.login");
    static String password = System.getProperty("db.password");


    @SneakyThrows
    public static Connection getConn() {
        return DriverManager.getConnection(url, login, password);
    }

    @SneakyThrows
    public static String getCreditStatusById() {
        var statusSQL = "SELECT status FROM credit_request_entity WHERE bank_id = ?;";
        var bankId = getPaymentId();
        String statusPayment = null;

        try (
                var connection = getConn();
                var status = connection.prepareStatement(statusSQL)) {
            status.setString(1, bankId);
            try (var rs = status.executeQuery()) {
                while (rs.next()) {
                    statusPayment = rs.getString("status");
                }
            }
        }
        return statusPayment;
    }

    @SneakyThrows
    public static void cleanDatabases() {
        var runner = new QueryRunner();
        var connection = getConn();
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
    }

    @SneakyThrows
    public static String getPaymentId() {
        var paymentId = "SELECT payment_id FROM order_entity ORDER BY created DESC limit 1;";
        var runner = new QueryRunner();

        try (
                var connection = getConn()) {
            var id = runner.query(connection, paymentId, new ScalarHandler<String>());
            return id;
        }
    }

    @SneakyThrows
    public static String getPaymentStatusById() {
        var paymentIdSQL = "SELECT status FROM payment_entity WHERE transaction_id = ?;";
        var transactionId = getPaymentId();
        String statusPayment = null;

        try (
                var connection = getConn();
                var status = connection.prepareStatement(paymentIdSQL)) {
            status.setString(1, transactionId);
            try (var rs = status.executeQuery()) {
                while (rs.next()) {
                    statusPayment = rs.getString("status");
                }
            }

        }
        return statusPayment;
    }

    @SneakyThrows
    public static String getPaymentAmountById() {
        var paymentIdSQL = "SELECT amount FROM payment_entity WHERE transaction_id = ?;";
        var transactionId = getPaymentId();
        String amountPayment = null;

        try (
                var connection = getConn();
                var amount = connection.prepareStatement(paymentIdSQL)) {
            amount.setString(1, transactionId);
            try (var rs = amount.executeQuery()) {
                while (rs.next()) {
                    amountPayment = rs.getString("amount");
                }
            }

        }
        return amountPayment;
    }
}
