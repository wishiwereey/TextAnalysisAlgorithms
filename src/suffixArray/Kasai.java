package suffixArray;
public class Kasai {
    public static int[] buildLCP(String text, int[] suffixArray) {
        int n = text.length();
        if (suffixArray.length != n) {
            throw new IllegalArgumentException("Invalid suffix array length");
        }
        int[] lcp = new int[n];
        int[] rank = new int[n];
        boolean[] seen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int position = suffixArray[i];
            if (position < 0 || position >= n || seen[position]) {
                throw new IllegalArgumentException("Invalid suffix array");
            }
            seen[position] = true;
            rank[position] = i;
        }
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) {
                k = 0;
                continue;
            }
            int j = suffixArray[rank[i] - 1];
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }
            lcp[rank[i]] = k;
            if (k > 0) {
                k--;
            }
        }
        return lcp;
    }
}
