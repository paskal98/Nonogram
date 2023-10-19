package com.game.nonogram.jpa.repositories;

import com.game.nonogram.jpa.models.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface FieldRepository extends JpaRepository<Field, Integer> {


}
