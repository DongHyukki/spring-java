package com.donghyukki.springjava.support.database.transaction;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public enum TransactionDefinitionType {

    REQUIRED(TransactionBuilder.builder()
            .propagation(TransactionDefinition.PROPAGATION_REQUIRED)
            .build()),
    REQUIRED_READ_ONLY(TransactionBuilder.builder()
            .propagation(TransactionDefinition.PROPAGATION_REQUIRED)
            .readOnly(true)
            .build()),
    PROPAGATION_REQUIRES_NEW(TransactionBuilder.builder()
            .propagation(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
            .build()),
    PROPAGATION_REQUIRES_NEW_READ_ONLY(TransactionBuilder.builder()
            .propagation(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
            .readOnly(true)
            .build());

    private final TransactionDefinition transactionDefinition;

    TransactionDefinitionType(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

    public TransactionDefinition getTransactionDefinition() {
        return transactionDefinition;
    }

    private static class TransactionBuilder extends DefaultTransactionDefinition {
        private TransactionBuilder() {}

        public static TransactionBuilder builder() {
            return new TransactionBuilder();
        }

        public TransactionBuilder propagation(int propagationBehavior) {
            setPropagationBehavior(propagationBehavior);
            return this;
        }

        public TransactionBuilder readOnly(boolean readOnly) {
            setReadOnly(readOnly);
            return this;
        }

        public TransactionBuilder timeout(int timeout) {
            setTimeout(timeout);
            return this;
        }

        public TransactionBuilder isolationLevel(int isolationLevel) {
            setIsolationLevel(isolationLevel);
            return this;
        }

        public TransactionDefinition build() {
            return this;
        }
    }
}
