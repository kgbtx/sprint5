package com.example.cs449project;

import javafx.scene.paint.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Game {
    final static int NO_OF_PLAYERS = 2;
    static int INITIAL_BOARD_SIZE = 4;
    final static int MIN_BOARD_SIZE = 3;
    final static int MAX_BOARD_SIZE = 12;

    // Game record file
    private boolean recordGame;

    private RecordGame gameRecord;


    public enum GameMode {
        Simple,
        General
    }
    private Player[] players;
    private Player currentPlayer;
    private GameMode gameMode = GameMode.Simple;
    private int boardSize = INITIAL_BOARD_SIZE;
    protected Cell[][] board;
    private boolean isGameStarted = false;

    protected Set<SOSCombination> sosCombinations;

    public Game() {
        this(Player.PlayerType.Human, Player.PlayerType.Human);
    }

    public Game(Player.PlayerType playerType1, Player.PlayerType playerType2) {
        // Initialize players - set player type to human by default
        players = new Player[NO_OF_PLAYERS];
        players[0] = new Player(Player.PlayerTeamColor.Blue, playerType1);
        players[1] = new Player(Player.PlayerTeamColor.Red, playerType2);
        currentPlayer = players[0];
        gameMode = getGameMode();
        boardSize = getBoardSize();
        board = new Cell[boardSize][boardSize];
        sosCombinations = new HashSet<>();
        recordGame = false;
    }
    public void startGame() {
        isGameStarted = true;
        // Log game start
        if (recordGame) {
            gameRecord = new RecordGame();
            gameRecord.record("Game Started");
        }
        computerPlay();
    }

    public void setGameMode(GameMode type) {
        gameMode = type;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Player[] getPlayers() {
        return players;
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        if (boardSize >= MIN_BOARD_SIZE && boardSize <= MAX_BOARD_SIZE) {
            this.boardSize = boardSize;
            this.board = new Cell[boardSize][boardSize];
        }
    }

    /**
     * isValidMove checks if a cell (row, col) is a valid location to place a symbol
     * By checking if it already has a symbol or is out of the board size bounds
     *
     * @param row the cell row index
     * @param col the cell column index
     * @return true if cell is valid to place symbol, false otherwise
     */
    public boolean isValidMove(int row, int col) {
        boolean valid = false;
        if (row >= 0 && row < this.boardSize && col >= 0 && col < this.boardSize) {
            valid = this.board[row][col] == null;
        }
        return valid;
    }

    public void changeCurrentPlayer() {
        this.currentPlayer = this.currentPlayer == players[0] ? players[1] : players[0];
        if (isRecordGame()) {
            gameRecord.record("Current player is " + this.currentPlayer.getFullName());
        }
    }

    /**
     * Simulates a computer player making a move in the game.
     * The player selects a symbol at random, and then selects a random cell to place the symbol.
     * The player keeps trying until they get a valid cell to place a symbol
     *
     * @return true if player is able to play, false otherwise
     */
    protected boolean computerPlay() {
        if (currentPlayer.getPlayerType() == Player.PlayerType.Computer) {
            Random random = new Random();
            // Select symbol randomly
            int rand = random.nextInt(2);
            if (rand == 0) {
                currentPlayer.setCurrentSymbol(Player.Symbol.S);
            } else {
                currentPlayer.setCurrentSymbol(Player.Symbol.O);
            }
            // Make random move by selecting a random cell
            int row = -1, col = -1;
            do {
                // Check if all cells are filled to avoid endless loop
                if (allCellsFilled()) break;
                row = random.nextInt(boardSize);
                col = random.nextInt(boardSize);
            } while (!isValidMove(row, col)); // Keep selecting until cell is valid
            // Make the move
            setCellState(row, col);
            // Check if game has been won
            return gameOver();
        }
        return false;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public Cell getCellState(int row, int col) {
        if (this.board[row][col] != null) {
            return this.board[row][col];
        }
        return null;
    }

    public boolean setCellState(int row, int col) {
        if (isValidMove(row,col)) {
            this.board[row][col] = new Cell();
            this.board[row][col].setSymbol(Player.Symbol.valueOf(currentPlayer.getCurrentSymbol().name()));
            if (isRecordGame()) {
                gameRecord.record(getCurrentPlayer().getFullName() + " played " + getCurrentPlayer().getCurrentSymbol() + " at (" + row + "," + col + ")");
            }
            return true;
        }
        return false;
    }


    /**
     * This function traverses the whole board checking for new SOS formed after a move.
     * SOS formed are added to the hashset sosCombinations. If a combination of cells already
     * exists in the hashset, then that is treated as an already accounted for SOS formation.
     *
     * @return true if a new SOS has been formed, false otherwise
     */
    public boolean checkSOS() {
        Color color = currentPlayer.getPlayerColor();
        boolean newSOS = false;
        if (isGameStarted()) {
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    int x1 = row, y1 = col;
                    Cell x = getCellState(x1, y1);
                    String symbol;
                    if (x != null) {
                        symbol = x.getSymbol().toString();

                        if (symbol != null) {
                            int x2, y2, x3, y3;
                            // Check for SOS horizontally to the right
                            if (y1 + 2 < getBoardSize()) {
                                x2 = x1; y2 = y1 + 1;
                                x3 = x1; y3 = y1 + 2;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (!sosCombinations.contains(sos)) {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.VERTICAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.VERTICAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.VERTICAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                            if (isRecordGame()) {
                                                gameRecord.record(getCurrentPlayer().getFullName() + " new SOS");
                                            }
                                        }
                                    }
                                }
                            }

                            // Check for SOS vertically down
                            if (x1 + 2 < getBoardSize()) {
                                x2 = x1 + 1; y2 = y1;
                                x3 = x1 + 2; y3 = y1;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (!sosCombinations.contains(sos)) {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.HORIZONTAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.HORIZONTAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.HORIZONTAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                            if (isRecordGame()) {
                                                gameRecord.record(getCurrentPlayer().getFullName() + " new SOS");
                                            }
                                        }
                                    }
                                }
                            }

                            // Check for SOS diagonally (top-left to bottom-right)
                            if (x1 + 2 < getBoardSize() && y1 + 2 < getBoardSize()) {
                                x2 = x1 + 1; y2 = y1 + 1;
                                x3 = x1 + 2; y3 = y1 + 2;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (!sosCombinations.contains(sos)) {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.TOPDOWNDIAGONAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.TOPDOWNDIAGONAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.TOPDOWNDIAGONAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                            if (isRecordGame()) {
                                                gameRecord.record(getCurrentPlayer().getFullName() + " new SOS");
                                            }
                                        }
                                    }
                                }
                            }


                            // Check for SOS diagonally (top-right to bottom-left)
                            if (x1 + 2 < getBoardSize() && y1 - 2 >= 0) {
                                x2 = x1 + 1; y2 = y1 - 1;
                                x3 = x1 + 2; y3 = y1 - 2;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (!sosCombinations.contains(sos)) {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.BOTTOMUPDIAGONAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.BOTTOMUPDIAGONAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.BOTTOMUPDIAGONAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                            if (isRecordGame()) {
                                                gameRecord.record(getCurrentPlayer().getFullName() + " new SOS");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // No SOS formation found around the given cell
        return newSOS;
    }

    /**
     * Checks if the game is over.
     *      A Simple Game is over if SOS has been formed or all cells have been filled.
     *      A General Game is over if all cells have been filled.
     * @return true if game is over, false otherwise
     */
    public boolean gameOver() {
        return false;
    }

    /**
     * This function checks which player won the game.
     *
     * @return The winning Player, otherwise null to represent a draw.
     */
    public Player getWinner() {
        Player winner;
        if (players[0].getNoOfSOS() > players[1].getNoOfSOS()) {
            winner = players[0];
        } else if (players[0].getNoOfSOS() < players[1].getNoOfSOS()) {
            winner = players[1];
        } else {
            winner = null;
        }
        return winner;
    }

    /**
     * Checks if all cells in the game have been filled
     * @return true if all cells filled, false otherwise
     */
    public boolean allCellsFilled() {
        for (int i = 0; i < getBoardSize(); i++) {
            for (int j = 0; j < getBoardSize(); j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isRecordGame() {
        return recordGame;
    }

    public void setRecordGame(boolean recordGame) {
        this.recordGame = recordGame;
    }

    public RecordGame getGameRecord() {
        return gameRecord;
    }

    public void setGameRecord(RecordGame gameRecord) {
        this.gameRecord = gameRecord;
    }
}

