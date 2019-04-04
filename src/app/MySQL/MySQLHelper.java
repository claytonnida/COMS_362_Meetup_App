package app.MySQL;

import app.Controllers.ProfileController;
import app.InputReader;
import app.models.Account;
import app.models.GroupAssociation;
import app.models.Profile;
import app.models.mappers.AccountMapper;
import app.models.mappers.GroupAssociationMapper;
import app.models.mappers.ProfileMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MySQLHelper {


    //TODO: Note for later https://www.tutorialspoint.com/springjdbc/springjdbc_rowmapper.htm


    //Tells the program what driver to use at startup
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException cnf){
            System.out.println("DEPENDENCY ERROR!! Don't worry, its an easy fix for us!\n" +
                    "1.\tDownload 'mysql-connector-java' version '5.1.39' from 'https://dev.mysql.com/downloads/connector/j/5.1.html'\n" +
                    "2.\tUnzip the folder and note the directory you are extracting to.\n" +
                    "3.\tGo to File > Project Structure > Modules > Dependencies\n" +
                    "4.\tClick the PLUS icon and add 'jars or directories'\n" +
                    "5.\tChoose the jar file you downloaded (NOT the binary one)");

        }catch (Exception e){
            System.out.println("ABANDON HOPE!! I don't know what's wrong!");
        }
    }

    //Establishes a connection to the database
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
                    "jdbc:mysql://cs362meetupdb.redirectme.net", "cs362admin", "q1w2e3r4t5000");

    }

    //Creates a statement for a query
    public static Statement createStatement() throws SQLException{
        return getConnection().createStatement();
    }

    /* create table example
    Statement stmt = con.createStatement();
    stmt.executeUpdate("create table meetup.group_chat (" +
                    "group_id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(100), " +
                    "status VARCHAR(20), " +
                    "PRIMARY KEY ( group_id )" +
                    ");");

     */

    /**
     * List the tables in the DB and the Columns in each table
     * @throws SQLException
     */
    public static void describeDataBase()throws SQLException{

        //Get tables
        List<String> tables = new ArrayList<>();
        Statement s = createStatement();
        ResultSet rs = s.executeQuery("show tables in meetup");
        while(rs.next()){
            tables.add(rs.getString(1));
        }

        //Describe each table
        for(String table: tables){
            System.out.println(table);
            String query = String.format("describe meetup.%s",table);
            ResultSet trs = s.executeQuery(query);
            while (trs.next()){
                String description = String.format("\t%s:%s",trs.getString(1),trs.getString(2));
                System.out.println(description);
            }
        }
    }

    public static boolean testConnection(){
        try{
            Statement stmt = createStatement();
            ResultSet rs = stmt.executeQuery("show tables in meetup");
            boolean con = rs.next();
            System.out.println(con?"Successful connection":"Failed to Connect");
            return con;
        }catch (Exception e){
            System.out.println("Failed to Connect");
            return false;
        }
    }

    public static String resultSetToString(ResultSet rs)throws SQLException{
        String str = "";
        ResultSetMetaData rsmd = rs.getMetaData();
        for(int i = 1; i <= rsmd.getColumnCount(); i++){
            if(i > 1){
                str += ",\t";
            }
            Object obji = rs.getObject(i);
            String value = (obji==null)?"null":obji.toString();
            String key = rsmd.getColumnLabel(i);
            str += String.format("%s->%s",key,value);
        }

        return str;
    }


    public static String buildInsertStatement(String table, Map<String,Object> map){
        int i = 0;
        Iterator<String> iter = map.keySet().iterator();

        String keys = "";
        String vals = "";
        while(iter.hasNext()){
            String key = iter.next();
            Object val = map.get(key);
            if(i > 0){
                keys += ", ";
                vals += ", ";
            }
            keys += key;

            if(val.getClass().getName().contains("String")){
                vals += String.format("'%s'",val.toString().replaceAll("'","\\\\'"));
            }else{
                vals += val.toString();
            }
            i++;
        }

        return String.format("INSERT INTO meetup.%s (%s) VALUES (%s) ",table,keys,vals);
    }

    public static String buildUpdateStatement(String table, Map<String,Object> map){
        int i = 0;
        Iterator<String> iter = map.keySet().iterator();

        String sets = "";
        while(iter.hasNext()){
            String key = iter.next();
            Object val = map.get(key);
            if(i > 0){
                sets += ", ";
            }

            if(val.getClass().getName().contains("String")){
                sets += key+" = '"+val.toString().replaceAll("'","\\\\'")+"'";
            }else{
                sets += key+" = "+val.toString();
            }
            i++;
        }

        return String.format("UPDATE meetup.%s " +
                "SET %s ",table,sets);
    }

    public static List<String> fullResultSetToStringList(ResultSet rs)throws SQLException{
        List<String> list = new ArrayList<>();
        while(rs.next()){
            list.add(resultSetToString(rs));
        }
        return list;
    }

    public static String getPrimaryKeyForTable(String tableName)throws SQLException{


            String query = String.format("describe meetup.%s",tableName);
            ResultSet trs = createStatement().executeQuery(query);
            while (trs.next()){
                if(trs.getString("Key").equals("PRI"))
                    return trs.getString("FIELD");
            }


        return null;
    }

    /**
     * Handles query updates for you
     * @param query
     * @return
     */
    public static boolean executeUpdate(String query){
        try{
            createStatement().executeUpdate(query);
            return true;
        }catch (SQLException sql){
            System.out.println("Oops! Server error! Sorry, whatever was supposed to happen didn't.");
            sql.printStackTrace();
            return false;
        }
    }

    /**
     * Handles queries for you
     * @param query
     * @return
     */
    public static ResultSet executeQuery(String query){
        try{
            return createStatement().executeQuery(query);

        }catch (SQLException sql){
            System.out.println("Oops! Server error! Sorry, whatever was supposed to happen didn't.");
            sql.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args)throws Exception{

        //describeDataBase();
        //executeUpdate("Update meetup.group set isPublic = 'Public' where id != ");

        GroupAssociationMapper gam = new GroupAssociationMapper();
        System.out.println(gam.toUpdateQueryQuery(new GroupAssociation()));

        System.out.println("Profiles");
        for(String s: fullResultSetToStringList(executeQuery("Select * from meetup.profile"))){
            System.out.println(s);
        }

        System.out.println("Groups");
        for(String s: fullResultSetToStringList(executeQuery("Select * from meetup.group"))){
            System.out.println(s);
        }

        System.out.println("GroupAssociations");
        for(String s: fullResultSetToStringList(executeQuery("Select * from meetup.groupAssociation"))){
            System.out.println(s);
        }
    }

    private static void resetDatabase()throws SQLException{
        if(InputReader.inputYesNo("Are your really sure?")
            && InputReader.inputYesNo("Like... REALLY sure?")){

            ResultSet rs = createStatement().executeQuery("show tables in meetup");
            while(rs.next()){
                createStatement().executeUpdate("Drop table meetup."+rs.getString(1));
            }

            Statement stmt = createStatement();
            stmt.executeUpdate("create table meetup.group (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(100), " +
                    "isPublic VARCHAR(20), " +
                    "created_by INT(11), " +
                    "PRIMARY KEY ( id )" +
                    ");");

            stmt.executeUpdate("create table meetup.profile (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(100), " +
                    "age INT(2), " +
                    "aboutMe VARCHAR(500), " +
                    "genderId VARCHAR(50), " +
                    "sexualPref VARCHAR(50), " +
                    "zodiac VARCHAR(50), " +
                    "major VARCHAR(50), " +
                    "spiritAnimal VARCHAR(50), " +
                    "appearOffline INT(1), " +
                    "isOnline INT(1), " +
                    "pictureURL varchar(255), " +
                    "PRIMARY KEY ( id )" +
                    ");");

            stmt.executeUpdate("create table meetup.account (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "username VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "profileid INT(11), " +
                    "PRIMARY KEY ( id )" +
                    ");");

            stmt.executeUpdate("create table meetup.groupAssociation (" +
                    "profileid INT(11), " +
                    "groupid INT(11) " +
                    ");");


        }
    }
}
