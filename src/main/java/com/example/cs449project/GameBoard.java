package com.example.cs449project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Objects;

public class GameBoard extends Application {
    private Game game;
    private Scene scene;
    private BorderPane rootBorderPane;
    private GridPane gameBoard;
    private HBox headingHBox;
    private HBox footerHBox;
    private VBox[] playerSymbolsBoxes;
    private BorderPane borderPane;

    private boolean modeSelected = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        game = new Game();
        rootBorderPane = createLayout();
        scene = new Scene(rootBorderPane, 520, 360);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SOS Game");
        primaryStage.show();
    }

    private BorderPane createLayout() {
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        createHeading();
        borderPane.setCenter(createGameBoard());
        createPlayersVBoxes();
        updateFooter();

        return borderPane;
    }

    private void createHeading() {
        headingHBox = new HBox(30);
        headingHBox.getChildren().clear();
        headingHBox.setAlignment(Pos.CENTER);

        Label gameLabel = new Label("SOS");
        ToggleGroup gameModeToggleGroup = new ToggleGroup();
        RadioButton simpleGameModeRadioButton = createRadioButton(Game.GameMode.Simple, false, gameModeToggleGroup);
        RadioButton generalGameModeRadioButton = createRadioButton(Game.GameMode.General, false, gameModeToggleGroup);

        TextField boardSizeTextField = createBoardSizeTextField();

        headingHBox.getChildren().addAll(gameLabel, simpleGameModeRadioButton, generalGameModeRadioButton, boardSizeTextField);

        borderPane.setTop(headingHBox);
    }

    private RadioButton createRadioButton(Game.GameMode mode, boolean selected, ToggleGroup toggleGroup) {
        RadioButton radioButton = new RadioButton(mode.name() + " game");
        radioButton.setUserData(mode);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setSelected(selected);

        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Game.GameMode selectedMode = (Game.GameMode) toggleGroup.getSelectedToggle().getUserData();

                game.setGameMode(selectedMode);
                modeSelected = true;
                if (selectedMode == Game.GameMode.Simple) {
                    game = new SimpleGame();
                } else if (selectedMode == Game.GameMode.General) {
                    game = new GeneralGame();
                }

                // Update player symbols when changing game mode
                createPlayersVBoxes();

                // Update the game board and footer
                updateGameBoard();
                updateFooter();
            }
        });

        return radioButton;
    }

    private TextField createBoardSizeTextField() {
        TextField boardSizeTextField = new TextField();
        boardSizeTextField.setPrefColumnCount(2);
        boardSizeTextField.setText(String.valueOf(game.getBoardSize()));

        boardSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boardSizeTextField.selectAll();
            try {
                if (!Objects.equals(newValue, "")) {
                    game.setBoardSize(Integer.parseInt(newValue));
                    updateGameBoard();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return boardSizeTextField;
    }

    private GridPane createGameBoard() {
        gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        updateGameBoard();

        return gameBoard;
    }

    private void updateGameBoard() {
        gameBoard.getChildren().clear();
        int size = game.getBoardSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                CustomTextField cell = createCell(i, j);
                gameBoard.add(cell, i, j);
            }
        }
        updateFooter();
        createPlayersVBoxes();
    }

    private CustomTextField createCell(int i, int j) {
        CustomTextField cell = new CustomTextField();
        cell.setEditable(false);
        cell.setPrefColumnCount(1);
        cell.setAlignment(Pos.CENTER);

        cell.setOnMouseClicked(event -> handleCellClick(i, j));

        Cell c = game.getCellState(i, j);
        if (c != null) {
            cell.setText(c.getSymbol().toString());
            for (CellLine line : c.getLines()) {
                cell.activateLine(line.getDirection(), line.getColor());
            }
        }

        return cell;
    }

    private void handleCellClick(int i, int j) {
        if (game.isGameStarted()) {
            if (game.isValidMove(i, j)) {
                game.setCellState(i, j);
                updateGameBoard();
            } else if (i != -1 && j != -1) {
                AlertUtil.showAlert(AlertType.WARNING, "Invalid Move", "Invalid Move! Try again!");
            } else if (i == -1 && j == -1) {
                game.changeCurrentPlayer();
            }
            if (game.gameOver()) {
                updateGameBoard();
                Player winner = game.getWinner();
                if (winner == null) {
                    AlertUtil.showAlert(AlertType.INFORMATION, "Game Over", "The Game is a Draw");
                    if (game.isRecordGame()) {
                        game.getGameRecord().record("Game Over - Game is a Draw");
                    }
                    if (game.getGameRecord() != null) {
                        game.getGameRecord().closeFile();
                    }
                } else {
                    AlertUtil.showAlert(AlertType.INFORMATION, "Game Over", "The Winner is " + winner.getFullName());
                    if (game.isRecordGame()) {
                        game.getGameRecord().record("Game Over - The Winner is " + winner.getFullName());
                    }
                    if (game.getGameRecord() != null) {
                        game.getGameRecord().closeFile();
                    }

                }
                restartGame();
            }


            updateGameBoard();

        } else {
            AlertUtil.showAlert(AlertType.WARNING, "Game Not Started", "Select a Game Mode\nSet a Board Size\nPress Start!");
        }
    }

    private void createPlayersVBoxes() {
        playerSymbolsBoxes = new VBox[game.getPlayers().length];

        for (int i = 0; i < game.getPlayers().length; i++) {
            // Get the player
            Player player = game.getPlayers()[i];
            // Create the general VBox to hold all the details
            playerSymbolsBoxes[i] = new VBox(10);
            // Create the player label
            Label playerLabel = new Label(player.getName());

            // Create player type radio buttons
            ToggleGroup playerTypeToggleGroup = new ToggleGroup();
            boolean isHumanPlayer = player.getPlayerType() == Player.PlayerType.Human;
            RadioButton humanPlayerRadioButton = createPlayerTypeRadioButton(player, Player.PlayerType.Human,
                    playerTypeToggleGroup, isHumanPlayer);
            RadioButton computerPlayerRadioButton = createPlayerTypeRadioButton(player, Player.PlayerType.Computer,
                    playerTypeToggleGroup, !isHumanPlayer);

            // Create player symbol radio buttons
            ToggleGroup playerToggleGroup = new ToggleGroup();
            boolean sIsCurrent = player.getCurrentSymbol() == Player.Symbol.S;
            RadioButton playerSRadioButton = createPlayerRadioButton(player, Player.Symbol.S, playerToggleGroup, sIsCurrent);
            RadioButton playerORadioButton = createPlayerRadioButton(player, Player.Symbol.O, playerToggleGroup, !sIsCurrent);

            // Create VBox to hold the symbols
            VBox playerSymbolBox = new VBox(10, playerLabel,
                    playerSRadioButton, playerORadioButton);
            playerSymbolBox.setPadding(new Insets(0, 10, 0, 10));
            // Create VBox to hold the symbols and the player type selections
            VBox playerTypeBox = new VBox(20, playerLabel, humanPlayerRadioButton,
                    playerSymbolBox, computerPlayerRadioButton);
            // Merge the VBoxes together
            playerSymbolsBoxes[i].getChildren().add(playerTypeBox);
            playerSymbolsBoxes[i].setAlignment(Pos.CENTER);
        }

        borderPane.setLeft(playerSymbolsBoxes[0]);
        borderPane.setRight(playerSymbolsBoxes[1]);
    }

    private RadioButton createPlayerTypeRadioButton(Player player, Player.PlayerType playerType,
                                                    ToggleGroup toggleGroup, boolean playerSelected) {
        RadioButton playerTypeRadioButton = new RadioButton(playerType.name());
        playerTypeRadioButton.setUserData(playerType);
        playerTypeRadioButton.setSelected(playerSelected);
        playerTypeRadioButton.setToggleGroup(toggleGroup);

        playerTypeRadioButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                player.setPlayerType((Player.PlayerType) playerTypeRadioButton.getUserData());
                for (int i = 0; i < Game.NO_OF_PLAYERS; i++) {
                    if (game.getPlayers()[i].getPlayerTeamColor() == player.getPlayerTeamColor()) {
                        game.getPlayers()[i] = new ComputerPlayer(game.getPlayers()[i].getPlayerTeamColor());
                    }
                }
            }
        }));

        return playerTypeRadioButton;
    }

    private RadioButton createPlayerRadioButton(Player player, Player.Symbol symbol, ToggleGroup toggleGroup, boolean selected) {
        RadioButton radioButton = new RadioButton(symbol.name());
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setUserData(symbol);
        radioButton.setSelected(selected);

        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                player.setCurrentSymbol((Player.Symbol) radioButton.getUserData());
            }
        });

        return radioButton;
    }

    private void updateFooter() {
        footerHBox = new HBox(50);
        footerHBox.getChildren().clear();
        footerHBox.setAlignment(Pos.CENTER);

        // Record game option
        CheckBox recordGameCheckBox = new CheckBox("Record Game");
        recordGameCheckBox.setSelected(game.isRecordGame());

        recordGameCheckBox.setOnAction(event -> {
            if (recordGameCheckBox.isSelected()) {
                game.setRecordGame(true);
            } else {
                game.setRecordGame(false);
            }
        });

        VBox gameControlButtonsVBox = new VBox(10);
        gameControlButtonsVBox.setAlignment(Pos.CENTER);

        Button startGameButton = createGameControlButton("Start Game", event -> {
            if (modeSelected) {
                game.startGame();
                handleCellClick(-1, -1);
            } else {
                AlertUtil.showAlert(AlertType.WARNING, "Select A Mode", "Please select a Game Mode!");
            }
        });
        Button cancelGameButton = createGameControlButton("Cancel Game", event -> {
            restartGame();
        });

        Label currentPlayerLabel = new Label("Current turn: " + game.getCurrentPlayer().getName());
        gameControlButtonsVBox.getChildren().addAll(startGameButton, cancelGameButton);

        footerHBox.getChildren().addAll(recordGameCheckBox, currentPlayerLabel, gameControlButtonsVBox);
        borderPane.setBottom(footerHBox);
    }

    private Button createGameControlButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    private void restartGame() {
        game = new Game();
        modeSelected = false;
        createHeading();
        createPlayersVBoxes();
        updateGameBoard();
    }

    public class AlertUtil {
        public static void showAlert(AlertType alertType, String title, String message) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}