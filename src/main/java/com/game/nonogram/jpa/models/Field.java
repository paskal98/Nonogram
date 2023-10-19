package com.game.nonogram.jpa.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "field")
public class Field {

    @Id
    @Column(name = "field_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int fieldId;

    @Column(name = "size")
    private int size;

    @Column(name = "grid_recorded_json")
    private String gridRecordedJSON;

    @Column(name = "grid_solved_json")
    private String gridSolvedJSON;

    @Column(name = "rows_clues_json")
    private String rowsCluesJSON;

    @Column(name = "columns_clue_json")
    private String columnsCluesJSON;

    @Column(name = "solving_time")
    private int solvingTime;

    @OneToOne(mappedBy = "field",fetch = FetchType.LAZY)
    private Record record;

    public Field() {
    }

    public Field(int fieldId, int size, String gridRecordedJSON, String gridSolvedJSON, String rowsCluesJSON, String columnsCluesJSON, int solvingTime) {
        this.fieldId = fieldId;
        this.size = size;
        this.gridRecordedJSON = gridRecordedJSON;
        this.gridSolvedJSON = gridSolvedJSON;
        this.rowsCluesJSON = rowsCluesJSON;
        this.columnsCluesJSON = columnsCluesJSON;
        this.solvingTime = solvingTime;
    }

    public Field(int size, String gridRecordedJSON, String gridSolvedJSON, String rowsCluesJSON, String columnsCluesJSON, int solvingTime) {
        this.size = size;
        this.gridRecordedJSON = gridRecordedJSON;
        this.gridSolvedJSON = gridSolvedJSON;
        this.rowsCluesJSON = rowsCluesJSON;
        this.columnsCluesJSON = columnsCluesJSON;
        this.solvingTime = solvingTime;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getGridRecordedJSON() {
        return gridRecordedJSON;
    }

    public void setGridRecordedJSON(String gridRecordedJSON) {
        this.gridRecordedJSON = gridRecordedJSON;
    }

    public String getGridSolvedJSON() {
        return gridSolvedJSON;
    }

    public void setGridSolvedJSON(String gridSolvedJSON) {
        this.gridSolvedJSON = gridSolvedJSON;
    }

    public String getRowsCluesJSON() {
        return rowsCluesJSON;
    }

    public void setRowsCluesJSON(String rowsCluesJSON) {
        this.rowsCluesJSON = rowsCluesJSON;
    }

    public String getColumnsCluesJSON() {
        return columnsCluesJSON;
    }

    public void setColumnsCluesJSON(String columnsCluesJSON) {
        this.columnsCluesJSON = columnsCluesJSON;
    }

    public int getSolvingTime() {
        return solvingTime;
    }

    public void setSolvingTime(int solvingTime) {
        this.solvingTime = solvingTime;
    }


    @Override
    public String toString() {
        return "Field{" +
                "fieldId=" + fieldId +
                ", size=" + size +
                ", gridRecordedJSON='" + gridRecordedJSON + '\'' +
                ", gridSolvedJSON='" + gridSolvedJSON + '\'' +
                ", rowsCluesJSON='" + rowsCluesJSON + '\'' +
                ", columnsCluesJSON='" + columnsCluesJSON + '\'' +
                ", solvingTime=" + solvingTime +
//                ", record=" + record +
                '}';
    }
}
