package app.pages;

import app.models.GroupChat;

public class GroupChatPage extends AbstractPage {


    public GroupChatPage(Context pageContext) {
        super(pageContext);
        requiredContextValues.put("group", GroupChat.class);
    }


    @Override
    protected void runPage() {
        System.out.println("Hello World");
    }



    @Override
    public void close() {

    }

}
