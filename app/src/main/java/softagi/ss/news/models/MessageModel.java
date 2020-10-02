package softagi.ss.news.models;

public class MessageModel
{
    private String message;
    private String senderName;
    private String senderUid;
    private String receiverName;
    private String receiverUid;

    public MessageModel(String message, String senderName, String senderUid, String receiverName, String receiverUid) {
        this.message = message;
        this.senderName = senderName;
        this.senderUid = senderUid;
        this.receiverName = receiverName;
        this.receiverUid = receiverUid;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }
}