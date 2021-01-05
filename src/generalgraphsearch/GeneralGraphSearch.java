package generalgraphsearch;

import java.util.*;

/**
 *
 * @author ozdemirHarun
 */
class Node {

    /*
    This class represents Nodes in 15 Puzzle Game.
    We keep pathCost, Parent node as a reference of child, State Information vs.
     */
    private int pathCost;
    private Node parent;
    private int depth = 0;
    //State Information in 4x4 array
    private int[][] stateInfo = new int[4][4];
    //Path of current node
    private String path = "";

    public Node(int[][] stateInfo, Node parent, int pathCost) {
        this.stateInfo = stateInfo;
        this.parent = parent;
        this.pathCost = pathCost;
    }

    /*
    These all are set and get methods.
     */
    public int getDepth() {
        return depth;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.depth += parent.getDepth() + 1;
        this.pathCost = pathCost;
    }

    public int[][] getStateInfo() {
        return stateInfo;
    }

    public void setPath(String NewPath) {
        this.path += parent.getPath() + NewPath + " ";
    }

    public String getPath() {
        return this.path;
    }

    public Node getParent() {
        return parent;
    }
}

public class GeneralGraphSearch {

    /*
    Initializing expanded node, exploredSet, goalState and some coordinate
    values.
     */
    private static int counter = 0, expandedNodes = 0;
    private static ArrayList<Node> exploredSet;
    private static int[][] goalState = {{1, 2, 3, 4}, {12, 13, 14, 5}, {11, 0, 15, 6}, {10, 9, 8, 7}};
    private static int x, y, i, j;

    /*
    Iterative Deepening funtion.
    Argurments as same as Graph Search funtion because we are calling 
    graph search function iteratively.
     */
    private static void iterativeDeepening(Queue<Node> Frontier, int initialState[][], String heuristic, double costLimit) {

        int iterativeDepth = 0;
        String solution = "";
        //If graph search returns solution then return
        while (!solution.equals("SOLUTION")) {
            solution = graphSearch(Frontier, initialState, heuristic, iterativeDepth, costLimit);
            Frontier.clear();
            //Increase depth limit by 1
            iterativeDepth++;
        }

    }

    /*
    Iterative Lengthening function.
    This function also have same arguments as graph search
    However this time we are increasing cost iteratively.
     */
    private static void iterativeLengthening(Queue<Node> Frontier, int initialState[][], String heuristic, double depthLimit) {

        int iterativeCost = 0;
        String solution = "";
        while (!solution.equals("SOLUTION")) {
            solution = graphSearch(Frontier, initialState, heuristic, depthLimit, iterativeCost);
            iterativeCost++;
        }

    }

    /*
    This is The One!
    This is The Choosen One!
    This is THE GRAPH SEARCH!!!
     */
    private static String graphSearch(Queue<Node> Frontier, int initialState[][], String heuristic, double depthLimit, double costLimit) {
        //First node of frontier is initial state.
        Frontier.add(new Node(initialState, null, heuristicSelection(initialState, heuristic)));
        exploredSet = new ArrayList<>(); //Explored Set
        exploredSet.clear();

        while (true) {
            //If frontier is empty then return fail!
            if (Frontier.isEmpty()) {
                return "FAIL!";
            }
            /*
            These statements is just for print the status of informations.
             */
            System.out.println("Path Cost: " + Frontier.peek().getPathCost() + " -- Counter: " + counter++ + "  Depth level: " + Frontier.peek().getDepth());
            System.out.println("Path: " + Frontier.peek().getPath());
            System.out.println("Frontier Size: " + Frontier.size());
            printArray(Frontier.peek().getStateInfo());
            /*
            If this node have goalState the return solution and expanded nodes.
             */
            if (Arrays.deepEquals(Frontier.peek().getStateInfo(), goalState)) {
                System.out.println("DONE!!!");
                System.out.println("Total number of expanded nodes: " + expandedNodes);
                return "SOLUTION";
            }
            //Add the node to explored set.
            exploredSet.add(Frontier.peek());
            expandedNodes++;
            //This conditions just for iterative algorithms.
            if (Frontier.peek().getDepth() < depthLimit && Frontier.peek().getPathCost() + 1 <= costLimit) {
                //Find child nodes of current node.
                findChildNodes(Frontier.poll(), Frontier, exploredSet, heuristic, costLimit);
            } else {
                //Remove the head element from Frontier.
                Frontier.poll();
            }
        }

    }

    /*
    Heuristic funntion h1.
    The number of misplaced tiles at node n
     */
    private static int FirstHeuristic(int[][] stateOfNode) {
        //number of miss placed
        int totalNumberOfMissPlacedTiles = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (stateOfNode[i][j] != goalState[i][j] && stateOfNode[i][j] != 0) {
                    totalNumberOfMissPlacedTiles++;
                }
            }
        }
        return totalNumberOfMissPlacedTiles;
    }

    /*
    Heuristic funntion h1.
    The sum of the city-block distances of each misplaced tile from its current 
    location to its goal location.
     */
    private static int SecondHeuristic(int[][] stateOfNode) {
        //city-block distance
        int cityBlockDistance = 0;
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (goalState[a][b] != 0) {
                    findNumber(goalState[a][b], stateOfNode);
                    cityBlockDistance += Math.abs(a - i) + Math.abs(b - j);
                }
            }
        }
        return cityBlockDistance;
    }

    /*
    Find specific number from de state information for heruristic calculations.
     */
    private static void findNumber(int number, int[][] stateOfNode) {
        outerloop:
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (stateOfNode[i][j] == number) {
                    break outerloop;
                }
            }
        }
    }

    /*
    Print array function.
    It prints state information for every current nodes.
     */
    private static void printArray(int[][] arr) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                System.out.print(arr[x][y] + "  ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /*
    It finds 0 Node for move operations.
     */
    private static void findBlank(int[][] state) {
        outerloop:
        for (x = 0; x < 4; x++) {
            for (y = 0; y < 4; y++) {
                if (state[x][y] == 0) {
                    break outerloop;
                }
            }
        }
    }

    private static int[][] merge2dArrays(int[][] arr1, int[][] arr2) {

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                arr2[x][y] = arr1[x][y];
            }
        }
        return arr2;
    }

    /*
    Change State information depends on move operation.
     */
    private static int[][] moveOperation(int[][] array, int x1, int y1, int x2, int y2) {
        int[][] arrayNew = new int[4][4];
        arrayNew = merge2dArrays(array, arrayNew);
        int temp = arrayNew[x2][y2];
        arrayNew[x2][y2] = arrayNew[x1][y1];
        arrayNew[x1][y1] = temp;
        return arrayNew;
    }

    /*
    If this movement is legal then return true.
     */
    private static boolean testArrayIndex(int x, int y) {
        return x < 4 && x >= 0 && y < 4 && y >= 0;
    }

    /*
    Loop Checker funtion.
    If child node alread exists in Frontier or explored set then do not add
    this node to Frontier or explored.
    If coming child node already exists in Frontier but it's path cost smaller than
    existed one then remove old node and add new child to the Frontier.
     */
    private static boolean loopChecker(Queue<Node> Frontier, Node node, ArrayList<Node> exploredSet) {
        boolean exp = true, front = true;

        int i = 0;
        int j = 0;
        for (Node element : exploredSet) {
            if (Arrays.deepEquals(element.getStateInfo(), node.getStateInfo())) {
                if (element.getPathCost() > node.getPathCost()) {
                    j++;
                }
                exp = false;

            }
        }

        for (Node element : Frontier) {

            if (Arrays.deepEquals(element.getStateInfo(), node.getStateInfo())) {
                if (element.getPathCost() > node.getPathCost()) {
                    i++;
                }
                front = false;

            }
        }
        //these if elses are path cost comparisons
        if (j > 0) {//if node is in the explored set and 
            if (i > 0) {//if node is frontier list node's path cost smaller than inside's node's path cost. Remove the bigger path cost and add the smaller path cost node
                Frontier.removeIf(element -> Arrays.deepEquals(element.getStateInfo(), node.getStateInfo()) && element.getPathCost() > node.getPathCost());
                return true;
            }
        } else if (j > 0) {////if node is in the explored set and path cost smaller 
            if (i == 0) {//if node is not the frontier list do nothing 

                return true;
            }

        } else if (j == 0) {//if node is not in the explored set and 
            if (i > 0) {//its in the frontier list and node's path cost smaller than inside's node's path cost. Remove the bigger path cost and add the smaller path cost node
                Frontier.removeIf(element -> Arrays.deepEquals(element.getStateInfo(), node.getStateInfo()) && element.getPathCost() > node.getPathCost());
                return true;
            }
        }
        return exp && front;
    }

    /*
    Select heuristic funtion.
    If desired algorithm is not A*star then return 0 value for heuristic.
     */
    private static int heuristicSelection(int[][] stateInfo, String heuristic) {
        switch (heuristic) {
            case "h1":
                return FirstHeuristic(stateInfo);
            case "h2":
                return SecondHeuristic(stateInfo);
            default:
                return 0;
        }
    }

    /*
    If testArrayIndex function and loopchecker function return true then
    add child node to the Frontier.
     */
    private static void childNodeForEachNode(Node node, Queue<Node> Frontier, ArrayList<Node> exploredSet, String heuristic,
            int cost, int arrx, int arry, String moveDir) {
        if (testArrayIndex(arrx, arry)) {//NORTH
            Node child = new Node(moveOperation(node.getStateInfo(), x, y, arrx, arry), node, node.getPathCost() + cost);
            child.setPathCost(child.getPathCost() + heuristicSelection(child.getStateInfo(), heuristic) - heuristicSelection(node.getStateInfo(), heuristic));
            child.setPath(moveDir);
            if (loopChecker(Frontier, child, exploredSet)) {
                Frontier.add(child);
            }
        }
    }

    /*
    Find child nodes of current node and test these movements are legal or not
    then call childNodeForEachNode function.
     */
    private static void findChildNodes(Node node, Queue<Node> Frontier, ArrayList<Node> exploredSet, String heuristic, double costLimit) {
        findBlank(node.getStateInfo());
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x - 1, y, "N");
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x + 1, y, "S");
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x, y + 1, "E");
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x, y - 1, "W");
        //This statement just for iterative lengthening function.
        if (node.getPathCost() + 3 < costLimit) {
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x - 1, y - 1, "NW");
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x - 1, y + 1, "NE");
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x + 1, y - 1, "SW");
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x + 1, y + 1, "SE");
        }
    }

    public static void main(String[] args) {
        //DFS -> LIFO queue.
        Queue<Node> DFS_QUEUE = Collections.asLifoQueue(new LinkedList<>());
        //BFS -> FIFO queue.
        Queue<Node> BFS_QUEUE = new LinkedList<>();
        /*
        UCS -> Priority queue.
        Comparator, compare all nodes in Frontier queue according to their path costs.
         */
        Comparator<Node> UCS_pathCost = Comparator.comparing(Node::getPathCost);
        PriorityQueue<Node> UCSandHeuristic_Frontier = new PriorityQueue<>(UCS_pathCost);

        //Initial State
        int[][] initialState = { { 1, 13, 3, 4 },{ 12, 11, 2, 5 },{9, 8, 15, 7 },{ 10, 6, 14, 0 }  };
        //Infinity number just for algorithms that are non-iterative
        double inf = Double.POSITIVE_INFINITY;

        /*
        
        Parameters for all of the Algorithms.
        
        --BFS--> graphSearch(BFS_QUEUE, initialState, "", inf, inf);
        --UCS--> graphSearch(UCSandHeuristic_Frontier, initialState, "", inf, inf);
        --DFS--> graphSearch(DFS_QUEUE, initialState, "", inf, inf);
        --IDS--> iterativeDeepening(DFS_QUEUE, initialState, "",inf);
        --ILS--> iterativeLengthening(UCSandHeuristic_Frontier, initialState, "", inf);
        --A* h1--> graphSearch(UCSandHeuristic_Frontier, initialState, "h1", inf, inf);
        --A* h2--> graphSearch(UCSandHeuristic_Frontier, initialState, "h2", inf, inf);
        
         */
        graphSearch(UCSandHeuristic_Frontier, initialState, "h1", inf, inf);

    }

}
