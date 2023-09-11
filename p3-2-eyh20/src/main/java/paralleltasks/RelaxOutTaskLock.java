package paralleltasks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private final List<Map<Integer, Integer>> list;
    private final int[] p, d1, d2;

    private final Lock[] l;
    private final int lo, hi;

    public RelaxOutTaskLock(List<Map<Integer, Integer>> list, int[] p, int[] d1, int[] d2, int lo, int hi) { //taken from ArrayCopyTask
        this.list = list;
        this.d1 = d1;
        this.d2 = d2;
        this.p = p;
        this.lo = lo;
        this.hi = hi;

        this.l = new Lock[d1.length];

        for (int i = 0; i < d1.length; i++) {
            l[i] = new ReentrantLock();
        }
    }

    protected void compute() {
        if (hi-lo <= CUTOFF) {
            sequential(list, p, d1, d2, lo, hi, l);
            return;
        }

        int mid = lo + (hi-lo)/2;
        RelaxOutTaskLock left = new RelaxOutTaskLock(list, p, d1, d2, lo, mid);
        RelaxOutTaskLock right = new RelaxOutTaskLock(list, p, d1, d2, mid, hi);

        left.fork();
        right.compute();
        left.join();
    }

    public static void sequential(List<Map<Integer, Integer>> list, int[] p, int[] d1, int[] d2, int lo, int hi, Lock[] l) {

        for (int v = lo; v < hi; v++) {


            for (int w : list.get(v).keySet()) { //for each edge find neighbors of v (go to v in array, find keySet)

                l[w].lock();
                int cost = list.get(v).get(w);

                if (d2[v] != Integer.MAX_VALUE && d1[w] > d2[v] + cost) { // gets cost from the key w

                    d1[w] = d2[v] + cost;
                    p[w] = v;
                }

                l[w].unlock();
            }
        }
    }

    public static void parallel(List<Map<Integer, Integer>> list, int[] p, int[] d1, int[] d2) {
        pool.invoke(new RelaxOutTaskBad(list, p, d1, d2, 0, d1.length));
    }
}
