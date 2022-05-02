package ilya.mp.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class DirectedCycle {
    private Deque<Integer> cycle;     // the directed cycle; null if digraph is acyclic

    public DirectedCycle(Digraph graph) {
        // indegrees of remaining vertices
        int[] indegree = new int[graph.getV()];
        for (int v = 0; v < graph.getV(); v++) {
            indegree[v] = graph.indegree(v);
        }

        // initialize queue to contain all vertices with indegree = 0
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < graph.getV(); v++) {
            if (indegree[v] == 0) {
                queue.add(v);
            }
        }

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int w : graph.adj(v)) {
                indegree[w]--;
                if (indegree[w] == 0) {
                    queue.add(w);
                }
            }
        }

        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        int[] edgeTo = new int[graph.getV()];
        int root = -1;  // any vertex with indegree >= -1
        for (int v = 0; v < graph.getV(); v++) {
            if (indegree[v] == 0) {
                continue;
            } else {
                root = v;
            }
            for (int w : graph.adj(v)) {
                if (indegree[w] > 0) {
                    edgeTo[w] = v;
                }
            }
        }

        if (root != -1) {
            // find any vertex on cycle
            boolean[] visited = new boolean[graph.getV()];
            while (!visited[root]) {
                visited[root] = true;
                root = edgeTo[root];
            }
            // extract cycle
            cycle = new ArrayDeque<>();
            int v = root;
            do {
                cycle.push(v);
                v = edgeTo[v];
            } while (v != root);
            cycle.push(root);
        }
        assert check();
    }

    /**
     * @return a directed cycle (as an iterable) if the digraph has a directed cycle, and {@code null} otherwise.
     */
    public Iterable<Integer> cycle() {
        return cycle;
    }

    /**
     * @return {@code true} if the digraph has a directed cycle, {@code false} otherwise.
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    // certify that digraph has a directed cycle if it reports one
    private boolean check() {
        if (hasCycle()) {
            // verify cycle
            int first = -1;
            int last = -1;
            for (int v : cycle()) {
                if (first == -1) {
                    first = v;
                }
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }
}
