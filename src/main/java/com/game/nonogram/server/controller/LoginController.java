package com.game.nonogram.server.controller;

import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static class PlayerLogin{

        private String nickname;
        private String password;

        public PlayerLogin(String nickname, String password) {
            this.nickname = nickname;
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private  Player player;

    @GetMapping()
    public Player showPreLoginPage( ) {

        return player;
    }

    @PostMapping()
    public PlayerLogin handleLoginFormSubmit(@RequestBody PlayerLogin playerLogin, RestTemplate restTemplate) {


        ResponseEntity<Boolean> responseIsExists = restTemplate.getForEntity("http://127.0.0.1:8080/api/player/isExists/"+playerLogin.getNickname(),boolean.class);


        if(responseIsExists.getBody()) {

            player = new Player(playerLogin.getNickname());
            ResponseEntity<Player> responseEntityPlayer = restTemplate.postForEntity("http://127.0.0.1:8080/api/player", player, Player.class);
            player = responseEntityPlayer.getBody();

            if(Objects.equals(player.getPassword(), playerLogin.getPassword())) return playerLogin;
            else return new PlayerLogin("wrong","wrong");


        } else {
            return new PlayerLogin("none","none");
        }

//        if(player.getNickname().isEmpty())
//            return "redirect:/login";
//
//        return "redirect:/nonogram?nickname="+player.getNickname();
    }



    @PostMapping("/signup")
    public PlayerLogin handleSignUPFormSubmit(@RequestBody PlayerLogin playerLogin, RestTemplate restTemplate) {


        System.out.println(playerLogin.getNickname());
        System.out.println(playerLogin.getPassword());

        ResponseEntity<Boolean> responseIsExists = restTemplate.getForEntity("http://127.0.0.1:8080/api/player/isExists/"+playerLogin.getNickname(),boolean.class);


        if(responseIsExists.getBody()) return new PlayerLogin("none","none");
        System.out.println("XXX");

        player = new Player(playerLogin.getNickname(),playerLogin.getPassword());
        ResponseEntity<Player> responseEntityPlayer = restTemplate.postForEntity("http://127.0.0.1:8080/api/player", player, Player.class);
        player = responseEntityPlayer.getBody();

        return playerLogin;

    }


}