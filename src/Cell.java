/**
 * Created by Joshua on 2/23/17.
 */
import java.util.ArrayList;

public class Cell {
    public ArrayList<Edge> edges;
    public String color;

    public Cell(String col, int a, int b, int c, int d){

        ArrayList<Edge> n = new ArrayList<>();

        n.add(new Edge(a, b));
        n.add(new Edge(b, c));
        n.add(new Edge(c, d));
        n.add(new Edge(d, a));

        color = col;
        edges = n;
    }

    @Override
    public String toString(){
        String s = "{" + color + " " + edges + "}";
        return s;
    }
}
