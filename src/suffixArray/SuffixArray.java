package suffixArray;
import java.util.*;
public class SuffixArray {
    public static int[] build(String text) {
        int n = text.length();
        if (n == 0) {
            return new int[0];
        }
        Integer[] order = new Integer[n];
        int[] rank = new int[n];
        int[] nextRank = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
            rank[i] = text.charAt(i);
        }
        for (int step = 1; step < n; step *= 2) {
            final int gap = step;
            final int[] currentRank = rank;
            Arrays.sort(order, (a, b) -> {
                if (currentRank[a] != currentRank[b]) {
                    return Integer.compare(currentRank[a], currentRank[b]);
                }
                int ra = a + gap < n ? currentRank[a + gap] : -1;
                int rb = b + gap < n ? currentRank[b + gap] : -1;
                return Integer.compare(ra, rb);
            });
            nextRank[order[0]] = 0;
            for (int i = 1; i < n; i++) {
                int a = order[i - 1];
                int b = order[i];
                boolean different = rank[a] != rank[b] || (a + gap < n ? rank[a + gap] : -1) != (b + gap < n ? rank[b + gap] : -1);
                nextRank[b] = nextRank[a] + (different ? 1 : 0);
            }
            System.arraycopy(nextRank, 0, rank, 0, n);
            if (rank[order[n - 1]] == n - 1) {
                break;
            }
        }
        int[] suffixArray = new int[n];
        for (int i = 0; i < n; i++) {
            suffixArray[i] = order[i];
        }
        return suffixArray;
    }
}