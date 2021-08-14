package com.nonogram.solver;

import com.nonogram.board.Board;
import com.nonogram.board.CellState;
import com.nonogram.board.TraversalType;
import com.nonogram.puzzle.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;

public class PuzzleSolver {
    private final Puzzle puzzle;
    private final Board searchBoard;
    private final ArrayList<Board> solutions;
    private boolean solved;

    public PuzzleSolver(Puzzle puzzle) throws Exception {
        this.puzzle = new Puzzle(puzzle);
        searchBoard = new Board(puzzle.getRows(), puzzle.getColumns());
        solutions = new ArrayList<>();
        solved = false;
    }

    public boolean isSolved() {
        return solved;
    }

    public ArrayList<Board> getSolutions() throws Exception {
        if(!isSolved()) {
            throw new Exception("Puzzle has not been solved yet");
        }
        return solutions;
    }

    public void solve() throws Exception {
        search(0, 0);
        solved = true;
    }

    /* Use DFS with backtracking, stop search when bad state hit
    *  For each cell, try either leaving it blank & filling it in. Go to next cell
    *  If end of puzzle is reached, we have a solution, add it to "solutions"
    * */
    private void search(int row, int column) throws Exception {
        if(row >= puzzle.getRows()) {
            if(checkFinalPuzzle()) {
                solutions.add(new Board(searchBoard));
            }
            return;
        }
        searchBoard.setState(row, column, CellState.EMPTY);
        if(rowOkay(row, column) && columnOkay(row, column)) {
            if(column >= puzzle.getColumns()-1) {
                search(row+1, 0);
            } else {
                search(row, column+1);
            }
        }
        searchBoard.setState(row, column, CellState.FILLED);
        if(rowOkay(row, column) && columnOkay(row, column)) {
            if(column >= puzzle.getColumns()-1) {
                search(row+1, 0);
            } else {
                search(row, column+1);
            }
        }
        searchBoard.setState(row, column, CellState.EMPTY);
    }

    /* Check the solution we have generated against the clues. */
    private boolean checkFinalPuzzle() throws Exception {
        for(int i = 0; i < puzzle.getColumns(); ++i) {
            var clues = puzzle.getColumnClue(i);
            var solver_col = searchBoard.getFilledGroups(i, TraversalType.COLUMN);
            if(clues.size() != solver_col.size()) {
                return false;
            }
            for(int j = 0; j < clues.size(); ++j) {
                if(!clues.get(j).equals(solver_col.get(j))) {
                    return false;
                }
            }
        }
        for(int i = 0; i < puzzle.getRows(); ++i) {
            var clues = puzzle.getRowClue(i);
            var solver_row = searchBoard.getFilledGroups(i, TraversalType.ROW);
            if(clues.size() != solver_row.size()) {
                return false;
            }
            for(int j = 0; j < clues.size(); ++j) {
                if(!clues.get(j).equals(solver_row.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /* Can the row be filled out according to the clue? */
    private boolean rowOkay(int row, int column) throws Exception {
        var clue = puzzle.getRowClue(row);
        var filledGroups = searchBoard.getFilledGroups(row, TraversalType.ROW);
        if(filledGroups.size() > clue.size()) {
            return false;
        }
        int availableSquares = puzzle.getColumns()-column-1;
        int tilesRequired;
        if(filledGroups.size() == 0) {
            tilesRequired = clue.size()-1;
            for(int s : clue) {
                tilesRequired += s;
            }
        } else {
            int index = filledGroups.size()-1;
            for(int i = 0; i < index; ++i) {
                if(!filledGroups.get(i).equals(clue.get(i))) {
                    return false;
                }
            }
            if(filledGroups.get(index) > clue.get(index)) {
                return false;
            }
            if(searchBoard.getState(row, column) == CellState.EMPTY && !filledGroups.get(index).equals(clue.get(index))) {
                return false;
            }
            tilesRequired = clue.get(index)-filledGroups.get(index);
            tilesRequired += clue.size()-filledGroups.size();
            if(searchBoard.getState(row, column) == CellState.EMPTY) {
                --tilesRequired;
            }
            for(int i = index+1; i < clue.size(); ++i) {
                tilesRequired += clue.get(i);
            }
        }
        return availableSquares >= tilesRequired;
    }

    /* Can the column be filled out according to the clue? */
    private boolean columnOkay(int row, int column) throws Exception {
        ArrayList<Integer> clue = puzzle.getColumnClue(column);
        var filledGroups = searchBoard.getFilledGroups(column, TraversalType.COLUMN);
        if(filledGroups.size() > clue.size()) {
            return false;
        }
        int availableSquares = puzzle.getRows()-row-1;
        int tilesRequired;
        if(filledGroups.size() == 0) {
            tilesRequired = clue.size()-1;
            for(int s : clue) {
                tilesRequired += s;
            }
        } else {
            int index = filledGroups.size()-1;
            if(filledGroups.get(index) > clue.get(index)) {
                return false;
            }
            if(searchBoard.getState(row, column) == CellState.EMPTY && !filledGroups.get(index).equals(clue.get(index))) {
                return false;
            }
            tilesRequired = clue.get(index)-filledGroups.get(index);
            tilesRequired += clue.size()-filledGroups.size();
            if(searchBoard.getState(row, column) == CellState.EMPTY) {
                --tilesRequired;
            }
            for(int i = index+1; i < clue.size(); ++i) {
                tilesRequired += clue.get(i);
            }
        }
        return availableSquares >= tilesRequired;
    }

    /* Test the solver with a manually inputted puzzle. Should generate unique solution */
    public static void main(String[] args) throws Exception {
        Puzzle puzzle = new Puzzle(10, 10);
        ArrayList<Integer>[] row_clues = new ArrayList[puzzle.getRows()];
        ArrayList<Integer>[] column_clues = new ArrayList[puzzle.getColumns()];
        Integer[][] r_clues = {
                {1, 1, 2}, {2, 1, 3}, {2, 3}, {2, 5}, {1, 1},
                {7}, {3, 1}, {5}, {4}, {6}
        };
        Integer[][] c_clues = {
                {3, 1}, {7}, {3, 1}, {5}, {1, 3},
                {2, 1, 5}, {3, 3}, {3, 1}, {4}, {4}
        };
        for(int i = 0; i < r_clues.length; ++i) {
            row_clues[i] = new ArrayList<>(Arrays.asList(r_clues[i]));
        }
        for(int i = 0; i < c_clues.length; ++i) {
            column_clues[i] = new ArrayList<>(Arrays.asList(c_clues[i]));
        }
        puzzle.setRowClues(row_clues);
        puzzle.setColumnClues(column_clues);
        PuzzleSolver solver = new PuzzleSolver(puzzle);
        solver.solve();
        for(Board b : solver.getSolutions()) {
            System.out.println(b);
        }
    }
}
