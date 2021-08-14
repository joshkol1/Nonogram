package com.nonogram.board;

public enum CellState {
    EMPTY {
        public String toString() {
            return "EMPTY  ";
        }
    },
    CROSSED {
        public String toString() {
            return "CROSSED";
        }
    },
    FILLED {
        public String toString() {
            return "FILLED ";
        }
    }
}
