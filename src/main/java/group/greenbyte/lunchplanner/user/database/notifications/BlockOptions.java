package group.greenbyte.lunchplanner.user.database.notifications;

public enum BlockOptions {
    NONE(0),PERMANENTLY(1), TODAY(2), INTERVAL(3);

    private int value;
    BlockOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BlockOptions fromString(String option) {
        switch (option) {
            case "permanently":
                return BlockOptions.PERMANENTLY;
            case "today":
                return BlockOptions.TODAY;
            case "interval":
                return BlockOptions.INTERVAL;
            case "none":
                return BlockOptions.NONE;
        }

        return null;
    }

    public static BlockOptions fromInt(int option) {
        switch(option) {
            case 0:
                return BlockOptions.NONE;
            case 1:
                return BlockOptions.PERMANENTLY;
            case 2:
                return BlockOptions.TODAY;
            case 3:
                return BlockOptions.INTERVAL;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (value) {
            case 0:
                return "none";
            case 1:
                return "permanently";
            case 2:
                return "today";
            case 3:
                return "interval";
        }

        return "unknown";
    }
}
