package app.serverclient;

import app.App;
import app.Controllers.MessageController;
import app.MySQL.MySQLHelper;
import app.models.Group;
import app.models.Message;
import app.models.Profile;
import app.models.mappers.MessageMapper;
import app.models.mappers.ProfileMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ChatGUI {

    private JTextField text;
    private JScrollPane scroll;
    private JPanel messagePanel;
    private JFrame frame;
    private int groupid;
    private Profile profile;
    private String lastUpdate = "00000000 00:00:00.000";

    public static void main(String[] args)throws Exception{
        ProfileMapper pm = new ProfileMapper();
        Profile me = pm.createObjectList("Select * from meetup.profile where id = 1").get(0);
        Group g = new Group();
        g.setId(20);
        g.setName("Maverick");

        ChatGUI tg = new ChatGUI(g,me);
        tg.getNewMessages();
        tg.open();
    }

    /**
     * Setup a chat interface for communicating with the group
     * @param group
     * @param profile
     */
    public ChatGUI(Group group, Profile profile){

        this.groupid = group.getId();
        this.profile = profile;

        //setup main frame
        frame = new JFrame(group.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //setup scroll pane for messages
        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        Container contentPane = frame.getContentPane();

        //Display name of the group
        JLabel name = new JLabel(group.getName());
        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 24));

        //create send button
        JPanel buttonPanel = new JPanel();
        text = new JTextField(30);
        buttonPanel.add(text);
        JButton send = new JButton("Send");

        //setup server interaction for live updates
        try{
            //connect and open streams
            Socket client = new Socket("localhost",8000);
            PrintWriter out = new PrintWriter(client.getOutputStream());
            Scanner in = new Scanner(client.getInputStream());
            ServerListener sl = new ServerListener(in,this);
            Thread th = new Thread(sl);
            th.start();

            //tells the server you connected and what group you are chatting with
            out.println("welcome:"+groupid);
            out.flush();

            //Notify everyone in the group that you have messaged them
            send.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // Build message
                    Message m = new Message();
                    MessageMapper mm = new MessageMapper();
                    m.setTo_id(groupid);
                    m.setFrom_id(profile.getId());
                    m.setBody(text.getText());
                    m.setImage(null);

                    //post message on server
                    MySQLHelper.executeUpdate(mm.toInsertQueryQuery(m));
                    text.setText("");

                    //ping other members
                    out.println("rec:"+groupid);
                    out.flush();
                }
            });

        }catch (Exception e){

        }

        //add stuff
        buttonPanel.add(send);
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        contentPane.setLayout(new BorderLayout());
        messagePanel.add(name);


        //messagePanel.setPreferredSize(new Dimension(messagePanel.getWidth(), 1500));
        scroll.setPreferredSize(new Dimension(500,500));
        scroll.setViewportView(messagePanel);
        scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        contentPane.add(scroll);
        contentPane.add(buttonPanel,BorderLayout.SOUTH);
        //mainWindow.add(contentPane);
        frame.setSize(500, 600);
        frame.setResizable(false);
    }

    /**
     * Gets the time from the server so everyone updates the same messages
     * @return
     */
    private String getTime(){
        try {
            ResultSet rs = MySQLHelper.executeQuery("select CURRENT_TIMESTAMP");
            rs.next();
            return rs.getString(1);
        }catch (Exception e){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
            Date date = new Date();
            return dateFormat.format(date)+"0"; //2016/11/16 12:08:43
        }
    }

    /**
     * Adds messages posted after most recent update to the gui
     */
    public void getNewMessages(){
        //get current time
        String newUpdate = getTime();

        //get messages we haven't seen yet
        MessageController mc = new MessageController();
        try {
            List<Message> ml = mc.getMessagesByGroupID(groupid, lastUpdate);
            for (Message m : ml) {
                addMessage(m);
            }
        }catch (Exception e){
            if(App.DEV_MODE)
                e.printStackTrace();
        }

        //update state
        lastUpdate = newUpdate;
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    /**
     * Show GUI
     */
    public void open(){
        frame.setVisible(true);
    }

    /**
     * Initial load of messages.
     * Loads all messages and adds to whatever is currently in window
     * @throws SQLException
     */
    public void loadMessages()throws SQLException{
        List<Message> mList = new MessageController().getMessagesByGroupID(20);

        for(Message m: mList){
            addMessage(m);
        }
        lastUpdate = getTime();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    /**
     * Add a message to the GUI
     * @param m
     */
    private void addMessage(Message m){
        JPanel message = generateRow(m.getFrom_pic(),m.getSender_name(),m.getBody());
        addMessage(message);
    }

    /**
     * Add a message to the GUI
     * @param row
     */
    private void addMessage(JPanel row){

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = messagePanel.getComponentCount() + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        messagePanel.add(row,gbc);

        messagePanel.revalidate();

    }

    /**
     * Create a row for message in the GUI
     * @param image
     * @param from
     * @param message
     * @return
     */
    public JPanel generateRow(BufferedImage image,String from, String message){
        //Create Containers
        JPanel row = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());

        //Scale user's pic
        Image scaledImage = image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        GridBagConstraints gbc = new GridBagConstraints();

        //add pic to row
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4,4,4,10);
        panel.add(picLabel,gbc);
        gbc.insets = new Insets(0,0,0,0);

        //build username and their message
        JLabel n = new JLabel(from);
        n.setFont(new Font(panel.getFont().getName(), Font.BOLD, 15));
        JTextArea m = new JTextArea(message);
        m.setLineWrap(true);
        m.setWrapStyleWord(true);
        m.setEditable(false);
        m.setBorder(null);
        m.setBackground(null);

        //Add username and their message
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(n,gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 4.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(m,gbc);

        //tidy up
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBorder(new EmptyBorder(5,10,5,10));

        //beautify
        row.setBackground(new Color(255,255,255));
        row.add(panel,BorderLayout.CENTER);
        row.setSize(row.getWidth(),panel.getHeight());
        return row;
    }

    /**
     * Listens to server for when to update the GUI
     */
    static class ServerListener implements Runnable{

        ChatGUI tg;
        Scanner in ;
        public ServerListener(Scanner in, ChatGUI tg){
            this.tg = tg;
            this.in = in;
        }
        @Override
        public void run() {
            while(true){
                try{
                    String command = in.nextLine();
                    if(command.equals("update"))
                        tg.getNewMessages();
                }catch (Exception e){
                    if(App.DEV_MODE)
                    e.printStackTrace();
                }
            }
        }
    }
}
