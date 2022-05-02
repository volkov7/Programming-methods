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
import java.util.Scanner;

public class TarjanTopologicalTest {
    private static final String TINY_DG_PATH = "src/test/java/ilya/mp/graphs/data/smallDG.txt";

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
    public void tarjanTopologicalSort() {
        String expected = "0\n" +
                            "3\n" +
                            "2\n" +
                            "1\n";
        Digraph digraph = createGraph(TINY_DG_PATH);
        TarjanTopological topological = new TarjanTopological(digraph);

        printTopological(topological);
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

    private void printTopological(TarjanTopological topological) {
        for (Integer v : topological.order()) {
            System.out.println(v);
        }
    }
}