import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private int[] boardArr;
    private final int width;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        width = tiles[0].length;
        boardArr = new int[width * width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                boardArr[i * width + j] = tiles[i][j];
            }
        }
    }

    private Board(int[] tiles) {
        width = (int) Math.sqrt(tiles.length);
        this.boardArr = new int[tiles.length];
        System.arraycopy(tiles, 0, this.boardArr, 0, tiles.length);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(width + "\n");
        for (int i = 0; i < boardArr.length; i++) {
            s.append(String.format("%2d ", boardArr[i]));
            if (i != 0 && (i + 1) % width == 0) {
                s.append("\n");
            }
        }
        return s.toString();
    }


    // board dimension n
    public int dimension() {
        return width;
    }


    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < width * width; i++) {
            if (boardArr[i] != i + 1 && boardArr[i] != 0) {
                count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        int val;
        int row;
        int col;
        for (int i = 0; i < width * width; i++) {
            if (boardArr[i] != i + 1 && boardArr[i] != 0) {
                row = Math.abs(((boardArr[i] - 1) / width) - (i / width));
                col = Math.abs(((boardArr[i] - 1) % width) - (i % width));
                val = row + col;
                count += val;
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean goalReached = false;
        int count = 0;
        for (int i = 0; i < width * width; i++) {
            if (boardArr[i] == i + 1) {
                count++;
            }
        }
        if (count == width * width - 1) {
            goalReached = true;
        }
        return goalReached;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (!y.getClass().equals(this.getClass())) {
            return false;
        }
        Board that = (Board) y;
        return (Arrays.equals(this.boardArr, that.boardArr));
    }

    private void exch(Board b, int first, int sec) {
        int hold = b.boardArr[first];
        b.boardArr[first] = b.boardArr[sec];
        b.boardArr[sec] = hold;

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        Board neighbor;
        int index = 0;
        boolean isFound = false;
        for (int i = 0; i < width * width; i++) {
            if (boardArr[i] == 0) {
                index = i;
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            return stack;
        }


        if (index / width != 0) { // check for topmost column
            neighbor = new Board(boardArr);
            exch(neighbor, index, index - width);
            stack.push(neighbor);
        }

        if (index / width + 1 != width) { // check for bottom column
            neighbor = new Board(boardArr);
            exch(neighbor, index, index + width);
            stack.push(neighbor);

        }

        if (index % width != 0) { // check for leftmost column
            neighbor = new Board(boardArr);
            exch(neighbor, index, index - 1);
            stack.push(neighbor);

        }

        if (index % width != width - 1) { // check for rightmost column
            neighbor = new Board(boardArr);
            exch(neighbor, index, index + 1);
            stack.push(neighbor);
        }
        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (width == 1) {
            return null;
        }
        Board twin = new Board(boardArr);
        if (boardArr[0] != 0 && boardArr[1] != 0) {
            exch(twin, 0, 1);
        } else if (boardArr[width] != 0 && boardArr[width + 1] != 0) {
            exch(twin, width, width + 1);
        }
        return twin;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] test = new int[n][n];
        while (!in.isEmpty()) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    test[i][j] = in.readInt();
                }
            }
        }

        Board testBoard = new Board(test);
        System.out.println(testBoard.toString());
        // 5System.out.println(testBoard.neighbors().toString());
        System.out.println(testBoard.twin().toString());

    }
}
