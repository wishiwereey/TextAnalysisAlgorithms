package stringMatching;
import java.util.ArrayList;
import java.util.List;
public class KMPSearch {
    private static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }
    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }
            return result;
        }
        int[] lps = computeLPS(pattern);
        int i = 0;
        int j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (j != 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
            if (j == m) {
                result.add(i - j);
                j = lps[j - 1];
            }
        }
        return result;
    }
}

