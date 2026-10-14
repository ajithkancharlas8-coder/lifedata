package lifedata.analytics;

import lifedata.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Robust CSV Data Loader for the LifeData platform.
 * Parses the 21-column CSV dataset with full support for quoted text,
 * numeric normalization, and conversions to native DSA platform models.
 */
public class CsvDataLoader {

    private static final String[] CANDIDATE_PATHS = {
            "data/lifedata_records.csv",
            "lifedata_records.csv",
            "../data/lifedata_records.csv",
            "C:/DSA project/data/lifedata_records.csv",
            "C:/DSA project/lifedata_records.csv"
    };

    private static List<CsvRecord> cachedRecords = null;

    /**
     * Loads and caches all CSV records from the dataset file.
     */
    public static synchronized List<CsvRecord> loadRecords() {
        if (cachedRecords != null && !cachedRecords.isEmpty()) {
            return cachedRecords;
        }

        File targetFile = findDatasetFile();
        if (targetFile == null || !targetFile.exists()) {
            System.err.println("[WARN] CSV dataset file not found in known paths. Using empty dataset.");
            cachedRecords = Collections.emptyList();
            return cachedRecords;
        }

        List<CsvRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(targetFile, StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            if (line == null) {
                cachedRecords = records;
                return records;
            }

            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                List<String> tokens = parseCsvLine(line);
                if (tokens.size() < 21) {
                    // Skip or pad if missing
                    continue;
                }

                try {
                    String recordId = clean(tokens.get(0));
                    String eventId = clean(tokens.get(1));
                    String userId = clean(tokens.get(2));
                    String timestamp = clean(tokens.get(3));
                    String activityType = clean(tokens.get(4));
                    String category = clean(tokens.get(5));
                    String noteText = clean(tokens.get(6));
                    String deviceType = clean(tokens.get(7));
                    int durationMinutes = parseInt(clean(tokens.get(8)), 0);
                    int stepCount = parseInt(clean(tokens.get(9)), 0);
                    int heartRateBpm = parseInt(clean(tokens.get(10)), 70);
                    double observationValue = parseDouble(clean(tokens.get(11)), 0.0);
                    String observationUnit = clean(tokens.get(12));
                    String userPreferences = clean(tokens.get(13));
                    boolean streamRecord = Boolean.parseBoolean(clean(tokens.get(14)));
                    String eligibleResources = clean(tokens.get(15));
                    String assignedResource = clean(tokens.get(16));
                    String constraintGroup = clean(tokens.get(17));
                    String constraintTags = clean(tokens.get(18));
                    String processingStage = clean(tokens.get(19));
                    double qualityScore = parseDouble(clean(tokens.get(20)), 0.8);

                    CsvRecord record = new CsvRecord(
                            recordId, eventId, userId, timestamp, activityType, category, noteText,
                            deviceType, durationMinutes, stepCount, heartRateBpm, observationValue,
                            observationUnit, userPreferences, streamRecord, eligibleResources,
                            assignedResource, constraintGroup, constraintTags, processingStage,
                            qualityScore
                    );
                    records.add(record);
                } catch (Exception ex) {
                    System.err.printf("[WARN] Error parsing CSV line %d: %s (%s)\n", lineNum, line, ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to read CSV dataset: " + e.getMessage());
        }

        cachedRecords = Collections.unmodifiableList(records);
        return cachedRecords;
    }

    public static File findDatasetFile() {
        for (String path : CANDIDATE_PATHS) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                return f;
            }
        }
        return null;
    }

    public static List<String> parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens;
    }

    private static String clean(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    private static double parseDouble(String s, double fallback) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    public static List<PersonalActivityRecord> loadAsActivities() {
        List<PersonalActivityRecord> list = new ArrayList<>();
        for (CsvRecord r : loadRecords()) {
            list.add(r.toPersonalActivityRecord());
        }
        return list;
    }

    public static List<PersonalNote> loadAsNotes() {
        List<PersonalNote> list = new ArrayList<>();
        for (CsvRecord r : loadRecords()) {
            list.add(r.toPersonalNote());
        }
        return list;
    }

    public static List<SensorObservation> loadAsSensorObservations() {
        List<SensorObservation> list = new ArrayList<>();
        for (CsvRecord r : loadRecords()) {
            list.add(r.toSensorObservation());
        }
        return list;
    }

    public static List<ActivityStreamItem> loadAsStreamItems() {
        List<ActivityStreamItem> list = new ArrayList<>();
        long seq = 1;
        for (CsvRecord r : loadRecords()) {
            if (r.isStreamRecord()) {
                list.add(r.toActivityStreamItem(seq++));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        List<CsvRecord> list = loadRecords();
        System.out.println("Loaded CSV Records count: " + list.size());
        if (!list.isEmpty()) {
            System.out.println("First Record: " + list.get(0));
            System.out.println("Last Record:  " + list.get(list.size() - 1));
            System.out.println("Stream Items count: " + loadAsStreamItems().size());
            System.out.println("Activities count:   " + loadAsActivities().size());
        }
    }
}
