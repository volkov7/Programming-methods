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

public class DFSTest {

    private static final String TINY_G_PATH = "src/test/java/ilya/mp/graphs/data/tinyG.txt";

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
     * 13 vertices, 13 edges
     * 0: 6 2 1 5
     * 1: 0
     * 2: 0
     * 3: 5 4
     * 4: 5 6 3
     * 5: 3 4 0
     * 6: 0 4
     * 7: 8
     * 8: 7
     * 9: 11 10 12
     * 10: 9
     * 11: 9 12
     * 12: 11 9
     */
    @Test
    public void dfsSmallGStartFrom0Test() {
        String expected = "0 1 2 3 4 5 6 \n";
        Graph graph = createGraph(TINY_G_PATH);
        DFS dfs = new DFS(graph, 0);
        printPath(graph, dfs);

        Assert.assertEquals(expected, outContent.toString());
    }

    /**
     * 13 vertices, 13 edges
     * 0: 6 2 1 5
     * 1: 0
     * 2: 0
     * 3: 5 4
     * 4: 5 6 3
     * 5: 3 4 0
     * 6: 0 4
     * 7: 8
     * 8: 7
     * 9: 11 10 12
     * 10: 9
     * 11: 9 12
     * 12: 11 9
     */
    @Test
    public void dfsSmallGStartFrom9Test() {
        String expected = "9 10 11 12 \n";
        Graph graph = createGraph(TINY_G_PATH);
        DFS dfs = new DFS(graph, 9);
        printPath(graph, dfs);

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

    private void printPath(Graph graph, DFS dfs) {
        for (int v = 0; v < graph.getV(); v++) {
            if (dfs.marked(v)) {
                System.out.print(v + " ");
            }
        }
        System.out.println();
    }
}