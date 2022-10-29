package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByResetPasswordToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE Role set name = ?2 WHERE id = ?1")
    void updateUserRole(long id, String name);
}
