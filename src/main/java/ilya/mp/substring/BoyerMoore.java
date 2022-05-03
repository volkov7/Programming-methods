package ilya.mp.substring;

import java.util.ArrayList;
import java.util.List;

/**
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  bad-character rule part of the Boyer-Moore algorithm.
 */
public class BoyerMoore {
    private static final int ALPHABET_SIZE = 256;
    private static final int[] RIGHT; // the bad-character skip array

    static {
        // position of rightmost occurrence of c in the pattern
        RIGHT = new int[ALPHABET_SIZE];
        for (int c = 0; c < ALPHABET_SIZE; c++) {
            RIGHT[c] = -1;
        }
    }

    /**
     * Returns the indexes of the occurrences of the pattern string in the text string.
     *
     * @return empty array in case if text doesn't contains pattern and array of indexes in another case.
     */
    public static List<Integer> search(char[] text, char[] pattern) {
        List<Integer> idxes = new ArrayList<>();
        int patternLength = pattern.length;
        int textLength = text.length;
        int skip;

        fillBadCharSkip(pattern);
        for (int i = 0; i <= textLength - patternLength; i += skip) {
            skip = 0;
            for (int j = patternLength - 1; j >= 0; j--) {
                if (pattern[j] != text[i + j]) {
                    skip = Math.max(1, j - RIGHT[text[i + j]]);
                    break;
                }
            }
            if (skip == 0) {
                idxes.add(i);
                skip = 1;
            }
        }
        return idxes;
    }

    private static void fillBadCharSkip(char[] pattern) {
        for (int j = 0; j < pattern.length; j++) {
            RIGHT[pattern[j]] = j;
        }
    }
}