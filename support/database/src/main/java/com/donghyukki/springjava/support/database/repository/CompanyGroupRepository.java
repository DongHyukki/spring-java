package com.donghyukki.springjava.support.database.repository;

import com.donghyukki.springjava.support.database.entity.CompanyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyGroupRepository extends JpaRepository<CompanyGroup, Long> {
}
