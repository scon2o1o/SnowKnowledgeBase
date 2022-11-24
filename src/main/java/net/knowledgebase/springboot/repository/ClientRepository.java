package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    @Query(
            value = "SELECT * FROM Client WHERE email = ? LIMIT 1",
            nativeQuery = true)
    Client findByEmail(String email);
}
