package experiments;
import dynamicProgramming.*;
import stringMatching.*;
import suffixArray.*;
import testing.AlgorithmTests;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;
public class ResultsExporter {
    private static final Random random = new Random(42);
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int RUNS = 5;
    private static volatile long checksum = 0;
    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
    private static double measure(Supplier<?> task) {
        Object warmup = task.get();
        checksum ^= resultChecksum(warmup);
        long totalTime = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            Object result = task.get();
            long end = System.nanoTime();
            checksum ^= resultChecksum(result);
            totalTime += end - start;
        }
        return totalTime / (double) RUNS / 1_000_000.0;
    }
    private static long resultChecksum(Object result) {
        if (result instanceof int[]) {
            return Arrays.hashCode((int[]) result);
        }
        return result == null ? 0 : result.hashCode();
    }
    private static void saveCSV(String filename, String content) throws IOException {
        Path directory = Paths.get("results");
        Files.createDirectories(directory);
        Path file = directory.resolve(filename);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        System.out.println("Saved: " + file.toAbsolutePath());
    }
    private static String format(double value) {
        return String.format(Locale.US, "%.6f", value);
    }
    private static void runDPExperiments() throws IOException {
        System.out.println();
        System.out.println("===== DP EXPERIMENTS =====");
        int[] sizes = {100, 500, 1000};
        StringBuilder csv = new StringBuilder();
        csv.append("size,lcs_ms,edit_distance_ms\n");
        for (int size : sizes) {
            String a = randomString(size);
            String b = randomString(size);
            double lcsTime = measure(() -> LCS.findLCS(a, b));
            double editTime = measure(() -> EditDistance.calculate(a, b));
            csv.append(size).append(",").append(format(lcsTime)).append(",").append(format(editTime)).append("\n");
            System.out.printf(Locale.US, "Size: %d | LCS: %.6f ms | Edit: %.6f ms%n", size, lcsTime, editTime);
        }
        saveCSV("csv/dp_results.csv", csv.toString());
    }
    private static void runMatchingExperiments() throws IOException {
        System.out.println();
        System.out.println("===== STRING MATCHING EXPERIMENTS =====");
        int[] sizes = {10000, 50000, 100000};
        StringBuilder csv = new StringBuilder();
        csv.append("size,pattern_type,naive_ms,kmp_ms,rabin_karp_ms," + "naive_comparisons,kmp_comparisons," + "rabin_karp_comparisons\n");
        for (int size : sizes) {
            String text = randomString(size);
            String presentPattern = text.substring(size / 2, size / 2 + 10);
            String absentPattern = "##########";
            runMatchingCase(size, "present", text, presentPattern, csv);
            runMatchingCase(size, "absent", text, absentPattern, csv);
        }
        saveCSV("csv/matching_results.csv", csv.toString());
    }
    private static void runMatchingCase(int size, String type, String text, String pattern, StringBuilder csv) {
        SearchMetrics naiveMetrics = SearchAnalysis.naive(text, pattern);
        SearchMetrics kmpMetrics = SearchAnalysis.kmp(text, pattern);
        SearchMetrics rkMetrics = SearchAnalysis.rabinKarp(text, pattern);
        List<Integer> expected = naiveMetrics.positions;
        if (!expected.equals(kmpMetrics.positions) || !expected.equals(rkMetrics.positions)) {
            throw new AssertionError("String matching algorithms disagree!");
        }
        double naiveTime = measure(() -> NaiveSearch.search(text, pattern));
        double kmpTime = measure(() -> KMPSearch.search(text, pattern));
        double rkTime = measure(() -> RabinKarp.search(text, pattern));
        csv.append(size).append(",")
                .append(type).append(",")
                .append(format(naiveTime)).append(",")
                .append(format(kmpTime)).append(",")
                .append(format(rkTime)).append(",")
                .append(naiveMetrics.comparisons).append(",")
                .append(kmpMetrics.comparisons).append(",")
                .append(rkMetrics.comparisons).append("\n");
        System.out.printf(Locale.US, "Size: %d | %s | Naive: %.6f | KMP: %.6f | RK: %.6f ms%n", size, type, naiveTime, kmpTime, rkTime);
        System.out.println("Comparisons: Naive=" + naiveMetrics.comparisons + ", KMP=" + kmpMetrics.comparisons + ", Rabin-Karp=" + rkMetrics.comparisons);
    }
    private static void runKasaiExperiments() throws IOException {
        System.out.println();
        System.out.println("===== KASAI EXPERIMENTS =====");
        int[] sizes = {1000, 5000, 10000};
        StringBuilder csv = new StringBuilder();
        csv.append("size,kasai_ms\n");
        for (int size : sizes) {
            String text = randomString(size);
            int[] suffixArray = SuffixArray.build(text);
            double kasaiTime = measure(() -> Kasai.buildLCP(text, suffixArray));
            csv.append(size).append(",").append(format(kasaiTime)).append("\n");
            System.out.printf(Locale.US, "Size: %d | Kasai: %.6f ms%n", size, kasaiTime);
        }
        saveCSV("csv/kasai_results.csv", csv.toString());
    }
    public static void main(String[] args) throws IOException {
        System.out.println("================================");
        System.out.println("ASSIGNMENT 4 - EXPERIMENTAL RESULTS");
        System.out.println("================================");
        System.out.println("Random seed: 42");
        System.out.println("Alphabet: " + ALPHABET);
        System.out.println("Measured runs: " + RUNS);
        AlgorithmTests.runTests();
        runDPExperiments();
        runMatchingExperiments();
        runKasaiExperiments();
        System.out.println();
        System.out.println("================================");
        System.out.println("ALL EXPERIMENTS COMPLETED");
        System.out.println("================================");
        System.out.println("Results saved to /results/");
        System.out.println("Checksum: " + checksum);
    }
}
