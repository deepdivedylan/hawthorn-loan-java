package io.deepdivedylan.hawthornloan;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
    private String transactionId;
    private double transactionAmount;
    private String transactionComment;
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private static final String HMAC_KEY = ApplicationProperties.getHMACKey();

    public Transaction(double newTransactionAmount, String newTransactionComment, String newTransactionDate, String newTransactionType) throws NoSuchAlgorithmException, InvalidKeyException {
        setTransactionAmount(newTransactionAmount);
        setTransactionComment(newTransactionComment);
        setTransactionDate(newTransactionDate);
        setTransactionType(newTransactionType);
        generateHMAC();
    }

    public Transaction(String newTransactionId, double newTransactionAmount, String newTransactionComment, LocalDate newTransactionDate, TransactionType newTransactionType) {
        setTransactionId(newTransactionId);
        setTransactionAmount(newTransactionAmount);
        setTransactionComment(newTransactionComment);
        setTransactionDate(newTransactionDate);
        setTransactionType(newTransactionType);
    }

    public String getTransactionId() {
        return transactionId;
    }

    private void setTransactionId(String newTransactionId) {
        transactionId = newTransactionId;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    private void setTransactionAmount(double newTransactionAmount) {
        transactionAmount = newTransactionAmount;
    }

    public String getTransactionComment() {
        return transactionComment;
    }

    private void setTransactionComment(String newTransactionComment) {
        transactionComment = newTransactionComment;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    private void setTransactionDate(LocalDate newTransactionDate) {
        transactionDate = newTransactionDate;
    }

    private void setTransactionDate(String newTransactionDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/d/yy");
        transactionDate = LocalDate.parse(newTransactionDate, dateTimeFormatter);
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    private void setTransactionType(String newTransactionType) {
        if (newTransactionType.equals("CR")) {
            transactionType = TransactionType.CREDIT;
        } else {
            transactionType = TransactionType.DEBIT;
        }
    }

    private void setTransactionType(TransactionType newTransactionType) {
        transactionType = newTransactionType;
    }

    private void generateHMAC() throws InvalidKeyException, NoSuchAlgorithmException {
        final String HMAC_SHA512 = "HmacSHA512";
        String data = transactionAmount + transactionComment + transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + transactionType;
        Mac hmacSha512 = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec secretKeySpec = new SecretKeySpec(HMAC_KEY.getBytes(StandardCharsets.UTF_8), HMAC_SHA512);
        hmacSha512.init(secretKeySpec);
        byte[] hmacBytes = hmacSha512.doFinal(data.getBytes(StandardCharsets.UTF_8));

        Formatter formatter = new Formatter();
        for (byte hmacByte : hmacBytes) {
            formatter.format("%02x", hmacByte);
        }
        transactionId = formatter.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.transactionAmount, transactionAmount) == 0 &&
                transactionId.equals(that.transactionId) &&
                transactionComment.equals(that.transactionComment) &&
                transactionDate.equals(that.transactionDate) &&
                transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, transactionAmount, transactionComment, transactionDate, transactionType);
    }

    @Override
    public int compareTo(Transaction that) {
        if (!this.transactionDate.equals(that.transactionDate)) {
            return this.transactionDate.compareTo(that.transactionDate);
        }
        return Double.compare(this.transactionAmount, that.transactionAmount);
    }
}
