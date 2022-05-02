package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.Graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Fleury {
    private Deque<Integer> path = null;   // Eulerian path; null if no such path

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
     * Computes an Eulerian path in the specified graph, if one exists.
     *
     * @param graph the graph
     */
    public Fleury(Graph graph) {
        // find vertex from which to start potential Eulerian path:
        // a vertex v with odd degree(v) if it exits;
        // otherwise a vertex with degree(v) > 0
        int oddDegreeVertices = 0;
        int s = nonIsolatedVertex(graph);
        for (int v = 0; v < graph.getV(); v++) {
            if (graph.degree(v) % 2 != 0) {
                oddDegreeVertices++;
                s = v;
            }
        }
        // graph can't have an Eulerian path
        // (this condition is needed for correctness)
        if (oddDegreeVertices > 2) {
            return;
        }
        // special case for graph with zero edges (has a degenerate Eulerian path)
        if (s == -1) {
            s = 0;
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
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(s);

        // greedily search through edges in iterative DFS style
        path = new ArrayDeque<>();
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
            // push vertex with no more leaving edges to path
            path.push(v);
        }

        // check if all edges are used
        if (path.size() != graph.getE() + 1) {
            path = null;
        }
    }

    /**
     * @return the sequence of vertices on an Eulerian path;
     *         {@code null} if no such path
     */
    public Iterable<Integer> path() {
        return path;
    }

    /**
     * @return {@code true} if the graph has an Eulerian path;
     *         {@code false} otherwise
     */
    public boolean hasEulerianPath() {
        return path != null;
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
