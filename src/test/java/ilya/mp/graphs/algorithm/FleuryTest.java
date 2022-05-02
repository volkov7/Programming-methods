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

public class FleuryTest {
    private static final String TINY_EULER_PATH = "src/test/java/ilya/mp/graphs/data/Eulerian.txt";

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
    public void fleuryTest() {
        String expected = "Eulerian path:  4 3 0 1 2 0 \n";
        Graph graph = createGraph(TINY_EULER_PATH);
        Fleury fleury = new Fleury(graph);

        printPath(fleury);

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

    private void printPath(Fleury fleury) {
        System.out.print("Eulerian path:  ");
        if (fleury.hasEulerianPath()) {
            for (int v : fleury.path()) {
                System.out.print(v + " ");
            }
            System.out.println();
        } else {
            System.out.println("none");
        }
    }
}