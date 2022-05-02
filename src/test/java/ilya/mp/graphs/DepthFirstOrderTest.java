package ilya.mp.graphs;

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

public class DepthFirstOrderTest {
    private static final String TINY_DAG_PATH = "src/test/java/ilya/mp/graphs/data/tinyDAG.txt";

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
    public void depthFirstOrderTest() {
        String expected = "   v  pre post\n" +
                            "--------------\n" +
                            "   0    0    8\n" +
                            "   1    3    2\n" +
                            "   2    9   10\n" +
                            "   3   10    9\n" +
                            "   4    2    0\n" +
                            "   5    1    1\n" +
                            "   6    4    7\n" +
                            "   7   11   11\n" +
                            "   8   12   12\n" +
                            "   9    5    6\n" +
                            "  10    8    5\n" +
                            "  11    6    4\n" +
                            "  12    7    3\n" +
                            "Preorder:  0 5 4 1 6 9 11 12 10 2 3 7 8 \n" +
                            "Postorder: 4 5 1 12 11 10 9 6 0 3 2 7 8 \n" +
                            "Reverse postorder: 8 7 2 3 0 6 9 10 11 12 1 5 4 \n";
        Digraph digraph = createGraph(TINY_DAG_PATH);
        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph);

        printOrders(depthFirstOrder, digraph);

        Assert.assertEquals(expected, outContent.toString());
    }

    private Digraph createGraph(String filePath) {
        Scanner sc;

        try {
            sc = new Scanner(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not open " + filePath, e);
        }
        return new Digraph(sc);
    }

    private void printOrders(DepthFirstOrder dfs, Digraph digraph) {
        System.out.println("   v  pre post");
        System.out.println("--------------");
        for (int v = 0; v < digraph.getV(); v++) {
            System.out.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
        }

        System.out.print("Preorder:  ");
        for (int v : dfs.pre()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Postorder: ");
        for (int v : dfs.post()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Reverse postorder: ");
        for (int v : dfs.reversePost()) {
            System.out.print(v + " ");
        }
        System.out.println();
    }
}