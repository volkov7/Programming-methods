package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.Graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class EulerianCycle {
    private Deque<Integer> cycle = new ArrayDeque<>();  // Eulerian cycle; null if no such cycle

    // an undirected edge, with a field to indicate whether the edge has already been used
    private static class Edge {
        private final int v;
        private final int w;
        private boolean isUsed;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
            isUsed = false;
        }

        // returns the other vertex of the edge
        public int other(int vertex) {
            if (vertex == v) {
                return w;
            } else if (vertex == w) {
                return v;
            } else {
                throw new IllegalArgumentException("Illegal endpoint");
            }
        }
    }

    /**
     * Computes an Eulerian cycle in the specified graph, if one exists.
     *
     * @param graph the graph
     */
    public EulerianCycle(Graph graph) {
        // must have at least one edge
        if (graph.getE() == 0) {
            return;
        }

        // necessary condition: all vertices have even degree
        // (this test is needed or it might find an Eulerian path instead of cycle)
        for (int v = 0; v < graph.getV(); v++) {
            if (graph.degree(v) % 2 != 0) {
                return;
            }
        }

        // create local view of adjacency lists, to iterate one vertex at a time
        // the helper Edge data type is used to avoid exploring both copies of an edge v-w
        Queue<Edge>[] adj = (Queue<Edge>[]) new LinkedList[graph.getV()];
        for (int v = 0; v < graph.getV(); v++) {
            adj[v] = new LinkedList<>();
        }

        for (int v = 0; v < graph.getV(); v++) {
            int selfLoops = 0;
            for (int w : graph.adj(v)) {
                // careful with self loops
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        Edge e = new Edge(v, w);
                        adj[v].add(e);
                        adj[w].add(e);
                    }
                    selfLoops++;
                }
                else if (v < w) {
                    Edge e = new Edge(v, w);
                    adj[v].add(e);
                    adj[w].add(e);
                }
            }
        }

        // initialize stack with any non-isolated vertex
        int s = nonIsolatedVertex(graph);
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(s);

        // greedily search through edges in iterative DFS style
        cycle = new ArrayDeque<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            while (!adj[v].isEmpty()) {
                Edge edge = adj[v].poll();
                if (edge.isUsed) {
                    continue;
                }
                edge.isUsed = true;
                stack.push(v);
                v = edge.other(v);
            }
            // push vertex with no more leaving edges to cycle
            cycle.push(v);
        }
        // check if all edges are used
        if (cycle.size() != graph.getE() + 1) {
            cycle = null;
        }
    }

    /**
     * @return the sequence of vertices on an Eulerian cycle;
     *         {@code null} if no such cycle
     */
    public Iterable<Integer> cycle() {
        return cycle;
    }

    /**
     * @return {@code true} if the graph has an Eulerian cycle;
     *         {@code false} otherwise
     */
    public boolean hasEulerianCycle() {
        return cycle != null;
    }

    // returns any non-isolated vertex; -1 if no such vertex
    private static int nonIsolatedVertex(Graph graph) {
        for (int v = 0; v < graph.getV(); v++) {
            if (graph.degree(v) > 0) {
                return v;
            }
        }
        return -1;
    }
}
