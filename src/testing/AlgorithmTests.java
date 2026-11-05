package testing;
import dynamicProgramming.EditDistance;
import dynamicProgramming.LCS;
import stringMatching.KMPSearch;
import stringMatching.NaiveSearch;
import stringMatching.RabinKarp;
import suffixArray.Kasai;
import java.util.*;
public class AlgorithmTests {
    private static int passed = 0;
    private static int failed = 0;
    private static void test(String name, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }
    private static void testSearch(String name, String text, String pattern, List<Integer> expected) {
        List<Integer> naive = NaiveSearch.search(text, pattern);
        List<Integer> kmp = KMPSearch.search(text, pattern);
        List<Integer> rk = RabinKarp.search(text, pattern);
        test(name, naive.equals(expected) && kmp.equals(expected) && rk.equals(expected));
    }
    public static void runTests() {
        passed = 0;
        failed = 0;
        System.out.println("===== dynamicprogramming.LCS TESTS =====");
        test("Identical strings", LCS.findLCS("abc", "abc") == 3);
        test("Different strings", LCS.findLCS("abc", "xyz") == 0);
        test("Empty string", LCS.findLCS("", "abc") == 0);
        test("Standard example", LCS.findLCS("AGGTAB", "GXTXAYB") == 4);
        System.out.println("\n===== EDIT DISTANCE TESTS =====");
        test("Identical strings", EditDistance.calculate("abc", "abc") == 0);
        test("Empty string", EditDistance.calculate("", "abc") == 3);
        test("Substitution", EditDistance.calculate("cat", "cut") == 1);
        test("Insertion", EditDistance.calculate("cat", "cats") == 1);
        test("Deletion", EditDistance.calculate("cats", "cat") == 1);
        test("Standard example", EditDistance.calculate("kitten", "sitting") == 3);
        System.out.println("\n===== STRING MATCHING TESTS =====");
        testSearch("Beginning", "abcdef", "abc", Arrays.asList(0));
        testSearch("Middle", "abcdef", "cd", Arrays.asList(2));
        testSearch("End", "abcdef", "ef", Arrays.asList(4));
        testSearch("Multiple occurrences", "abababa", "aba", Arrays.asList(0, 2, 4));
        testSearch("Absent pattern", "abcdef", "xyz", Collections.emptyList());
        testSearch("Pattern longer than text", "abc", "abcdef", Collections.emptyList());
        testSearch("Empty pattern", "abc", "", Arrays.asList(0, 1, 2, 3));
        testSearch("Both strings empty", "", "", Arrays.asList(0));
        System.out.println("\n===== KASAI TESTS =====");
        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        int[] expectedLCP = {0, 1, 3, 0, 0, 2};
        test("Banana LCP", Arrays.equals(Kasai.buildLCP("banana", suffixArray), expectedLCP));
        test("Empty string", Kasai.buildLCP("", new int[0]).length == 0);
        System.out.println("\n===== TEST SUMMARY =====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        if (failed > 0) {
            throw new AssertionError(failed + " tests failed");
        }
    }
    public static void main(String[] args) {
        runTests();
    }
}
