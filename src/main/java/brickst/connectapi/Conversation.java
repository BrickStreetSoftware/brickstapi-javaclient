package brickst.connectapi;

public class Conversation
{
    public Long id;
    public String name;
    public String description;
    public String customerCount;
    
    // this is required
    public long departmentID;

    public Long senderID;
    public Long mailFarmID;
    public Long receiverDomain;

    public Boolean signingEnabled;

    public String defaultURL;
    public String trackerDomain;
    public String unsubscribeFormURL;
    public String unsubscribeFormSubmittedURL;
    public String unsubscribeDefaultText;
    public String referFriendFormURL;
    public String referFriendFormSuccessURL;
    public String referFriendDefaultText;
}
