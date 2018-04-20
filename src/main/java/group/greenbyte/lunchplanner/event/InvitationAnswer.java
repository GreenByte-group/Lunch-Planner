package group.greenbyte.lunchplanner.event;

public enum InvitationAnswer {

    ACCEPT(0), REJECT(1), MAYBE(2);

    private final int value;
    private InvitationAnswer(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
