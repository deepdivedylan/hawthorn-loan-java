package io.deepdivedylan.hawthornloan;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRegister {
    List<Transaction> transactions;

    public TransactionRegister() {
        transactions = new LinkedList<Transaction>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void importFromCSV(String filename) {
        /**
         * row map from Hawthorn Bank:
         *
         * row[0]: account name/number (always same value)
         * row[1]: serial number (always 0000000000)
         * row[2]: transaction comment
         * row[3]: transaction date in m/d/y format
         * row[4]: transaction amount
         * row[5]: transaction multiplier (CR or DR)
         **/
        try {
            Reader input = new FileReader(filename);
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
            for (CSVRecord csvRecord : csvRecords) {
                String transactionComment = csvRecord.get(2);
                String transactionDate = csvRecord.get(3);
                double transactionAmount = Double.parseDouble(csvRecord.get(4));
                String transactionType = csvRecord.get(5);
                transactions.add(new Transaction(transactionAmount, transactionComment, transactionDate, transactionType));
            }
            Collections.sort(transactions);
        } catch (Exception exception) {
            System.err.println("Exception: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public double sumAllTransactions() {
        return sumTransactions(transactions);
    }

    public double sumDylansTransactions() {
        List<Transaction> dylansTransactions = transactions.stream().filter(TransactionRegister::transactionBelongsToDylan).collect(Collectors.toList());
        return sumTransactions(dylansTransactions);
    }

    public double sumLanesTransactions() {
        List<Transaction> lanesTransactions = transactions.stream().filter(TransactionRegister::transactionBelongsToLane).collect(Collectors.toList());
        return sumTransactions(lanesTransactions);
    }

    private double sumTransactions(List<Transaction> transactionList) {
        double sum = 0.0;
        for (Transaction transaction : transactionList) {
            sum = sum + (transaction.getTransactionAmount() * transaction.getTransactionType().getMultiplier());
        }
        return sum;
    }

    public static boolean transactionBelongsToDylan(Transaction transaction) {
        return transaction.getTransactionComment().contains("Principal Payment") || transaction.getTransactionComment().contains("Automatic Princ Transfer");
    }

    public static boolean transactionBelongsToLane(Transaction transaction) {
        return transaction.getTransactionComment().equals("Capitalized Finance Charge Payment");
    }
}
