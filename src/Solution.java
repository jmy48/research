/**
 * Created by Joshua on 2/10/17.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class Solution {
    public UndirectedGraph<Integer, DefaultEdge> board;
    public int start;
    public int finish;
    public ArrayList<Integer> blackDots;


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
        }

        reader.close();

        board = gr;

        System.out.println(board.vertexSet());
        System.out.println(board.edgeSet());

        if(blackDots == null) {
            new Dfs("regular", start, finish, new ArrayList<>(board.vertexSet().size()));
        }
        else{
            new Dfs("blackdots", start, finish, new ArrayList<>(board.vertexSet().size()));
        }
    }

    public class Dfs{

        ArrayList<ArrayList<Integer>> allPaths;
        int end;

        public Dfs(String indicator, int source, int e, ArrayList<Integer> v){
                end = e;
                allPaths = new ArrayList<>();

            if(indicator.equals("regular")) {
                dfsRegular(source, new ArrayList<>());

                System.out.println(allPaths);
            }
            else if(indicator.equals("blackdots")){
                dfsRegular(source, new ArrayList<>());

                Iterator<ArrayList<Integer>> iter = allPaths.iterator();

                while (iter.hasNext()) {
                    ArrayList<Integer> path = iter.next();

                    if (!path.containsAll(blackDots)) {
                        iter.remove();
                    }
                }
                System.out.println(allPaths);
            }
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
