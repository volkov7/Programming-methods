package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.edge.Edge;
import ilya.mp.graphs.EdgeWeightedGraph;
import ilya.mp.graphs.IndexMinPQ;
import ilya.mp.graphs.UF;

import java.util.LinkedList;
import java.util.Queue;

public class Prim {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private final Edge[] edgeTo;
    private final double[] distTo;
    private final boolean[] marked;
    private final IndexMinPQ<Double> pq;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param graph the edge-weighted graph
     */
    public Prim(EdgeWeightedGraph graph) {
        edgeTo = new Edge[graph.getV()];
        distTo = new double[graph.getV()];
        marked = new boolean[graph.getV()];
        pq = new IndexMinPQ<>(graph.getV());

        for (int v = 0; v < graph.getV(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        for (int v = 0; v < graph.getV(); v++) {
            if (!marked[v]) {
                prim(graph, v); // run from each vertex to find minimum spanning forest
            }
        }
        // check optimality conditions
        assert check(graph);
    }

    // run Prim's algorithm in graph graph, starting from vertex s
    private void prim(EdgeWeightedGraph graph, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(graph, v);
        }
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph graph, int v) {
        marked[v] = true;
        for (Edge e : graph.adj(v)) {
            int w = e.other(v);
            if (marked[w]) {
                continue; // v-w is obsolete edge
            }
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) {
                    pq.decreaseKey(w, distTo[w]);
                } else {
                    pq.insert(w, distTo[w]);
                }
            }
        }
    }

    /**
     * @return the edges in a minimum spanning tree (or forest) as an iterable of edges
     */
    public Iterable<Edge> edges() {
        Queue<Edge> mst = new LinkedList<>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }

    /**
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges()) {
            weight += e.weight();
        }
        return weight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph graph) {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
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
            for (Edge f : edges()) {
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
