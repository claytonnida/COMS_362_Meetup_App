import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class CodeThinger {
    public static void main(String[] args)throws Exception{
        File f = new File("src");

        PrintWriter pw = new PrintWriter("Classes.txt");
        folder(f,pw);

        pw.close();
    }
    public static void folder(File f,PrintWriter pw)throws Exception{
        if(f.isDirectory()){
            for(File file: f.listFiles()){
                folder(file,pw);
            }
        }else{
            file(f,pw);
        }
    }

    public static void file(File f,PrintWriter pw)throws Exception{
        pw.println(f.getName().replaceAll("([\\w_]+[/\\\\])+src",""));
        Scanner s = new Scanner(f);
        while(s.hasNextLine()){
            String line = s.nextLine();
            pw.println(line);
            pw.flush();
        }
    }
}
