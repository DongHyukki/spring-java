package com.donghyukki.springjava.domain.service.cache;

import com.donghyukki.springjava.domain.support.cache.DomainCacheService;
import com.donghyukki.springjava.domain.support.cache.DomainCacheSpec;
import com.donghyukki.springjava.support.common.cache.InMemoryCacheable;
import com.donghyukki.springjava.support.common.cache.RedisCacheable;
import com.donghyukki.springjava.support.common.jwt.JsonWebTokenManager;
import com.donghyukki.springjava.support.database.entity.Company;
import com.donghyukki.springjava.support.database.repository.CompanyRepository;
import com.donghyukki.springjava.support.security.model.AuthUser;
import com.donghyukki.springjava.support.security.repository.AuthUserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.function.Supplier;

@Service
public class CacheCheckService {
    private final AuthUserRepository authUserRepository;
    private final CompanyRepository companyRepository;
    private final RestTemplate restTemplate;
    private final JsonWebTokenManager jsonWebTokenManager;
    private final DomainCacheService domainCacheService;

    public CacheCheckService(AuthUserRepository authUserRepository,
                             CompanyRepository companyRepository,
                             RestTemplate restTemplate,
                             JsonWebTokenManager jsonWebTokenManager,
                             DomainCacheService domainCacheService
    ) {
        this.authUserRepository = authUserRepository;
        this.companyRepository = companyRepository;
        this.restTemplate = restTemplate;
        this.jsonWebTokenManager = jsonWebTokenManager;
        this.domainCacheService = domainCacheService;
    }

    @InMemoryCacheable(cacheNames = {"USER_INFO"}, key = "#userId")
    public AuthUser getUser(String userId) {
        System.out.println("GET_USER_CALLED");
        return authUserRepository.findByUserId(userId);
    }

    @RedisCacheable(cacheNames = {"API_CALL"}, key = "#userId")
    public String callApi(String userId) {
        System.out.println("API_CALLED");

        var token = jsonWebTokenManager.createToken(userId, Instant.now().plusMillis(1000 * 60 * 60)).value();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange("http://localhost:8080/api/async/task", HttpMethod.GET, entity, String.class).getBody();
    }

    public Company getCompany(String companyName) {
        Supplier<Company> supplier = () -> companyRepository.findByName(companyName).orElseThrow();
        return domainCacheService.cache(DomainCacheSpec.GET_COMPANY, companyName, supplier);
    }
}
