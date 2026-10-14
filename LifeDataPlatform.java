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
 * LifeDataPlatform: Main orchestrator connecting all 6 DSA modules
 * to personal health analytics, sensor streams, notes, and activity scheduling.
 */
public class LifeDataPlatform {

    private final List<PersonalActivityRecord> activities;
    private final List<PersonalNote> notes;
    private final List<SensorObservation> sensorObservations;
    private final List<ActivityStreamItem> activityStream;

    private final List<CsvRecord> csvRecords;
    private final List<PersonalActivityRecord> csvActivities;
    private final List<PersonalNote> csvNotes;
    private final List<SensorObservation> csvSensorObservations;
    private final List<ActivityStreamItem> csvActivityStream;
    private final DatasetComparator comparator;

    public LifeDataPlatform() {
        this.activities = SampleDataGenerator.generateActivities();
        this.notes = SampleDataGenerator.generateNotes();
        this.sensorObservations = SampleDataGenerator.generateSensorObservations(10000);
        this.activityStream = SampleDataGenerator.generateStream(5000);

        this.csvRecords = CsvDataLoader.loadRecords();
        this.csvActivities = CsvDataLoader.loadAsActivities();
        this.csvNotes = CsvDataLoader.loadAsNotes();
        this.csvSensorObservations = CsvDataLoader.loadAsSensorObservations();
        this.csvActivityStream = CsvDataLoader.loadAsStreamItems();
        this.comparator = new DatasetComparator(this);
    }

    public List<PersonalActivityRecord> getActivities() { return activities; }
    public List<PersonalNote> getNotes() { return notes; }
    public List<SensorObservation> getSensorObservations() { return sensorObservations; }
    public List<ActivityStreamItem> getActivityStream() { return activityStream; }

    public List<CsvRecord> getCsvRecords() { return csvRecords; }
    public List<PersonalActivityRecord> getCsvActivities() { return csvActivities; }
    public List<PersonalNote> getCsvNotes() { return csvNotes; }
    public List<SensorObservation> getCsvSensorObservations() { return csvSensorObservations; }
    public List<ActivityStreamItem> getCsvActivityStream() { return csvActivityStream; }
    public DatasetComparator getComparator() { return comparator; }

    // ==========================================
    // MODULE 1: String Algorithms (CO1)
    // ==========================================
    public void runModule1StringAlgorithms() {
        System.out.println("\n========================================================");
        System.out.println(">>> MODULE 1: STRING ALGORITHMS (M1 - CO1)");
        System.out.println("========================================================");

        String combinedNotes = notes.stream().map(PersonalNote::getContent).collect(Collectors.joining(" | "));
        String targetKeyword = "fatigue";

        System.out.println("\n--- 1.1 Knuth-Morris-Pratt (KMP) Keyword Search ---");
        System.out.println("Target Keyword: \"" + targetKeyword + "\"");
        List<Integer> kmpHits = KMPMatcher.search(combinedNotes, targetKeyword);
        System.out.printf("Found %d matches in corpus of %d characters.\n", kmpHits.size(), combinedNotes.length());
        List<String> snippets = KMPMatcher.extractContextSnippets(combinedNotes, targetKeyword, 25);
        snippets.forEach(s -> System.out.println("  " + s));

        System.out.println("\n--- 1.2 Z-Function Repeated Pattern Detection ---");
        String sampleText = "recovery hydrate rest recovery hydrate sleep recovery hydrate";
        int[] z = ZFunctionPatternMatcher.calculateZ(sampleText);
        String repPattern = ZFunctionPatternMatcher.findLongestRepeatedPrefixPattern(sampleText);
        System.out.println("Text: \"" + sampleText + "\"");
        System.out.println("Z-Array for first 15 chars: " + Arrays.toString(Arrays.copyOf(z, Math.min(15, z.length))));
        System.out.println("Detected Longest Repeated Prefix Pattern: \"" + repPattern + "\"");

        System.out.println("\n--- 1.3 Rabin-Karp Rolling Hash for Record Identifiers ---");
        String streamIdsText = activityStream.stream().map(ActivityStreamItem::getEventId).collect(Collectors.joining(","));
        String searchTargetId = "EVT-STREAM-5042";
        System.out.println("Searching for stream record ID: " + searchTargetId);
        long rkStart = System.nanoTime();
        List<Integer> rkMatches = RabinKarpMatcher.search(streamIdsText, searchTargetId);
        long rkDuration = System.nanoTime() - rkStart;
        System.out.printf("Rabin-Karp matched at text index %s in %.3f ms\n", rkMatches, rkDuration / 1_000_000.0);

        System.out.println("\n--- 1.4 Aho-Corasick Multi-Pattern Automaton ---");
        AhoCorasickAutomaton ac = new AhoCorasickAutomaton();
        List<String> healthVocab = Arrays.asList("heart rate", "cardio", "fatigue", "recovery", "hydration", "swimming", "sleep");
        ac.addPatterns(healthVocab);
        ac.buildAutomaton();
        System.out.println("Simultaneously matching dictionary: " + healthVocab);
        List<AhoCorasickAutomaton.Match> acMatches = ac.searchAll(combinedNotes);
        System.out.printf("Total concurrent matches found: %d\n", acMatches.size());
        Map<String, Integer> freq = ac.countKeywordFrequencies(combinedNotes);
        freq.forEach((term, count) -> System.out.printf("  * %-15s : %d occurrences\n", term, count));

        // Side-by-side comparative analytics across Dataset 1 (Synthetic) and Dataset 2 (CSV)
        comparator.compareModule1StringAlgorithms();
    }

    // ==========================================
    // MODULE 2: Suffix Structures (CO2)
    // ==========================================
    public void runModule2SuffixStructures() {
        System.out.println("\n========================================================");
        System.out.println(">>> MODULE 2: SUFFIX STRUCTURES - DOCUMENT SIMILARITY (M2 - CO2)");
        System.out.println("========================================================");

        String noteCorpus = "trail run endurance conditioning. trail run endurance recovery. trail run cardio stamina.";

        System.out.println("\n--- 2.1 Suffix Array (SA-IS Linear Time Construction) ---");
        long saisStart = System.nanoTime();
        int[] saLinear = SAIS.buildSuffixArray(noteCorpus);
        long saisDuration = System.nanoTime() - saisStart;
        System.out.printf("Built Suffix Array for %d characters in %.3f ms via SA-IS.\n", noteCorpus.length(), saisDuration / 1_000_000.0);
        System.out.println("First 8 suffix positions: " + Arrays.toString(Arrays.copyOf(saLinear, Math.min(8, saLinear.length))));

        SuffixArray saObj = new SuffixArray(noteCorpus, saLinear);
        String query = "trail run";
        int[] hits = saObj.search(query);
        System.out.printf("Binary search for \"%s\": found %d occurrences at indices %s\n", query, hits.length, Arrays.toString(hits));

        System.out.println("\n--- 2.2 Kasai's LCP (Longest Common Prefix) Algorithm ---");
        int[] lcp = KasaiLCP.buildLCP(noteCorpus, saLinear);
        String longestRep = KasaiLCP.findLongestRepeatedPhrase(noteCorpus, saLinear, lcp);
        System.out.println("Longest Repeated Phrase across personal logs: \"" + longestRep + "\"");
        List<KasaiLCP.RepeatedPhrase> repeatedPhrases = KasaiLCP.findRepeatedPhrases(noteCorpus, saLinear, lcp, 9);
        repeatedPhrases.forEach(p -> System.out.println("  * " + p));

        System.out.println("\n--- 2.3 Suffix Automaton (SAM) Substring Queries ---");
        SuffixAutomaton sam = new SuffixAutomaton(noteCorpus);
        System.out.println("Automaton distinct substrings count: " + sam.countDistinctSubstrings());
        String testSub = "endurance conditioning";
        System.out.printf("Contains substring \"%s\"? -> %b (O(|P|) query)\n", testSub, sam.containsSubstring(testSub));

        String doc1 = notes.get(0).getContent();
        String doc2 = notes.get(1).getContent();
        String commonCrossDoc = KasaiLCP.findLongestCommonSubstring(doc1, doc2);
        System.out.println("\nLongest Common Phrase between Note #1 and Note #2: \"" + commonCrossDoc + "\"");

        System.out.println("\n--- 2.4 Suffix Tree Hierarchical Query ---");
        SuffixTree tree = new SuffixTree("running and swimming and running");
        List<Integer> treeHits = tree.search("running");
        System.out.println("Suffix Tree search for \"running\": indices = " + treeHits);

        // Side-by-side comparative analytics across Dataset 1 (Synthetic) and Dataset 2 (CSV)
        comparator.compareModule2SuffixStructures();
    }

    // ==========================================
    // MODULE 3: Advanced Dynamic Programming (CO3)
    // ==========================================
    public void runModule3DynamicProgramming() {
        System.out.println("\n========================================================");
        System.out.println(">>> MODULE 3: ADVANCED DYNAMIC PROGRAMMING (M3 - CO3)");
        System.out.println("========================================================");

        System.out.println("\n--- 3.1 Damerau-Levenshtein Typo Correction (with Transposition) ---");
        List<String> validActivities = Arrays.asList("running", "swimming", "cycling", "weightlifting", "calisthenics", "meditation", "stretching");
        String misspelled = "runnign"; // transposition of 'g' and 'n'
        int dLev = DamerauLevenshteinDistance.computeDamerauLevenshtein(misspelled, "running");
        int lev = DamerauLevenshteinDistance.computeLevenshtein(misspelled, "running");
        System.out.printf("Misspelled input: \"%s\"\n", misspelled);
        System.out.printf("  Classic Levenshtein Distance:        %d (requires substitute + substitute)\n", lev);
        System.out.printf("  Damerau-Levenshtein Distance:        %d (single transposition detected!)\n", dLev);
        System.out.println("  Auto-corrected suggestion:           " + DamerauLevenshteinDistance.autoCorrect(misspelled, validActivities));

        System.out.println("\n--- 3.2 Weighted Edit Distance (QWERTY Keyboard Proximity) ---");
        String s1 = "cardio";
        String s2_close = "vardio"; // 'v' is physically adjacent to 'c'
        String s2_far = "pardio";   // 'p' is far away from 'c'
        System.out.printf("Distance (\"%s\", \"%s\") [Adjacent 'c'-'v']: %.2f penalty\n", s1, s2_close, WeightedEditDistance.compute(s1, s2_close));
        System.out.printf("Distance (\"%s\", \"%s\") [Far 'c'-'p']:      %.2f penalty\n", s1, s2_far, WeightedEditDistance.compute(s1, s2_far));

        System.out.println("\n--- 3.3 Bitmask DP: Optimal Daily Activity Schedule ---");
        List<ActivityBitmaskDP.ActivityCandidate> candidates = new ArrayList<>();
        candidates.add(new ActivityBitmaskDP.ActivityCandidate(0, "Morning Yoga", 30, 120, 1, 85));
        candidates.add(new ActivityBitmaskDP.ActivityCandidate(1, "Tempo Run", 45, 500, 4, 90));
        candidates.add(new ActivityBitmaskDP.ActivityCandidate(2, "Heavy Squats", 60, 400, 5, 80));
        candidates.add(new ActivityBitmaskDP.ActivityCandidate(3, "Sauna Recovery", 25, 90, 1, 75));
        candidates.add(new ActivityBitmaskDP.ActivityCandidate(4, "Core HIIT", 20, 250, 3, 70));
        candidates.add(new ActivityBitmaskDP.ActivityCandidate(5, "Evening Stroll", 30, 110, 1, 65));

        // Synergies: Yoga before Run (+15), Squats followed by Sauna (+25)
        double[][] synergy = new double[6][6];
        synergy[0][1] = 15.0;
        synergy[2][3] = 25.0;

        int maxTime = 120; // 2 hours max
        int maxFatigue = 7; // max fatigue budget
        ActivityBitmaskDP.ScheduleResult schedule = ActivityBitmaskDP.solveOptimalSchedule(candidates, synergy, maxTime, maxFatigue);
        System.out.println(schedule);

        System.out.println("\n--- 3.4 Matrix-Chain Multiplication (MCM) Pipeline Optimization ---");
        // Pipeline: Raw Sensors (10000x20) x Filter (20x20) x Feature Transform (20x5) x Classifier (5x1)
        int[] dims = {10000, 20, 20, 5, 1};
        String[] stageNames = {"RawSensors", "FilterMatrix", "FeatureProj", "Classifier"};
        MatrixChainOptimization.MCMResult mcm = MatrixChainOptimization.optimize(dims, stageNames);
        System.out.println(mcm);

        // Side-by-side comparative analytics across Dataset 1 (Synthetic) and Dataset 2 (CSV)
        comparator.compareModule3DynamicProgramming();
    }

    // ==========================================
    // MODULE 4: Network Flow (CO4)
    // ==========================================
    public void runModule4NetworkFlow() {
        System.out.println("\n========================================================");
        System.out.println(">>> MODULE 4: NETWORK FLOW & RESOURCE ALLOCATION (M4 - CO4)");
        System.out.println("========================================================");

        // Build data pipeline network:
        // 0: Source (Wearable Ingestion)
        // 1: Local Preprocessing Buffer
        // 2: Edge Feature Extractor
        // 3: Cloud Streaming Ingest
        // 4: Analytics Storage & ML Sink
        FlowNetwork net = new FlowNetwork(5);
        net.addEdge(0, 1, 100);
        net.addEdge(0, 2, 50);
        net.addEdge(1, 2, 40);
        net.addEdge(1, 3, 70);
        net.addEdge(2, 4, 80);
        net.addEdge(3, 4, 60);

        System.out.println("\n--- 4.1 Flow Algorithm Comparison (Throughput Modeling) ---");
        long ffFlow = FordFulkerson.computeMaxFlow(net.copy(), 0, 4);
        long ekFlow = EdmondsKarp.computeMaxFlow(net.copy(), 0, 4);
        long dinicFlow = DinicAlgorithm.computeMaxFlow(net.copy(), 0, 4);

        System.out.printf("Ford-Fulkerson Max Throughput: %d records/sec\n", ffFlow);
        System.out.printf("Edmonds-Karp   Max Throughput: %d records/sec\n", ekFlow);
        System.out.printf("Dinic's Flow   Max Throughput: %d records/sec\n", dinicFlow);

        System.out.println("\n--- 4.2 Bipartite Matching for Task-Resource Allocation ---");
        List<String> tasks = Arrays.asList("ECG Filtering", "Sleep Stage AI", "GPS Elevation Profile", "Calorie Aggregation", "Stress Score Model");
        List<String> resources = Arrays.asList("Wearable NPU", "Local CPU-Core1", "Local CPU-Core2", "Cloud Worker A", "Cloud GPU Node");

        Map<String, List<String>> eligibility = new HashMap<>();
        eligibility.put("ECG Filtering", Arrays.asList("Wearable NPU", "Local CPU-Core1"));
        eligibility.put("Sleep Stage AI", Arrays.asList("Local CPU-Core2", "Cloud GPU Node"));
        eligibility.put("GPS Elevation Profile", Arrays.asList("Local CPU-Core1", "Cloud Worker A"));
        eligibility.put("Calorie Aggregation", Arrays.asList("Wearable NPU", "Local CPU-Core2"));
        eligibility.put("Stress Score Model", Arrays.asList("Cloud GPU Node"));

        BipartiteMatching.TaskResourceAssignment assignment = BipartiteMatching.allocateTasks(tasks, resources, eligibility);
        System.out.println(assignment);

        System.out.println("\n--- 4.3 Min-Cut Bottleneck Analysis ---");
        Map<Integer, String> labels = Map.of(
                0, "Ingestion (Wearables)",
                1, "Local Buffer",
                2, "Edge Extractor",
                3, "Cloud Stream Ingest",
                4, "Analytics Sink"
        );
        MinCutBottleneckAnalyzer.BottleneckReport bottleneck = MinCutBottleneckAnalyzer.findBottlenecks(net.copy(), 0, 4, labels);
        System.out.println(bottleneck);

        // Side-by-side comparative analytics across Dataset 1 (Synthetic) and Dataset 2 (CSV)
        comparator.compareModule4NetworkFlow();
    }

    // ==========================================
    // MODULE 5: NP-Completeness and Approximation (CO5)
    // ==========================================
    public void runModule5NPCompleteness() {
        System.out.println("\n========================================================");
        System.out.println(">>> MODULE 5: NP-COMPLETENESS & APPROXIMATION (M5 - CO5)");
        System.out.println("========================================================");

        System.out.println("\n--- 5.1 3-SAT Activity Preference Constraints (DPLL Solver) ---");
        SATSolver.CNFFormula formula = new SATSolver.CNFFormula();

        // Preference rules:
        // 1. (MorningRun OR Fasting OR HighProtein)
        // 2. (NOT MorningRun OR NOT HeavyLegDay OR RecoveryStretching)
        // 3. (HeavyLegDay OR MorningRun OR ElectrolyteDrink)
        // 4. (NOT Fasting OR NOT HeavyLegDay OR HighProtein)
        formula.addClause(new BooleanClause(
                new BooleanClause.Literal("MorningRun", true),
                new BooleanClause.Literal("Fasting", true),
                new BooleanClause.Literal("HighProtein", true)
        ));
        formula.addClause(new BooleanClause(
                new BooleanClause.Literal("MorningRun", false),
                new BooleanClause.Literal("HeavyLegDay", false),
                new BooleanClause.Literal("RecoveryStretching", true)
        ));
        formula.addClause(new BooleanClause(
                new BooleanClause.Literal("HeavyLegDay", true),
                new BooleanClause.Literal("MorningRun", true),
                new BooleanClause.Literal("ElectrolyteDrink", true)
        ));
        formula.addClause(new BooleanClause(
                new BooleanClause.Literal("Fasting", false),
                new BooleanClause.Literal("HeavyLegDay", false),
                new BooleanClause.Literal("HighProtein", true)
        ));

        System.out.println("Input 3-SAT Preference Formula:\n  " + formula);
        SATSolver.SATResult satResult = SATSolver.solve(formula);
        System.out.println("\n" + satResult);

        System.out.println("\n--- 5.2 Karp Reduction Chain (3-SAT -> CLIQUE -> IS -> VC) ---");
        NPReductions.ReductionResult cliqueRes = NPReductions.reduce3SatToClique(formula);
        System.out.println(cliqueRes.description);

        NPReductions.ReductionResult isRes = NPReductions.reduceCliqueToIndependentSet(cliqueRes);
        System.out.println(isRes.description);

        NPReductions.ReductionResult vcRes = NPReductions.reduceIndependentSetToVertexCover(isRes);
        System.out.println(vcRes.description);

        System.out.println("\n--- 5.3 Vertex Cover 2-Approximation for Activity Conflicts ---");
        // Personal conflict graph: schedule overlaps / equipment conflicts
        NPReductions.Graph conflictGraph = new NPReductions.Graph(7);
        conflictGraph.vertexLabels.put(0, "Morning Run 07:00");
        conflictGraph.vertexLabels.put(1, "Gym Squats 07:30 [Conflict with Run]");
        conflictGraph.vertexLabels.put(2, "Team Meeting 08:30");
        conflictGraph.vertexLabels.put(3, "Physio Rehab 08:45 [Conflict with Meeting]");
        conflictGraph.vertexLabels.put(4, "HIIT Class 17:30");
        conflictGraph.vertexLabels.put(5, "Swimming Lane 17:45 [Conflict with HIIT]");
        conflictGraph.vertexLabels.put(6, "Evening Meditation 21:00");

        // Conflicts
        conflictGraph.addEdge(0, 1);
        conflictGraph.addEdge(2, 3);
        conflictGraph.addEdge(4, 5);
        conflictGraph.addEdge(1, 4); // Shared muscular fatigue conflict

        VertexCoverApproximation.ApproximationReport vcReport = VertexCoverApproximation.analyze(conflictGraph);
        System.out.println(vcReport);

        // Side-by-side comparative analytics across Dataset 1 (Synthetic) and Dataset 2 (CSV)
        comparator.compareModule5NPCompleteness();
    }

    // ==========================================
    // MODULE 6: Randomized and Parallel Algorithms (CO6)
    // ==========================================
    public void runModule6RandomizedAndParallel() {
        System.out.println("\n========================================================");
        System.out.println(">>> MODULE 6: RANDOMIZED & PARALLEL ALGORITHMS (M6 - CO6)");
        System.out.println("========================================================");

        System.out.println("\n--- 6.1 Randomized QuickSort (Activity Record Ranking) ---");
        List<PersonalActivityRecord> toRank = new ArrayList<>(activities);
        System.out.println("Unranked Activities count: " + toRank.size());
        RandomizedQuickSort.rankByCaloriesDescending(toRank);
        System.out.println("Top 4 Activities by Calories Burned (Descending):");
        for (int i = 0; i < Math.min(4, toRank.size()); i++) {
            System.out.println("  " + toRank.get(i));
        }

        System.out.println("\n--- 6.2 Reservoir Sampling (Continuous Sensor Stream) ---");
        int sampleCapacity = 5;
        ReservoirSampling<ActivityStreamItem> sampler = new ReservoirSampling<>(sampleCapacity);
        sampler.feedAll(activityStream);
        System.out.printf("Stream Ingestion: %d continuous events streamed.\n", sampler.getTotalSeen());
        System.out.printf("Reservoir Capacity: %d items | Uniform Inclusion Probability: %.4f%%\n",
                sampler.getCapacity(), sampler.getInclusionProbability() * 100.0);
        System.out.println("Maintained Uniform Random Sample:");
        sampler.getSample().forEach(item -> System.out.println("  " + item));

        System.out.println("\n--- 6.3 Miller-Rabin Primality Test (Cryptographic Timestamp / Hash Seeds) ---");
        long testPrime1 = 1_000_000_007L;
        long testComposite = 1_000_000_009L;
        long largePrime = 2_147_483_647L; // 2^31 - 1 Mersenne prime
        System.out.printf("Miller-Rabin isPrime(%d) -> %b\n", testPrime1, MillerRabinPrimality.isPrime(testPrime1));
        System.out.printf("Miller-Rabin isPrime(%d) -> %b\n", testComposite, MillerRabinPrimality.isPrime(testComposite));
        System.out.printf("Miller-Rabin isPrime(%d) -> %b\n", largePrime, MillerRabinPrimality.isPrime(largePrime));
        long nextP = MillerRabinPrimality.nextPrime(10_000_000);
        System.out.printf("Next prime after 10,000,000 for hash table: %d\n", nextP);

        System.out.println("\n--- 6.4 Blelloch Parallel Scan (Cumulative Daily Calories) ---");
        long[] hourlyCalories = {120, 80, 450, 60, 90, 520, 110, 300};
        long[] cumulativeCalories = BlellochScan.inclusiveScan(hourlyCalories);
        System.out.println("Hourly Calories Burned:     " + Arrays.toString(hourlyCalories));
        System.out.println("Cumulative Timeline (Scan): " + Arrays.toString(cumulativeCalories));

        System.out.println("\n--- 6.5 Parallel Data Reduce (Fork-Join Sensor Analytics) ---");
        List<SensorObservation> largeSensorList = SampleDataGenerator.generateSensorObservations(50000);
        long redStart = System.nanoTime();
        ParallelDataReduce.AggregateStats stats = ParallelDataReduce.reduceSensors(largeSensorList);
        long redDur = System.nanoTime() - redStart;
        System.out.printf("Reduced %,d Sensor Observations in %.3f ms across CPU cores:\n",
                largeSensorList.size(), redDur / 1_000_000.0);
        System.out.println("  " + stats);

        System.out.println("\n--- 6.6 Brent's Theorem Performance Evaluation ---");
        int[] cores = {1, 2, 4, 8, 16, 32};
        BrentsTheoremEvaluator.AnalysisReport reduceAnalysis =
                BrentsTheoremEvaluator.evaluateParallelReduce(1000000, cores);
        System.out.println(reduceAnalysis);

        BrentsTheoremEvaluator.AnalysisReport scanAnalysis =
                BrentsTheoremEvaluator.evaluateBlellochScan(1000000, cores);
        System.out.println(scanAnalysis);

        // Side-by-side comparative analytics across Dataset 1 (Synthetic) and Dataset 2 (CSV)
        comparator.compareModule6RandomizedParallel();
    }

    public void runComprehensiveDatasetComparison() {
        comparator.printComprehensiveComparisonDashboard();
    }

    public void runFullEndToEndDemo() {
        System.out.println("\n*******************************************************************************");
        System.out.println("  LIFEDATA: LARGE-SCALE PERSONAL DATA ANALYTICS PLATFORM - FULL SYSTEM DEMO  ");
        System.out.println("*******************************************************************************");
        runModule1StringAlgorithms();
        runModule2SuffixStructures();
        runModule3DynamicProgramming();
        runModule4NetworkFlow();
        runModule5NPCompleteness();
        runModule6RandomizedAndParallel();

        System.out.println("\n========================================================");
        System.out.println(">>> ALL 6 DSA MODULES SUCCESSFULLY EXECUTED!");
        System.out.println("========================================================\n");

        // Present master comparative dashboard summarizing baseline vs CSV results
        comparator.printComprehensiveComparisonDashboard();
    }
}
