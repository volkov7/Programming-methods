package ilya.mp.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class DepthFirstOrder {
    private final boolean[] marked;
    private final int[] pre;
    private final int[] post;
    private final Queue<Integer> preorder;
    private final Queue<Integer> postorder;
    private int preCounter;
    private int postCounter;

    /**
     * Determines a depth-first order for the digraph {@code graph}.
     * @param graph the digraph
     */
    public DepthFirstOrder(Digraph graph) {
        pre = new int[graph.getV()];
        post = new int[graph.getV()];
        postorder = new LinkedList<>();
        preorder = new LinkedList<>();
        marked = new boolean[graph.getV()];

        for (int v = 0; v < graph.getV(); v++) {
            if (!marked[v]) dfs(graph, v);
        }
        assert check();
    }

    // run DFS in digraph graph from vertex v and compute preorder/postorder
    private void dfs(Digraph graph, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.add(v);
        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }
        postorder.add(v);
        post[v] = postCounter++;
    }

    /**
     * @param  v the vertex
     * @return the preorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int pre(int v) {
        validateVertex(v);
        return pre[v];
    }

    /**
     * @param  v the vertex
     * @return the postorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int post(int v) {
        validateVertex(v);
        return post[v];
    }

    /**
     * @return the vertices in postorder, as an iterable of vertices
     */
    public Iterable<Integer> post() {
        return postorder;
    }

    /**
     * @return the vertices in preorder, as an iterable of vertices
     */
    public Iterable<Integer> pre() {
        return preorder;
    }

    /**
     * @return the vertices in reverse postorder, as an iterable of vertices
     */
    public Iterable<Integer> reversePost() {
        Deque<Integer> reverse = new ArrayDeque<>();
        for (int v : postorder) {
            reverse.push(v);
        }
        return reverse;
    }

    // check that pre() and post() are consistent with pre(v) and post(v)
    private boolean check() {

        // check that post(v) is consistent with post()
        int r = 0;
        for (int v : post()) {
            if (post(v) != r) {
                System.out.println("post(v) and post() inconsistent");
                return false;
            }
            r++;
        }

        // check that pre(v) is consistent with pre()
        r = 0;
        for (int v : pre()) {
            if (pre(v) != r) {
                System.out.println("pre(v) and pre() inconsistent");
                return false;
            }
            r++;
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }
}
