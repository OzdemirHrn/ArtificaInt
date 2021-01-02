package generalgraphsearch;

import java.util.*;

/**
 *
 * @author ozdemirHarun
 */
class Node {

    private int pathCost;
    private Node parent;
    private int depth = 0;
    int[][] stateInfo = new int[4][4];
    private String path = "";

    public Node(int[][] stateInfo, Node parent, int pathCost) {
        this.stateInfo = stateInfo;
        this.parent = parent;
        this.pathCost = pathCost;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getStateInfo());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Arrays.deepEquals(this.stateInfo, other.stateInfo)) {
            return false;
        }
        return true;
    }

    public boolean deneme(Queue Frontier,Node node) {
        if (Frontier.contains(node)) {
            return false;
        }
        return true;
    }

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
    HashMap<Integer[][], Integer> hash = new HashMap<>();
    private static ArrayList<Node> exploredSet;
    private static int[][] goalState = {{1, 2, 3, 4}, {12, 13, 14, 5}, {11, 0, 15, 6}, {10, 9, 8, 7}};
    private static int x, y, i, j;

    private static void iterativeDeepening(Queue<Node> Frontier, int initialState[][], String heuristic, double costLimit) {

        int iterativeDepth = 0;
        String solution = "";
        while (!solution.equals("SOLUTION")) {
            solution = graphSearch(Frontier, initialState, heuristic, iterativeDepth, costLimit);
            iterativeDepth++;
        }

    }

    private static void iterativeLengthening(Queue<Node> Frontier, int initialState[][], String heuristic, double depthLimit) {

        int iterativeCost = 0;
        String solution = "";
        while (!solution.equals("SOLUTION")) {
            solution = graphSearch(Frontier, initialState, heuristic, depthLimit, iterativeCost);
            iterativeCost++;
        }

    }

    private static String graphSearch(Queue<Node> Frontier, int initialState[][], String heuristic, double depthLimit, double costLimit) {
        Frontier.add(new Node(initialState, null, heuristicSelection(initialState, heuristic)));
        exploredSet = new ArrayList<>(); //Explored Set
        int counter = 1, expandedNodes = 0;
        while (true) {
            if (Frontier.isEmpty()) {
                return "FAIL!";
            }
            System.out.println("Path Cost: " + Frontier.peek().getPathCost() + " -- Counter: " + counter++ + "  Depth level: " + Frontier.peek().getDepth());
            System.out.println("Path: " + Frontier.peek().getPath());
            printArray(Frontier.peek().getStateInfo());
            if (Arrays.deepEquals(Frontier.peek().getStateInfo(), goalState)) {
                System.out.println("DONE!!!");
                System.out.println("Total number of expanded nodes: " + expandedNodes);
                return "SOLUTION";
            }

            exploredSet.add(Frontier.peek());
            expandedNodes++;
            if (Frontier.peek().getDepth() < depthLimit && Frontier.peek().getPathCost() + 1 <= costLimit) {
                findChildNodes(Frontier.poll(), Frontier, exploredSet, heuristic, costLimit);
            } else {
                Frontier.poll();
            }
        }

    }

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

    private static void printArray(int[][] arr) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                System.out.print(arr[x][y] + "  ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

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

    private static int[][] moveOperation(int[][] array, int x1, int y1, int x2, int y2) {
        int[][] arrayNew = new int[4][4];
        arrayNew = merge2dArrays(array, arrayNew);
        int temp = arrayNew[x2][y2];
        arrayNew[x2][y2] = arrayNew[x1][y1];
        arrayNew[x1][y1] = temp;
        return arrayNew;
    }

    private static boolean testArrayIndex(int x, int y) {
        return x < 4 && x >= 0 && y < 4 && y >= 0;
    }

    /*
   @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node toCompare = (Node) o;
            return Arrays.deepEquals(((Node) o).getStateInfo(),toCompare.getStateInfo());
            //return ((Node) o).getStateInfo().equals(toCompare.getStateInfo());
        }
        return false;
    }
     */
    private static boolean loopChecker(Queue<Node> Frontier, Node node, ArrayList<Node> exploredSet) {
        boolean exp = true, front = true;

        front = node.deneme(Frontier,node);
        
        //front = !Frontier.stream().anyMatch(o -> Arrays.deepEquals(o.getStateInfo(), node.getStateInfo()));
        
        /*
        if (Frontier.contains(node.getStateInfo())) {
            front = false;
        }*/

 /* for (Node element : Frontier) {
           // Frontier.remove(element);
            if (Arrays.deepEquals(element.getStateInfo(), node.getStateInfo())) {
                front = false;
                break;
            }
        }*/
        //explored  sete ekledim costu 40
        /*
        cost 30 gelince explored settekini güncellicek ama frontiere de eklicek!!
         */
        for (Node element : exploredSet) {
            if (Arrays.deepEquals(element.getStateInfo(), node.getStateInfo())) {
                exp = false;
                break;
            }
        }

        return front && exp;
    }

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

    private static void findChildNodes(Node node, Queue<Node> Frontier, ArrayList<Node> exploredSet, String heuristic, double costLimit) {
        findBlank(node.getStateInfo());
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x - 1, y, "N");
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x + 1, y, "S");
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x, y + 1, "E");
        childNodeForEachNode(node, Frontier, exploredSet, heuristic, 1, x, y - 1, "W");
        if (node.getPathCost() + 3 < costLimit) {
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x - 1, y - 1, "NW");
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x - 1, y + 1, "NE");
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x + 1, y - 1, "SW");
            childNodeForEachNode(node, Frontier, exploredSet, heuristic, 3, x + 1, y + 1, "SE");
        }
    }

    public static void main(String[] args) {
        //stack
        Queue<Node> DFS_QUEUE = Collections.asLifoQueue(new LinkedList<Node>());
        //fifo
        Queue<Node> BFS_QUEUE = new LinkedList<>();

        Comparator<Node> UCS_pathCost = Comparator.comparing(Node::getPathCost);
        PriorityQueue<Node> UCSandHeuristic_Frontier = new PriorityQueue<>(UCS_pathCost);
        int[][] initialState = {{ 1, 3, 5, 4 },{ 2, 13, 14, 15 },{11, 12, 9, 6 },{ 0, 10, 8, 7 }};

        double inf = Double.POSITIVE_INFINITY;

        /*
        -iterativelere bakmak lazım. test etmek lazım
        counterları sıfırlanıyr. En baştan outputa bakarsanız anlarsınız. Global falan yapılabilir counter değişkeni.
        -ILS'de başta aynı cost levela 3 kez bakıyor.
        loop var gibi ona baklılır. Sebebine bakamadım çok.***BUNU YAPTIM AMA YİNE KONTROL ETMEKTE FAYDA VAR. IDS İÇİN DE***
        -Heuristiclerde path costlar yanlış hesaplanıyor. önceki heuristic değerlerinin falan
        çıkartılması gerekiyor galiba pathcost hesaplarken. bi kere test etseniz direkt anlarsnız zaten.
        -Iterativeleri çok test etmedim...
        -ILS DOĞRU MU? EXPLORED SET FALAN SIFIRLANCAKTI FRONTİER FALAN HER İTERASYONDA
        
         */
        graphSearch(BFS_QUEUE, initialState, "", inf, inf);

        //iterativeDeepening(DFS_QUEUE, initialState, "",inf);
        //iterativeLengthening(UCSandHeuristic_Frontier, initialState, "", inf);
    }

}
