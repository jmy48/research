/**
 * Created by Joshua on 2/23/17.
 */
public class Edge implements Comparator<Edge> {
    public int source;
    public int sink;

    public Edge(int so, int si){
        source = so;
        sink = si;
    }

    @Override
    public String toString(){
        String s = "{" + source + " " + sink + "}";
        return s;
    }

    @Override
    public int compare(Edge e1, Edge e2){
        if(e1.source > e2.source){
            return 1;
        }
        else if(e1.source < e2. source){
            return -1;
        }
        else if(e1.sink > e2.sink){
            return 1;
        }
        else if(e1.sink < e2.sink){
            return -1;
        }
        else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object e){
        Edge ed = (Edge) e;
        return (
                (this.source == ed.source && this.sink == ed.sink) ||
                        (this.source == ed.sink && this.sink == ed.source)
        );
    }
}
