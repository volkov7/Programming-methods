package ilya.mp.graphs.algorithm;

import ilya.mp.graphs.Digraph;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class KosarajuTest {
    private static final String TINY_DG_PATH = "src/test/java/ilya/mp/graphs/data/tinyDG.txt";

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
    public void kosarajuTest() {
        String expected = "5 strong components\n" +
                            "1 \n" +
                            "0 2 3 4 5 \n" +
                            "9 10 11 12 \n" +
                            "6 8 \n" +
                            "7 \n";
        Digraph digraph = createGraph(TINY_DG_PATH);
        Kosaraju kosaraju = new Kosaraju(digraph);

        printComponents(kosaraju, digraph);

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

    private void printComponents(Kosaraju kosaraju, Digraph digraph) {
        int m = kosaraju.count();
        System.out.println(m + " strong components");

        // compute list of vertices in each strong component
        Queue<Integer>[] components = (Queue<Integer>[]) new LinkedList[m];
        for (int i = 0; i < m; i++) {
            components[i] = new LinkedList<>();
        }
        for (int v = 0; v < digraph.getV(); v++) {
            components[kosaraju.id(v)].add(v);
        }

        // print results
        for (int i = 0; i < m; i++) {
            for (int v : components[i]) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
    }
}