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

public class PrimTest {
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
    public void primTinyEWGTest() {
        String expected = "1-7 0.19000\n" +
                            "0-2 0.26000\n" +
                            "2-3 0.17000\n" +
                            "4-5 0.35000\n" +
                            "5-7 0.28000\n" +
                            "6-2 0.40000\n" +
                            "0-7 0.16000\n" +
                            "1.81000\n";
        EdgeWeightedGraph weightedGraph = createGraph(TINY_EWG_PATH);
        Prim prim = new Prim(weightedGraph);

        printMst(prim);

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

    private void printMst(Prim prim) {
        for (Edge e : prim.edges()) {
            System.out.println(e);
        }
        System.out.printf("%.5f\n", prim.weight());
    }
}