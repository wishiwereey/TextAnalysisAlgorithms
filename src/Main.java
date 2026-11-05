import dynamicProgramming.EditDistance;
import dynamicProgramming.LCS;
import stringMatching.KMPSearch;
import stringMatching.NaiveSearch;
import stringMatching.RabinKarp;
import suffixArray.Kasai;

import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        System.out.println("ASSIGNMENT 4");
        String a = "AGGTAB";
        String b = "GXTXAYB";
        System.out.println("dynamicprogramming.LCS: " + LCS.findLCS(a, b));
        System.out.println("Edit Distance: " + EditDistance.calculate("kitten", "sitting"));
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        System.out.println("Naive: " + NaiveSearch.search(text, pattern));
        System.out.println("KMP: " + KMPSearch.search(text, pattern));
        System.out.println("Rabin-Karp: " + RabinKarp.search(text, pattern));
        String str = "banana";
        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        int[] lcp = Kasai.buildLCP(str, suffixArray);
        System.out.println("suffixArray.Kasai LCP: " + Arrays.toString(lcp));
    }
}
