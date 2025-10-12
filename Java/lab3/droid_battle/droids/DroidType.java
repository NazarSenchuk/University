package droids;

import java.util.ArrayList;

public enum DroidType {
    ARMOR(1, "Броньований", ArmorDroid.class),
    ASSAULT(2, "Штурмовий", AssaultDroid.class);

    private final int id;
    private final String displayName;
    private final Class<? extends Droid> droidClass;

    DroidType(int id, String displayName, Class<? extends Droid> droidClass) {
        this.id = id;
        this.displayName = displayName;
        this.droidClass = droidClass;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<? extends Droid> getDroidClass() {
        return droidClass;
    }

    public static ArrayList<String> getTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (DroidType type : values()) {
            names.add(type.displayName);
        }
        return names;
    }

    public static DroidType getById(int id) {
        for (DroidType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Невірний ід типу: " + id); 
    }

    public static void displayAllTypes() {
        for (DroidType type : values()) {
            System.out.println(type.id + ". " + type.displayName);
        }
    }
}