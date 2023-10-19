package com.game.nonogram.jpa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name ="player")
public class Player {

    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;


    @Column(name = "nickname")
    @NonNull
    private String nickname;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "player",fetch = FetchType.LAZY)
    private List<Record> records;

    public Player() {
    }

    public Player(int playerId, String nickname) {
        this.playerId = playerId;
        this.nickname = nickname;
    }

    public Player(int playerId, String nickname, String password) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.password = password;
    }

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public Player(@NonNull String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", nickname='" + nickname + '\'' +
//                ", records=" + records +
                '}';
    }
}
