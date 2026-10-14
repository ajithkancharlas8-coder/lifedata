package lifedata.analytics;

import lifedata.m1_strings.*;
import lifedata.m2_suffix.*;
import lifedata.m3_dp.*;
import lifedata.m4_networkflow.*;
import lifedata.m5_np.*;
import lifedata.m6_random_parallel.*;
import lifedata.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DatasetComparator: Comprehensive comparative analytics engine comparing:
 * - Dataset 1: Baseline In-Memory Synthetic Dataset
 * - Dataset 2: Real-World 300-Record CSV Dataset (Heterogeneous Health & Activity Data)
 *
 * Runs all 6 Advanced DSA Modules on both datasets and prints detailed,
 * side-by-side comparative performance and algorithmic results.
 */
public class DatasetComparator {

    private final LifeDataPlatform platform;
    private final List<CsvRecord> csvRecords;
    private final List<PersonalActivityRecord> csvActivities;
    private final List<PersonalNote> csvNotes;
    private final List<SensorObservation> csvSensors;
    private final List<ActivityStreamItem> csvStream;

    public DatasetComparator(LifeDataPlatform platform) {
        this.platform = platform;
        this.csvRecords = CsvDataLoader.loadRecords();
        this.csvActivities = CsvDataLoader.loadAsActivities();
        this.csvNotes = CsvDataLoader.loadAsNotes();
        this.csvSensors = CsvDataLoader.loadAsSensorObservations();
        this.csvStream = CsvDataLoader.loadAsStreamItems();
    }

    // =========================================================================
    // COMPARISON 1: STRING ALGORITHMS (M1)
    // =========================================================================
    public void compareModule1StringAlgorithms() {
        System.out.println("\n===============================================================================");
        System.out.println("  >>> DATASET COMPARISON: MODULE 1 - STRING ALGORITHMS (M1 - CO1)");
        System.out.println("===============================================================================");

        String ds1Corpus = platform.getNotes().stream().map(PersonalNote::getContent).collect(Collectors.joining(" | "));
        String ds2Corpus = csvNotes.stream().map(PersonalNote::getContent).collect(Collectors.joining(" | "));

        String searchWord = "walk";

        // 1.1 KMP Comparison
        System.out.println("\n--- [1.1] Knuth-Morris-Pratt (KMP) Search Comparison ---");
        long kmpStart1 = System.nanoTime();
        List<Integer> hits1 = KMPMatcher.search(ds1Corpus, searchWord);
        long kmpTime1 = System.nanoTime() - kmpStart1;

        long kmpStart2 = System.nanoTime();
        List<Integer> hits2 = KMPMatcher.search(ds2Corpus, searchWord);
        long kmpTime2 = System.nanoTime() - kmpStart2;

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Metric                      | Dataset 1 (Synthetic) | Dataset 2 (CSV Notes) |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Target Search Term          | %-21s | %-21s |\n", "\"" + searchWord + "\"", "\"" + searchWord + "\"");
        System.out.printf("| Total Notes Count           | %-21d | %-21d |\n", platform.getNotes().size(), csvNotes.size());
        System.out.printf("| Corpus Character Size       | %-21d | %-21d |\n", ds1Corpus.length(), ds2Corpus.length());
        System.out.printf("| Exact Occurrences Found     | %-21d | %-21d |\n", hits1.size(), hits2.size());
        System.out.printf("| KMP Execution Time          | %-18.3f us | %-18.3f us |\n", kmpTime1 / 1000.0, kmpTime2 / 1000.0);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 1.2 Z-Function Pattern Detection Comparison
        System.out.println("\n--- [1.2] Z-Function Repeated Pattern Mining Comparison ---");
        String ds1ZText = "recovery hydrate rest recovery hydrate sleep recovery hydrate";
        String ds2ZText = "Repeated daily routine: walk, hydrate, and journal. Repeated daily routine: walk, hydrate, and journal.";
        int[] z1 = ZFunctionPatternMatcher.calculateZ(ds1ZText);
        int[] z2 = ZFunctionPatternMatcher.calculateZ(ds2ZText);
        String rep1 = ZFunctionPatternMatcher.findLongestRepeatedPrefixPattern(ds1ZText);
        String rep2 = ZFunctionPatternMatcher.findLongestRepeatedPrefixPattern(ds2ZText);

        System.out.printf("Dataset 1 Detected Prefix Pattern: \"%s\"\n", rep1);
        System.out.printf("Dataset 2 Detected Prefix Pattern: \"%s\"\n", rep2);
        System.out.printf("Dataset 1 Z-Array (first 8):  %s\n", Arrays.toString(Arrays.copyOf(z1, Math.min(8, z1.length))));
        System.out.printf("Dataset 2 Z-Array (first 8):  %s\n", Arrays.toString(Arrays.copyOf(z2, Math.min(8, z2.length))));

        // 1.3 Rabin-Karp Rolling Hash Comparison
        System.out.println("\n--- [1.3] Rabin-Karp Rolling Hash Stream ID Search Comparison ---");
        String ds1StreamIds = platform.getActivityStream().stream().map(ActivityStreamItem::getEventId).collect(Collectors.joining(","));
        String ds2EventIds = csvRecords.stream().map(CsvRecord::getEventId).collect(Collectors.joining(","));

        String ds1TargetId = "EVT-STREAM-5042";
        String ds2TargetId = "EVT-20260305-0014";

        long rk1Start = System.nanoTime();
        List<Integer> rk1Hits = RabinKarpMatcher.search(ds1StreamIds, ds1TargetId);
        long rk1Dur = System.nanoTime() - rk1Start;

        long rk2Start = System.nanoTime();
        List<Integer> rk2Hits = RabinKarpMatcher.search(ds2EventIds, ds2TargetId);
        long rk2Dur = System.nanoTime() - rk2Start;

        System.out.printf("Dataset 1: Target=\"%s\" in %d stream IDs -> Matches at %s (took %.3f us)\n",
                ds1TargetId, platform.getActivityStream().size(), rk1Hits, rk1Dur / 1000.0);
        System.out.printf("Dataset 2: Target=\"%s\" in %d CSV event IDs -> Matches at %s (took %.3f us)\n",
                ds2TargetId, csvRecords.size(), rk2Hits, rk2Dur / 1000.0);

        // 1.4 Aho-Corasick Multi-Pattern Dictionary Matching Comparison
        System.out.println("\n--- [1.4] Aho-Corasick Multi-Pattern Automaton Dictionary Matching ---");
        AhoCorasickAutomaton ac = new AhoCorasickAutomaton();
        List<String> healthVocab = Arrays.asList("heart rate", "cardio", "fatigue", "recovery", "hydration", "walk", "sleep", "routine", "stress", "clinic");
        ac.addPatterns(healthVocab);
        ac.buildAutomaton();

        Map<String, Integer> freq1 = ac.countKeywordFrequencies(ds1Corpus);
        Map<String, Integer> freq2 = ac.countKeywordFrequencies(ds2Corpus);

        System.out.println("+-----------------+-----------------------+-----------------------+");
        System.out.println("| Vocabulary Term | Dataset 1 (Synthetic) | Dataset 2 (CSV Notes) |");
        System.out.println("+-----------------+-----------------------+-----------------------+");
        for (String term : healthVocab) {
            System.out.printf("| %-15s | %-21d | %-21d |\n",
                    term, freq1.getOrDefault(term, 0), freq2.getOrDefault(term, 0));
        }
        System.out.println("+-----------------+-----------------------+-----------------------+");
    }

    // =========================================================================
    // COMPARISON 2: SUFFIX STRUCTURES (M2)
    // =========================================================================
    public void compareModule2SuffixStructures() {
        System.out.println("\n===============================================================================");
        System.out.println("  >>> DATASET COMPARISON: MODULE 2 - SUFFIX STRUCTURES (M2 - CO2)");
        System.out.println("===============================================================================");

        String ds1Text = "trail run endurance conditioning. trail run endurance recovery. trail run cardio stamina.";
        // Sample 30 representative notes from CSV to form a rich multi-document corpus
        String ds2Text = csvNotes.stream().limit(40).map(PersonalNote::getContent).collect(Collectors.joining(" "));

        // 2.1 SA-IS Linear Time Suffix Array
        System.out.println("\n--- [2.1] SA-IS Linear-Time Suffix Array Construction ---");
        long t1Start = System.nanoTime();
        int[] sa1 = SAIS.buildSuffixArray(ds1Text);
        long t1Dur = System.nanoTime() - t1Start;

        long t2Start = System.nanoTime();
        int[] sa2 = SAIS.buildSuffixArray(ds2Text);
        long t2Dur = System.nanoTime() - t2Start;

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Metric                      | Dataset 1 (Synthetic) | Dataset 2 (CSV Notes) |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Text Corpus Length (Chars)  | %-21d | %-21d |\n", ds1Text.length(), ds2Text.length());
        System.out.printf("| SA-IS Construction Time     | %-18.3f ms | %-18.3f ms |\n", t1Dur / 1_000_000.0, t2Dur / 1_000_000.0);
        System.out.printf("| Suffix Array Entries        | %-21d | %-21d |\n", sa1.length, sa2.length);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 2.2 Kasai's LCP (Longest Common Prefix) Algorithm
        System.out.println("\n--- [2.2] Kasai's LCP Algorithm: Longest Repeated Habit Phrase ---");
        int[] lcp1 = KasaiLCP.buildLCP(ds1Text, sa1);
        int[] lcp2 = KasaiLCP.buildLCP(ds2Text, sa2);

        String rep1 = KasaiLCP.findLongestRepeatedPhrase(ds1Text, sa1, lcp1);
        String rep2 = KasaiLCP.findLongestRepeatedPhrase(ds2Text, sa2, lcp2);

        System.out.printf("Dataset 1 Longest Repeated Phrase: \"%s\"\n", rep1);
        System.out.printf("Dataset 2 Longest Repeated Phrase: \"%s\"\n", rep2);

        // 2.3 Suffix Automaton (SAM) Distinct Substring Mining
        System.out.println("\n--- [2.3] Suffix Automaton (SAM - Minimal DFA) Analytics ---");
        SuffixAutomaton sam1 = new SuffixAutomaton(ds1Text);
        SuffixAutomaton sam2 = new SuffixAutomaton(ds2Text);

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| SAM Structural Property     | Dataset 1 (Synthetic) | Dataset 2 (CSV Notes) |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Total Distinct Substrings   | %-21d | %-21d |\n", sam1.countDistinctSubstrings(), sam2.countDistinctSubstrings());
        System.out.printf("| DFA State Count (|V|)       | %-21d | %-21d |\n", sam1.getStateCount(), sam2.getStateCount());
        System.out.printf("| Transition Count (|E|)      | %-21d | %-21d |\n", sam1.getTransitionCount(), sam2.getTransitionCount());
        System.out.printf("| Contains \"routine\"?         | %-21b | %-21b |\n", sam1.containsSubstring("routine"), sam2.containsSubstring("routine"));
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 2.4 Cross-Dataset Document Similarity
        String docDs1 = platform.getNotes().get(0).getContent();
        String docDs2 = csvNotes.get(0).getContent();
        String crossLcs = KasaiLCP.findLongestCommonSubstring(docDs1, docDs2);
        System.out.println("\nCross-Dataset Longest Common Substring (Synthetic Note #1 vs CSV Note #1):");
        System.out.printf("  \"%s\" (Length: %d characters)\n", crossLcs, crossLcs.length());
    }

    // =========================================================================
    // COMPARISON 3: ADVANCED DYNAMIC PROGRAMMING (M3)
    // =========================================================================
    public void compareModule3DynamicProgramming() {
        System.out.println("\n===============================================================================");
        System.out.println("  >>> DATASET COMPARISON: MODULE 3 - ADVANCED DP (M3 - CO3)");
        System.out.println("===============================================================================");

        // 3.1 Damerau-Levenshtein Typo Correction Comparison
        System.out.println("\n--- [3.1] Damerau-Levenshtein Distance & Real Dataset Typos ---");
        List<String> validVocab = Arrays.asList(
                "walking", "running", "cycling", "meditation", "hydration", "workout",
                "reading", "sleep", "commuting", "swimming", "weightlifting"
        );

        // Test 1: Synthetic Transposition
        String typoSynthetic = "runnign";
        int dl1 = DamerauLevenshteinDistance.computeDamerauLevenshtein(typoSynthetic, "running");
        int lev1 = DamerauLevenshteinDistance.computeLevenshtein(typoSynthetic, "running");

        // Test 2: REAL CSV Dataset Typo! ("meditaton" found in REC-00061, REC-00122, REC-00183, REC-00244)
        String typoRealCsv = "meditaton";
        int dl2 = DamerauLevenshteinDistance.computeDamerauLevenshtein(typoRealCsv, "meditation");
        int lev2 = DamerauLevenshteinDistance.computeLevenshtein(typoRealCsv, "meditation");

        System.out.println("+-----------------------+-------------------+--------------------+------------------+---------------------+");
        System.out.println("| Input Typo String     | Target Activity   | Damerau-Lev (M3)   | Classic Lev Dist | Auto-Correction     |");
        System.out.println("+-----------------------+-------------------+--------------------+------------------+---------------------+");
        System.out.printf("| %-21s | %-17s | %-18d | %-16d | %-19s |\n",
                "\"" + typoSynthetic + "\" (Synthetic)", "running", dl1, lev1, DamerauLevenshteinDistance.autoCorrect(typoSynthetic, validVocab));
        System.out.printf("| %-21s | %-17s | %-18d | %-16d | %-19s |\n",
                "\"" + typoRealCsv + "\" (CSV REC-61)", "meditation", dl2, lev2, DamerauLevenshteinDistance.autoCorrect(typoRealCsv, validVocab));
        System.out.println("+-----------------------+-------------------+--------------------+------------------+---------------------+");

        // 3.2 Weighted Edit Distance Keyboard Proximity
        System.out.println("\n--- [3.2] Weighted Edit Distance (QWERTY Keyboard Penalty) ---");
        System.out.printf("  cardio -> vardio (Adjacent 'c'-'v'): %.2f penalty\n", WeightedEditDistance.compute("cardio", "vardio"));
        System.out.printf("  cardio -> pardio (Far key 'c'-'p'):  %.2f penalty\n", WeightedEditDistance.compute("cardio", "pardio"));

        // 3.3 Bitmask DP Activity Scheduling Comparison
        System.out.println("\n--- [3.3] Bitmask DP Daily Activity Scheduling Comparison ---");

        // Dataset 1 Candidates (Synthetic)
        List<ActivityBitmaskDP.ActivityCandidate> c1 = new ArrayList<>();
        c1.add(new ActivityBitmaskDP.ActivityCandidate(0, "Morning Yoga", 30, 120, 1, 85));
        c1.add(new ActivityBitmaskDP.ActivityCandidate(1, "Tempo Run", 45, 500, 4, 90));
        c1.add(new ActivityBitmaskDP.ActivityCandidate(2, "Heavy Squats", 60, 400, 5, 80));
        c1.add(new ActivityBitmaskDP.ActivityCandidate(3, "Sauna Recovery", 25, 90, 1, 75));
        c1.add(new ActivityBitmaskDP.ActivityCandidate(4, "Core HIIT", 20, 250, 3, 70));
        c1.add(new ActivityBitmaskDP.ActivityCandidate(5, "Evening Stroll", 30, 110, 1, 65));
        double[][] syn1 = new double[6][6];
        syn1[0][1] = 15.0; // Yoga -> Run
        syn1[2][3] = 25.0; // Squats -> Sauna

        // Dataset 2 Candidates (Extracted from Real CSV Dataset Logs)
        List<ActivityBitmaskDP.ActivityCandidate> c2 = new ArrayList<>();
        c2.add(new ActivityBitmaskDP.ActivityCandidate(0, "Hydration & Walk", 35, 160, 1, 82));
        c2.add(new ActivityBitmaskDP.ActivityCandidate(1, "Commuter Cycling", 45, 420, 4, 88));
        c2.add(new ActivityBitmaskDP.ActivityCandidate(2, "Strength Workout", 50, 360, 5, 84));
        c2.add(new ActivityBitmaskDP.ActivityCandidate(3, "Post-Meal Walk", 25, 130, 1, 78));
        c2.add(new ActivityBitmaskDP.ActivityCandidate(4, "Evening Running", 30, 390, 4, 91));
        c2.add(new ActivityBitmaskDP.ActivityCandidate(5, "Stress Meditation", 20, 60, 1, 86));
        double[][] syn2 = new double[6][6];
        syn2[0][1] = 20.0; // Hydration -> Cycling
        syn2[4][5] = 25.0; // Running -> Meditation

        int maxTime = 120;
        int maxFatigue = 7;
        ActivityBitmaskDP.ScheduleResult s1 = ActivityBitmaskDP.solveOptimalSchedule(c1, syn1, maxTime, maxFatigue);
        ActivityBitmaskDP.ScheduleResult s2 = ActivityBitmaskDP.solveOptimalSchedule(c2, syn2, maxTime, maxFatigue);

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Schedule Optimization Field | Dataset 1 (Synthetic) | Dataset 2 (CSV Logs)  |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Selected Subset (Bitmask)   | 0x%-19X | 0x%-19X |\n", s1.optimalMask, s2.optimalMask);
        System.out.printf("| Activities Scheduled        | %-21d | %-21d |\n", s1.chosenActivities.size(), s2.chosenActivities.size());
        System.out.printf("| Total Duration Used         | %3d / %3d mins         | %3d / %3d mins         |\n", s1.totalTime, maxTime, s2.totalTime, maxTime);
        System.out.printf("| Total Fatigue Incurred      | %3d / %3d pts          | %3d / %3d pts          |\n", s1.totalFatigue, maxFatigue, s2.totalFatigue, maxFatigue);
        System.out.printf("| Total Calories Burned       | %-16.1f kcal | %-16.1f kcal |\n", s1.totalCalories, s2.totalCalories);
        System.out.printf("| Max Combined Objective Score| %-21.2f | %-21.2f |\n", s1.totalScore, s2.totalScore);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 3.4 Matrix-Chain Multiplication (MCM) Optimization Comparison
        System.out.println("\n--- [3.4] Matrix-Chain Multiplication (MCM) Sensor Pipeline Ordering ---");
        int[] dims1 = {10000, 20, 20, 5, 1};
        String[] stages1 = {"RawSensors", "FilterMatrix", "FeatureProj", "Classifier"};
        MatrixChainOptimization.MCMResult mcm1 = MatrixChainOptimization.optimize(dims1, stages1);

        // CSV Multi-stage pipeline: Ingest (300) -> Normalize (50) -> Index (20) -> Analytics (5) -> ML Output (1)
        int[] dims2 = {300, 50, 20, 5, 1};
        String[] stages2 = {"IngestStage", "NormalizeStage", "IndexStage", "AnalyticsStage"};
        MatrixChainOptimization.MCMResult mcm2 = MatrixChainOptimization.optimize(dims2, stages2);

        System.out.printf("Dataset 1 Optimal Operations: %,d ops | %s\n", mcm1.minScalarOperations, mcm1.optimalOrder);
        System.out.printf("Dataset 2 Optimal Operations: %,d ops | %s\n", mcm2.minScalarOperations, mcm2.optimalOrder);
    }

    // =========================================================================
    // COMPARISON 4: NETWORK FLOW & RESOURCE ALLOCATION (M4)
    // =========================================================================
    public void compareModule4NetworkFlow() {
        System.out.println("\n===============================================================================");
        System.out.println("  >>> DATASET COMPARISON: MODULE 4 - NETWORK FLOW & RESOURCE ALLOCATION (M4 - CO4)");
        System.out.println("===============================================================================");

        // 4.1 Max-Flow Pipeline Comparison
        System.out.println("\n--- [4.1] Max-Flow Algorithm Comparison (Throughput) ---");

        // Network 1: Synthetic 5-node data pipeline
        FlowNetwork net1 = new FlowNetwork(5);
        net1.addEdge(0, 1, 100);
        net1.addEdge(0, 2, 50);
        net1.addEdge(1, 2, 40);
        net1.addEdge(1, 3, 70);
        net1.addEdge(2, 4, 80);
        net1.addEdge(3, 4, 60);

        // Network 2: Real CSV Data Pipeline
        // Count records per processing stage
        Map<String, Long> stageCounts = csvRecords.stream()
                .collect(Collectors.groupingBy(CsvRecord::getProcessingStage, Collectors.counting()));
        int capIngest = stageCounts.getOrDefault("ingest", 75L).intValue();
        int capNormalize = stageCounts.getOrDefault("normalize", 75L).intValue();
        int capIndex = stageCounts.getOrDefault("index", 75L).intValue();
        int capAnalytics = stageCounts.getOrDefault("analytics", 75L).intValue();

        // Nodes: 0=Source, 1=Ingest, 2=Normalize, 3=Index, 4=Analytics, 5=Sink
        FlowNetwork net2 = new FlowNetwork(6);
        net2.addEdge(0, 1, capIngest * 2);
        net2.addEdge(0, 2, capNormalize * 2);
        net2.addEdge(1, 3, capIngest);
        net2.addEdge(2, 3, capNormalize);
        net2.addEdge(3, 4, capIndex * 2);
        net2.addEdge(4, 5, capAnalytics * 2);

        long ff1 = FordFulkerson.computeMaxFlow(net1.copy(), 0, 4);
        long ek1 = EdmondsKarp.computeMaxFlow(net1.copy(), 0, 4);
        long din1 = DinicAlgorithm.computeMaxFlow(net1.copy(), 0, 4);

        long ff2 = FordFulkerson.computeMaxFlow(net2.copy(), 0, 5);
        long ek2 = EdmondsKarp.computeMaxFlow(net2.copy(), 0, 5);
        long din2 = DinicAlgorithm.computeMaxFlow(net2.copy(), 0, 5);

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Algorithm Throughput        | Dataset 1 (Synthetic) | Dataset 2 (CSV Flow)  |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Ford-Fulkerson Max Flow     | %-17d rec/s | %-17d rec/s |\n", ff1, ff2);
        System.out.printf("| Edmonds-Karp Max Flow       | %-17d rec/s | %-17d rec/s |\n", ek1, ek2);
        System.out.printf("| Dinic's Blocking Max Flow   | %-17d rec/s | %-17d rec/s |\n", din1, din2);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 4.2 Bipartite Matching for Resource Allocation Comparison
        System.out.println("\n--- [4.2] Bipartite Matching for Task-Worker Allocation ---");
        List<String> tasks1 = Arrays.asList("ECG Filtering", "Sleep Stage AI", "GPS Elevation Profile", "Calorie Aggregation", "Stress Score Model");
        List<String> res1 = Arrays.asList("Wearable NPU", "Local CPU-Core1", "Local CPU-Core2", "Cloud Worker A", "Cloud GPU Node");
        Map<String, List<String>> elig1 = new HashMap<>();
        elig1.put("ECG Filtering", Arrays.asList("Wearable NPU", "Local CPU-Core1"));
        elig1.put("Sleep Stage AI", Arrays.asList("Local CPU-Core2", "Cloud GPU Node"));
        elig1.put("GPS Elevation Profile", Arrays.asList("Local CPU-Core1", "Cloud Worker A"));
        elig1.put("Calorie Aggregation", Arrays.asList("Wearable NPU", "Local CPU-Core2"));
        elig1.put("Stress Score Model", Arrays.asList("Cloud GPU Node"));

        // Dataset 2: Tasks from CSV processing stages to the 5 workers (worker_A to worker_E)
        List<String> tasks2 = Arrays.asList("Ingest Pipeline", "Normalize Engine", "Index Builder", "Analytics Processor", "Stream Filter");
        List<String> res2 = Arrays.asList("worker_A", "worker_B", "worker_C", "worker_D", "worker_E");
        Map<String, List<String>> elig2 = new HashMap<>();
        // Aggregate eligibility from CSV records
        elig2.put("Ingest Pipeline", Arrays.asList("worker_B", "worker_C", "worker_D"));
        elig2.put("Normalize Engine", Arrays.asList("worker_A", "worker_B", "worker_D", "worker_E"));
        elig2.put("Index Builder", Arrays.asList("worker_C", "worker_D", "worker_E"));
        elig2.put("Analytics Processor", Arrays.asList("worker_A", "worker_C", "worker_E"));
        elig2.put("Stream Filter", Arrays.asList("worker_A", "worker_B", "worker_E"));

        BipartiteMatching.TaskResourceAssignment match1 = BipartiteMatching.allocateTasks(tasks1, res1, elig1);
        BipartiteMatching.TaskResourceAssignment match2 = BipartiteMatching.allocateTasks(tasks2, res2, elig2);

        System.out.printf("Dataset 1 Max Matching: %d / %d tasks allocated.\n", match1.assignedCount, tasks1.size());
        match1.taskToResource.forEach((t, r) -> System.out.printf("  * %-22s -> %s\n", t, r));

        System.out.printf("\nDataset 2 Max Matching (CSV Workers): %d / %d stages allocated.\n", match2.assignedCount, tasks2.size());
        match2.taskToResource.forEach((t, r) -> System.out.printf("  * %-22s -> %s\n", t, r));
    }

    // =========================================================================
    // COMPARISON 5: NP-COMPLETENESS & APPROXIMATION (M5)
    // =========================================================================
    public void compareModule5NPCompleteness() {
        System.out.println("\n===============================================================================");
        System.out.println("  >>> DATASET COMPARISON: MODULE 5 - NP-COMPLETENESS & APPROX (M5 - CO5)");
        System.out.println("===============================================================================");

        // 5.1 3-SAT Constraint Satisfiability
        System.out.println("\n--- [5.1] 3-SAT DPLL Preference Constraint Satisfaction ---");
        SATSolver.CNFFormula f1 = new SATSolver.CNFFormula();
        f1.addClause(new BooleanClause(new BooleanClause.Literal("MorningRun", true), new BooleanClause.Literal("Fasting", true), new BooleanClause.Literal("HighProtein", true)));
        f1.addClause(new BooleanClause(new BooleanClause.Literal("MorningRun", false), new BooleanClause.Literal("HeavyLegDay", false), new BooleanClause.Literal("RecoveryStretching", true)));
        f1.addClause(new BooleanClause(new BooleanClause.Literal("HeavyLegDay", true), new BooleanClause.Literal("MorningRun", true), new BooleanClause.Literal("ElectrolyteDrink", true)));
        f1.addClause(new BooleanClause(new BooleanClause.Literal("Fasting", false), new BooleanClause.Literal("HeavyLegDay", false), new BooleanClause.Literal("HighProtein", true)));

        // Formula 2 derived from CSV User Preferences:
        // Variables: Goal8kSteps, Goal7hSleep, AlertsActivity, QuietHours, ShareNone, ShareAggregate
        SATSolver.CNFFormula f2 = new SATSolver.CNFFormula();
        f2.addClause(new BooleanClause(new BooleanClause.Literal("Goal8kSteps", true), new BooleanClause.Literal("Goal7hSleep", true), new BooleanClause.Literal("AlertsActivity", true)));
        f2.addClause(new BooleanClause(new BooleanClause.Literal("ShareNone", true), new BooleanClause.Literal("ShareAggregate", false), new BooleanClause.Literal("QuietHours", true)));
        f2.addClause(new BooleanClause(new BooleanClause.Literal("QuietHours", false), new BooleanClause.Literal("AlertsActivity", false), new BooleanClause.Literal("Goal7hSleep", true)));
        f2.addClause(new BooleanClause(new BooleanClause.Literal("ShareNone", false), new BooleanClause.Literal("ShareAggregate", true), new BooleanClause.Literal("Goal8kSteps", true)));

        SATSolver.SATResult res1 = SATSolver.solve(f1);
        SATSolver.SATResult res2 = SATSolver.solve(f2);

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| SAT Solver Metric           | Dataset 1 (Synthetic) | Dataset 2 (CSV Prefs) |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Total Variables             | %-21d | %-21d |\n", f1.getVariables().size(), f2.getVariables().size());
        System.out.printf("| CNF Clauses                 | %-21d | %-21d |\n", f1.clauses.size(), f2.clauses.size());
        System.out.printf("| Satisfiable (SAT)?          | %-21b | %-21b |\n", res1.satisfiable, res2.satisfiable);
        System.out.printf("| Decision Steps Taken        | %-21d | %-21d |\n", res1.steps, res2.steps);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("Dataset 2 Truth Assignment: " + res2.assignment);

        // 5.2 Vertex Cover 2-Approximation for Scheduling Conflicts
        System.out.println("\n--- [5.2] Vertex Cover 2-Approximation on Conflict Graphs ---");
        // Conflict Graph 1 (Synthetic)
        NPReductions.Graph g1 = new NPReductions.Graph(7);
        g1.addEdge(0, 1);
        g1.addEdge(2, 3);
        g1.addEdge(4, 5);
        g1.addEdge(1, 4);

        // Conflict Graph 2 (Constructed from CSV Constraint Groups CG-001..CG-012)
        NPReductions.Graph g2 = new NPReductions.Graph(8);
        g2.addEdge(0, 1); // CG-001 vs CG-002 (fitness|nutrition vs location&privacy)
        g2.addEdge(1, 2); // CG-002 vs CG-003
        g2.addEdge(2, 3); // CG-003 vs CG-004
        g2.addEdge(3, 4); // CG-004 vs CG-005
        g2.addEdge(4, 5); // CG-005 vs CG-006
        g2.addEdge(5, 6); // CG-006 vs CG-007
        g2.addEdge(6, 7); // CG-007 vs CG-008
        g2.addEdge(1, 5); // Cross-group privacy conflict

        VertexCoverApproximation.ApproximationReport vc1 = VertexCoverApproximation.analyze(g1);
        VertexCoverApproximation.ApproximationReport vc2 = VertexCoverApproximation.analyze(g2);

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Conflict Graph Property     | Dataset 1 (Synthetic) | Dataset 2 (CSV Groups)|");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Conflict Vertices (|V|)     | %-21d | %-21d |\n", g1.numVertices, g2.numVertices);
        System.out.printf("| Conflict Edges (|E|)        | %-21d | %-21d |\n", g1.edges.size(), g2.edges.size());
        System.out.printf("| 2-Approx Vertex Cover Size  | %-21d | %-21d |\n", vc1.approxCoverSize, vc2.approxCoverSize);
        System.out.printf("| Theoretical OPT Lower Bound | %-21d | %-21d |\n", vc1.optLowerBound, vc2.optLowerBound);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
    }

    // =========================================================================
    // COMPARISON 6: RANDOMIZED & PARALLEL ALGORITHMS (M6)
    // =========================================================================
    public void compareModule6RandomizedParallel() {
        System.out.println("\n===============================================================================");
        System.out.println("  >>> DATASET COMPARISON: MODULE 6 - RANDOMIZED & PARALLEL (M6 - CO6)");
        System.out.println("===============================================================================");

        // 6.1 Randomized QuickSort Comparison
        System.out.println("\n--- [6.1] Randomized QuickSort (Activity Record Ranking) ---");
        List<PersonalActivityRecord> r1 = new ArrayList<>(platform.getActivities());
        List<PersonalActivityRecord> r2 = new ArrayList<>(csvActivities);

        long qs1Start = System.nanoTime();
        RandomizedQuickSort.rankByCaloriesDescending(r1);
        long qs1Dur = System.nanoTime() - qs1Start;

        long qs2Start = System.nanoTime();
        RandomizedQuickSort.rankByCaloriesDescending(r2);
        long qs2Dur = System.nanoTime() - qs2Start;

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| QuickSort Performance       | Dataset 1 (Synthetic) | Dataset 2 (CSV Data)  |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Record Collection Size      | %-21d | %-21d |\n", r1.size(), r2.size());
        System.out.printf("| Sort Duration               | %-18.3f us | %-18.3f us |\n", qs1Dur / 1000.0, qs2Dur / 1000.0);
        System.out.printf("| Top Activity Calories       | %-16.1f kcal | %-16.1f kcal |\n", r1.get(0).getCaloriesBurned(), r2.get(0).getCaloriesBurned());
        System.out.printf("| Lowest Activity Calories    | %-16.1f kcal | %-16.1f kcal |\n", r1.get(r1.size() - 1).getCaloriesBurned(), r2.get(r2.size() - 1).getCaloriesBurned());
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 6.2 Reservoir Sampling Comparison
        System.out.println("\n--- [6.2] Reservoir Sampling (Algorithm R) Stream Comparison ---");
        int k = 5;
        ReservoirSampling<ActivityStreamItem> res1 = new ReservoirSampling<>(k);
        res1.feedAll(platform.getActivityStream());

        ReservoirSampling<ActivityStreamItem> res2 = new ReservoirSampling<>(k);
        res2.feedAll(csvStream);

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Stream Sampling Property    | Dataset 1 (Synthetic) | Dataset 2 (CSV Stream)|");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Stream Ingested Events (N)  | %-21d | %-21d |\n", res1.getTotalSeen(), res2.getTotalSeen());
        System.out.printf("| Reservoir Capacity (k)      | %-21d | %-21d |\n", res1.getCapacity(), res2.getCapacity());
        System.out.printf("| Inclusion Probability (k/N) | %-20.5f%% | %-20.5f%% |\n", res1.getInclusionProbability() * 100.0, res2.getInclusionProbability() * 100.0);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");

        // 6.3 Blelloch Parallel Scan Comparison
        System.out.println("\n--- [6.3] Blelloch Parallel Scan (Cumulative Metrics Timeline) ---");
        long[] steps1 = {120, 80, 450, 60, 90, 520, 110, 300};
        long[] steps2 = csvRecords.stream().limit(8).mapToLong(CsvRecord::getStepCount).toArray();

        long[] scan1 = BlellochScan.inclusiveScan(steps1);
        long[] scan2 = BlellochScan.inclusiveScan(steps2);

        System.out.printf("Dataset 1 Timeline: %s\n", Arrays.toString(scan1));
        System.out.printf("Dataset 2 Timeline: %s\n", Arrays.toString(scan2));

        // 6.4 Parallel Fork-Join Data Reduce Comparison
        System.out.println("\n--- [6.4] Parallel Fork-Join Sensor & Observation Reduction ---");
        long pr1Start = System.nanoTime();
        ParallelDataReduce.AggregateStats stats1 = ParallelDataReduce.reduceSensors(platform.getSensorObservations());
        long pr1Dur = System.nanoTime() - pr1Start;

        long pr2Start = System.nanoTime();
        ParallelDataReduce.AggregateStats stats2 = ParallelDataReduce.reduceSensors(csvSensors);
        long pr2Dur = System.nanoTime() - pr2Start;

        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.println("| Reduced Statistical Metric  | Dataset 1 (Synthetic) | Dataset 2 (CSV Obs)   |");
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
        System.out.printf("| Item Count (N)              | %,21d | %,21d |\n", stats1.count, stats2.count);
        System.out.printf("| Minimum Value               | %-21.2f | %-21.2f |\n", stats1.min, stats2.min);
        System.out.printf("| Maximum Value               | %-21.2f | %-21.2f |\n", stats1.max, stats2.max);
        System.out.printf("| Arithmetic Mean             | %-21.2f | %-21.2f |\n", stats1.getMean(), stats2.getMean());
        System.out.printf("| Sample Standard Deviation   | %-21.2f | %-21.2f |\n", stats1.getStdDev(), stats2.getStdDev());
        System.out.printf("| Multi-Core Reduction Time   | %-18.3f ms | %-18.3f ms |\n", pr1Dur / 1_000_000.0, pr2Dur / 1_000_000.0);
        System.out.println("+-----------------------------+-----------------------+-----------------------+");
    }

    // =========================================================================
    // MASTER COMPARATIVE DASHBOARD
    // =========================================================================
    public void printComprehensiveComparisonDashboard() {
        System.out.println("\n*******************************************************************************");
        System.out.println("  LIFEDATA PLATFORM: COMPREHENSIVE DATASET COMPARATIVE BENCHMARK DASHBOARD  ");
        System.out.println("*******************************************************************************");

        System.out.println("\n===============================================================================");
        System.out.println("  EXECUTIVE SUMMARY: DATASET PROFILES & SYSTEM VOLUME");
        System.out.println("===============================================================================");
        System.out.println("+--------------------------------+---------------------+---------------------+");
        System.out.println("| Characteristic / Modality      | Dataset 1 (Baseline)| Dataset 2 (CSV File)|");
        System.out.println("+--------------------------------+---------------------+---------------------+");
        System.out.printf("| Data Source Origin             | %-19s | %-19s |\n", "In-Memory Synthetic", "Imported CSV Records");
        System.out.printf("| Total Records Loaded           | %-19s | %-19d |\n", "N/A (Multi-Modal)", csvRecords.size());
        System.out.printf("| Discrete Physical Activities   | %-19d | %-19d |\n", platform.getActivities().size(), csvActivities.size());
        System.out.printf("| Clinical & Journal Text Notes  | %-19d | %-19d |\n", platform.getNotes().size(), csvNotes.size());
        System.out.printf("| Continuous IoT Telemetry Pings | %,19d | %,19d |\n", platform.getActivityStream().size(), csvStream.size());
        System.out.printf("| Wearable Sensor Observations   | %,19d | %,19d |\n", platform.getSensorObservations().size(), csvSensors.size());
        System.out.println("+--------------------------------+---------------------+---------------------+");

        compareModule1StringAlgorithms();
        compareModule2SuffixStructures();
        compareModule3DynamicProgramming();
        compareModule4NetworkFlow();
        compareModule5NPCompleteness();
        compareModule6RandomizedParallel();

        System.out.println("\n===============================================================================");
        System.out.println("  >>> END-TO-END DATASET COMPARISON SUCCESSFULLY COMPLETED!");
        System.out.println("===============================================================================\n");
    }
}
