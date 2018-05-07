package group.greenbyte.lunchplanner.event.database;

import java.util.Date;

public class CommentDatabase {

    private int commentId;

    private int eventId;

    private String userName; // unsicher TODO

    private String commentText;

    private Date date;


    public Comment getComment() {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setEventId(eventId);
        comment.setUserName(userName);
        comment.setCommentText(commentText);
        comment.setDate(date);


        return comment;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
