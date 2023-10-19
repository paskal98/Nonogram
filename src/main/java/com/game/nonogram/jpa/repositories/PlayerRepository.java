package com.game.nonogram.jpa.repositories;

import com.game.nonogram.jpa.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableJpaRepositories
public interface PlayerRepository extends JpaRepository<Player, Integer> {

   boolean existsPlayerByNickname(String nickname);

   Player searchByNickname(String nickname);

   Player findPlayerByNickname(String nickname);




}
