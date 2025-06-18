package com.donghyukki.springjava.support.database.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
public class TransactionalOperationInvoker {
    private final PlatformTransactionManager transactionManager;

    public TransactionalOperationInvoker(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public <T> T invokeInTransaction(Supplier<T> supplier) {
        TransactionTemplate template = new TransactionTemplate(
                transactionManager,
                TransactionDefinitionType.REQUIRED.getTransactionDefinition());

        return template.execute(status -> supplier.get());
    }

    public <T> T invokeInTransaction(
            Supplier<T> supplier,
            TransactionDefinitionType transactionDefinitionType) {
        TransactionTemplate template = new TransactionTemplate(
                transactionManager,
                transactionDefinitionType.getTransactionDefinition());

        return template.execute(status -> supplier.get());
    }
}
