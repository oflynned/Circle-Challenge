package com.interview.MoneyTransferUseCase.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoneyTransaction {
    private String transactionId;
    private String fromId;
    private String toId;
    private int amount;
    private long timestamp;

    public MoneyTransaction(@JsonProperty("from_id") String fromId,
                            @JsonProperty("to_id") String toId,
                            @JsonProperty("amount") int amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.timestamp = System.currentTimeMillis();
        this.amount = amount;
    }

    public MoneyTransaction(String transactionId, String fromId, String toId, long timestamp, int amount) {
        this.transactionId = transactionId;
        this.fromId = fromId;
        this.toId = toId;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "(" + transactionId + ") " + fromId + " -> " + toId + ": " + amount;
    }
}
