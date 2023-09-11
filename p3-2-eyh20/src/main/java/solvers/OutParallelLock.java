package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskLock;

import java.util.List;
import java.util.Map;

public class OutParallelLock implements BellmanFordSolver {
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
            d2 = new ArrayCopyTask(d1, d2, 0, adjMatrix.length).copy(d1); //uses d1, d2
            new RelaxOutTaskLock(list, p, d1, d2, 0, adjMatrix.length).parallel(list, p, d1, d2); //uses d1, d2, p
        }
        return GraphUtil.getCycle(p);
    }

}