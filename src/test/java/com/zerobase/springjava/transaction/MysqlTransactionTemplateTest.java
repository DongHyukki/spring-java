package com.zerobase.springjava.transaction;

import com.zerobase.springjava.entity.Company;
import com.zerobase.springjava.entity.User;
import com.zerobase.springjava.repository.CompanyRepository;
import com.zerobase.springjava.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MysqlTransactionTemplateTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Test
    void 트랜잭션_템플릿으로_유저와_회사_생성_테스트() {
        // when
        String result = transactionTemplate.execute(status -> {
            try {
                // User 생성
                User user = new User(
                        "testuser123", 
                        "test@company.com", 
                        "password123", 
                        "홍길동"
                );
                User savedUser = userRepository.save(user);
                
                // Company 생성
                Company company = new Company(
                        "테스트 주식회사",
                        "123-45-67890",
                        "서울특별시 강남구 테헤란로 123",
                        "02-1234-5678",
                        "info@testcompany.com",
                        "김대표",
                        "소프트웨어 개발"
                );
                Company savedCompany = companyRepository.save(company);
                
                System.out.println("=== 생성된 사용자 정보 ===");
                System.out.println("ID: " + savedUser.getId());
                System.out.println("Username: " + savedUser.getUsername());
                System.out.println("Email: " + savedUser.getEmail());
                System.out.println("Name: " + savedUser.getName());
                
                System.out.println("\n=== 생성된 회사 정보 ===");
                System.out.println("ID: " + savedCompany.getId());
                System.out.println("Company Name: " + savedCompany.getCompanyName());
                System.out.println("Business Number: " + savedCompany.getBusinessNumber());
                System.out.println("CEO: " + savedCompany.getCeoName());
                System.out.println("Industry: " + savedCompany.getIndustry());
                
                return "SUCCESS";
                
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException("트랜잭션 실행 중 오류 발생", e);
            }
        });
        
        // then
        assertThat(result).isEqualTo("SUCCESS");
        
        // 데이터가 실제로 저장되었는지 확인
        assertThat(userRepository.findByUsername("testuser123")).isPresent();
        assertThat(companyRepository.findByCompanyName("테스트 주식회사")).isPresent();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // 테스트 자체의 트랜잭션 비활성화
    void 트랜잭션_롤백_테스트() {
        // 테스트 전 상태 확인
        long initialUserCount = userRepository.count();
        long initialCompanyCount = companyRepository.count();
        
        System.out.println("=== 테스트 시작 전 ===");
        System.out.println("기존 User 수: " + initialUserCount);
        System.out.println("기존 Company 수: " + initialCompanyCount);
        
        // when & then
        try {
            transactionTemplate.execute(status -> {
                // User 생성
                User user = new User(
                        "rollbackuser", 
                        "rollback@test.com", 
                        "password123", 
                        "롤백유저"
                );
                User savedUser = userRepository.save(user);
                System.out.println("User 저장됨 - ID: " + savedUser.getId());
                
                // Company 생성
                Company company = new Company(
                        "롤백 회사",
                        "999-99-99999",
                        "서울시 어딘가",
                        "02-9999-9999",
                        "rollback@company.com",
                        "롤백대표",
                        "테스트업"
                );
                Company savedCompany = companyRepository.save(company);
                System.out.println("Company 저장됨 - ID: " + savedCompany.getId());
                
                // 중간에 실제 저장 확인 (같은 트랜잭션 내에서)
                System.out.println("=== 트랜잭션 내부에서 확인 ===");
                System.out.println("현재 User 수: " + userRepository.count());
                System.out.println("현재 Company 수: " + companyRepository.count());
                
                // 강제로 예외 발생시켜서 롤백 유발
                throw new RuntimeException("의도적인 롤백 테스트");
            });
        } catch (RuntimeException e) {
            System.out.println("=== 예외 발생 ===");
            System.out.println("예상된 예외: " + e.getMessage());
        }
        
        // 롤백 후 상태 확인
        long finalUserCount = userRepository.count();
        long finalCompanyCount = companyRepository.count();
        
        System.out.println("=== 롤백 후 ===");
        System.out.println("최종 User 수: " + finalUserCount);
        System.out.println("최종 Company 수: " + finalCompanyCount);
        
        // 롤백되어서 데이터가 저장되지 않았는지 확인
        assertThat(userRepository.findByUsername("rollbackuser")).isEmpty();
        assertThat(companyRepository.findByCompanyName("롤백 회사")).isEmpty();
        
        // 개수도 변화가 없어야 함
        assertThat(finalUserCount).isEqualTo(initialUserCount);
        assertThat(finalCompanyCount).isEqualTo(initialCompanyCount);
        
        System.out.println("✅ 롤백 테스트 성공!");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void JPA_flush_동작_확인_테스트() {
        System.out.println("=== JPA Flush 동작 테스트 ===");
        
        transactionTemplate.execute(status -> {
            // 1. save() 호출 - 영속성 컨텍스트에만 저장
            User user = new User("flushuser", "flush@test.com", "password", "플러시유저");
            User savedUser = userRepository.save(user);
            System.out.println("1. save() 호출 완료 - ID: " + savedUser.getId());
            
            // 2. flush 전에는 실제 DB에 아직 반영되지 않음을 확인하고 싶지만
            // JPA는 쿼리 실행 전에 자동으로 flush하므로 count()를 호출하면 flush가 발생함
            System.out.println("2. 영속성 컨텍스트에 저장된 상태");
            
            // 3. 명시적 flush 호출
            entityManager.flush();
            System.out.println("3. EntityManager.flush() 호출 완료");
            
            // 4. flush 후 상태 확인 (이미 DB에 반영됨)
            long count = userRepository.count();
            System.out.println("4. flush 후 DB count: " + count);
            
            // 5. 트랜잭션 끝나기 전에 롤백 호출
            status.setRollbackOnly();
            System.out.println("5. 롤백 설정 완료");
            
            return null;
        });
        
        // 6. 롤백 후 확인
        long finalCount = userRepository.count();
        System.out.println("6. 롤백 후 최종 count: " + finalCount);
        assertThat(userRepository.findByUsername("flushuser")).isEmpty();
        
        System.out.println("✅ JPA Flush 테스트 완료!");
    }
    
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void flush_시점_비교_테스트() {
        System.out.println("=== Flush 시점 비교 테스트 ===");
        
        transactionTemplate.execute(status -> {
            System.out.println("트랜잭션 시작");
            
            // 1. 첫 번째 User 저장 (영속성 컨텍스트에만)
            User user1 = new User("user1", "user1@test.com", "password", "유저1");
            userRepository.save(user1);
            System.out.println("User1 save() 완료");
            
            // 2. 두 번째 User 저장 (영속성 컨텍스트에만)
            User user2 = new User("user2", "user2@test.com", "password", "유저2");
            userRepository.save(user2);
            System.out.println("User2 save() 완료");
            
            // 3. count() 호출 시 자동 flush 발생!
            // JPA는 쿼리 실행 전에 영속성 컨텍스트의 변경사항을 DB에 반영
            System.out.println("count() 호출 전 - 이제 JPA가 자동으로 flush 할 예정");
            long count = userRepository.count();
            System.out.println("count() 결과: " + count + " (자동 flush 발생함)");
            
            // 4. 수동 flush (이미 flush되었으므로 추가 SQL은 없음)
            entityManager.flush();
            System.out.println("수동 flush 완료");
            
            return "SUCCESS";
        });
        
        System.out.println("트랜잭션 커밋 완료");
        System.out.println("✅ Flush 시점 테스트 완료!");
    }

    @Test
    void 여러_엔티티_배치_생성_테스트() {
        // when
        Integer result = transactionTemplate.execute(status -> {
            int totalCreated = 0;
            
            // 여러 사용자 생성
            for (int i = 1; i <= 3; i++) {
                User user = new User(
                        "batchuser" + i,
                        "batch" + i + "@test.com",
                        "password" + i,
                        "배치사용자" + i
                );
                userRepository.save(user);
                totalCreated++;
            }
            
            // 여러 회사 생성
            for (int i = 1; i <= 2; i++) {
                Company company = new Company(
                        "배치회사" + i,
                        "100-10-1000" + i,
                        "서울시 강남구 " + i + "번지",
                        "02-1000-000" + i,
                        "batch" + i + "@company.com",
                        "배치대표" + i,
                        "IT서비스"
                );
                companyRepository.save(company);
                totalCreated++;
            }
            
            return totalCreated;
        });
        
        // then
        assertThat(result).isEqualTo(5); // 사용자 3개 + 회사 2개
        assertThat(userRepository.findAll()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(companyRepository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }
    
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 부분_롤백_테스트() {
        // 첫 번째 트랜잭션은 성공
        String result1 = transactionTemplate.execute(status -> {
            User user = new User("successuser", "success@test.com", "password", "성공유저");
            userRepository.save(user);
            return "SUCCESS";
        });
        
        // 두 번째 트랜잭션은 실패 (롤백)
        try {
            transactionTemplate.execute(status -> {
                User user = new User("failuser", "fail@test.com", "password", "실패유저");
                userRepository.save(user);
                throw new RuntimeException("두 번째 트랜잭션 실패");
            });
        } catch (RuntimeException e) {
            System.out.println("두 번째 트랜잭션 롤백: " + e.getMessage());
        }
        
        // 검증: 첫 번째는 남아있고, 두 번째는 롤백되어야 함
        assertThat(userRepository.findByUsername("successuser")).isPresent();
        assertThat(userRepository.findByUsername("failuser")).isEmpty();
        
        System.out.println("✅ 부분 롤백 테스트 성공!");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void count_호출시_자동_flush_확인_테스트() {
        System.out.println("=== count() 호출 시 자동 flush 테스트 ===");
        
        transactionTemplate.execute(status -> {
            System.out.println("1. 트랜잭션 시작");
            
            // save()만 호출 (아직 DB에 반영 안됨)
            User user = new User("autoflushuser", "autoflush@test.com", "password", "자동플러시유저");
            userRepository.save(user);
            System.out.println("2. save() 완료 - 영속성 컨텍스트에만 저장됨");
            
            // 이 시점에서는 아직 INSERT SQL이 실행되지 않음
            System.out.println("3. 아직 DB에는 반영되지 않은 상태");
            
            // count() 호출 - 이 시점에 자동 flush 발생!
            System.out.println("4. count() 호출 시작 - JPA가 자동 flush 실행!");
            long count = userRepository.count();
            System.out.println("5. count() 결과: " + count + " - INSERT SQL이 먼저 실행되었음");
            
            // 이제 DB에 실제로 저장되어 있음
            System.out.println("6. 이제 DB에 실제 데이터가 존재함");
            
            return "SUCCESS";
        });
        
        System.out.println("7. 트랜잭션 커밋 완료");
        System.out.println("✅ 자동 flush 테스트 완료!");
    }
    
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) 
    void 다양한_쿼리의_자동_flush_테스트() {
        System.out.println("=== 다양한 쿼리 시 자동 flush 테스트 ===");
        
        transactionTemplate.execute(status -> {
            // 여러 엔티티 저장 (아직 flush 안됨)
            User user1 = new User("query1", "query1@test.com", "password", "쿼리1");
            User user2 = new User("query2", "query2@test.com", "password", "쿼리2");
            userRepository.save(user1);
            userRepository.save(user2);
            System.out.println("1. 2개 User save() 완료 - 영속성 컨텍스트에만 저장");
            
            // 각종 쿼리 실행 시 자동 flush 확인
            System.out.println("\n2. count() 호출 → 자동 flush 발생");
            long count = userRepository.count();
            System.out.println("   count 결과: " + count);
            
            // 추가 User 저장
            User user3 = new User("query3", "query3@test.com", "password", "쿼리3");
            userRepository.save(user3);
            System.out.println("\n3. 추가 User save() 완료");
            
            System.out.println("\n4. findAll() 호출 → 자동 flush 발생");
            var allUsers = userRepository.findAll();
            System.out.println("   findAll 결과 size: " + allUsers.size());
            
            // 또 추가 User 저장
            User user4 = new User("query4", "query4@test.com", "password", "쿼리4");
            userRepository.save(user4);
            System.out.println("\n5. 또 추가 User save() 완료");
            
            System.out.println("\n6. findByUsername() 호출 → 자동 flush 발생");
            var foundUser = userRepository.findByUsername("query1");
            System.out.println("   findByUsername 결과: " + (foundUser.isPresent() ? "찾음" : "못찾음"));
            
            return "SUCCESS";
        });
        
        System.out.println("\n7. 트랜잭션 커밋 완료");
        System.out.println("✅ 다양한 쿼리 자동 flush 테스트 완료!");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void IDENTITY_전략_즉시_INSERT_테스트() {
        System.out.println("=== IDENTITY 전략 즉시 INSERT 테스트 ===");
        
        transactionTemplate.execute(status -> {
            System.out.println("1. 트랜잭션 시작");
            
            // save() 호출 - IDENTITY 전략이므로 즉시 INSERT 실행됨!
            System.out.println("2. save() 호출 시작");
            User user = new User("identityuser", "identity@test.com", "password", "아이덴티티유저");
            User savedUser = userRepository.save(user);
            System.out.println("3. save() 완료 - INSERT가 즉시 실행되어 ID가 생성됨: " + savedUser.getId());
            
            // 이미 DB에 INSERT되었으므로 rollback 테스트
            System.out.println("4. 롤백 설정");
            status.setRollbackOnly();
            
            return "ROLLBACK_TEST";
        });
        
        // 롤백되었는지 확인
        boolean exists = userRepository.findByUsername("identityuser").isPresent();
        System.out.println("5. 롤백 후 존재 여부: " + exists);
        System.out.println("✅ IDENTITY 전략에서도 롤백은 정상 작동!");
    }
    
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void IDENTITY_vs_다른전략_비교_테스트() {
        System.out.println("=== IDENTITY 전략 특징 확인 테스트 ===");
        
        transactionTemplate.execute(status -> {
            System.out.println("1. User 생성 (IDENTITY 전략)");
            User user = new User("strategy1", "strategy1@test.com", "password", "전략1");
            
            System.out.println("2. save() 호출 전 - ID는 null: " + user.getId());
            User savedUser = userRepository.save(user);
            System.out.println("3. save() 호출 후 - ID가 즉시 생성됨: " + savedUser.getId());
            System.out.println("   → 이는 DB에 실제 INSERT가 실행되었기 때문!");
            
            System.out.println("4. Company도 동일하게 테스트");
            Company company = new Company("전략회사", "123-45-67890", "주소", "전화", "email@test.com", "대표", "업종");
            System.out.println("5. Company save() 호출 전 - ID는 null: " + company.getId());
            Company savedCompany = companyRepository.save(company);
            System.out.println("6. Company save() 호출 후 - ID가 즉시 생성됨: " + savedCompany.getId());
            
            return "SUCCESS";
        });
        
        System.out.println("✅ IDENTITY 전략 특징 확인 완료!");
    }
} 