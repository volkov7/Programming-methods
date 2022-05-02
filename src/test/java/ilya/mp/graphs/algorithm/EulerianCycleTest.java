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

public class EulerianCycleTest {
    private static final String TINY_EULER_CYCLE_PATH = "src/test/java/ilya/mp/graphs/data/EulerianCycle.txt";

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
    public void eulerianCycleTest() {
        String expected = "Eulerian cycle: 0 1 2 0 3 4 0 \n";
        Graph graph = createGraph(TINY_EULER_CYCLE_PATH);
        EulerianCycle eulerianCycle = new EulerianCycle(graph);

        printEulerianCycle(eulerianCycle);

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

    private void printEulerianCycle(EulerianCycle eulerianCycle) {
        System.out.print("Eulerian cycle: ");
        if (eulerianCycle.hasEulerianCycle()) {
            for (int v : eulerianCycle.cycle()) {
                System.out.print(v + " ");
            }
            System.out.println();
        } else {
            System.out.println("none");
        }
    }
}