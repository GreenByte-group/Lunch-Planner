package group.greenbyte.lunchplanner.event;

public enum InvitationAnswer {

    ACCEPT("ACCEPT"), REJECT("REJECT"), MAYBE("MAYBE");

    private String answer;

    InvitationAnswer(String answer){
        this.answer = answer;
    }
}
