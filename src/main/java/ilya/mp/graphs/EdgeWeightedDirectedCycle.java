package ilya.mp.graphs;

import ilya.mp.graphs.edge.DirectedEdge;

import java.util.ArrayDeque;
import java.util.Deque;

public class EdgeWeightedDirectedCycle {
    private final boolean[] marked;
    private final DirectedEdge[] edgeTo;
    private final boolean[] onStack;
    private Deque<DirectedEdge> cycle;

    /**
     * Determines whether the edge-weighted digraph {@code graph} has a directed cycle and,
     * if so, finds such a cycle.
     * @param graph the edge-weighted digraph
     */
    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph graph) {
        marked  = new boolean[graph.getV()];
        onStack = new boolean[graph.getV()];
        edgeTo  = new DirectedEdge[graph.getV()];
        for (int v = 0; v < graph.getV(); v++) {
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
        // check that digraph has a cycle
        assert check();
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(EdgeWeightedDigraph graph, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (DirectedEdge e : graph.adj(v)) {
            int w = e.to();

            // short circuit if directed cycle found
            if (cycle != null) {
                return;
            } else if (!marked[w]) {
                // found new vertex, so recur
                edgeTo[w] = e;
                dfs(graph, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new ArrayDeque<>();

                DirectedEdge f = e;
                while (f.from() != w) {
                    cycle.push(f);
                    f = edgeTo[f.from()];
                }
                cycle.push(f);
                return;
            }
        }
        onStack[v] = false;
    }

    /**
     * @return {@code true} if the edge-weighted digraph has a directed cycle, {@code false} otherwise.
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * @return a directed cycle (as an iterable) if the edge-weighted digraph
     *    has a directed cycle, and {@code null} otherwise.
     */
    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }

    // certify that digraph is either acyclic or has a directed cycle
    private boolean check() {
        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            DirectedEdge first = null;
            DirectedEdge last = null;
            for (DirectedEdge e : cycle()) {
                if (first == null) {
                    first = e;
                }
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }
            if (last.to() != first.from()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first);
                return false;
            }
        }
        return true;
    }
}
