package app.serverclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class Server {

    public static String HOST = "192.168.1.118";
    public static int PORT = 8000;

    ServerSocket socket;
    public static void main(String[] args){
        Server s = new Server();
        s.begin();
    }
    static Map<Integer, List<ClientSender>> groupMaps = new HashMap<>() ;
    public Server(){
        try{
            socket = new ServerSocket(PORT);

            String publicIpAddress = "unknown";

                URL url_name = new URL("http://bot.whatismyipaddress.com");

                BufferedReader sc =
                        new BufferedReader(new InputStreamReader(url_name.openStream()));

                // reads system IPAddress
                publicIpAddress = sc.readLine().trim();

            System.out.println("\tPublic IP Address: " + publicIpAddress );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void begin(){
        while(true){
            try {
                Socket client = socket.accept();
                ClientHandler ch = new ClientHandler(client);
                Thread th = new Thread(ch);
                th.start();

            }catch (Exception e){
                break;
            }
        }
    }

    static class ClientHandler implements Runnable{

        Socket client;
        public ClientHandler(Socket c){
            client = c;
        }
        @Override
        public void run() {
            try {
                Scanner in = new Scanner(client.getInputStream());
                while (true) {
                    try {
                        String line[] = in.nextLine().split(":");
                        if(line[0].equals("rec")){
                            int group = Integer.parseInt(line[1]);
                            for(ClientSender s: groupMaps.get(group)){
                                s.send("update");
                            }
                        }
                        if(line[0].equals("welcome")){

                            int group = Integer.parseInt(line[1]);
                            System.out.println("Some one joined group "+group);
                            if(groupMaps.containsKey(group)){
                                groupMaps.get(group).add(new ClientSender(client,group));
                            }else {
                                List<ClientSender> l = new ArrayList<>();
                                l.add(new ClientSender(client,group));
                                groupMaps.put(group,l);
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    static class ClientSender {
        public PrintWriter out;
        public int myGroup;
        public ClientSender(Socket s,int myGroup){
            try{
                out = new PrintWriter(s.getOutputStream());
                this.myGroup = myGroup;
            }catch (Exception e){}
        }
        public void send(String s){
            try{
                out.println(s);
                out.flush();
                System.out.println("sent Update to "+this+" (group "+myGroup+")");
            }catch (Exception e){
                groupMaps.get(myGroup).remove(this);
            }
        }
    }
}
