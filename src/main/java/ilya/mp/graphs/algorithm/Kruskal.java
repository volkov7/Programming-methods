package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.edge.Edge;
import ilya.mp.graphs.EdgeWeightedGraph;
import ilya.mp.graphs.UF;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Kruskal {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private double weight;
    private final Queue<Edge> mst = new LinkedList<>();

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param graph the edge-weighted graph
     */
    public Kruskal(EdgeWeightedGraph graph) {
        // create array of edges, sorted by weight
        Edge[] edges = new Edge[graph.getE()];
        int t = 0;
        for (Edge e: graph.edges()) {
            edges[t++] = e;
        }
        Arrays.sort(edges);

        // run greedy algorithm
        UF uf = new UF(graph.getV());
        for (int i = 0; i < graph.getE() && mst.size() < graph.getV() - 1; i++) {
            Edge e = edges[i];
            int v = e.either();
            int w = e.other(v);

            // v-w does not create a cycle
            if (uf.find(v) != uf.find(w)) {
                uf.union(v, w);
                mst.add(e);
                weight += e.weight();
            }
        }
        // check optimality conditions
        assert check(graph);
    }

    /**
     * @return the edges in a minimum spanning tree (or forest) as an iterable of edges
     */
    public Iterable<Edge> edges() {
        return mst;
    }

    /**
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        return weight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph graph) {

        // check total weight
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(graph.getV());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : graph.edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {
            // all edges in MST except e
            uf = new UF(graph.getV());
            for (Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }
            // check that e is min weight edge in crossing cut
            for (Edge f : graph.edges()) {
                int x = f.either(), y = f.other(x);
                if (uf.find(x) != uf.find(y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
