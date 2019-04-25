import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassDiagramBuilder {

    static String param = "((int|double|[A-Z][a-z<>\\[\\]A-Z]*)\\s+\\w+(\\s*,\\s*)?)";
    static String camelCase = "[a-z]\\w*";
    static String args = "\\(\\s*"+param+"*\\s*\\)";
    static String encaps = "(public|private|protected)";

    public static void main(String[] args) throws Exception{
        PrintWriter pw = new PrintWriter("ClassDiagrams.txt");
        File f = new File("C:\\Users\\nkallen\\IdeaProjects\\COMS_362_Meetup_App\\src\\app");
        //System.out.println(f.getPath());
        folder(pw,f);
        pw.close();
    }

    public static void build(String method, PrintWriter pw){
        String args = "";
        Pattern argP = Pattern.compile("(\\w+)[^,()]+(\\w+)");
        Matcher m = argP.matcher(method);
        for(int i = 0; m.find(); i++){
            if(i>0){
                args+=", ";
            }
            MatchResult mr = m.toMatchResult();
            String type = mr.group(1);
            String name = mr.group(2);
            args+=name+":"+type;

            pw.println(method.split("\\(")[0]+"("+args+")");
        }
    }

    public static void files(PrintWriter pw,File f){
        String patt = camelCase+"\\s*"+args;
        Pattern p = Pattern.compile(encaps+".*?("+camelCase+"\\s*"+args+")");
        try{
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                String line = s.nextLine();
                Matcher m = p.matcher(line);
                if(m.find()){
                    build(m.toMatchResult().group(2),pw);
                }
            }
        }catch (Exception e){
            //
        }
    }

    public static void varsFirst(PrintWriter pw, File f){
        String patt = camelCase+"\\s*"+args;
        //private String username
        Pattern p = Pattern.compile("(public|private|protected)\\s*(static)?\\s*(\\w+)\\s*(\\w+)\\s*;");
        try{
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                String line = s.nextLine();
                Matcher m = p.matcher(line);
                if(m.find()){
                    MatchResult mr = m.toMatchResult();
                    pw.println(mr.group(4)+":"+mr.group(3));
                }
            }
        }catch (Exception e){
            //
        }
    }
    public static void folder(PrintWriter pw, File f){
        pw.println("\n\n");
        for(File file: f.listFiles()){
            if(file.isDirectory())
                folder(pw,file);
            else {
                pw.println("---");
                pw.println(file.getName().split("\\.")[0]);
                pw.println("---");
                varsFirst(pw,file);
                pw.println("---");
                files(pw, file);
                pw.println("---");
                pw.println("\n\n");
            }
        }
    }
}
