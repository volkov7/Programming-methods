package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.Graph;

import java.util.Iterator;
import java.util.Stack;

public class DFS {
    private final boolean[] marked;

    /**
     * Computes the vertices connected to the source vertex {@code s} in the graph {@code graph}.
     *
     * @param graph the graph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DFS(Graph graph, int s) {
        marked = new boolean[graph.getV()];

        validateVertex(s);

        // to be able to iterate over each adjacency list, keeping track of which
        // vertex in each adjacency list needs to be explored next
        Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[graph.getV()];
        for (int v = 0; v < graph.getV(); v++) {
            adj[v] = graph.adj(v).iterator();
        }

        // depth-first search using an explicit stack
        Stack<Integer> stack = new Stack<>();
        marked[s] = true;
        stack.push(s);
        while (!stack.isEmpty()) {
            int v = stack.peek();
            if (adj[v].hasNext()) {
                int w = adj[v].next();
                if (!marked[w]) {
                    // discovered vertex w for the first time
                    marked[w] = true;
                    // edgeTo[w] = v;
                    stack.push(w);
                }
            } else {
                stack.pop();
            }
        }
    }

    /**
     * @param v the vertex
     * @return {@code true} if vertex {@code v} is connected to the source vertex {@code s},
     *    and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean marked(int v) {
        validateVertex(v);
        return marked[v];
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }
}
