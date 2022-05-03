package ilya.mp.substring;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Reads in two strings, the pattern and the input text, and
 * searches for the pattern in the input text using the
 * Las Vegas version of the Rabin-Karp algorithm.
 */
public class RabinKarp {
    private static final int ALPHABET_SIZE = 256; 
    
    private static long patternHash;
    private static long q;          // a large prime, small enough to avoid long overflow
    private static long RM;         // ALPHABET_SIZE^(M-1) % Q

    /**
     * Returns the indexes of the occurrences of the pattern string in the text string.
     *
     * @return empty array in case if text doesn't contains pattern and array of indexes in another case.
     */
    public static List<Integer> search(char[] text, char[] pattern) {
        preprocessPattern(pattern);
        List<Integer> idxes = new ArrayList<>();
        int textLen = text.length;
        int patternLen = pattern.length;
        if (textLen < patternLen) {
            return new ArrayList<>();
        }
        long textHash = hash(text, patternLen);

        // check for match at offset 0
        if ((patternHash == textHash) && check(text, pattern,0)) {
            idxes.add(0);
        }

        // check for hash match; if hash match, check for exact match
        for (int i = patternLen; i < textLen; i++) {
            // Remove leading digit, add trailing digit, check for match.
            textHash = (textHash + q - RM * text[i - patternLen] % q) % q;
            textHash = (textHash * ALPHABET_SIZE + text[i]) % q;

            // match
            int offset = i - patternLen + 1;
            if ((patternHash == textHash) && check(text, pattern, offset)) {
                idxes.add(offset);
            }
        }
        return idxes;
    }

    private static void preprocessPattern(char[] pattern) {
        q = longRandomPrime();

        // precompute ALPHABET_SIZE ^ (m - 1) % q for use in removing leading digit
        RM = 1;
        for (int i = 1; i <= pattern.length -1; i++) {
            RM = (ALPHABET_SIZE * RM) % q;
        }
        patternHash = hash(pattern, pattern.length);
    }

    // Compute hash for key[0..m-1].
    private static long hash(char[] key, int m) {
        long h = 0;
        for (int j = 0; j < m; j++) {
            h = (ALPHABET_SIZE * h + key[j]) % q;
        }
        return h;
    }

    // Las Vegas version: does pat[] match text[i..i-m+1]
    private static boolean check(char[] text, char[] pattern, int i) {
        for (int j = 0; j < pattern.length; j++) {
            if (pattern[j] != text[i + j]) {
                return false;
            }
        }
        return true;
    }

    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }
}
