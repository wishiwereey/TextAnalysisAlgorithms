package stringMatching;
public class SearchAnalysis {
    public static SearchMetrics naive(String text, String pattern) {
        SearchMetrics result = new SearchMetrics();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.positions.add(i);
            }
            return result;
        }
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                result.comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                result.positions.add(i);
            }
        }
        return result;
    }
    public static SearchMetrics kmp(String text, String pattern) {
        SearchMetrics result = new SearchMetrics();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.positions.add(i);
            }
            return result;
        }
        int[] lps = new int[m];
        int len = 0;
        for (int i = 1; i < m;) {
            result.comparisons++;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        int i = 0;
        int j = 0;
        while (i < n) {
            result.comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    result.positions.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j != 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return result;
    }
    public static SearchMetrics rabinKarp(String text, String pattern) {
        SearchMetrics result = new SearchMetrics();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.positions.add(i);
            }
            return result;
        }
        if (m > n) {
            return result;
        }
        final long base = 256;
        final long mod = 1000000007L;
        long patternHash = 0;
        long windowHash = 0;
        long power = 1;
        for (int i = 1; i < m; i++) {
            power = (power * base) % mod;
        }
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * base + pattern.charAt(i)) % mod;
            windowHash = (windowHash * base + text.charAt(i)) % mod;
        }
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == windowHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    result.comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.positions.add(i);
                }
            }
            if (i < n - m) {
                long outgoing = (text.charAt(i) * power) % mod;
                windowHash = (base * ((windowHash - outgoing + mod) % mod) + text.charAt(i + m)) % mod;
            }
        }
        return result;
    }
}