package main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list
     */

    public static List<Map<Integer, Integer>> parse(int[][] adjMatrix) {
        List<Map<Integer, Integer>> list = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            Map<Integer, Integer> index = new HashMap<>();
            for (int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE) {
                    index.put(j, adjMatrix[i][j]);
                }
            }
            list.add(i, index);
        }
        return list;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list with incoming edges
     */

    public static  List<Map<Integer, Integer>> parseInverse(int[][] adjMatrix) {
        List<Map<Integer, Integer>> list = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            Map<Integer, Integer> index = new HashMap<>();
            for (int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[j][i] != Integer.MAX_VALUE) {
                    index.put(j, adjMatrix[j][i]);
                }
            }
            list.add(i, index);
        }
        return list;
    }
}
