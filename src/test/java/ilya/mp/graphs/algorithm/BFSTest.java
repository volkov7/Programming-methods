package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.Graph;
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

public class BFSTest {
    private static final String TINY_CG_PATH = "src/test/java/ilya/mp/graphs/data/tinyCG.txt";

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

    /**
     * 6 vertices, 8 edges
     * 0: 2 1 5
     * 1: 0 2
     * 2: 0 1 3 4
     * 3: 5 4 2
     * 4: 3 2
     * 5: 3 0
     */
    @Test
    public void dfsTinyCGTest() {
        String expected = "0 to 0 (0):  0\n" +
                            "0 to 1 (1):  0-1\n" +
                            "0 to 2 (1):  0-2\n" +
                            "0 to 3 (2):  0-2-3\n" +
                            "0 to 4 (2):  0-2-4\n" +
                            "0 to 5 (1):  0-5\n";
        int s = 0;
        Graph graph = createGraph(TINY_CG_PATH);
        BFS bfs = new BFS(graph, s);
        printPath(graph, bfs, s);

        Assert.assertEquals(expected, outContent.toString());
    }

    private Graph createGraph(String filePath) {
        Scanner sc;

        try {
            sc = new Scanner(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not open " + filePath, e);
        }
        return new Graph(sc);
    }

    private void printPath(Graph graph, BFS bfs, int s) {
        for (int v = 0; v < graph.getV(); v++) {
            if (bfs.hasPathTo(v)) {
                System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) {
                        System.out.print(x);
                    } else {
                        System.out.print("-" + x);
                    }
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d (-):  not connected\n", s, v);
            }
        }
    }
}