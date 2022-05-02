package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.DepthFirstOrder;
import ilya.mp.graphs.Digraph;

public class Kosaraju {
    private final boolean[] marked;
    private final int[] id;
    private int count;

    /**
     * Computes the strong components of the digraph {@code graph}.
     *
     * @param graph the digraph
     */
    public Kosaraju(Digraph graph) {
        // compute reverse postorder of reverse graph
        DepthFirstOrder dfs = new DepthFirstOrder(graph.reverse());

        // run DFS on graph, using reverse postorder to guide calculation
        marked = new boolean[graph.getV()];
        id = new int[graph.getV()];
        for (int v : dfs.reversePost()) {
            if (!marked[v]) {
                dfs(graph, v);
                count++;
            }
        }
    }

    // DFS on graph graph
    private void dfs(Digraph graph, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }
    }

    /**
     * @return the number of strong components
     */
    public int count() {
        return count;
    }

    /**
     * @param  v one vertex
     * @param  w the other vertex
     * @return {@code true} if vertices {@code v} and {@code w} are in the same
     *         strong component, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
    public boolean stronglyConnected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id[v] == id[w];
    }

    /**
     * @param  v the vertex
     * @return the component id of the strong component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public int id(int v) {
        validateVertex(v);
        return id[v];
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }
}
