package com.nonogram.board;

import java.util.ArrayList;

public class Board {
    private final int rows, columns;
    CellState[][] gameBoard;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        gameBoard = new CellState[rows][columns];
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < columns; ++j) {
                gameBoard[i][j] = CellState.EMPTY;
            }
        }
    }

    public Board(Board b2) {
        rows = b2.getRows();
        columns = b2.getColumns();
        gameBoard = new CellState[rows][columns];
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < columns; ++j) {
                gameBoard[i][j] = b2.getState(i, j);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public CellState getState(int i, int j) {
        return gameBoard[i][j];
    }

    public void setState(int i, int j, CellState state) {
        gameBoard[i][j] = state;
    }

    /* Set all cells to empty */
    public void reset() {
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < columns; ++j) {
                gameBoard[i][j] = CellState.EMPTY;
            }
        }
    }

    /* Given a row/column, return a list of filled square blocks */
    public ArrayList<Integer> getFilledGroups(int index, TraversalType t) {
        var squareGroups = new ArrayList<Integer>();
        int filled_group = 0;
        int loop_limit = (t == TraversalType.ROW) ? getColumns() : getRows();
        for(int i = 0; i < loop_limit; ++i) {
            CellState state;
            if(t == TraversalType.ROW) {
                state = getState(index, i);
            } else {
                state = getState(i, index);
            }
            if(state == CellState.FILLED) {
                ++filled_group;
            } else {
                if(filled_group > 0) {
                    squareGroups.add(filled_group);
                    filled_group = 0;
                }
            }
        }
        if(filled_group > 0) {
            squareGroups.add(filled_group);
        }
        return squareGroups;
    }

    public String toString() {
        StringBuilder answer = new StringBuilder();
        answer.append("Board(\n");
        answer.append("\tRows: ").append(getRows()).append("\n");
        answer.append("\tColumns: ").append(getColumns()).append("\n");
        for(int i = 0; i < getRows(); ++i) {
            answer.append("\t");
            for(int j = 0; j < getColumns(); ++j) {
                answer.append(gameBoard[i][j]).append(" ");
            }
            answer.append("\n");
        }
        answer.append(")");
        return answer.toString();
    }

    /* Test state setter & toString() */
    public static void main(String[] args) {
        Board b = new Board(5, 5);
        for(int i = 0; i < b.getRows(); ++i) {
            for(int j = 0; j < b.getColumns(); ++j) {
                b.setState(i, j, CellState.CROSSED);
            }
        }
        System.out.println(b);
    }
}
