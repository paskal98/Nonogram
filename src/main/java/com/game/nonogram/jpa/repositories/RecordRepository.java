package com.game.nonogram.jpa.repositories;

import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.models.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface RecordRepository extends JpaRepository<Record, Integer> {

    List<Record> findAllByPlayerIs(Player player);

    Record findRecordByField(Field field);

}
