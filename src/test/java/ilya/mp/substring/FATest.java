package ilya.mp.substring;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FATest {

    @Test
    public void faTest_1() {
        String text = "THIS IS A TEST TEXT";
        String pattern = "TEST";
        List<Integer> expected = List.of(10);

        Assert.assertEquals(expected, FA.search(text.toCharArray(), pattern.toCharArray()));
    }

    @Test
    public void faTest_2() {
        String text = "TEST";
        String pattern = "TEST";
        List<Integer> expected = List.of(0);

        Assert.assertEquals(expected, FA.search(text.toCharArray(), pattern.toCharArray()));
    }

    @Test
    public void faTest_3() {
        String text = "";
        String pattern = "TEST";

        Assert.assertTrue(FA.search(text.toCharArray(), pattern.toCharArray()).isEmpty());
    }

    @Test
    public void faTest_4() {
        String text = "AABAACAADAABAABA";
        String pattern = "AABA";
        List<Integer> expected = List.of(0, 9, 12);

        Assert.assertEquals(expected, FA.search(text.toCharArray(), pattern.toCharArray()));
    }
}