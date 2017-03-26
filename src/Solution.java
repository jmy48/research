/**
 * Created by Joshua on 2/10/17.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class Solution {
    public UndirectedGraph<Integer, DefaultEdge> board;
    public int xDim;
    public int yDim;

    public ArrayList<Integer> start;
    public ArrayList<Integer> finish;

    public ArrayList<Integer> blackDots;
    public ArrayList<Cell> cells;

    public Solution(String args[]) throws IOException {
         UndirectedGraph<Integer, DefaultEdge> gr =
                 new SimpleGraph<>(DefaultEdge.class);

        String filename = args[0];
        BufferedReader reader = Files.newBufferedReader(Paths.get(filename));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" "); //split on whitespace

            if (tokens[0].charAt(0) == '#') { // Comment
                continue;
            }
            else if(tokens[0].equals("v")){ //vertex/edge positions
                for(String s: tokens){
                    if(s.equals("v")){
                        continue;
                    }
                    int first =  Integer.parseInt(s.substring(0, s.indexOf(',')));
                    int second =  Integer.parseInt(s.substring(s.indexOf(',')+1, s.length()));

                    gr.addVertex(first);
                    gr.addVertex(second);

                    gr.addEdge(first, second);
                }
            }
            else if(tokens[0].equals("s")) { //starting
                start = new ArrayList<>();
                for(int i = 1; i < tokens.length; i++){
                    start.add(Integer.parseInt(tokens[i]));
                }
            }
            else if(tokens[0].equals("e")) { //ending
                finish = new ArrayList<>();
                for(int i = 1; i < tokens.length; i++){
                    finish.add(Integer.parseInt(tokens[i]));
                }
            }
            else if(tokens[0].equals("a")){
                ArrayList<Integer> a = new ArrayList<>();
                for(int i = 1; i < tokens.length; i++){
                    a.add(Integer.parseInt(tokens[i]));
                }
                blackDots = a;
            }
            else if(tokens[0].equals("c")){
                ArrayList<Cell> c = new ArrayList<>();

                //for cells: must enter x and y dimension size as tokens 1 and 2
                xDim = Integer.parseInt(tokens[1]);
                yDim = Integer.parseInt(tokens[2]);

                for(int i = 3; i < tokens.length; i++){
                    String s = tokens[i];

                    List<String> t = Arrays.asList(s.split(","));

                    c.add(new Cell(t.get(0), Integer.parseInt(t.get(1)),
                            Integer.parseInt(t.get(2)),
                            Integer.parseInt(t.get(3)),
                            Integer.parseInt(t.get(4))));
                }
                cells = c;
            }
        }

        reader.close();

        board = gr;

        System.out.println(board.vertexSet());
        System.out.println(board.edgeSet());

        ArrayList<String> ar = new ArrayList<>();

        ar.add("regular");

        if(blackDots != null){
            ar.add("blackdots");
        }
        if(cells != null){
            ar.add("cells");
        }

        new Dfs(ar, start, finish, new ArrayList<>(board.vertexSet().size()));
    }

    public class Dfs{

        ArrayList<ArrayList<Integer>> allPaths;
        ArrayList<Integer> end;
        ArrayList<String> indicator;
        int counter;
        ArrayList<Integer> nodesExpanded;
        int counterNumPaths;

        public Dfs(ArrayList<String> ind, ArrayList<Integer> source, ArrayList<Integer> e, ArrayList<Integer> v){

                end = e;
                allPaths = new ArrayList<>();
                counter = 0;
                counterNumPaths = 0;
                nodesExpanded = new ArrayList<>();
                indicator = ind;


            ArrayList<ArrayList<ArrayList<Integer>>> allAllPaths = new ArrayList<>();
            ArrayList<ArrayList<Integer>> allNodesExpanded = new ArrayList<>();

            for(int s: source){
                dfsRegular(s, new ArrayList<>());
                ArrayList<ArrayList<Integer>> allPathsClone = new ArrayList<>();
                allPathsClone.addAll(allPaths);
                allAllPaths.add(allPathsClone);


                ArrayList<Integer> nodesExpandedClone = new ArrayList<>();
                nodesExpandedClone.addAll(nodesExpanded);
                allNodesExpanded.add(nodesExpandedClone);

                allPaths.clear();
                nodesExpanded.clear();
            }

            System.out.println("dfs all paths: " + allAllPaths);
            System.out.println("dfs nodes expanded: " + allNodesExpanded);

            int total_of_worst_nodes = 0;
            for(ArrayList<Integer> a : allNodesExpanded) {
                for (int i = 0; i < a.size(); i++) {
                    total_of_worst_nodes += a.get(i);
                }
            }
            System.out.println("dfs nodes expanded total: " + total_of_worst_nodes);


            allPaths.clear();
            nodesExpanded.clear();
            allNodesExpanded.clear();
            allAllPaths.clear();
            counterNumPaths = 0;

            for(int s: source){
                bfsRegular(s, new ArrayList<>());
                ArrayList<ArrayList<Integer>> allPathsClone = new ArrayList<>();
                allPathsClone.addAll(allPaths);
                allAllPaths.add(allPathsClone);


                ArrayList<Integer> nodesExpandedClone = new ArrayList<>();
                nodesExpandedClone.addAll(nodesExpanded);
                allNodesExpanded.add(nodesExpandedClone);

                allPaths.clear();
                nodesExpanded.clear();
            }

            System.out.println("bfs all paths: " + allAllPaths);
            System.out.println("bfs nodes expanded: " + allNodesExpanded);

            total_of_worst_nodes = 0;
            for(ArrayList<Integer> a : allNodesExpanded) {
                for (int i = 0; i < a.size(); i++) {
                    total_of_worst_nodes += a.get(i);
                }
            }
            System.out.println("bfs nodes expanded total: " + total_of_worst_nodes);
        }

        public boolean checkPath(ArrayList<Integer> visited) {
            if (indicator.contains("blackdots")) {

                if (!visited.containsAll(blackDots)) {
                    return false;
                }
            }

            if (indicator.contains("cells")) {

                ArrayList<Edge> path_to_edges = graphUtils.path_to_edges(visited);

                //add border to player-made path
                ArrayList<Edge> walls = new ArrayList<>();
                walls.addAll(path_to_edges);
                //graphUtils.addBorderEdges(walls, xDim, yDim);

                for(int i = 0; i < cells.size(); i++){
                    ArrayList<Cell> was_visited = new ArrayList<>();
                    Stack<Cell> stack = new Stack<>();

                    stack.push(cells.get(i));

                    while(!stack.isEmpty()) {
                        Cell current = stack.pop();

                        for (int j = 0; j < current.edges.size(); j++) {
                            Edge current_Edge = current.edges.get(j);

                            if (!walls.contains(current_Edge)) {
                                Cell neighbor = graphUtils.getNeighbor(current, j, cells);

                                if (!was_visited.contains(neighbor) && neighbor != null) {
                                    stack.push(neighbor);
                                    was_visited.add(neighbor);
                                }
                            }
                        }
                    }

                    if(was_visited.size() == 1){
                        continue;
                    }

                    for(int k = 0; k < was_visited.size()-1; k++){
                        if(!was_visited.get(k).color.equals(was_visited.get(k+1).color)){
                            return false;
                        }
                    }
                }

            }
            return true;
        }

        public ArrayList<Integer> dfsStack(int source, ArrayList<Integer> visited){
            Stack<Integer> s = new Stack<>();
            s.push(source);
            int v = source;

            while(!(end.contains(v)) || !checkPath(visited)){
                if(s.empty()){
                    return null;
                }

                v = s.pop();

                if(end.contains(v)){
                    if(checkPath(visited)) {
                        return visited;
                    }
                }

                if(!visited.contains(v)) {
                    visited.add(v);
                    counter++;

                    for (Integer i : Graphs.neighborListOf(board, v)) {
                        if (!visited.contains(i)) {
                            s.push(i);
                        }
                    }
                }
            }
            return visited;
        }

        public void dfsRegular(int source, ArrayList<Integer> visited){
            visited.add(source);

            if(end.contains(source)){
                counterNumPaths++;
                if(checkPath(visited)) {

                    allPaths.add(visited);
                    nodesExpanded.add(counter);
                    counter = 0;
                    return;
                }
            }
            counter++;

            for(Integer i : Graphs.neighborListOf(board, source)){
                if(!visited.contains(i)){
                    ArrayList<Integer> newVisited = new ArrayList<>(visited);

                    dfsRegular(i, newVisited);
                }
            }
        }

        public void bfsRegular(int source, ArrayList<Integer> visited){
            Queue<ArrayList<Integer>> queue = new LinkedList<>();

            //Adds to end of queue
            ArrayList<Integer> tempPath = new ArrayList<>();
            tempPath.add(source);

            queue.add(tempPath);

            while(!queue.isEmpty())
            {
                //removes from front of queue
                tempPath = queue.remove();
                int last_node = tempPath.get(tempPath.size()-1);

                if((end.contains(last_node)) && checkPath(tempPath)){

                       allPaths.add(tempPath);

                       nodesExpanded.add(counter);
                       counter = 0;
                }
                counter++;

                //Visit child first before grandchild
                for(Integer i : Graphs.neighborListOf(board, last_node)){
                    if(!tempPath.contains(i)){
                        ArrayList<Integer> newPath = new ArrayList<>();
                        newPath.addAll(tempPath);
                        newPath.add(i);

                        queue.add(newPath);
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws IOException{
        new Solution(args);
    }
}
