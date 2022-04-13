package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {
}
