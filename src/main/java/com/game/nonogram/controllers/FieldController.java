package com.game.nonogram.controllers;


import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.services.FieldServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field")
public class FieldController {

    private final FieldServices fieldServices;

    @Autowired
    public FieldController(FieldServices fieldServices) {
        this.fieldServices = fieldServices;
    }

    @GetMapping("/fields")
    public List<Field> getAll() {
        List<Field> fields = fieldServices.findAll();
        return fields;
    }

    @PostMapping()
    public Field save(@RequestBody Field field) {
        return fieldServices.save(field);
    }

    @GetMapping("/{id}")
    public Field getById(@PathVariable("id") int id) {
        return fieldServices.findOne(id);
    }

    @PutMapping("/{id}")
    public Field update(@RequestBody Field field, @PathVariable("id") int id) {

        Field updateField = new Field();
        updateField.setFieldId(id);
        updateField.setColumnsCluesJSON(field.getColumnsCluesJSON());
        updateField.setGridRecordedJSON(field.getGridRecordedJSON());
        updateField.setSolvingTime(field.getSolvingTime());
        updateField.setRowsCluesJSON(field.getRowsCluesJSON());
        updateField.setGridSolvedJSON(field.getGridSolvedJSON());
        updateField.setSize(field.getSize());

        return fieldServices.save(updateField);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        fieldServices.deleteById(id);
    }

}
