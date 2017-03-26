import java.util.ArrayList;

/**
 * Created by Joshua on 2/24/17.
 *
 */
public class graphUtils {

   public static void addBorderEdges(ArrayList<Edge> ar, int x, int y){

       for(int i = 1; i < x; i++){
           ar.add(new Edge(i-1, i));
           ar.add(new Edge((i-1) + x*(y-1), i + x*(y-1)));
       }

       for(int i = 1; i < y; i++){
           ar.add(new Edge((i-1) * x, i * x));
           ar.add(new Edge((x-1) + (i-1)*x, (x-1) + (i) * x));
       }
   }

   public static ArrayList<Edge> path_to_edges(ArrayList<Integer> visited){
       ArrayList<Edge> edge_path = new ArrayList<>();
       for (int i = 1; i < visited.size(); i++) {
           edge_path.add(new Edge(
                   visited.get(i - 1), visited.get(i)
           ));
       }
       return edge_path;
   }

   public static Cell getNeighbor(Cell current, int direction, ArrayList<Cell> cell_list){
        int neighbor_edge_direction = 0;

        if(direction == 3){
            neighbor_edge_direction = 0;
        }
        else if(direction == 2){
            neighbor_edge_direction = 1;
        }
        else if(direction == 1){
            neighbor_edge_direction = 2;
        }
        else{
            neighbor_edge_direction = 3;
        }

        for(Cell c: cell_list){
            if(c.edges.get(neighbor_edge_direction).equals(current.edges.get(direction))){
                return c;
            }
        }
        return null;
   }
}
