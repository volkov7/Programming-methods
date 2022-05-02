package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.edge.Edge;
import ilya.mp.graphs.EdgeWeightedGraph;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class DijkstraTest {
    private static final String TINY_EWG_PATH = "src/test/java/ilya/mp/graphs/data/tinyEWG.txt";

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

    @Test
    public void dijkstraTinyEWGTest() {
        String expected =  "6 to 0 (0.58)  6-0 0.58000   \n" +
                            "6 to 1 (0.76)  6-2 0.40000   1-2 0.36000   \n" +
                            "6 to 2 (0.40)  6-2 0.40000   \n" +
                            "6 to 3 (0.52)  3-6 0.52000   \n" +
                            "6 to 4 (0.93)  6-4 0.93000   \n" +
                            "6 to 5 (1.02)  6-2 0.40000   2-7 0.34000   5-7 0.28000   \n" +
                            "6 to 6 (0.00)  \n" +
                            "6 to 7 (0.74)  6-2 0.40000   2-7 0.34000   \n";
        int s = 6;
        EdgeWeightedGraph weightedGraph = createGraph(TINY_EWG_PATH);
        Dijkstra dijkstra = new Dijkstra(weightedGraph, s);
        printShortestPath(dijkstra, weightedGraph, s);

        Assert.assertEquals(expected, outContent.toString());
    }

    private EdgeWeightedGraph createGraph(String filePath) {
        Scanner sc;

        try {
            sc = new Scanner(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not open " + filePath, e);
        }
        return new EdgeWeightedGraph(sc);
    }

    private void printShortestPath(Dijkstra dijkstra, EdgeWeightedGraph weightedGraph, int s) {
        for (int t = 0; t < weightedGraph.getV(); t++) {
            if (dijkstra.hasPathTo(t)) {
                System.out.printf("%d to %d (%.2f)  ", s, t, dijkstra.distTo(t));
                for (Edge e : dijkstra.pathTo(t)) {
                    System.out.print(e + "   ");
                }
                System.out.println();
            }
            else {
                System.out.printf("%d to %d         no path\n", s, t);
            }
        }
    }
}