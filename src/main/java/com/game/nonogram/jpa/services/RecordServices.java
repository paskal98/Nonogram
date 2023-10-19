package com.game.nonogram.jpa.services;

import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.models.Record;
import com.game.nonogram.jpa.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional()
public class RecordServices {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordServices(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<Record> findAll() {
        return recordRepository.findAll();
    }

    public Record findOne(int id) {
        Optional<Record> foundRecord = recordRepository.findById(id);
        return foundRecord.orElse(null);
    }

    public Record findRecordByField(Field field) {
        return recordRepository.findRecordByField(field);
    }

    public List<Record> getRecordsByPlayer(Player player) {
        return recordRepository.findAllByPlayerIs(player);
    }

    public Record save(Record record) {
        return recordRepository.save(record);
    }

    public Record update(Record record) {
        return recordRepository.save(record);
    }

    public void deleteById(Integer id) {
        recordRepository.deleteById(id);
    }
}
