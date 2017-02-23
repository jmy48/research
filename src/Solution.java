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

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class Solution {
    public UndirectedGraph<Integer, DefaultEdge> board;
    public int start;
    public int finish;
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
            else if(tokens[0].equals("s")) { //starting, ending
                start = Integer.parseInt(tokens[1]);
                finish = Integer.parseInt(tokens[2]);
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
                for(int i = 1; i < tokens.length; i++){
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
        int end;

        public Dfs(ArrayList<String> indicator, int source, int e, ArrayList<Integer> v){
                end = e;
                allPaths = new ArrayList<>();

            dfsRegular(source, new ArrayList<>());


            if(indicator.contains("blackdots")){
                Iterator<ArrayList<Integer>> iter = allPaths.iterator();

                while (iter.hasNext()) {
                    ArrayList<Integer> path = iter.next();

                    if (!path.containsAll(blackDots)) {
                        iter.remove();
                    }
                }
            }

            if(indicator.contains("cells")){
                ArrayList<Edge> needs_these_Edges = new ArrayList<>();

                for(Cell i: cells){
                    for(Cell j: cells){
                        if(!i.color.equals(j.color)){
                            for(Edge ed: i.edges){
                                if(j.edges.contains(ed)){
                                    needs_these_Edges.add(ed);
                                }
                            }
                        }
                    }
                }


                Iterator<ArrayList<Integer>> iter = allPaths.iterator();

                while (iter.hasNext()) {
                    ArrayList<Integer> path = iter.next();
                    ArrayList<Edge> path_to_edges = new ArrayList<>();
                    for(int i = 1; i < path.size(); i++){
                        path_to_edges.add(new Edge(
                                path.get(i-1), path.get(i)
                        ));
                    }

                    if (!path_to_edges.containsAll(needs_these_Edges)) {
                        iter.remove();
                    }
                }
            }

            System.out.println(allPaths);
        }

        public void dfsRegular(int source, ArrayList<Integer> visited){
            visited.add(source);

            if(source == end){
                allPaths.add(visited);
                return;
            }
            for(Integer i : Graphs.neighborListOf(board, source)){
                if(!visited.contains(i)){
                    ArrayList<Integer> newVisited = new ArrayList<>(visited);
                    dfsRegular(i, newVisited);
                }
            }
        }
    }

    public static void main(String args[]) throws IOException{
        new Solution(args);
    }
}
