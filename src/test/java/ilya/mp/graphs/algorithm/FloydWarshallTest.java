package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.AdjMatrixEdgeWeightedDigraph;
import ilya.mp.graphs.edge.DirectedEdge;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FloydWarshallTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test public void floydWarshallTest() {
        String expected = "       0      1      2      3 \n" +
                        "  0:   0.00   5.00   8.00   9.00 \n" +
                        "  1:   Inf   0.00   3.00   4.00 \n" +
                        "  2:   Inf   Inf   0.00   1.00 \n" +
                        "  3:   Inf   Inf   Inf   0.00 \n" +
                        "0 to 0 ( 0.00)  \n" +
                        "0 to 1 ( 5.00)  0->1  5.00  \n" +
                        "0 to 2 ( 8.00)  0->1  5.00  1->2  3.00  \n" +
                        "0 to 3 ( 9.00)  0->1  5.00  1->2  3.00  2->3  1.00  \n" +
                        "1 to 0 no path\n" +
                        "1 to 1 ( 0.00)  \n" +
                        "1 to 2 ( 3.00)  1->2  3.00  \n" +
                        "1 to 3 ( 4.00)  1->2  3.00  2->3  1.00  \n" +
                        "2 to 0 no path\n" +
                        "2 to 1 no path\n" +
                        "2 to 2 ( 0.00)  \n" +
                        "2 to 3 ( 1.00)  2->3  1.00  \n" +
                        "3 to 0 no path\n" +
                        "3 to 1 no path\n" +
                        "3 to 2 no path\n" +
                        "3 to 3 ( 0.00)  \n";

        AdjMatrixEdgeWeightedDigraph digraph = createGraph();
        FloydWarshall floydWarshall = new FloydWarshall(digraph);

        // print all-pairs shortest path distances
        System.out.print("  ");
        for (int v = 0; v < digraph.getV(); v++) {
            System.out.printf("%6d ", v);
        }
        System.out.println();
        for (int v = 0; v < digraph.getV(); v++) {
            System.out.printf("%3d: ", v);
            for (int w = 0; w < digraph.getV(); w++) {
                if (floydWarshall.hasPath(v, w)) {
                    System.out.printf("%6.2f ", floydWarshall.dist(v, w));
                } else {
                    System.out.print("  Inf ");
                }
            }
            System.out.println();
        }

        // print negative cycle
        if (floydWarshall.hasNegativeCycle()) {
            System.out.println("Negative cost cycle:");
            for (DirectedEdge e : floydWarshall.negativeCycle()) {
                System.out.println(e);
            }
            System.out.println();
        } else {
            for (int v = 0; v < digraph.getV(); v++) {
                for (int w = 0; w < digraph.getV(); w++) {
                    if (floydWarshall.hasPath(v, w)) {
                        System.out.printf("%d to %d (%5.2f)  ", v, w, floydWarshall.dist(v, w));
                        for (DirectedEdge e : floydWarshall.path(v, w)) {
                            System.out.print(e + "  ");
                        }
                        System.out.println();
                    }
                    else {
                        System.out.printf("%d to %d no path\n", v, w);
                    }
                }
            }
        }

        Assert.assertEquals(expected, outContent.toString());
    }

    /**
     *              10
     *        (0)------->(3)
     *         |         /|\
     *       5 |          |
     *         |          | 1
     *        \|/         |
     *        (1)------->(2)
     *             3
     */
    private AdjMatrixEdgeWeightedDigraph createGraph() {
        int v = 4;
        AdjMatrixEdgeWeightedDigraph digraph = new AdjMatrixEdgeWeightedDigraph(v);
        digraph.addEdge(new DirectedEdge(0, 1, 5));
        digraph.addEdge(new DirectedEdge(0, 3, 10));
        digraph.addEdge(new DirectedEdge(1, 2, 3));
        digraph.addEdge(new DirectedEdge(2, 3, 1));
        return digraph;
    }
}