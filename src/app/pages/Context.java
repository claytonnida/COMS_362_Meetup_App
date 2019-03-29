package app.pages;

import java.util.HashMap;
import java.util.Map;

public class Context {

    public static Context UNIVERSAL_CONTEXT = new Context();

    /**
     * Put commonly used session variables here
     */
    public enum SessionKeys{
        VAR1("var1"),//Most commonly, a page will pass a context with a single variable. VAR1 simplifies that transaction
        ACCOUNT("account");

        private final String name;

        private SessionKeys(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }


    private Map<String,Object> sessionVariables = new HashMap<>();

    public Map<String,Object> getSessionVariables(){
        return sessionVariables;
    }

    public void setVariable(String key, Object value){
        getSessionVariables().put(key,value);
    }

    public void setVariable(SessionKeys key, Object value){
        setVariable(key.toString(),value);
    }

    public Object getVariable(String key){
        return sessionVariables.get(key);
    }

    public Object getVariable(SessionKeys key){
        return getVariable(key.toString());
    }

    public void clearSessionVariables(){
        sessionVariables = new HashMap<>();
    }
}
