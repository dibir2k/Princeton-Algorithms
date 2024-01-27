import edu.princeton.cs.algs4.MinPQ;
// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Comparator;
import java.util.LinkedList;


public class Solver {
    //private LinkedList<SearchNode> nodeSequence;
    // private LinkedList<SearchNode> nodeSequenceTwin;
    private int movesToSolve;
    private SearchNode finalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Constructor provided with null object.");

        MinPQ<SearchNode> pq;
        MinPQ<SearchNode> pqTwin;

        // 
        // initialBoard = initial;
        movesToSolve = 0;

        SearchNode node = new SearchNode(initial, movesToSolve, null);
        pq = new MinPQ<SearchNode>(node);
        // nodeSequence = new LinkedList<>();
 
        pq.insert(node);
        // Board dqedBoard = null;
        // List<SearchNode> visitedNodes = new ArrayList<>();

        Board twinBoard = initial.twin();
        SearchNode nodeTwin = new SearchNode(twinBoard, movesToSolve, null);
        pqTwin = new MinPQ<SearchNode>(nodeTwin);
        // nodeSequenceTwin = null;
 
        pqTwin.insert(nodeTwin);
        // Board dqedBoardTwin = null;
        // List<SearchNode> visitedNodesTwin = new ArrayList<>();

        boolean found = false;
        boolean foundTwin = false;


        while ((!found && !pq.isEmpty()) && (!foundTwin && !pqTwin.isEmpty())) {

            SearchNode n = solveStep(pq); //, nodeSequence); // visitedNodes,
            SearchNode nTwin = solveStep(pqTwin); // , nodeSequenceTwin);

            found = n.thisBoard.isGoal();
            foundTwin = nTwin.thisBoard.isGoal();

            movesToSolve = n.moves;

            if (found) finalNode = n;
        }
        
    }

    private SearchNode solveStep(MinPQ<SearchNode> pqueue) { //, LinkedList<SearchNode> sequenceOfNodes) { // List<SearchNode> nodesVisited
        SearchNode dqedNode = pqueue.delMin();

        // nodesVisited.add(dqedNode);
        
        Board dqedBoard = dqedNode.thisBoard;
        
        
        // Add board of dequeued node to the board sequence
        // if (sequenceOfNodes != null) sequenceOfNodes.add(dqedNode);

        boolean found = dqedBoard.isGoal();

        Iterable<Board> neighbors = null;

        if (!found) {

            neighbors = dqedBoard.neighbors();
                        
            for (Board b : neighbors) {
                SearchNode prevNode = dqedNode.prevNode;
                if (prevNode == null || !b.equals(prevNode.thisBoard)) {
                    SearchNode newNode = new SearchNode(b, dqedNode.moves+1, dqedNode); 
                    pqueue.insert(newNode);
                    // }
                }
            }
        }
        return dqedNode;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return finalNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return movesToSolve;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        else {
            LinkedList<Board> boardSequence = new LinkedList<>();
            SearchNode prevNode = finalNode.prevNode;
            boardSequence.add(finalNode.thisBoard);
            while (prevNode != null) {
                boardSequence.addFirst(prevNode.thisBoard);
                prevNode = prevNode.prevNode;
            }
            return boardSequence;
        }
    }

    private class SearchNode implements Comparator<SearchNode> {
        private Board thisBoard;
        private int moves;
        private SearchNode prevNode;
        private int priority;

        private SearchNode(Board b, int m, SearchNode node) {
            thisBoard = b;
            moves = m;
            prevNode = node;
            priority = b.manhattan() + moves;
        }

        @Override
        public int compare(SearchNode n1, SearchNode n2) {
            if (n1.priority < n2.priority) return -1;
            else if (n1.priority == n2.priority) return 0;
            else return 1;
        }
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
