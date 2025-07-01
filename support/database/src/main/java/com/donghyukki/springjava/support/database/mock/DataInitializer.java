package com.donghyukki.springjava.support.database.mock;

import com.donghyukki.springjava.support.database.entity.Company;
import com.donghyukki.springjava.support.database.entity.CompanyGroup;
import com.donghyukki.springjava.support.database.repository.CompanyGroupRepository;
import com.donghyukki.springjava.support.database.repository.CompanyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    private final CompanyRepository companyRepository;
    private final CompanyGroupRepository companyGroupRepository;

    public DataInitializer(CompanyRepository companyRepository, CompanyGroupRepository companyGroupRepository) {
        this.companyRepository = companyRepository;
        this.companyGroupRepository = companyGroupRepository;
    }

    @PostConstruct
    public void init() {
        companyRepository.save(new Company("TEST-COMPANY-1"));
        companyRepository.save(new Company("TEST-COMPANY-2"));
        companyGroupRepository.save(new CompanyGroup("TEST-COMPANY-GROUP-1"));
        companyGroupRepository.save(new CompanyGroup("TEST-COMPANY-GROUP-2"));
    }
}
