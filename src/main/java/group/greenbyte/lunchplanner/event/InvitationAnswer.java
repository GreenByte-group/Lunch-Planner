package group.greenbyte.lunchplanner.event;

public enum InvitationAnswer {

    ACCEPT("accept"), REJECT("reject");

    private String answer;

    InvitationAnswer(String answer){
        this.answer = answer;
    }
}
