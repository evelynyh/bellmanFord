package paralleltasks;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxInTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final List<Map<Integer, Integer>> list;
    private final int[] p, d1, d2;

    private final int lo, hi;

    public RelaxInTask(List<Map<Integer, Integer>> list, int[] p, int[] d1, int[] d2, int lo, int hi) { //taken from ArrayCopyTask
        this.list = list;
        this.d1 = d1;
        this.d2 = d2;
        this.p = p;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {
        if (hi-lo <= CUTOFF) {
            sequential(list, p, d1, d2, lo, hi);
            return;
        }

        int mid = lo + (hi-lo)/2;
        RelaxInTask left = new RelaxInTask(list, p, d1, d2, lo, mid);
        RelaxInTask right = new RelaxInTask(list, p, d1, d2, mid, hi);

        left.fork();
        right.compute();
        left.join();
    }

    public static void sequential(List<Map<Integer, Integer>> list, int[] p, int[] d1, int[] d2, int lo, int hi) {
        for (int v = lo; v < hi; v++) {
            for (int w : list.get(v).keySet()) {
                int cost = list.get(v).get(w);

                if (d2[w] != Integer.MAX_VALUE && d1[v] > d2[w] + cost) { // gets cost from the key w

                    d1[v] = d2[w] + cost;
                    p[v] = w;
                }
            }
        }

    }

    public static void parallel(List<Map<Integer, Integer>> list, int[] p, int[] d1, int[] d2) {
        pool.invoke(new RelaxInTask(list, p, d1, d2, 0, d1.length));
    }

}
