package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> list = Parser.parse(adjMatrix);
        int[] d1 = new int[adjMatrix.length];
        int[] d2 = new int[adjMatrix.length];
        int[] p = new int[adjMatrix.length];
        for (int i = 0; i < adjMatrix.length; i++) { //fill D1 and D2 with max value
            p[i] = -1;
            if (i == source) {
                d1[i] = 0;
                d2[i] = 0;
            } else {
                d1[i] = Integer.MAX_VALUE;
                d2[i] = Integer.MAX_VALUE;

            }
        }

        for (int n = 0; n < adjMatrix.length; n++) { // loop n times
            for (int i = 0; i < adjMatrix.length; i++) {
                d2[i] = d1[i]; //D2 equals D1
            }

            for (int v = 0; v < adjMatrix.length; v++) { // loop n times
                for (int w : list.get(v).keySet()) { //for each edge find neighbors of v (go to v in array, find keySet)
                    int cost = list.get(v).get(w);
                    if (d2[v] != Integer.MAX_VALUE && d1[w] > d2[v] + cost) { // gets cost from the key w
                        d1[w] = d2[v] + cost;
                        p[w] = v;
                    }
                }
            }


        }
        return GraphUtil.getCycle(p);
    }

}