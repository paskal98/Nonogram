package com.game.nonogram.jpa.services;

import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional()
public class PlayerServices {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServices(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findOne(int id) {
        Optional<Player> foundPlayer = playerRepository.findById(id);
        return foundPlayer.orElse(null);
    }

    public Player save(Player player) {
        if (!playerRepository.existsPlayerByNickname(player.getNickname())) {
            playerRepository.save(player);
            return player;
        } else
            return playerRepository.searchByNickname(player.getNickname());
    }

    public Player update(Player player) {
       return playerRepository.save(player);
    }

    public void deleteById(Integer id) {
        playerRepository.deleteById(id);
    }

    public boolean existsPlayerByNickname(String nickname){
      return   playerRepository.existsPlayerByNickname(nickname);
    }

    public Player findPlayerByNickname(String nickname){
        return   playerRepository.findPlayerByNickname(nickname);
    }
}
