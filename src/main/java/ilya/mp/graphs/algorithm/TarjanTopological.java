package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.DepthFirstOrder;
import ilya.mp.graphs.Digraph;
import ilya.mp.graphs.DirectedCycle;

public class TarjanTopological {
    private Iterable<Integer> order;
    private int[] rank;

    /**
     * Determines whether the digraph {@code graph} has a topological order and, if so,
     * finds such a topological order.
     * @param graph the digraph
     */
    public TarjanTopological(Digraph graph) {
        DirectedCycle finder = new DirectedCycle(graph);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(graph);
            order = dfs.reversePost();
            rank = new int[graph.getV()];
            int i = 0;
            for (int v : order) {
                rank[v] = i++;
            }
        }
    }

    /**
     * @return a topological order of the vertices (as an interable) if the
     *    digraph has a topological order (or equivalently, if the digraph is a DAG),
     *    and {@code null} otherwise
     */
    public Iterable<Integer> order() {
        return order;
    }

    /**
     * @return {@code true} if the digraph has a topological order (or equivalently,
     *    if the digraph is a DAG), and {@code false} otherwise
     */
    public boolean hasOrder() {
        return order != null;
    }

    /**
     * The the rank of vertex {@code v} in the topological order;
     * -1 if the digraph is not a DAG
     *
     * @param v the vertex
     * @return the position of vertex {@code v} in a topological order
     *    of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int rank(int v) {
        validateVertex(v);
        if (hasOrder()) {
            return rank[v];
        } else {
            return -1;
        }
    }

    private void validateVertex(int v) {
        int V = rank.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }
}
