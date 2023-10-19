package com.game.nonogram.jpa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Record")
public class Record {

    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "player_id",referencedColumnName = "player_id")
    private  Player player;

    @OneToOne
    @JoinColumn(name = "field_id",referencedColumnName = "field_id")
    private  Field field;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private Date date;

    @Column(name = "rating")
    private int rating;



    public Record(int recordId, Field field, String status) {
        this.recordId = recordId;
        this.field = field;
        this.status = status;
    }

    public Record(Player player, Field field, String status) {
        this.player = player;
        this.field = field;
        this.status = status;
    }

    public Record(Player player, Field field, String status, Date date) {
        this.player = player;
        this.field = field;
        this.status = status;
        this.date = date;
    }



    public Record() {
    }

    public Date getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", player=" + player +
                ", field=" + field +
                ", status='" + status + '\'' +
                '}';
    }
}
