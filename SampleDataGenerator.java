package lifedata.analytics;

import lifedata.model.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Generates realistic personal activity data, health notes, sensor streams, and user preferences.
 */
public class SampleDataGenerator {

    public static List<PersonalActivityRecord> generateActivities() {
        List<PersonalActivityRecord> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        list.add(new PersonalActivityRecord("ACT-101", "USR-7", "MORNING_RUN", now.minusDays(6).withHour(7).withMinute(0),
                45, 480.0, 155, 6, "Morning trail run with moderate tempo and elevation climb."));
        list.add(new PersonalActivityRecord("ACT-102", "USR-7", "HIIT_CIRCUIT", now.minusDays(5).withHour(18).withMinute(30),
                35, 390.0, 168, 8, "High intensity interval training with kettlebell swings and burpees."));
        list.add(new PersonalActivityRecord("ACT-103", "USR-7", "SWIMMING", now.minusDays(4).withHour(8).withMinute(15),
                50, 450.0, 142, 5, "Endurance swim 2000m freestyle at local aquatic center."));
        list.add(new PersonalActivityRecord("ACT-104", "USR-7", "CYCLING", now.minusDays(3).withHour(16).withMinute(0),
                75, 620.0, 148, 7, "Long road cycling route along riverfront parkway."));
        list.add(new PersonalActivityRecord("ACT-105", "USR-7", "POWER_LIFTING", now.minusDays(2).withHour(17).withMinute(45),
                60, 310.0, 130, 9, "Heavy deadlifts and bench press strength workout. High CNS fatigue."));
        list.add(new PersonalActivityRecord("ACT-106", "USR-7", "YOGA_RECOVERY", now.minusDays(1).withHour(20).withMinute(0),
                40, 140.0, 95, 2, "Restorative yoga mobility flow and breathing mindfulness."));
        list.add(new PersonalActivityRecord("ACT-107", "USR-7", "INTERVAL_SPRINT", now.withHour(9).withMinute(30),
                30, 360.0, 175, 8, "Track sprint repetitions with 2-minute walking recovery intervals."));
        list.add(new PersonalActivityRecord("ACT-108", "USR-7", "AFTERNOON_WALK", now.withHour(13).withMinute(0),
                25, 110.0, 102, 1, "Brisk lunch walk to hit daily step target and sunshine exposure."));

        return list;
    }

    public static List<PersonalNote> generateNotes() {
        List<PersonalNote> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        list.add(new PersonalNote("NOTE-001", "Cardio Conditioning Log",
                "Felt energized during morning trail run. Kept heart rate steady at 155 bpm. Hydration was optimal. " +
                "Knee pain was negligible, recovery smoothie helped fatigue.",
                Arrays.asList("cardio", "run", "heart rate", "recovery"), now.minusDays(6)));

        list.add(new PersonalNote("NOTE-002", "HIIT Training Notes",
                "Intense kettlebell workout today. Heart rate peaked at 178 bpm. High fatigue observed in upper body. " +
                "Need extra sleep tonight to promote recovery and muscle repair.",
                Arrays.asList("hiit", "fatigue", "sleep", "heart rate"), now.minusDays(5)));

        list.add(new PersonalNote("NOTE-003", "Swimming Technique & Aerobic Base",
                "Swimming 2000m freestyle. Steady breathing rhythm and smooth cadence. Aerobic base conditioning felt strong. " +
                "Cardio stamina is improving consistently week over week.",
                Arrays.asList("swimming", "cardio", "endurance", "stamina"), now.minusDays(4)));

        list.add(new PersonalNote("NOTE-004", "Strength Progression & CNS Fatigue",
                "Heavy deadlifts felt solid. Noticeable CNS fatigue after 5th working set. Need to schedule light yoga tomorrow. " +
                "Hydration and electrolyte replenishment critical before next interval sprint.",
                Arrays.asList("strength", "fatigue", "recovery", "hydration"), now.minusDays(2)));

        return list;
    }

    public static List<SensorObservation> generateSensorObservations(int count) {
        List<SensorObservation> list = new ArrayList<>(count);
        Random rng = new Random(42);
        LocalDateTime time = LocalDateTime.now().minusHours(count / 60);

        String[] devices = {"AppleWatchUltra", "OuraRingGen3", "WhoopStrap4", "GarminHRM"};
        String[] metrics = {"HEART_RATE", "HRV", "SPO2", "BODY_TEMP", "STEPS"};

        for (int i = 0; i < count; i++) {
            String dev = devices[rng.nextInt(devices.length)];
            String met = metrics[rng.nextInt(metrics.length)];
            double val = 0.0;
            String unit = "";

            switch (met) {
                case "HEART_RATE":
                    val = 60 + rng.nextInt(120);
                    unit = "bpm";
                    break;
                case "HRV":
                    val = 25 + rng.nextInt(90);
                    unit = "ms";
                    break;
                case "SPO2":
                    val = 95.0 + rng.nextDouble() * 4.9;
                    unit = "%";
                    break;
                case "BODY_TEMP":
                    val = 36.2 + rng.nextDouble() * 1.5;
                    unit = "C";
                    break;
                case "STEPS":
                    val = rng.nextInt(150);
                    unit = "steps";
                    break;
            }

            list.add(new SensorObservation("SENS-" + (1000 + i), dev, met, val, unit, time.plusMinutes(i)));
        }
        return list;
    }

    public static List<ActivityStreamItem> generateStream(int count) {
        List<ActivityStreamItem> list = new ArrayList<>(count);
        Random rng = new Random(101);
        long startMillis = System.currentTimeMillis() - (count * 1000L);

        String[] eventTypes = {"STEP_PING", "HEART_SAMPLE", "GPS_COORDINATE", "CADENCE_UPDATE", "CALORIE_DELTA"};

        for (int i = 0; i < count; i++) {
            String type = eventTypes[rng.nextInt(eventTypes.length)];
            double val = 10 + rng.nextDouble() * 190;
            list.add(new ActivityStreamItem(i + 1, "EVT-STREAM-" + (5000 + i), type, val, startMillis + (i * 1000L)));
        }
        return list;
    }
}
