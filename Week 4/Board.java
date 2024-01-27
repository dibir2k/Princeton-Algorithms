import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] board;
    private int n;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        board = new int[n][n];
        // board = tiles.clone();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder boardScheme = new StringBuilder();
        boardScheme.append(Integer.toString(n) + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String str = Integer.toString(board[i][j]) + "\t";
                boardScheme.append(str); 
            }
            boardScheme.append("\n");
        }
        return boardScheme.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int misplaced = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n-1 && j == n-1) continue;
                int correctTile = n * i + j + 1;
                if (board[i][j] != correctTile) misplaced++;
            }
        }
        return misplaced;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int movesNeeded = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) continue;
                int iCorrect = (board[i][j] - 1) / n;
                int jCorrect = (board[i][j] - 1) % n;
                movesNeeded += Math.abs(jCorrect - j) + Math.abs(iCorrect - i);
            }
        }
        return movesNeeded;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board yBoard = (Board) y;
        return (this.n == yBoard.n && Arrays.deepEquals(this.board, yBoard.board)); 
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbours = new ArrayList<Board>();
        int i = 0;
        int j = 0;
        for (int k = 0; k < n; k++) {
            for (int s = 0; s < n; s++) {
                if (board[k][s] == 0) {
                    i = k;
                    j = s;
                }
            }
        }

        if (i != n-1) {
            Board down = new Board(board);
            down.board[i][j] = down.board[i+1][j];
            down.board[i+1][j] = 0;
            neighbours.add(down);
        }
        if (i != 0) {
            Board up = new Board(board);
            up.board[i][j] = up.board[i-1][j];
            up.board[i-1][j] = 0;
            neighbours.add(up);
        }
        if (j != n-1) {
            Board left = new Board(board);
            left.board[i][j] = left.board[i][j+1];
            left.board[i][j+1] = 0;
            neighbours.add(left);
        }
        if (j != 0) {
            Board right = new Board(board);
            right.board[i][j] = right.board[i][j-1];
            right.board[i][j-1] = 0;
            neighbours.add(right);
        }     
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int tile;
        Board twin = new Board(board);

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < n-1; col++) {
                tile = twin.board[row][col];
                if (tile != 0 && twin.board[row][col+1] != 0) {
                    twin.board[row][col] = twin.board[row][col+1];
                    twin.board[row][col+1] = tile;
                    return twin;
                }
            }
        }
        
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int n = 4;
        int[][] tiles = new int[n][n];
        int[][] tilesTwo = new int[n][n];
        int[] numbers = new int[n*n];
        for (int k = 0; k < n*n; k++) {
            numbers[k] = k;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tilesTwo[i][j] = numbers[n * i + j];
            }
        }

        StdRandom.shuffle(numbers);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = numbers[n * i + j];
            }
        }
        Board myBoard = new Board(tiles);

        Board secondBoard = new Board(tilesTwo);

        Board copyBoard = new Board(tiles);

        Board twinBoard = myBoard.twin();

        Board twinBoard2 = myBoard.twin();

        StdOut.println(myBoard.toString());

        StdOut.printf("The board has dimension: %d\n", myBoard.dimension());

        StdOut.printf("The Hamming distance is: %d\n", myBoard.hamming());

        StdOut.printf("The Manhattan distance is: %d\n", myBoard.manhattan());

        StdOut.printf("The board is goal: %b\n", myBoard.isGoal());

        StdOut.printf("The boards are equal: %b\n", myBoard.equals(myBoard));

        StdOut.printf("The boards are equal: %b\n", myBoard.equals(myBoard));

        StdOut.printf("The boards are equal: %b\n", myBoard.equals(secondBoard));

        StdOut.printf("The boards are equal: %b\n", myBoard.equals(copyBoard));

        StdOut.println("The neighbours are: \n");
        for (Board b : myBoard.neighbors()) {
            StdOut.println(b.toString());
        }

        StdOut.printf("Twin board is \n%s", twinBoard.toString());

        StdOut.printf("Twin board is \n%s", twinBoard2.toString());
    }

}
