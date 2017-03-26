/**
 * Created by Joshua on 2/23/17.
 */
import java.util.ArrayList;
import java.util.Arrays;


public class Cell {
    /*
    first element: south
    second element: west
    third element: east
    fourth element: north
     */
    public ArrayList<Edge> edges;
    public String color;

    public Cell(String col, int a, int b, int c, int d){

        int[] vertex_list = new int[4];
        vertex_list[0] = a;
        vertex_list[1] = b;
        vertex_list[2] = c;
        vertex_list[3] = d;

        Arrays.sort(vertex_list);

        ArrayList<Edge> n = new ArrayList<>();

        n.add(new Edge(vertex_list[0], vertex_list[1]));
        n.add(new Edge(vertex_list[0], vertex_list[2]));
        n.add(new Edge(vertex_list[1], vertex_list[3]));
        n.add(new Edge(vertex_list[2], vertex_list[3]));

        color = col;
        edges = n;
    }

    @Override
    public String toString(){
        //String s = "{" + color + " " + edges + "}";
        String s = color;
        return s;
    }
}
