package ilya.mp.substring;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BoyerMooreTest {

    @Test
    public void boyerMoore_1() {
        String text = "THIS IS A TEST TEXT";
        String pattern = "TEST";
        List<Integer> expected = List.of(10);

        Assert.assertEquals(expected, BoyerMoore.search(text.toCharArray(), pattern.toCharArray()));
    }

    @Test
    public void boyerMoore_2() {
        String text = "TEST";
        String pattern = "TEST";
        List<Integer> expected = List.of(0);

        Assert.assertEquals(expected, BoyerMoore.search(text.toCharArray(), pattern.toCharArray()));
    }

    @Test
    public void boyerMoore_3() {
        String text = "";
        String pattern = "TEST";

        Assert.assertTrue(BoyerMoore.search(text.toCharArray(), pattern.toCharArray()).isEmpty());
    }

    @Test
    public void boyerMoore_4() {
        String text = "AABAACAADAABAABA";
        String pattern = "AABA";
        List<Integer> expected = List.of(0, 9, 12);

        Assert.assertEquals(expected, BoyerMoore.search(text.toCharArray(), pattern.toCharArray()));
    }
}