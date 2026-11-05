package experiments;
import dynamicProgramming.EditDistance;
import dynamicProgramming.LCS;
import stringMatching.KMPSearch;
import stringMatching.NaiveSearch;
import stringMatching.RabinKarp;
import suffixArray.Kasai;
import suffixArray.SuffixArray;
import java.util.*;
import java.util.function.Supplier;
public class Experiments {
    private static final Random random = new Random(42);
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static volatile long checksum = 0;
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
    public static double measure(Supplier<?> task) {
        Object warmup = task.get();
        checksum ^= warmup.hashCode();
        long total = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            Object result = task.get();
            long end = System.nanoTime();
            checksum ^= result.hashCode();
            total += end - start;
        }
        return total / 5.0 / 1_000_000.0;
    }
    public static void runDP() {
        System.out.println("\n===== DP EXPERIMENTS =====");
        System.out.println("Size,LCS_ms,EditDistance_ms");
        int[] sizes = {100, 500, 1000};
        for (int size : sizes) {
            String a = randomString(size);
            String b = randomString(size);
            double lcsTime = measure(() -> LCS.findLCS(a, b));
            double editTime = measure(() -> EditDistance.calculate(a, b));
            System.out.printf(Locale.US, "%d,%.6f,%.6f%n", size, lcsTime, editTime);
        }
    }
    public static void runMatching() {
        System.out.println("\n===== MATCHING EXPERIMENTS =====");
        System.out.println("Size,Pattern,Naive_ms,KMP_ms,RabinKarp_ms");
        int[] sizes = {10000, 50000, 100000};
        for (int size : sizes) {
            String text = randomString(size);
            String present = text.substring(size / 2, size / 2 + 10);
            String absent = "##########";
            runMatchingCase(size, "Present", text, present);
            runMatchingCase(size, "Absent", text, absent);
        }
    }
    private static void runMatchingCase(int size, String type, String text, String pattern) {
        List<Integer> expected = NaiveSearch.search(text, pattern);
        if (!expected.equals(KMPSearch.search(text, pattern)) || !expected.equals(RabinKarp.search(text, pattern))) {
            throw new AssertionError("Search algorithms disagree");
        }
        double naive = measure(() -> NaiveSearch.search(text, pattern));
        double kmp = measure(() -> KMPSearch.search(text, pattern));
        double rk = measure(() -> RabinKarp.search(text, pattern));
        System.out.printf(Locale.US, "%d,%s,%.6f,%.6f,%.6f%n", size, type, naive, kmp, rk);
    }
    public static void runKasai() {
        System.out.println("\n===== KASAI EXPERIMENTS =====");
        System.out.println("Size,Kasai_ms");
        int[] sizes = {1000, 5000, 10000};
        for (int size : sizes) {
            String text = randomString(size);
            int[] suffixArray = SuffixArray.build(text);
            double time = measure(() -> Kasai.buildLCP(text, suffixArray));
            System.out.printf(Locale.US, "%d,%.6f%n", size, time);
        }
    }
    public static void main(String[] args) {
        runDP();
        runMatching();
        runKasai();
        System.out.println("Checksum: " + checksum);
    }
}

