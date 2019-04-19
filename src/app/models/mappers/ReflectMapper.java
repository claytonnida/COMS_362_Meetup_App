package app.models.mappers;

import app.App;
import app.Controllers.AccountController;
import app.MySQL.MySQLHelper;
import app.models.Account;
import app.models.Profile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectMapper<T> {

    private Class<T> clazz;
    private String className;
    private String useTableName;

    public ReflectMapper(Class<T> clazz){
        this.clazz = clazz;
        String canonicalName = clazz.getCanonicalName();
        className = canonicalName.split("\\.")[canonicalName.split("\\.").length-1];
        className = Character.toLowerCase(className.charAt(0))+className.substring(1);

        /*
        useTableName = "";
        for(Character c: className.toCharArray()){
            if(Character.isUpperCase(c)){
                useTableName += "_"+Character.toLowerCase(c);
            }else{
                useTableName += Character.toLowerCase(c);
            }
        }
        */
        useTableName = className;


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
            if(App.DEV_MODE)
                e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of objects of type T, built from rs
     * @param rs
     * @return
     */
    public List<T> toObjectList(ResultSet rs)throws SQLException{

            List<T> list = new ArrayList<>();
            while(rs.next()){
                T obj = toObject(rs);
                if(obj!=null)
                    list.add(obj);
            }
            return list;
    }

    /**
     * Returns object of type T, built from rs
     * @param rs
     * @return
     */
    public T toObject(ResultSet rs) throws SQLException {
        try {
            T obj = clazz.newInstance();

            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String fieldName = rsmd.getColumnName(i);
                setField(obj, rs, fieldName);
            }

            return obj;
        }catch (IllegalAccessException ie){
            //shouldn't happen
            return null;
        }catch (InstantiationException i){
            //shoudn't happen
            return null;
        }
    }

    /**
     * Returns object of type T, built from query
     * @param query
     * @return
     */
    public T toObject(String query) throws SQLException {
        try {

            T obj = clazz.newInstance();

            ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
            boolean exists = rs.next();

            if(!exists)
                return null;

            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String fieldName = rsmd.getColumnName(i);
                setField(obj, rs, fieldName);
            }

            return obj;

        }catch (IllegalAccessException ie){
            //shouldn't happen
            return null;
        }catch (InstantiationException i){
            //shoudn't happen
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
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Class type = field.getType();
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            if (type.getName().contains("String")) {
                String value = rs.getString(fieldName);
                value.replaceAll("'","\\'");
                field.set(obj, value);
            } else if (type.getName().contains("double")) {
                double value = rs.getDouble(fieldName);
                field.set(obj, value);
            }else if(type.getName().contains("BufferedImage")){
                InputStream is = rs.getBinaryStream(fieldName);
                BufferedImage bi = ImageIO.read(is);
                field.set(obj,bi);
            }else {
                int value = rs.getInt(fieldName);
                field.set(obj, value);
            }
            field.setAccessible(isAccessible);
        }catch (Exception e){
            //shouldn't happen
        }
    }

    /**
     * Creates an insert SQL statement for the provided object
     * @param obj
     * @return
     */
    public String toInsertStatement(T obj){
        try{

            String primaryKey = MySQLHelper.getPrimaryKeyForTable(useTableName);
            String query = String.format("describe meetup.%s",useTableName);
            ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
            rs.next();
            Map<String,Object> fields = new HashMap<>();
            while (rs.next()){
                String fieldName = rs.getString(1);

                //dont insert the primary key, because it is auto incremented
                if(fieldName.equals(primaryKey))
                    continue;

                //get field and make it accessible
                Field field=null;
                try {
                    field = clazz.getDeclaredField(fieldName);
                }catch (Exception e){
                    continue;
                }
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
            if(App.DEV_MODE)
                e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates an insert SQL statement for the provided object
     * @param obj
     * @return
     */
    public String toUpdateStatement(T obj){return toUpdateStatement(obj,false);}
    public String toUpdateStatement(T obj,boolean allowBlob){
        try{

            String primaryKey = MySQLHelper.getPrimaryKeyForTable(useTableName);
            String query = String.format("describe meetup.%s",useTableName);
            ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
            rs.next();
            Map<String,Object> fields = new HashMap<>();
            while (rs.next()){
                String fieldName = rs.getString(1);

                //dont insert the primary key, because it is auto incremented
                if(fieldName.equals(primaryKey))
                    continue;

                //get field and make it accessible
                Field field=null;
                try {
                    field = clazz.getDeclaredField(fieldName);
                }catch (Exception e){
                    continue;
                }
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
            return MySQLHelper.buildUpdateStatement(className,fields,allowBlob);
        }catch (Exception e){
            if(App.DEV_MODE)
                e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception{

        ResultSet rs = MySQLHelper.createStatement().executeQuery("Select id from meetup.account");
        while(rs.next()){
            int id = rs.getInt(1);
            Account acc = new AccountController().fetchAccount(id);
            System.out.println(acc);
        }

    }
}
