package com.game.nonogram.game.kernel.dbcontrollers;

import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.services.FieldServices;
import com.game.nonogram.jpa.services.PlayerServices;
import com.game.nonogram.jpa.services.RecordServices;


public class Controller {

    private final PlayerServices playerServices;
    private final RecordServices recordServices;
    private final FieldServices fieldServices;
    private Player player;

    public Controller(PlayerServices playerServices, RecordServices recordServices, FieldServices fieldServices) {
        this.playerServices = playerServices;
        this.recordServices = recordServices;
        this.fieldServices = fieldServices;
    }

    public PlayerServices getPlayerServices() {
        return playerServices;
    }


    public RecordServices getRecordServices() {
        return recordServices;
    }


    public FieldServices getFieldServices() {
        return fieldServices;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
