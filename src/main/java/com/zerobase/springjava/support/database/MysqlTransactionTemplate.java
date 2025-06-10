package com.zerobase.springjava.support.database;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
public class MysqlTransactionTemplate {
    private final PlatformTransactionManager transactionManager;

    public MysqlTransactionTemplate(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public <T> T invokeInTransaction(Supplier<T> supplier) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        return template.execute(status -> supplier.get());
    }
}
