# LifeData: Large-Scale Personal Data Analytics Platform
**Advanced Data Structures & Algorithms (DSA) Comprehensive Implementation**

LifeData models a production-grade personal data analytics platform managing heterogeneous personal health and activity data:
- **Personal Activity and Event Records** (Runs, HIIT, Swimming, Cycling, Strength, Rest)
- **Textual Notes & Journal Entries** (Conditioning reflections, symptoms, technique logs)
- **Wearable & IoT Sensor Observations** (Heart rate, HRV, SpO2, Temperature, Steps)
- **User Preferences & Category Constraints** (Nutrition, training conflicts, recovery goals)
- **Continuously Generated Activity Streams** (Real-time IoT telemetry pings)

---

## 🚀 How to Run Directly in Eclipse IDE

This repository is pre-configured as a native **Eclipse Java Project** (containing `.project`, `.classpath`, and compiler settings).

### Step 1: Open Eclipse IDE
Launch your Eclipse IDE (e.g., Eclipse IDE for Java Developers).

### Step 2: Import the Project
1. In Eclipse, click **File** $\rightarrow$ **Open Projects from File System...** (or **File** $\rightarrow$ **Import...** $\rightarrow$ **General** $\rightarrow$ **Existing Projects into Workspace**).
2. Click **Directory...** and select:
   ```
   C:\DSA project
   ```
3. Ensure `LifeData-Analytics-Platform` is checked.
4. Click **Finish**.

### Step 3: Run the Application
1. In the **Package Explorer**, expand:
   `LifeData-Analytics-Platform` $\rightarrow$ `src` $\rightarrow$ `lifedata`
2. Right-click on **`Main.java`** $\rightarrow$ **Run As** $\rightarrow$ **Java Application**.
3. View the full automated demonstration and use the interactive console menu in Eclipse's **Console view**!

### Step 4: Run Verification Tests
- Right-click **`LifeDataTestSuite.java`** $\rightarrow$ **Run As** $\rightarrow$ **Java Application**.
- Verifies all 31 compliance tests across all 6 modules with a 100% pass rate.

---

## 💻 Alternative Quick Run via Command Line / Batch
Double-click `run.bat` in `C:\DSA project` or run:
```powershell
.\run.bat
```

---

## 🧠 DSA Module Mapping & Architecture

```
C:\DSA project\src\lifedata\
├── Main.java                          (Application Entry Point)
├── LifeDataTestSuite.java             (Complete Algorithmic Test Suite)
├── model/
│   ├── PersonalActivityRecord.java    (Activity records: calories, duration, HR, fatigue)
│   ├── PersonalNote.java              (Text notes, tags, journals)
│   ├── SensorObservation.java         (Wearable IoT telemetry readings)
│   ├── UserPreference.java            (User goals and category preferences)
│   └── ActivityStreamItem.java        (Lightweight stream events)
├── m1_strings/
│   ├── KMPMatcher.java                (Knuth-Morris-Pratt: Prefix function π)
│   ├── ZFunctionPatternMatcher.java   (Z-Algorithm: Pattern & repeated prefix mining)
│   ├── RabinKarpMatcher.java          (Rolling polynomial hash for record IDs)
│   └── AhoCorasickAutomaton.java      (Multi-pattern Aho-Corasick automaton)
├── m2_suffix/
│   ├── SuffixArray.java               (Prefix-doubling Suffix Array & binary search)
│   ├── SAIS.java                      (Linear-time O(N) Suffix Array Induced Sorting)
│   ├── KasaiLCP.java                  (Kasai's algorithm O(N) for repeated phrases)
│   ├── SuffixAutomaton.java           (Minimal DFA Suffix Automaton for O(|P|) queries)
│   └── SuffixTree.java                (Compact Suffix Tree representation)
├── m3_dp/
│   ├── DamerauLevenshteinDistance.java (Edit distance with adjacent transpositions)
│   ├── WeightedEditDistance.java      (QWERTY physical keyboard layout penalty DP)
│   ├── ActivityBitmaskDP.java         (2^N state exploration for optimal scheduling)
│   └── MatrixChainOptimization.java   (MCM DP for sensor pipeline ordering)
├── m4_networkflow/
│   ├── FlowEdge.java                  (Directed residual flow edge)
│   ├── FlowNetwork.java               (Flow network graph structure)
│   ├── FordFulkerson.java             (DFS augmenting paths)
│   ├── EdmondsKarp.java               (BFS shortest augmenting paths - O(V * E^2))
│   ├── DinicAlgorithm.java            (Level graph BFS + blocking flow DFS - O(V^2 * E))
│   ├── BipartiteMatching.java         (Task-to-compute-resource assignment)
│   └── MinCutBottleneckAnalyzer.java  (Max-Flow Min-Cut bottleneck isolation)
├── m5_np/
│   ├── BooleanClause.java             (CNF clause and literal representation)
│   ├── SATSolver.java                 (DPLL 3-SAT solver with unit propagation)
│   ├── NPReductions.java              (3-SAT -> CLIQUE -> INDEPENDENT SET -> VERTEX COVER)
│   └── VertexCoverApproximation.java  (2-Approximation for activity conflict resolution)
├── m6_random_parallel/
│   ├── RandomizedQuickSort.java       (Randomized pivot 3-way Dutch National Flag sort)
│   ├── ReservoirSampling.java         (Algorithm R uniform continuous stream sampling)
│   ├── MillerRabinPrimality.java      (Deterministic 64-bit Miller-Rabin test)
│   ├── BlellochScan.java              (Parallel prefix sum up-sweep and down-sweep)
│   ├── ParallelDataReduce.java        (Fork-Join tree reduction for large datasets)
│   └── BrentsTheoremEvaluator.java    (Theoretical speedup, span, and work bounds)
├── analytics/
│   ├── SampleDataGenerator.java       (Synthetic multi-modal health dataset)
│   └── LifeDataPlatform.java          (End-to-end platform coordinator)
└── cli/
    └── LifeDataMenu.java              (Interactive console terminal interface)
```

---

## 📊 Detailed Algorithm Analysis & Theoretical Complexities

| Module | Algorithm / Structure | Purpose in LifeData | Time Complexity | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **M1** | **KMP (Knuth-Morris-Pratt)** | Keyword search in personal notes | $\mathcal{O}(N + M)$ | $\mathcal{O}(M)$ |
| **M1** | **Z-Function** | Detect repeating patterns in habit logs | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ |
| **M1** | **Rabin-Karp** | Locate stream record IDs via rolling hash | $\mathcal{O}(N + M)$ avg | $\mathcal{O}(1)$ |
| **M1** | **Aho-Corasick** | Concurrently match multiple health terms | $\mathcal{O}(N + \sum M + K)$ | $\mathcal{O}(\sum M)$ |
| **M2** | **SA-IS** | Linear-time Suffix Array construction | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ |
| **M2** | **Kasai's LCP** | Extract repeated phrases & doc similarity | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ |
| **M2** | **Suffix Automaton (SAM)**| Minimal DFA for $\mathcal{O}(\|P\|)$ substring queries | $\mathcal{O}(N)$ build, $\mathcal{O}(\|P\|)$ query | $\mathcal{O}(N)$ |
| **M2** | **Suffix Tree** | Hierarchical prefix & suffix tree | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ |
| **M3** | **Damerau-Levenshtein** | Auto-correct misspelled activity names | $\mathcal{O}(N \cdot M)$ | $\mathcal{O}(N \cdot M)$ |
| **M3** | **Weighted Edit Distance** | QWERTY physical distance penalty matrix | $\mathcal{O}(N \cdot M)$ | $\mathcal{O}(N \cdot M)$ |
| **M3** | **Bitmask DP** | Optimal daily activity subset & schedule | $\mathcal{O}(2^N \cdot N^2)$ | $\mathcal{O}(2^N \cdot N)$ |
| **M3** | **Matrix-Chain Mult (MCM)**| Sensor data transformation matrix pipeline | $\mathcal{O}(K^3)$ | $\mathcal{O}(K^2)$ |
| **M4** | **Ford-Fulkerson** | Baseline resource flow modeling | $\mathcal{O}(E \cdot F)$ | $\mathcal{O}(V + E)$ |
| **M4** | **Edmonds-Karp** | BFS shortest augmenting path flow | $\mathcal{O}(V \cdot E^2)$ | $\mathcal{O}(V + E)$ |
| **M4** | **Dinic's Algorithm** | Level graph + blocking flow DFS | $\mathcal{O}(V^2 \cdot E)$ | $\mathcal{O}(V + E)$ |
| **M4** | **Bipartite Matching** | Allocate tasks to heterogeneous workers | $\mathcal{O}(E \sqrt{V})$ | $\mathcal{O}(V + E)$ |
| **M4** | **Min-Cut Bottleneck** | Identify saturated pipeline bottlenecks | $\mathcal{O}(V^2 \cdot E)$ | $\mathcal{O}(V + E)$ |
| **M5** | **3-SAT (DPLL)** | Model personal activity constraint rules | Exponential worst-case | $\mathcal{O}(\text{Vars})$ |
| **M5** | **Karp Reductions** | 3-SAT $\to$ CLIQUE $\to$ IS $\to$ VC | Polynomial $\mathcal{O}(k^2)$ | $\mathcal{O}(k^2)$ |
| **M5** | **Vertex Cover 2-Approx**| Resolve overlapping activity conflicts | $\mathcal{O}(V + E)$ (2.0x OPT) | $\mathcal{O}(V)$ |
| **M6** | **Randomized QuickSort** | Rank activity logs by calories/intensity | Expected $\mathcal{O}(N \log N)$ | $\mathcal{O}(\log N)$ stack |
| **M6** | **Reservoir Sampling** | Uniform $K$-sample from infinite stream | $\mathcal{O}(1)$ per item | $\mathcal{O}(K)$ |
| **M6** | **Miller-Rabin Primality**| Timestamp hashing and hash seeds | $\mathcal{O}(k \log^3 n)$ | $\mathcal{O}(1)$ |
| **M6** | **Blelloch Parallel Scan**| Cumulative steps/calories timeline | Work $\mathcal{O}(N)$, Span $\mathcal{O}(\log N)$ | $\mathcal{O}(N)$ |
| **M6** | **Parallel Data Reduce** | Multi-core Fork-Join sensor aggregation | Work $\mathcal{O}(N)$, Span $\mathcal{O}(\log N)$ | $\mathcal{O}(\log N)$ |
| **M6** | **Brent's Theorem** | Multi-core speedup & efficiency bounds | $T_p \le \frac{T_1 - T_\infty}{p} + T_\infty$ | $\mathcal{O}(1)$ |

---

## 🧪 Verification & Test Results
Running `LifeDataTestSuite`:
```
=================================================================
     LIFEDATA ALGORITHMIC VERIFICATION & COMPLIANCE TEST SUITE    
=================================================================
[Testing Module 1: String Algorithms]          --> 7 / 7 PASSED
[Testing Module 2: Suffix Structures]          --> 6 / 6 PASSED
[Testing Module 3: Advanced DP]                --> 4 / 4 PASSED
[Testing Module 4: Network Flow]               --> 5 / 5 PASSED
[Testing Module 5: NP-Completeness & Approx]   --> 3 / 3 PASSED
[Testing Module 6: Randomized & Parallel]      --> 6 / 6 PASSED
=================================================================
Test Execution Complete: 31 / 31 Tests PASSED (100% Success Rate)
=================================================================
```
