package com.nonogram.puzzle;

import com.nonogram.board.Board;
import com.nonogram.board.TraversalType;
import com.nonogram.solver.PuzzleSolver;

public class PuzzleGenerator {
    private final int rows;
    private final int columns;
    private final Board generatedBoard;

    public PuzzleGenerator(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        generatedBoard = new Board(rows, columns);
    }

    public Puzzle generatePuzzle() throws Exception {
        Puzzle puzzle = new Puzzle(rows, columns);
        generatedBoard.reset();
        /* Do below while # solutions is not 1 */
        // TODO: Implement puzzle generation algorithm
        /* Do above while # solutions is not 1 */
        puzzle.setSolution(generatedBoard);
        for(int i = 0; i < rows; ++i) {
            puzzle.setRowClue(i, generatedBoard.getFilledGroups(i, TraversalType.ROW));
        }
        for(int i = 0; i < columns; ++i) {
            puzzle.setColumnClue(i, generatedBoard.getFilledGroups(i, TraversalType.COLUMN));
        }
        return puzzle;
    }

    public static void main(String[] args) throws Exception {
        PuzzleGenerator generator = new PuzzleGenerator(10, 10);
        Puzzle new_puzzle = generator.generatePuzzle();
        System.out.println(new_puzzle.getSolution());
        PuzzleSolver solver = new PuzzleSolver(new_puzzle);
        System.out.println(solver.getSolutions().size()); // This should be 1
    }
}
