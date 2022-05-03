package ilya.mp.substring;

import java.util.ArrayList;
import java.util.List;

/**
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  KMP algorithm.
 */
public class KMP {

    /**
     * Returns the indexes of the occurrences of the pattern string in the text string.
     *
     * @return empty array in case if text doesn't contains pattern and array of indexes in another case.
     */
    public static List<Integer> search(char[] text, char[] pattern) {
        List<Integer> idxes = new ArrayList<>();
        int patternLen = pattern.length;
        int textLen = text.length;

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int[] lps = new int[patternLen];
        int j = 0; // index for pat[]

        // Preprocess the pattern (calculate lps[] array)
        computeLPSArray(pattern, patternLen, lps);

        int i = 0; // index for text[]
        while (i < textLen) {
            if (pattern[j] == text[i]) {
                j++;
                i++;
            }
            if (j == patternLen) {
                idxes.add(i - j);
                j = lps[j - 1];
            } else if (i < textLen && pattern[j] != text[i]) { // mismatch after j matches
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i = i + 1;
                }
            }
        }
        return idxes;
    }

    private static void computeLPSArray(char[] pattern, int patternLen, int[] lps) {
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < patternLen) {
            if (pattern[i] == pattern[len]) {
                len++;
                lps[i] = len;
                i++;
            } else {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }
}
