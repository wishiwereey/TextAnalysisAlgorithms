package stringMatching;
import java.util.ArrayList;
import java.util.List;
public class RabinKarp {
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
        if (m > n) {
            return result;
        }
        long base = 256;
        long mod = 1000000007L;
        long patternHash = 0;
        long textHash = 0;
        long power = 1;
        for (int i = 0; i < m - 1; i++) {
            power = (power * base) % mod;
        }
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * base + pattern.charAt(i)) % mod;
            textHash = (textHash * base + text.charAt(i)) % mod;
        }
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.add(i);
                }
            }
            if (i < n - m) {
                textHash = (base * ((textHash - text.charAt(i) * power % mod + mod) % mod) + text.charAt(i + m)) % mod;
            }
        }
        return result;
    }
}
