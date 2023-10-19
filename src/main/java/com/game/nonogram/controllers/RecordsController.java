package com.game.nonogram.controllers;


import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.models.Record;
import com.game.nonogram.jpa.services.RecordServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordsController {

    private final RecordServices recordServices;

    public RecordsController(RecordServices recordServices) {
        this.recordServices = recordServices;
    }

    @GetMapping("/records")
    public List<Record> getAll(){
        return recordServices.findAll();
    }

    @GetMapping("/{id}")
    public Record getById(@PathVariable("id") int id){
        return recordServices.findOne(id);
    }

    @GetMapping("/findRecordsByNickName")
    public List<Record> getByNickname(@RequestParam(value = "nickname") String nickname, RestTemplate restTemplate) {

        ResponseEntity<Player> responsePlayer = restTemplate.getForEntity("http://127.0.0.1:8080/api/player/findByNickName/"+nickname, Player.class);

        return recordServices.getRecordsByPlayer(responsePlayer.getBody());

    }

    @GetMapping("/addRating")
    public void  getByNickname(@RequestParam(value = "fieldId") int fieldId,@RequestParam(value = "rating") int rating, RestTemplate restTemplate) {

        ResponseEntity<Field> responseFiled = restTemplate.getForEntity("http://127.0.0.1:8080/api/field/"+fieldId, Field.class);

        System.out.println(responseFiled.getBody().getSize());

        Record updatedRecord = recordServices.findRecordByField(responseFiled.getBody());
        updatedRecord.setRating(rating);

        recordServices.update(updatedRecord);

    }



    @PostMapping()
    public Record save(@RequestBody Record record) {
       return recordServices.save(record);
    }


    @PutMapping("/{id}")
    public Record update(@RequestBody Record record,@PathVariable("id") int id){

        Record updatedRecord = new Record();
        updatedRecord.setField(record.getField());
        updatedRecord.setPlayer(record.getPlayer());
        updatedRecord.setStatus(record.getStatus());
        updatedRecord.setRecordId(id);

        return recordServices.update(record);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id){
        RestTemplate restTemplate = new RestTemplate();

        Record onDelete = restTemplate.getForObject("http://localhost:8080/api/record/"+id,Record.class);

        if (onDelete==null) return;

        int fieldId = onDelete.getField().getFieldId();
        int playerId = onDelete.getPlayer().getPlayerId();

        recordServices.deleteById(id);

        restTemplate.delete("http://localhost:8080/api/field/"+fieldId);
        restTemplate.delete("http://localhost:8080/api/player/"+playerId);
    }

}
