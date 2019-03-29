package app.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Chose abstract rather than interface, to provide the page with default implementations
 */
public abstract class AbstractPage {

    //Used variables to setup/populate the page
    protected Context pageContext;

    //Use to validate that proper context for populating the page is provided
    protected Map<String,Class> requiredContextValues = new HashMap<>();

    public AbstractPage(Context pageContext){
        if(pageContext == null){
            this.pageContext = new Context();
        }else {
            this.pageContext = pageContext;
        }
    }

    /**
     * This is the first action that should be taken for the page
     * Usually, this will probably display a menu prompt
     *
     * @throws IllegalArgumentException Some pages require some key value pairs to be present in the context.
     *  These required pairs should be validated and passed on to help the developer troubleshoot your page
     */
    public final void open() throws IllegalArgumentException {
        try {
            validateContext();
        }catch (IllegalArgumentException e){
            throw e;
        }

        runPage();
    }

    /**
     * Called by open() method after validating the context
     */
    protected abstract void runPage();

    /**
     * Performs the validation of the provided context
     * @throws IllegalArgumentException
     */
    private final void validateContext() throws IllegalArgumentException{

        String message = "\n";
        String messageTemplate = "\t%s: Context requires key:%s mapped to value of type: %s\n";

        //iterate through required args
        Iterator<String> iter = requiredContextValues.keySet().iterator();
        while(iter.hasNext()){
            String key = iter.next();
            Class reqClass = requiredContextValues.get(key);

            //Was the argument provided?
            if(pageContext.getVariable(key)==null){
                message += String.format(messageTemplate,"MISSING VARIABLE",key,reqClass.toString());

            //Was the provided argument of the correct type?
            }else{
                Class c = pageContext.getVariable(key).getClass();
                if(c != reqClass)
                    message += String.format(messageTemplate,"INCORRECT VARIABLE TYPE",key,reqClass.toString());
            }
        }

        if(!message.equals("\n")){
            throw new IllegalArgumentException(message);
        }
    }




    /**
     * This is the procedure that should be taken when closing the page.
     * In text UI, this probably won't do anything
     * In Graphical UI, this should at least close the page's threads and listeners
     */
    public abstract void close();
}
