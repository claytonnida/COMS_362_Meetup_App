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

    public static void main(String[] args){
        GroupChatPage gcp = new GroupChatPage(null);
        try{
            gcp.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
