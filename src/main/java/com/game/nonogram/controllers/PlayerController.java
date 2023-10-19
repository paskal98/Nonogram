package com.game.nonogram.controllers;


import com.game.nonogram.jpa.services.PlayerServices;
import com.game.nonogram.jpa.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private  final PlayerServices playerServices;

    @Autowired
    public PlayerController(PlayerServices playerServices) {
        this.playerServices = playerServices;
    }

    @GetMapping("/players")
    public List<Player> getAll(){
        return playerServices.findAll();
    }

    @GetMapping("/{id}")
    public Player getById(@PathVariable("id") int id) {
        return playerServices.findOne(id);
    }

    @GetMapping("/findByNickName/{nickname}")
    public Player getByNickname(@PathVariable("nickname") String nickname) {
        return playerServices.findPlayerByNickname(nickname);
    }

    @GetMapping("/isExists/{nickname}")
    public boolean playerWithNickNameExists(@PathVariable("nickname") String nickname) {

        return playerServices.existsPlayerByNickname(nickname);
    }

    @PostMapping()
    public Player save(@RequestBody Player player){
        return playerServices.save(player);
    }

    @PutMapping("/{id}")
    public Player update(@RequestBody Player player, @PathVariable("id") int id) {

       Player updatedPlayer = new Player();
        updatedPlayer.setNickname(player.getNickname());
        updatedPlayer.setNickname(player.getPassword());
       updatedPlayer.setPlayerId(id);

        return playerServices.save(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id){
        playerServices.deleteById(id);
    }



}
