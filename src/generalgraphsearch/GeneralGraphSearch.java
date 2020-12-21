package generalgraphsearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author ozdemirHarun
 */

class Node{
  
    private int pathCost;
    private Node parent;
    private int depth=0;
    int[][] stateInfo = new int[4][4];
    private String path="";
            
    public Node(int[][]stateInfo,Node parent,int pathCost){
        this.stateInfo=stateInfo;
        this.parent=parent;
        this.pathCost=pathCost;
        
        
    }

    public int getDepth() {
        return depth;
    }
    public int getPathCost() {
        return pathCost;
    }
    public void setPathCost(int pathCost) {
        this.depth+=parent.getDepth()+1;
        this.pathCost = pathCost;
    }
    public int[][] getStateInfo() {
        return stateInfo;
    }
    public void setPath(String NewPath){
        this.path+=parent.getPath()+NewPath+" ";
    }
    public String getPath(){
        return this.path;
    }
    
}

public class GeneralGraphSearch {
    
   private static ArrayList <Node> exploredSet;

   private static int[][]goalState={ { 1, 2, 3, 4 },{ 12, 13, 14, 5 },{11, 0, 15, 6 },{ 10, 9, 8, 7}   }; 
   
   private static int x,y,i,j;
    
   private static String graphSearch(Queue<Node> Frontier,int initialState[][],String heuristic){
        Frontier.add(new Node(initialState,null,heuristicSelection(initialState,heuristic)));
        exploredSet=new ArrayList<>(); //Explored Set
        int counter=1;
        while(true){
            if(Frontier.isEmpty())return "FAIL!";
            System.out.println("Path Cost: "+Frontier.peek().getPathCost()+" -- Counter: "+counter++ +"  Depth level: "+Frontier.peek().getDepth());
            System.out.println("Path: "+Frontier.peek().getPath());
            printArray(Frontier.peek().getStateInfo());
            if(compare2dArrays(Frontier.peek().getStateInfo(),goalState)){
                System.out.println("DONE!!!");
                return "SOLUTION";
            }
            exploredSet.add(Frontier.peek());
            findChildNodes(Frontier.poll(),Frontier,exploredSet,heuristic);
        }
        
    }
   
   private static int FirstHeuristic(int[][]stateOfNode){
       //number of miss placed
       int totalNumberOfMissPlacedTiles=0;
       for(int i=0;i<4;i++){
           for(int j=0;j<4;j++){
               if(stateOfNode[i][j]!=goalState[i][j]&&stateOfNode[i][j]!=0){
                   totalNumberOfMissPlacedTiles++;
               }
           }
       }
       return totalNumberOfMissPlacedTiles;
   }
   
   private static int SecondHeuristic(int[][]stateOfNode){
       //city-block distance
       int cityBlockDistance=0;
       for(int a=0;a<4;a++){
           for(int b=0;b<4;b++){
               if(goalState[a][b]!=0){
                 findNumber(goalState[a][b],stateOfNode);
                 cityBlockDistance+=Math.abs(a-i)+Math.abs(b-j);
                 
               }
           }
       }
       return cityBlockDistance;
   }
   
    private static void findNumber(int number,int[][]stateOfNode){
       outerloop: 
       for(i=0;i<4;i++){
           for(j=0;j<4;j++){
               if(stateOfNode[i][j]==number)
                   break outerloop;
               
           }
       }
   }
   

   private static void printArray(int[][] arr){
       for(int x=0;x<4;x++){
           for(int y=0;y<4;y++){
               System.out.print(arr[x][y]+"  ");
           }
           System.out.println("");
       }
       System.out.println("");
   }
   
    private static void findBlank(int[][]state){
       outerloop:
       for(x=0;x<4;x++){
           for(y=0;y<4;y++){
               if(state[x][y]==0)
                   break outerloop;
               
           }
       }
       
   }

    private static boolean compare2dArrays(int[][]arr1,int[][]arr2){
        int equality=0;
        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){
                if(arr1[x][y]==arr2[x][y]){
                    equality++;  
            }
            }
        }
        return equality==16;
    }
   
    private static int[][] merge2dArrays(int[][]arr1,int[][]arr2){
        
        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){
            	arr2[x][y]=arr1[x][y];
            }
        }
        return arr2;
    }
 
    private static int[][] moveOperation(int[][]array,int x1,int y1,int x2,int y2)
    {
    	int[][] arrayNew = new int[4][4];
    	arrayNew=merge2dArrays(array,arrayNew);
        int temp=arrayNew[x2][y2];
        arrayNew[x2][y2]=arrayNew[x1][y1];
        arrayNew[x1][y1]=temp;
       
        return arrayNew;
    }
    
    private static boolean testArrayIndex(int x,int y){
       return x<4 && x>=0 && y<4 && y>=0;
 }
    
    private static boolean loopChecker(Queue<Node> Frontier,Node node,ArrayList<Node>exploredSet){
        boolean exp=true,front=true;
        for(Node element : Frontier) {
            if(compare2dArrays(element.getStateInfo(),node.getStateInfo()))
                front=false;
         }
        for(Node element : exploredSet) {
            if(compare2dArrays(element.getStateInfo(),node.getStateInfo()))
                exp=false;
         } 
        
        return exp&&front;
    }
    
    private static int heuristicSelection(int[][]stateInfo,String heuristic){
       switch (heuristic) {
           case "h1":
               return FirstHeuristic(stateInfo);
           case "h2":
               return SecondHeuristic(stateInfo);
           default:
               return 0;
       }
    } 
   
    private static void findChildNodes(Node node,Queue<Node>Frontier,ArrayList <Node> exploredSet,String heuristic){
        findBlank(node.getStateInfo());
        /*
        fun(x-1,y,node
        */
        if(testArrayIndex(x-1,y)){//NORTH
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x-1,y),node,node.getPathCost()+1);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("N");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
       
        if(testArrayIndex(x+1,y)){//SOUTH
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x+1,y),node,node.getPathCost()+1);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("S");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
       
        if(testArrayIndex(x,y+1)){//EAST
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x,y+1),node,node.getPathCost()+1);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("E");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
        
        if(testArrayIndex(x,y-1)){//WEST                  
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x,y-1),node,node.getPathCost()+1);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("W");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
   
        if(testArrayIndex(x-1,y-1)){//NORTH-WEST                     
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x-1,y-1),node,node.getPathCost()+3);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("NW");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
       
        if(testArrayIndex(x-1,y+1)){//NORTH-EAST                      
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x-1,y+1),node,node.getPathCost()+3);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("NE");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
      
        if(testArrayIndex(x+1,y-1)){//SOUTH-WEST                        
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x+1,y-1),node,node.getPathCost()+3);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("SW");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
      
        if(testArrayIndex(x+1,y+1)){//SOUTH-EAST                       
            Node child=new Node(moveOperation(node.getStateInfo(),x,y,x+1,y+1),node,node.getPathCost()+3);
            child.setPathCost(child.getPathCost()+heuristicSelection(child.getStateInfo(),heuristic));
            child.setPath("SE");
            if(loopChecker(Frontier,child,exploredSet))
                Frontier.add(child);
                    }
 
    }

    
     public static void main(String[] args) {
        Queue<Node> BFS_QUEUE = new LinkedList<>(); 
        Comparator<Node> UCS_pathCost = Comparator.comparing(Node::getPathCost);
        PriorityQueue<Node> UCSandHeuristic_Frontier = new PriorityQueue<>(UCS_pathCost);
        int[][]initialState={   { 1, 13, 3, 4 },{ 12, 11, 2, 5 },{9, 8, 15, 7 },{ 10, 6, 14, 0 }   };
        
        graphSearch(UCSandHeuristic_Frontier,initialState,"h2");
    } 
}
