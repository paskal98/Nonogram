package com.game.nonogram.controllers;

import com.game.nonogram.game.metadata.NonogramGameData;
import com.game.nonogram.game.solver.NonogramSolver;
import com.game.nonogram.game.solver.NonogramUniqueSolver;
import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.models.Record;
import com.game.nonogram.server.game.NonogramGame;
import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.game.nonogram.game.utils.CharToBooleanConverter.convertCharToBooleanArray;
import static com.game.nonogram.game.utils.IntToBoolenConverter.convertIntToBooleanArray;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static class CreateGameJSON {

        public int[][] rows;
        public int[][] columns;
        public boolean[][] grid;

        public List<boolean[][]> stepByStep;

        public CreateGameJSON() {
        }

        public CreateGameJSON(int[][] rows, int[][] columns, boolean[][] grid, List<boolean[][]> stepByStep) {
            this.rows = rows;
            this.columns = columns;
            this.grid = grid;
            this.stepByStep = stepByStep;
        }

        @Override
        public String toString() {
            return "CreateGameJSON{" +
                    "rows=" + Arrays.deepToString(rows) +
                    ", columns=" + Arrays.deepToString(columns) +
                    ", grid=" + Arrays.deepToString(grid) +
                    '}';
        }
    }

    private static class SolveGameJSON{

        int[][] rowClues;
        int[][] columnClues;

        int size;

        public SolveGameJSON(int[][] rowClues, int[][] columnClues, int size) {
            this.rowClues = rowClues;
            this.columnClues = columnClues;
            this.size = size;
        }

        public int[][] getRowClues() {
            return rowClues;
        }

        public void setRowClues(int[][] rowClues) {
            this.rowClues = rowClues;
        }

        public int[][] getColumnClues() {
            return columnClues;
        }

        public void setColumnClues(int[][] columnClues) {
            this.columnClues = columnClues;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    private static class SaveGameJson{

        private CreateGameJSON createGameJSON;
        private String score;
        private String gameStatus;
        private int time;
        private int size;

        private Date date;
        private String player;

        public SaveGameJson() {
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public CreateGameJSON getCreateGameJSON() {
            return createGameJSON;
        }

        public void setCreateGameJSON(CreateGameJSON createGameJSON) {
            this.createGameJSON = createGameJSON;
        }

        public String getScore() {
            return score;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getGameStatus() {
            return gameStatus;
        }

        public void setGameStatus(String gameStatus) {
            this.gameStatus = gameStatus;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "SaveGameJson{" +
                    "createGameJSON=" + createGameJSON +
                    ", score='" + score + '\'' +
                    ", gameStatus='" + gameStatus + '\'' +
                    ", time=" + time +
                    ", size=" + size +
                    ", date=" + date +
                    ", player='" + player + '\'' +
                    '}';
        }
    }

    private static class ScoreTable{


        private int id;
        private String partyname;
        private int players;
        private int place;
        private int score;
        private String status;
        private String date;

        private int size;

        private int rating;

        public ScoreTable() {
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public  int getId() {
            return id;
        }

        public  void setId(int id) {
            this.id = id;
        }

        public String getPartyname() {
            return partyname;
        }

        public void setPartyname(String partyname) {
            this.partyname = partyname;
        }

        public int getPlayers() {
            return players;
        }

        public void setPlayers(int players) {
            this.players = players;
        }

        public int getPlace() {
            return place;
        }

        public void setPlace(int place) {
            this.place = place;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "ScoreTable{" +
                    "id=" + id +
                    ", partyname='" + partyname + '\'' +
                    ", players=" + players +
                    ", place=" + place +
                    ", score=" + score +
                    ", status='" + status + '\'' +
                    ", date=" + date +
                    '}';
        }
    }


    private static class SaveRating{

        int fieldId;
        int rating;

        public SaveRating() {
        }

        public SaveRating(int fieldId, int rating) {
            this.fieldId = fieldId;
            this.rating = rating;
        }

        public int getFieldId() {
            return fieldId;
        }

        public void setFieldId(int fieldId) {
            this.fieldId = fieldId;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }

    private NonogramGame nonogramGame = new NonogramGame();

    @GetMapping("/create")
    public CreateGameJSON getAll(@RequestParam(value = "size", required = false) Integer size){

        if (size == null)
            size = 5;

        nonogramGame.createGame(size);
        NonogramGameData gameData = nonogramGame.getGameData();

        List<boolean[][]> stepByStep=new ArrayList<>();

        for(int i=0;i<nonogramGame.getNonogramGenerator().getStepByStepSolution().size();i++){
            stepByStep.add(convertCharToBooleanArray(nonogramGame.getNonogramGenerator().getStepByStepSolution().get(i).getField()));
        }

        return new CreateGameJSON(
                gameData.getRows(),
                gameData.getColumns(),
                gameData.getGrid(),
                stepByStep
        );
    }

    @PostMapping("/solve")
    public boolean[][] solve(@RequestBody SolveGameJSON solveGameJSON){



        for(int[] item: solveGameJSON.getRowClues()){
            if(item.length==0) return new boolean[][]{{false}};
        }

        for(int[] item: solveGameJSON.getColumnClues()){
            if(item.length==0) return new boolean[][]{{false}};
        }


        NonogramSolver nonogramSolver = new NonogramSolver(
                new boolean[solveGameJSON.getSize()][solveGameJSON.getSize()],
                solveGameJSON.getRowClues(),
                solveGameJSON.getColumnClues(),
                solveGameJSON.getSize());

        try {
             nonogramSolver.solver();
        } catch (Exception e){
            return new boolean[][]{{false}};
        }


        int countAlLFallse=0;

        for(int i=0;i<solveGameJSON.getSize();i++)
            for(int j=0;j<solveGameJSON.getSize();j++)
                if(nonogramSolver.getSolved()[i][j]==0)
                    countAlLFallse++;

        if(countAlLFallse==(solveGameJSON.getSize()*solveGameJSON.getSize())) return new boolean[][]{{false}};

        // check if it has uniqe solution
        NonogramUniqueSolver nonogramUniqueSolver = new NonogramUniqueSolver(solveGameJSON.getSize(),solveGameJSON.getRowClues(),solveGameJSON.getColumnClues());
        System.out.println(nonogramUniqueSolver.check());

//        if(nonogramUniqueSolver.check()) return new boolean[][]{{false}, {false}};


        return convertIntToBooleanArray(nonogramSolver.getSolved());
    }


    @PostMapping("/save")
    public int saveGame(@RequestBody SaveGameJson saveGameJson, RestTemplate restTemplate){



        Gson gson = new Gson();
        Field field = new Field(
                saveGameJson.getSize(),
                gson.toJson(saveGameJson.getCreateGameJSON().stepByStep.get( saveGameJson.getCreateGameJSON().stepByStep.size()-1)),
                gson.toJson(saveGameJson.getCreateGameJSON().grid),
                gson.toJson(saveGameJson.getCreateGameJSON().rows),
                gson.toJson(saveGameJson.getCreateGameJSON().columns),
                saveGameJson.getTime()
        );


        ResponseEntity<Field> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8080/api/field", field, Field.class);
        Field newField = responseEntity.getBody();

        ResponseEntity<Player> responsePlayer = restTemplate.getForEntity("http://127.0.0.1:8080/api/player/findByNickName/"+saveGameJson.getPlayer(), Player.class);

        System.out.println(responsePlayer);

        Record record = new Record(responsePlayer.getBody(), newField, saveGameJson.getGameStatus(),saveGameJson.getDate());
        restTemplate.postForEntity("http://127.0.0.1:8080/api/record", record, Record.class);


        return newField.getFieldId();
    }


    @PostMapping("/rating")
    public void saveGameRating(@RequestBody SaveRating saveRating, RestTemplate restTemplate){

        System.out.println("=======");
        System.out.println(saveRating.getRating());
        System.out.println(saveRating.getFieldId());

        ResponseEntity<Void> responsePlayer = restTemplate.getForEntity("http://127.0.0.1:8080/api/record/addRating?fieldId="+saveRating.getFieldId()+"&rating="+saveRating.getRating(), void.class);



    }


    @GetMapping("/gamesCount")
    public int getGamesCount(@RequestParam(value = "nickname") String nickname, RestTemplate restTemplate) {

        ResponseEntity<List<Record>> responseEntity = restTemplate.exchange("http://127.0.0.1:8080/api/record/findRecordsByNickName?nickname="+ nickname, HttpMethod.GET, null, new ParameterizedTypeReference<List<Record>>() {
        });
        List<Record> records = responseEntity.getBody();

        assert records != null;
        return records.size();

    }

    @GetMapping("/scoreTable")
    public List<ScoreTable> getScoreTableByNickName(@RequestParam(value = "nickname") String nickname, RestTemplate restTemplate){


        ResponseEntity<List<Record>> responseEntity = restTemplate.exchange("http://127.0.0.1:8080/api/record/findRecordsByNickName?nickname="+ nickname, HttpMethod.GET, null, new ParameterizedTypeReference<List<Record>>() {
        });
        List<Record> records = responseEntity.getBody();


        List<ScoreTable> scoreTables = new ArrayList<>();

        assert records != null;
        int i=1;
        for (Record record : records){
            ScoreTable scoreTable = new ScoreTable();

            scoreTable.setId(i);
            scoreTable.setPartyname("SINGLE");
            scoreTable.setPlayers(1);
            scoreTable.setPlace(1);

            scoreTable.setScore((record.getField().getSolvingTime()/record.getField().getSize()));
            scoreTable.setStatus(record.getStatus());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(record.getDate());

            scoreTable.setDate(formattedDate);

            scoreTable.setSize(record.getField().getSize());

            scoreTable.setRating(record.getRating());

            scoreTables.add(scoreTable);
            i++;

        }

        System.out.println(scoreTables);


        return scoreTables;

    }


}
