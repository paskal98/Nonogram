package com.game.nonogram.game.kernel;

import com.game.nonogram.game.generator.NonogramGenerator;
import com.game.nonogram.game.kernel.dbcontrollers.Controller;
import com.game.nonogram.game.metadata.NonogramGameData;
import com.game.nonogram.game.kernel.procedural.MenuTitle;
import com.game.nonogram.game.kernel.procedural.ScreenParameter;
import com.game.nonogram.game.utils.MenuOptions;
import com.game.nonogram.game.utils.TextColor;
import com.game.nonogram.jpa.models.Record;
import com.game.nonogram.jpa.services.FieldServices;
import com.game.nonogram.jpa.services.PlayerServices;
import com.game.nonogram.jpa.services.RecordServices;
import com.google.gson.Gson;
import com.game.nonogram.jpa.models.Field;
import com.game.nonogram.jpa.models.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class NonogramKernel {

    private Controller controller;
    private NonogramGenerator generator;
    private MenuOptions menuOption;
    private List<NonogramGameData> gameRecords = new ArrayList<>();
    private NonogramGameData gameData;


    private Scanner scanner = new Scanner(System.in);


    public NonogramKernel(PlayerServices playerServices, RecordServices recordServices, FieldServices fieldServices) {

        this.controller = new Controller(playerServices,recordServices,fieldServices);

        this.gameData = new NonogramGameData(5);

    }

    /////////////////////////// game
    public void loadGame() {

        //registration player
        System.out.println(TextColor.TEXT_YELLOW.get() + "Write your nickname: ");
        String result = scanner.nextLine();


        Player player = new Player();
        player.setNickname(result);
        player = controller.getPlayerServices().save(player);
        controller.setPlayer(player);

        System.out.println(player);

        //lobby
        this.displayLobby();
    }

    private void startGame() {
        generator = new NonogramGenerator(gameData.getSize());

        System.out.println(TextColor.TEXT_PURPLE.get() + "Generating field..." + TextColor.TEXT_RESET.get());

        try {
            generator.generatePuzzle();
        } catch (Exception e) {
            System.out.println(e);
        }

        gameData.setGrid(generator.getGrid());
        gameData.setRows(generator.getRows());
        gameData.setColumns(generator.getColumns());

        initField();

        play();

        optionsProvider();

    }

    private void play() {
        long startTime = System.nanoTime();

        int hintIter = 1;

        while (true) {
            showField();

            System.out.println(TextColor.TEXT_WHITE.get() + "'0' [EXIT]");
            System.out.println(TextColor.TEXT_WHITE.get() + "'r' [RESET]");
            System.out.println(TextColor.TEXT_WHITE.get() + "'s' [SUBMIT]");
            System.out.println(TextColor.TEXT_WHITE.get() + "'h' [HINT]");
            System.out.println(TextColor.TEXT_WHITE.get() + "'!' [SOLUTION]");
            System.out.println();
            System.out.println(TextColor.TEXT_YELLOW.get() + "For select/unselect block input: <row index> <column index> [Enter]");

            String input = scanner.nextLine();

            String[] result = input.split(" ");

            if (result.length == 2) {

                try {
                    int row = Integer.parseInt(result[0]);
                    int column = Integer.parseInt(result[1]);

                    if (row <= 5 && column <= 5 && row > 0 && column > 0) {
                        fillField(row - 1, column - 1);
                    } else
                        System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Row or Column index out of indexes" + TextColor.TEXT_RESET.get());

                } catch (NumberFormatException e) {
                    System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Input not a number, please try again" + TextColor.TEXT_RESET.get());
                }

            } else if (result.length == 1) {

                if (Objects.equals(result[0], "0")) {
                    this.menuOption = MenuOptions.BACK;
                    saveGameData("" + (System.nanoTime() - startTime) / 1000000);
                    break;
                } else if (Objects.equals(result[0], "r")) {
                    initField();
                    System.out.println(TextColor.TEXT_CYAN.get() + "FIELD RESET" + TextColor.TEXT_RESET.get());
                } else if (Objects.equals(result[0], "h")) {
                    giveHint(hintIter++);

                } else if (Objects.equals(result[0], "!")) {
                    solvePuzzle();
                    showField();
                    System.out.println(TextColor.TEXT_PURPLE.get() + "SOLUTION DISPLAYED" + TextColor.TEXT_RESET.get());
                    saveGameData("" + (System.nanoTime() - startTime) / 1000000);
                    this.menuOption = MenuOptions.BACK;
                    break;
                } else if (Objects.equals(result[0], "s")) {
                    if (isSolved()) {
                        System.out.println(TextColor.GREEN_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Congratulations! You Solved!" + TextColor.TEXT_RESET.get());
                        this.menuOption = MenuOptions.BACK;
                        saveGameData("" + (System.nanoTime() - startTime) / 1000000);
                        break;
                    } else
                        System.out.println(TextColor.YELLOW_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Puzzle is not solved" + TextColor.TEXT_RESET.get());
                } else {
                    System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() +
                            "Wrong input. Option no detected" + TextColor.TEXT_RESET.get());
                }

            } else {
                System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() +
                        "Wrong input" + TextColor.TEXT_RESET.get());
            }


        }


    }

    private void giveHint(int hintIter) {


        if (hintIter < generator.getStepByStepSolution().size()) {
            System.out.println(TextColor.TEXT_CYAN.get() + "HINT USED\n" + TextColor.TEXT_RESET.get());
        } else {
            System.out.println(TextColor.TEXT_CYAN.get() + "ALL HINT USED\n" + TextColor.TEXT_RESET.get());
            hintIter=generator.getStepByStepSolution().size()-1;
        }


            char[][] hintField = new char[gameData.getSize()][gameData.getSize()];
            for (int i = 0; i < gameData.getSize(); i++) {
                for (int j = 0; j < gameData.getSize(); j++) {
                    hintField[i][j] = gameData.getFilledField()[i][j];
                }
            }

            for (int i = 0; i < gameData.getSize(); i++)
                for (int j = 0; j < gameData.getSize(); j++)
                    hintField[i][j] = generator.getStepByStepSolution().get(hintIter).getField()[i][j];

            for (int i = 0; i < gameData.getSize(); i++) {
                for (int j = 0; j < gameData.getSize(); j++) {
                    System.out.printf(TextColor.TEXT_BLUE.get() + hintField[i][j] + " " + TextColor.TEXT_RESET.get());
                }
                System.out.println();
            }



    }

    private void solvePuzzle() {

        char[][] field = new char[gameData.getSize()][gameData.getSize()];
        for (int i = 0; i < gameData.getSize(); i++)
            for (int j = 0; j < gameData.getSize(); j++)
                field[i][j] = generator.getGrid()[i][j] ? 'x' : '_';
        gameData.setFilledField(field);
    }

    private void saveGameData(String gameTime) {
        gameData.setSolved(isSolved());
        gameData.setGameTime(gameTime);
        gameRecords.add(new NonogramGameData(gameData.getFilledField(), gameData.getGrid(), gameData.getRows(), gameData.getColumns(), gameData.getSize(),gameData.getGameTime(), gameData.isSolved()));
    }

    private boolean isSolved() {

        for (int i = 0; i < gameData.getSize(); i++) {
            for (int j = 0; j < gameData.getSize(); j++) {
                if ((!gameData.getGrid()[i][j] && gameData.getFilledField()[i][j] == 'x') || (gameData.getGrid()[i][j] && gameData.getFilledField()[i][j] == '_')) {
                    return false;
                }
            }
        }
        return true;
    }

    ///////////////////////// file manager
    private void startGameUploadMap(boolean[][] uploadGrid) {

        gameData.setSize(uploadGrid.length);

        generator = new NonogramGenerator(gameData.getSize());

        try {
            generator.prepareUploadMap(uploadGrid);
        } catch (Exception e) {
            System.out.println(e);
        }


        gameData.setGrid(generator.getGrid());
        gameData.setRows(generator.getRows());
        gameData.setColumns(generator.getColumns());

        initField(uploadGrid.length);

        play();

        gameData.setSize(5);


    }

    private boolean isFilePathExists(String path) {
        return new File(path).exists();
    }


    ///////////////////////// initializers
    private void initField() {
        char[][] field = new char[gameData.getSize()][gameData.getSize()];
        for (int i = 0; i < gameData.getSize(); i++)
            for (int j = 0; j < gameData.getSize(); j++)
                field[i][j] = '_';
        gameData.setFilledField(field);
    }

    private void initField(int size) {
        char[][] field = new char[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                field[i][j] = '_';
        gameData.setFilledField(field);
    }


    ///////////////////////// menu options
    private Optional<MenuOptions> getMenuOptionByClassifier(int option) {
        MenuOptions[] menuOptions = MenuOptions.class.getEnumConstants();
        Optional<MenuOptions> result = Arrays.stream(menuOptions).filter(type -> type.get() == option).findFirst();
        return result;
    }

    private void optionsProvider() {

        switch (menuOption) {
            case START_GAME:
                displayGameMenu();
                break;
            case SHOW_RECORDS:
                displayRecords();
                break;
            case EXIT:
                displayExit();
                break;
            case BACK:
                displayLobby();
                break;
            case START:
                startGame();
                break;
            case SIZE:
                displaySettingsSize();
                break;
            case UPLOAD:
                displaySettingsUpload();
                break;
            default:
                break;
        }

    }

    private boolean isOptionExists(int option) {
        return getMenuOptionByClassifier(option).isPresent();
    }

    private void optionsGame() {
        System.out.println(TextColor.TEXT_GREEN.get() + "5. Start");
        System.out.println(TextColor.TEXT_GREEN.get() + "6. Set size");
        System.out.println(TextColor.TEXT_GREEN.get() + "7. Upload map");
        System.out.println(TextColor.TEXT_GREEN.get() + "4. BACK to lobby");
        printLine(".");
    }

    private void optionsRecords() {
        System.out.println(TextColor.TEXT_GREEN.get() + "4. BACK to lobby");
        printLine(".");
    }

    private void optionsMainMenu() {
        System.out.println(TextColor.TEXT_GREEN.get() + "1. Start Game");
        System.out.println(TextColor.TEXT_GREEN.get() + "2. Show records");
        System.out.println(TextColor.TEXT_GREEN.get() + "3. Exit");
        printLine(".");
    }


    /////////////////////////input manager
    private void inputOption() {

        String result;
        int option;
        System.out.println(TextColor.TEXT_YELLOW.get() + "Choose option: ");

        while (true) {
            result = scanner.nextLine();
            try {
                option = Integer.parseInt(result);
                if (isOptionExists(option))
                    break;
                else
                    System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "No such option under this number" + TextColor.TEXT_RESET.get());

            } catch (NumberFormatException e) {
                System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Input not a number, please try again" + TextColor.TEXT_RESET.get());
            }
        }

        this.menuOption = getMenuOptionByClassifier(option).get();
    }

    private int inputSize() {


        String size;
        int number;
        System.out.println(TextColor.TEXT_WHITE.get() + "[for EXIT input 0] ");
        System.out.println(TextColor.TEXT_RED.get() + "[min=" + NonogramGenerator.SIZE_MIN + " max=" + NonogramGenerator.SIZE_MAX + "]");
        System.out.println(TextColor.TEXT_YELLOW.get() + "Input filed size: ");

        while (true) {
            size = scanner.nextLine();
            try {
                number = Integer.parseInt(size);
                if (number == 0) {
                    number = 5;
                    break;
                }
                if (number >= NonogramGenerator.SIZE_MIN && number <= NonogramGenerator.SIZE_MAX)
                    break;
                else
                    System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Field size can be between " + NonogramGenerator.SIZE_MIN + " and " + NonogramGenerator.SIZE_MAX + TextColor.TEXT_RESET.get());

            } catch (NumberFormatException e) {
                System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "Input not a number, please try again" + TextColor.TEXT_RESET.get());
            }
        }

        return number;

    }

    private void inputMapPath() {

        File directoryPath = new File("src/main/maps");
        String contents[] = directoryPath.list();
        System.out.println(TextColor.TEXT_BLUE.get());
        for (int i = 0; i < contents.length; i++) {
            System.out.println(contents[i]);
        }
        System.out.println(TextColor.TEXT_RESET.get());

        String path;
        System.out.println(TextColor.TEXT_WHITE.get() + "'0' [EXIT] ");

        while (true) {
            path = scanner.nextLine();


            if (Objects.equals(path, "0")) {
                break;
            } else if (isFilePathExists("src/main/maps/" + path)) {
                startGameUploadMap(uploadMap("src/main/maps/" + path));
                break;
            } else
                System.out.println(TextColor.WHITE_BACKGROUND.get() + TextColor.TEXT_BLACK.get() + "File not exists" + TextColor.TEXT_RESET.get());


        }

    }


    /////////////////////////display manager
    private void displayLobby() {

        //Print game logo
        this.displayGameLogo();

        //MainMenu
        this.optionsMainMenu();
        inputOption();

        optionsProvider();
    }

    private void displaySettingsSize() {

        // title
        printLine("_");
        printTitle(MenuTitle.SIZE_TITLE.get(), TextColor.TEXT_PURPLE.get());
        printLine("-");

        // options
        gameData.setSize(inputSize());
        this.menuOption = MenuOptions.START_GAME;
        optionsProvider();

    }

    private void displayGameMenu() {

        String info = "Current Field size: " + TextColor.TEXT_YELLOW.get() + gameData.getSize() + "x" + gameData.getSize();

        // title
        printLine("_");
        printTitle(MenuTitle.GAME_ONSTART_TITLE.get(), TextColor.TEXT_PURPLE.get());
        printLine("-");

        // info
        printTitle(info, TextColor.TEXT_WHITE.get());
        printLine("-");

        // options
        optionsGame();
        inputOption();
        optionsProvider();

    }

    private void displayExit() {
        printLine("_");
        printTitle(MenuTitle.EXIT_MSG.get(), TextColor.TEXT_PURPLE.get());
        printLine("-");

        //save all player data
        for (int i = 0; i < gameRecords.size(); i++) {

            Gson gson = new Gson();
            Field fieldItem = new Field(
                    gameRecords.get(i).getSize(),
                    gson.toJson(gameRecords.get(i).getFilledField()),
                    gson.toJson(gameRecords.get(i).getGrid()),
                    gson.toJson(gameRecords.get(i).getRows()),
                    gson.toJson(gameRecords.get(i).getColumns()),
                    Integer.parseInt(gameRecords.get(i).getGameTime())
            );

            fieldItem = controller.getFieldServices().save(fieldItem);
            Record record = new Record(controller.getPlayer(), fieldItem, String.valueOf(gameRecords.get(i).isSolved()));
            controller.getRecordServices().save(record);
        }

    }

    private void displayRecords() {

        // title
        printLine("_");
        printTitle(MenuTitle.RECORDS_TITLE.get(), TextColor.TEXT_PURPLE.get());
        printLine("-");

        //load from db
        List<Record> records = controller.getRecordServices().getRecordsByPlayer(controller.getPlayer());

        List<NonogramGameData> gameDataDB = new ArrayList<>();
        if (records.size() > 0) {


            for (Record recordItem : records) {
                Gson gson = new Gson();
                char[][] recorded = gson.fromJson(recordItem.getField().getGridRecordedJSON(), char[][].class);
                boolean[][] solved = gson.fromJson(recordItem.getField().getGridSolvedJSON(), boolean[][].class);
                int[][] rows_clues = gson.fromJson(recordItem.getField().getRowsCluesJSON(), int[][].class);
                int[][] columns_clues = gson.fromJson(recordItem.getField().getColumnsCluesJSON(), int[][].class);

                NonogramGameData gameDataItem = new NonogramGameData();
                gameDataItem.setFilledField(recorded);
                gameDataItem.setGrid(solved);
                gameDataItem.setRows(rows_clues);
                gameDataItem.setColumns(columns_clues);
                gameDataItem.setGameTime(recordItem.getField().getSolvingTime() + "");
                gameDataItem.setSolved(Boolean.parseBoolean(recordItem.getStatus()));
                gameDataItem.setSize(recordItem.getField().getSize());

                gameDataDB.add(gameDataItem);
            }


        }


        //result
        int i = 1;
        i = printRecords(gameRecords, i);

        if (gameDataDB.size() > 0)
            printRecords(gameDataDB, i);


        // options
        optionsRecords();
        inputOption();
        optionsProvider();
    }

    private int printRecords(List<NonogramGameData> gameDataList, int i) {


        for (NonogramGameData gameData : gameDataList) {
            System.out.println(TextColor.TEXT_WHITE.get() + i + ".");
            System.out.println(TextColor.TEXT_YELLOW.get() + "Field size: " + TextColor.TEXT_CYAN.get() + gameData.getSize() + "x" + gameData.getSize() + ".\n" +
                    TextColor.TEXT_YELLOW.get() + "Time for solving in millis: " + TextColor.TEXT_CYAN.get() + gameData.getGameTime() + ".\n" + TextColor.TEXT_YELLOW.get() +
                    "Solved: " + TextColor.TEXT_CYAN.get() + gameData.isSolved());
            gameData.showField();
            i++;
        }
        return i;
    }

    private void displayGameLogo() {
        printLine("+");
        printTitle(MenuTitle.GAME_TITLE.get(), TextColor.TEXT_CYAN.get());
        printLine("+");
    }

    private void displaySettingsUpload() {

        String info = TextColor.TEXT_WHITE.get() + "*Note, files should located in dir 'maps'" + TextColor.TEXT_RESET.get();

        // title
        printLine("_");
        printTitle(MenuTitle.UPLOAD_TITLE.get(), TextColor.TEXT_PURPLE.get());
        printLine("-");

        // info
        printTitle(info, TextColor.TEXT_WHITE.get());
        printLine("-");

        // options
        inputMapPath();
        this.menuOption = MenuOptions.START_GAME;
        optionsProvider();

    }


    ///////////////////////// additional, other
    private void fillField(int row, int column) {

        if (gameData.getFilledField()[row][column] == 'x')
            gameData.getFilledField()[row][column] = '_';
        else
            gameData.getFilledField()[row][column] = 'x';

    }

    private void showField() {

        // print column indexes

        if (gameData.getSize() >= 10) {
            System.out.print(" ");
        }

        System.out.print(TextColor.TEXT_WHITE.get() + "  ");
        for (int l = 0; l < gameData.getSize(); l++)
            System.out.print((l + 1) + " ");
        System.out.println();

        // print field rows and clues
        for (int g = 0; g < gameData.getSize(); g++) {

            // index of row
            System.out.print(TextColor.TEXT_WHITE.get() + (g + 1) + " ");
            if (g < 9 & gameData.getSize() >= 10) {
                System.out.print(" ");
            }

            // field row and clue
            for (int l = 0; l < gameData.getSize(); l++)
                System.out.print(TextColor.TEXT_CYAN.get() + gameData.getFilledField()[g][l] + " ");

            for (int l = 0; l < gameData.getRows()[g].length; l++)
                System.out.print(TextColor.TEXT_GREEN.get() + gameData.getRows()[g][l] + " ");
            System.out.println();

        }

        // print column clues
        for (int g = 0; g < getLineMaxLength(gameData.getColumns()); g++) {
            System.out.print(TextColor.TEXT_GREEN.get() + "  ");

            if (g < 9 & gameData.getSize() >= 10) {
                System.out.print(" ");
            }

            //  clue
            for (int i = 0; i < gameData.getSize(); i++)
                if (g < gameData.getColumns()[i].length)
                    System.out.print(gameData.getColumns()[i][g] + " ");
                else
                    System.out.print("  ");

            System.out.println();
        }


        System.out.println();

    }

    private int getLineMaxLength(int line[][]) {
        int maxLength = 0;
        for (int i = 0; i < line.length; i++)
            if (line[i].length > maxLength)
                maxLength = line[i].length;
        return maxLength;
    }

    private boolean[][] uploadMap(String path) {

        boolean[][] newGrid = null;
        File fileOfCombinations = new File(path);
        Scanner myReader = null;
        int size = 0;
        List<int[]> tempList = new ArrayList<>();
        try {
            myReader = new Scanner(fileOfCombinations);

            String data = myReader.nextLine();

            try {
                size = Integer.parseInt(data);
            } catch (NumberFormatException e) {

                throw new NumberFormatException();
            }


            while (myReader.hasNextLine()) {
                data = myReader.nextLine();

                String[] tempOut = data.split(" ");

                int[] tempLine = new int[size];
                for (int i = 0; i < size; i++)
                    tempLine[i] = Integer.parseInt(tempOut[i]);

                tempList.add(tempLine);

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        newGrid = new boolean[size][size];

        int i = 0;
        for (int[] line : tempList) {
            int j = 0;
            for (int block : line) {
                if (block == 1)
                    newGrid[i][j] = true;
                else
                    newGrid[i][j] = false;
                j++;
            }
            i++;
        }


        return newGrid;

    }

    private void printLine(String charSymbol) {
        for (int i = 0; i < ScreenParameter.WIDTH.get(); i++)
            System.out.print(TextColor.TEXT_BLUE.get() + charSymbol);
        System.out.println();
    }

    private void printTitle(String title, String color) {
        for (int i = 0; i < ScreenParameter.WIDTH.get() - title.length(); i++) {
            System.out.print(" ");
            if (i == (((ScreenParameter.WIDTH.get() - title.length()) / 2) - 1))
                System.out.print(color + title);
        }
        System.out.println();
    }

}
