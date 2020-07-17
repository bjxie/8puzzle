import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private SearchNode solution;

    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previous;

        public SearchNode(Board initial) {
            board = initial;
            moves = 0;
            previous = null;
        }
    }

    private class QueueOrder implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode current, SearchNode next) {
            int thisManhattan = current.board.manhattan() + current.moves;
            int thatManhattan = next.board.manhattan() + next.moves;

            if (thisManhattan < thatManhattan) {
                return -1;
            }
            if (thisManhattan > thatManhattan) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        QueueOrder compare = new QueueOrder();

        MinPQ<SearchNode> queue = new MinPQ<>(compare);
        MinPQ<SearchNode> twinqueue = new MinPQ<>(compare);

        SearchNode first = new SearchNode(initial);
        SearchNode twinfirst = new SearchNode(initial.twin());
        queue.insert(first);
        twinqueue.insert(twinfirst);

        SearchNode removed = queue.delMin();
        SearchNode twinremoved = twinqueue.delMin();


        while (!removed.board.isGoal() && !twinremoved.board.isGoal()) {
            for (Board b : removed.board.neighbors()) {
                if (removed.previous == null || !b.equals(removed.previous.board)) {
                    SearchNode add = new SearchNode(b);
                    add.moves = removed.moves + 1;
                    add.previous = removed;
                    queue.insert(add);
                }
            }

            for (Board b : twinremoved.board.neighbors()) {
                if (twinremoved.previous == null || !b.equals(twinremoved.previous.board)) {
                    SearchNode add = new SearchNode(b);
                    add.moves = twinremoved.moves + 1;
                    add.previous = twinremoved;
                    twinqueue.insert(add);
                }
            }
            removed = queue.delMin();
            twinremoved = twinqueue.delMin();
        }
        if (removed.board.isGoal()) {
            solution = removed;
        } else {
            solution = null;
        }

    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        } else {
            return solution.moves;
        }
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        LinkedList<Board> sol = new LinkedList<>();
        for (SearchNode s = solution; s != null; s = s.previous) {
            sol.addFirst(s.board);
        }
        return sol;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}

