package app.ExampleCode;

import app.models.GroupAssociation;
import app.models.GroupChat;
import app.pages.Context;
import app.pages.GroupChatPage;

public class PageUsage {
    public static void main(String[] args){
        PageUsage pu = new PageUsage();

        System.out.println("pu.incorrectPageOpener1()");
        /*
        This page usage is incorrect because it does not provide a context to the GroupChatPage
        GroupChatPage has declared in its constructor that it needs a context variable called "group"
        and the value of "group" should be of type "GroupChat"

        Since no context is provided, the required "group" variable is not fulfilled
         */
        try {
            pu.incorrectPageOpener1();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("pu.incorrectPageOpener2()");
        /*
        This page usage is incorrect because it provides the context variable of the wrong type
        GroupChatPage has declared in its constructor that it needs a context variable called "group"
        and the value of "group" should be of type "GroupChat"

        Since the context provieds "group" as type "GroupAssociation" the variable type is incorrect
         */
        try {
            pu.incorrectPageOpener2();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("pu.correctPageOpener()");
        /*
        This usage is correct because the page is provided with a context that meets all variable requirements
         */
        try {
            pu.correctPageOpener();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void correctPageOpener(){
        Context gcpContext = new Context();
        gcpContext.setVariable("group",new GroupChat());

        GroupChatPage gcp = new GroupChatPage(gcpContext);
        try{
            gcp.open();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void incorrectPageOpener2(){
        Context gcpContext = new Context();
        gcpContext.setVariable("group",new GroupAssociation());

        GroupChatPage gcp = new GroupChatPage(gcpContext);
        try{
            gcp.open();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void incorrectPageOpener1(){
        GroupChatPage gcp = new GroupChatPage(null);
        try{
            gcp.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
