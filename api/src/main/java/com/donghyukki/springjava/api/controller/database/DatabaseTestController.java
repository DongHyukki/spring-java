package com.donghyukki.springjava.api.controller.database;

import com.donghyukki.springjava.support.database.entity.Company;
import com.donghyukki.springjava.support.database.entity.CompanyGroup;
import com.donghyukki.springjava.support.database.repository.CompanyGroupRepository;
import com.donghyukki.springjava.support.database.repository.CompanyRepository;
import com.donghyukki.springjava.support.database.transaction.TransactionalOperationInvoker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseTestController {

    private final CompanyRepository companyRepository;
    private final CompanyGroupRepository companyGroupRepository;
    private final TransactionalOperationInvoker transactionalOperationInvoker;

    public DatabaseTestController(
            CompanyRepository companyRepository,
            CompanyGroupRepository companyGroupRepository,
            TransactionalOperationInvoker transactionalOperationInvoker) {
        this.companyRepository = companyRepository;
        this.companyGroupRepository = companyGroupRepository;
        this.transactionalOperationInvoker = transactionalOperationInvoker;
    }

    @GetMapping("/api/database/test-dirty-checking")
    public ResponseEntity<Company> testDirtyChecking() {
        transactionalOperationInvoker.invokeInTransaction(() -> {
            companyRepository.save(new Company("A"));
            return null;
        });

        var changedId = transactionalOperationInvoker.invokeInTransaction(() -> {
            var company = companyRepository.findAll().stream().findFirst().get();
            company.changeName("NEW");
            return company.getId();
        });

        var changed = companyRepository.findById(changedId).get();

        Assert.isTrue(changed.getName().equals("NEW"), "Company should have changed name");

        return new ResponseEntity<>(changed, HttpStatus.OK);
    }

    @GetMapping("/api/database/test-roll-back")
    public ResponseEntity<Company> testRollback() {

        System.out.println("isPresent ::: " + companyRepository.findByName("C").isEmpty());

        transactionalOperationInvoker.invokeInTransaction(() -> {
            companyRepository.save(new Company("C"));
            companyGroupRepository.save(new CompanyGroup("D"));
            throw new IllegalStateException("throw");
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
