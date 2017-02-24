package scenarioWeek;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * This code was inspired by http://www.sanfoundry.com/java-program-find-mst-using-kruskals-algorithm/
 */
public class KruskalAlgorithm {
//    private List<Side> edges;
//    private int numberOfVertices;
//    public static final int MAX_VALUE = 99999;
//    private int visited[];
//    private int spanning_tree[][];
//
//    public KruskalAlgorithm(int numberOfVertices)
//    {
//        this.numberOfVertices = numberOfVertices;
//        edges = new LinkedList<Side>();
//        visited = new int[this.numberOfVertices + 1];
//        spanning_tree = new int[numberOfVertices + 1][numberOfVertices + 1];
//    }
//
//    public int[][] kruskalAlgorithm(int adjacencyMatrix[][])
//    {
//        boolean finished = false;
//        for (int source = 1; source <= numberOfVertices; source++)
//        {
//            for (int destination = 1; destination <= numberOfVertices; destination++)
//            {
//                if (adjacencyMatrix[source][destination] != MAX_VALUE && source != destination)
//                {
//                    Side edge = new Side();
//                    edge.sourcevertex = source;
//                    edge.destinationvertex = destination;
//                    edge.weight = adjacencyMatrix[source][destination];
//                    adjacencyMatrix[destination][source] = MAX_VALUE;
//                    edges.add(edge);
//                }
//            }
//        }
//        Collections.sort(edges, new SideComparator());
//        CheckCycle checkCycle = new CheckCycle();
//        for (Side edge : edges)
//        {
//            spanning_tree[edge.sourcevertex][edge.destinationvertex] = edge.weight;
//            spanning_tree[edge.destinationvertex][edge.sourcevertex] = edge.weight;
//            if (checkCycle.checkCycle(spanning_tree, edge.sourcevertex))
//            {
//                spanning_tree[edge.sourcevertex][edge.destinationvertex] = 0;
//                spanning_tree[edge.destinationvertex][edge.sourcevertex] = 0;
//                edge.weight = -1;
//                continue;
//            }
//            visited[edge.sourcevertex] = 1;
//            visited[edge.destinationvertex] = 1;
//            for (int i = 0; i < visited.length; i++)
//            {
//                if (visited[i] == 0)
//                {
//                    finished = false;
//                    break;
//                } else
//                {
//                    finished = true;
//                }
//            }
//            if (finished)
//                break;
//        }
////        System.out.println("The spanning tree is ");
////        for (int i = 1; i <= numberOfVertices; i++)
////            System.out.print("\t" + i);
////        System.out.println();
////        for (int source = 1; source <= numberOfVertices; source++)
////        {
////            System.out.print(source + "\t");
////            for (int destination = 1; destination <= numberOfVertices; destination++)
////            {
////                System.out.print(spanning_tree[source][destination] + "\t");
////            }
////            System.out.println();
////        }
//        return spanning_tree;
//    }
}
