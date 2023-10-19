package com.game.nonogram.server.controller;

import com.game.nonogram.game.metadata.NonogramGameData;
import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;
import com.game.nonogram.jpa.models.Record;
import com.game.nonogram.jpa.services.PlayerServices;
import com.game.nonogram.server.game.GameStatus;
import com.game.nonogram.server.game.NonogramGame;
import com.game.nonogram.server.thymeleafrecognizer.TableData;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/nonogram")
public class NonogramController {


    private final NonogramGame nonogramGame;
    private Player player;


    @Autowired
    public NonogramController(NonogramGame nonogramGame) {
        this.nonogramGame = nonogramGame;
    }




    @GetMapping()
    public String index(@RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "nickname", required = false) String nickname, Model model, RestTemplate restTemplate) {

        if (size == null)
            size = 5;


        if (nickname != null) {
            player = new Player(nickname);
            ResponseEntity<Player> responseEntityPlayer = restTemplate.postForEntity("http://127.0.0.1:8080/api/player", player, Player.class);
            player = responseEntityPlayer.getBody();

        }

        model.addAttribute("size", size);
        nonogramGame.createGame(size);


        return "nonogram";

    }


    @GetMapping("/fill/{indexRow}/{indexColumn}")
    public String fillField(@PathVariable("indexRow") int indexRow, @PathVariable("indexColumn") int indexColumn) {
        nonogramGame.fillCell(indexRow, indexColumn);

        if (nonogramGame.isSolved())
            return "redirect:/nonogram/submit";

        return "nonogram";
    }

    @GetMapping("/solve")
    public String fillField() {
        nonogramGame.solve();
        return "nonogram";

    }

    @GetMapping("/hint")
    public String hint() {

        nonogramGame.hint();

        return "nonogram";
    }

    @GetMapping("/reset")
    public String reset() {
        nonogramGame.resetField();
        return "nonogram";
    }

    @GetMapping("/submit")
    public String submit(RestTemplate restTemplate) {

        if (nonogramGame.isSolved()) {

            if (player == null) return "redirect:/nonogram";
//            if (nonogramGame.getGameStatus() != GameStatus.DONE) return "redirect:/nonogram/score";



            Gson gson = new Gson();
            Field field = new Field(
                    nonogramGame.getGameData().getSize(),
                    gson.toJson(nonogramGame.getFilledField()),
                    gson.toJson(nonogramGame.getGrid()),
                    gson.toJson(nonogramGame.getRows()),
                    gson.toJson(nonogramGame.getColumns()),
                    (int) ((System.nanoTime() - nonogramGame.getGameStartTime()) / 1000000)
            );


            ResponseEntity<Field> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8080/api/field", field, Field.class);
            Field newField = responseEntity.getBody();

            Record record = new Record(player, newField, String.valueOf(nonogramGame.getGameStatus()));
            restTemplate.postForEntity("http://127.0.0.1:8080/api/record", record, Record.class);


            return "redirect:/nonogram/score";
        }

        return "nonogram";
    }


    @GetMapping("/score")
    public String scoreTable(RestTemplate restTemplate, Model model) {

        ResponseEntity<List<Record>> responseEntity = restTemplate.exchange("http://127.0.0.1:8080/api/record/records", HttpMethod.GET, null, new ParameterizedTypeReference<List<Record>>() {
        });
        List<Record> records = responseEntity.getBody();

        if (records != null) {

            List<TableData> tableData = new ArrayList<>();

            for (int i = 0; i < records.size(); i++) {
                tableData.add(new TableData(
                        records.get(i).getPlayer().getNickname(),
                        String.valueOf(records.get(i).getField().getSolvingTime()),
                        "underscore",
                        String.valueOf(records.get(i).getField().getSize()),
                        records.get(i).getStatus()));
            }


            model.addAttribute("tableData", tableData);

        }
        return "score";

    }


    public String getHtmlField() {


        // clues_row
        StringBuilder grid = new StringBuilder();

        grid.append("<div class=\"clue clue_row\">");

        for (int i = 0; i < nonogramGame.getGameData().getRows().length; i++) {
            grid.append(" <div class=\"clue_row_line clue_line\">");
            for (int j = 0; j < nonogramGame.getGameData().getRows()[i].length; j++) {
                grid.append("<div class=\"clue_row_item clue_item\">");
                grid.append(nonogramGame.getGameData().getRows()[i][j]);
                grid.append("</div>");
            }
            grid.append("</div>");
        }
        grid.append("</div>");

        //wraper for clue_column and grid
        grid.append("<div>");

        // clue column
        grid.append("<div class=\"clue clue_column\">");
        for (int i = 0; i < nonogramGame.getGameData().getColumns().length; i++) {
            grid.append(" <div class=\"clue_column_line clue_line\">");
            for (int j = 0; j < nonogramGame.getGameData().getColumns()[i].length; j++) {
                grid.append("<div class=\"clue_column_item clue_item\">");
                grid.append(nonogramGame.getGameData().getColumns()[i][j]);
                grid.append("</div>");
            }
            grid.append("</div>");
        }
        grid.append("</div>");


        //grid
        grid.append(" <div class=\"grid_wrap\">");
        for (int i = 0; i < nonogramGame.getGameData().getSize(); i++) {
            grid.append("<div  class=\"grid_wrap_row\">");
            for (int j = 0; j < nonogramGame.getGameData().getSize(); j++) {
                grid.append("<div  class=\"grid_wrap_row_item\">");
                grid.append(String.format("<a href='/nonogram/fill/%d/%d'>", i + 1, j + 1));
                if (nonogramGame.getGameData().getFilledField()[i][j] == 'x')
                    grid.append(nonogramGame.getGameData().getFilledField()[i][j]);
                else
                    grid.append(" ");

                grid.append("</a>");
                grid.append("</div>");
            }
            grid.append("</div  class=\"grid_wrap_row\">");
        }
        grid.append("</div>");


        grid.append("</div>");

        return String.valueOf(grid);
    }


}
