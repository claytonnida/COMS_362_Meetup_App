package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.Account;
import app.models.Profile;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectMapper<T> {

    private Class<T> clazz;
    private String className;

    public ReflectMapper(Class<T> clazz){
        this.clazz = clazz;
        String canonicalName = clazz.getCanonicalName().toLowerCase();
        className = canonicalName.split("\\.")[canonicalName.split("\\.").length-1];
    }

    /**
     * Returns list of objects of type T, built from rs
     * @param query
     * @return
     */
    public List<T> toObjectList(String query){
        try{
            Statement statement = MySQLHelper.createStatement();
            ResultSet rs = statement.executeQuery(query);
            return toObjectList(rs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of objects of type T, built from rs
     * @param rs
     * @return
     */
    public List<T> toObjectList(ResultSet rs){
        try{
            List<T> list = new ArrayList<>();
            while(rs.next()){
                T obj = toObject(rs);
                if(obj!=null)
                    list.add(obj);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns object of type T, built from rs
     * @param rs
     * @return
     */
    public T toObject(ResultSet rs){
        try{
            T obj = clazz.newInstance();

            ResultSetMetaData rsmd = rs.getMetaData();
            for(int i = 1; i < rsmd.getColumnCount(); i++){
                String fieldName = rsmd.getColumnName(i);
                setField(obj,rs,fieldName);
            }

            return obj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Sets obj.<fieldName> to rs.get*(fieldName)
     * @param obj
     * @param rs
     * @param fieldName
     */
    private void setField(T obj, ResultSet rs, String fieldName){
        try{
            Field field = clazz.getDeclaredField(fieldName);
            Class type = field.getType();
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            if(type.getName().contains("String")){
                String value = rs.getString(fieldName);
                field.set(obj,value);
            }else{
                int value = rs.getInt(fieldName);
                field.set(obj,value);
            }
            field.setAccessible(isAccessible);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates an insert SQL statement for the provided object
     * @param obj
     * @return
     */
    public String toInsertStatement(T obj){
        try{

            String primaryKey = MySQLHelper.getPrimaryKeyForTable(className);
            String query = String.format("SELECT * FROM meetup.%s LIMIT 1",className);
            ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            Map<String,Object> fields = new HashMap<>();
            for(int i = 1; i < count; i++){
                String fieldName = rsmd.getColumnName(i);

                //dont insert the primary key, because it is auto incremented
                if(fieldName.equals(primaryKey))
                    continue;

                //get field and make it accessible
                Field field = clazz.getDeclaredField(fieldName);
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);

                //get the value of the field from obj
                Object val = field.get(obj);

                //if field is null, don't insert it
                if(val == null)
                    continue;


                fields.put(fieldName,val);
                field.setAccessible(isAccessible);
            }
            return MySQLHelper.buildInsertStatement(className,fields);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
        for(Profile p: pmapper.toObjectList("Select * from meetup.profile")){
            System.out.println(p.toString());
        }

        Profile p = new Profile();
        p.setProfileid("123");
        p.setMajor("SE");
        p.setZodiac("None");
        p.setAge(22);
        p.setSexualpref("Yea");
        p.setGenderid("God-kin");
        p.setAboutme("Hey");

        System.out.println(pmapper.toInsertStatement(p));
    }
}
