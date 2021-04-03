package com.account.accountservice.repository;

import com.account.accountservice.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"alerts"})
  User findByUsername(String userName);
  User findByEmail(String email);
  User findByAlertsId(long id);
//  void deleteById(long id);
}
