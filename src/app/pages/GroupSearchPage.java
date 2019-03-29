package app.pages;

import app.models.Account;

public class GroupSearchPage extends AbstractPage{
    public GroupSearchPage(Context pageContext) {
        super(pageContext);

        requiredUniversalContextValues.put(Context.SessionKeys.ACCOUNT.toString(), Account.class);
    }

    @Override
    protected void runPage() {

    }

    @Override
    public void close() {

    }


    //TODO put your methods here. (Non static methods)

}
