package com.nonogram.puzzle;

import com.nonogram.board.Board;
import com.nonogram.board.CellState;

import java.util.ArrayList;

public class Puzzle {
    private final Board gameBoard;
    private Board solution;
    ArrayList<Integer>[] rowClues;
    ArrayList<Integer>[] columnClues;

    public Puzzle(int rows, int columns) {
        gameBoard = new Board(rows, columns);
        solution = new Board(rows, columns);
        rowClues = new ArrayList[rows];
        columnClues = new ArrayList[columns];
    }

    public Puzzle(Puzzle new_puzzle) throws Exception {
        gameBoard = new Board(new_puzzle.getRows(), new_puzzle.getColumns());
        solution = new Board(new_puzzle.getSolution());
        rowClues = new ArrayList[new_puzzle.getRows()];
        columnClues = new ArrayList[new_puzzle.getColumns()];
        for(int i = 0; i < new_puzzle.getRows(); ++i) {
            rowClues[i] = new ArrayList<>(new_puzzle.getRowClue(i));
        }
        for(int i = 0; i < new_puzzle.getColumns(); ++i) {
            columnClues[i] = new ArrayList<>(new_puzzle.getColumnClue(i));
        }
    }

    public Board getBoard() {
        return new Board(gameBoard);
    }

    public int getRows() {
        return gameBoard.getRows();
    }

    public int getColumns() {
        return gameBoard.getColumns();
    }

    public ArrayList<Integer> getRowClue(int row) throws Exception {
        if(row < 0 || row >= getRows()) {
            throw new Exception("Row clue index out of bounds");
        }
        return new ArrayList<>(rowClues[row]);
    }

    public ArrayList<Integer> getColumnClue(int column) throws Exception {
        if(column < 0 || column >= getColumns()) {
            throw new Exception("Column clue index out of bounds");
        }
        return new ArrayList<>(columnClues[column]);
    }

    public Board getSolution() {
        return new Board(solution);
    }

    public void setRowClues(ArrayList<Integer>[] clues) throws Exception {
        if(clues.length != getRows()) {
            throw new Exception("Mismatched number of row clues");
        }
        for(int i = 0; i < getRows(); ++i) {
            rowClues[i] = new ArrayList<>(clues[i]);
        }
    }

    public void setColumnClues(ArrayList<Integer>[] clues) throws Exception {
        if(clues.length != getColumns()) {
            throw new Exception("Mismatched number of column clues");
        }
        for(int i = 0; i < getRows(); ++i) {
            columnClues[i] = new ArrayList<>(clues[i]);
        }
    }

    public void setRowClue(int row, ArrayList<Integer> clue) throws Exception {
        if(row < 0 || row >= getRows()) {
            throw new Exception("Row clue index out of bounds");
        }
        rowClues[row] = new ArrayList<>(clue);
    }

    public void setColumnClue(int column, ArrayList<Integer> clue) throws Exception {
        if(column < 0 || column >= getColumns()) {
            throw new Exception("Column clue index out of bounds");
        }
        columnClues[column] = new ArrayList<>(clue);
    }

    public void setSolution(Board b) {
        solution = new Board(b);
    }

    // Are the squares filled in so far correct?
    public boolean filledCorrect() {
        for(int i = 0; i < getRows(); ++i) {
            for(int j = 0; j < getColumns(); ++j) {
                if(gameBoard.getState(i, j) == CellState.FILLED) {
                    if(solution.getState(i, j) != CellState.FILLED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /* Is the puzzle filled out fully correctly? */
    public boolean allCorrect() {
        for(int i = 0; i < getRows(); ++i) {
            for(int j = 0; j < getColumns(); ++j) {
                // If solution filled, must be filled
                // If solution empty, must be crossed or empty
                if(solution.getState(i, j) == CellState.FILLED) {
                    if(gameBoard.getState(i, j) != CellState.FILLED) {
                        return false;
                    }
                } else {
                    if(gameBoard.getState(i, j) == CellState.FILLED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
