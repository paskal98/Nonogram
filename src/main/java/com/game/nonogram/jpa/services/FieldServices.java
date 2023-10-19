package com.game.nonogram.jpa.services;

import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional()
public class FieldServices {

    private final FieldRepository fieldRepository;

    @Autowired
    public FieldServices(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public List<Field> findAll(){
        return fieldRepository.findAll();
    }

    public Field findOne(int id){
        Optional<Field> foundField  = fieldRepository.findById(id);
        return foundField.orElse(null);
    }

    public Field save(Field field){
        fieldRepository.save(field);
        return field;
    }

    public void deleteById(Integer id){
        fieldRepository.deleteById(id);
    }

}
