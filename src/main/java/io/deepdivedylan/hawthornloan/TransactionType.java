package io.deepdivedylan.hawthornloan;

public enum TransactionType {
    CREDIT(1), DEBIT(-1);

    private int multiplier;

    private TransactionType(int newMultiplier) {
        multiplier = newMultiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
