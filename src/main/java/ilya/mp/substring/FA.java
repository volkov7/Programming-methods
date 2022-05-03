package ilya.mp.substring;

import java.util.ArrayList;
import java.util.List;

public class FA {
    private static final int ALPHABET_SIZE = 256;

    private static int[][] dfa;

    /**
     * Returns the indexes of the occurrences of the pattern string in the text string.
     *
     * @return empty array in case if text doesn't contains pattern and array of indexes in another case.
     */
    public static List<Integer> search(char[] text, char[] pattern) {
        preprocessPattern(pattern);
        List<Integer> idxes = new ArrayList<>();

        // simulate operation of DFA on text
        int patternLen = pattern.length;
        int textLen = text.length;
        int state = 0;
        for (int i = 0; i < textLen; i++) {
            state = dfa[state][text[i]];
            if (state == patternLen) {
                idxes.add(i - patternLen + 1);
            }
        }
        return idxes;
    }

    private static void preprocessPattern(char[] pattern) {
        int patternLen = pattern.length;

        // build DFA from pattern
        dfa = new int[patternLen + 1][ALPHABET_SIZE];
        for (int state = 0; state <= patternLen; state++) {
            for (int x = 0; x < ALPHABET_SIZE; x++) {
                dfa[state][x] = getNextState(pattern, patternLen, state, x);
            }
        }
    }

    private static int getNextState(char[] pattern, int patternLen, int state, int x) {
        if(state < patternLen && x == pattern[state]) {
            return state + 1;
        }

        for (int ns = state; ns > 0; ns--) {
            if (pattern[ns - 1] == x) {
                int i;
                for (i = 0; i < ns - 1; i++) {
                    if (pattern[i] != pattern[state - ns + 1 + i]) {
                        break;
                    }
                }
                if (i == ns - 1) {
                    return ns;
                }
            }
        }
        return 0;
    }
}
