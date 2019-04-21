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
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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


    //TODO edit color for box containing username and message
    //TODO edit color for box surrounding username and their message
    //TODO edit location of message?
    //TODO edit location of username?
    //TODO edit location of image?
    //TODO change username and body text color
    //TODO set Background color for message container
    //TODO change text box color
    //TODO change name and owner text color


    public static void main(String[] args)throws Exception{
        ProfileMapper pm = new ProfileMapper();
        Profile me = pm.createObjectList("Select * from meetup.profile where id = 3").get(0);
        //Profile me = new Profile();
        Group g = new Group();
        g.setId(21);
        g.setName("Test Chat");

        ChatGUI tg = new ChatGUI(g,me);
        tg.loadMessages();
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
        scroll.setBorder(null);

        //Display name of the group
        //TODO change name and owner text color
        JPanel propertyPanel = new JPanel(new BorderLayout());
        JLabel name = new JLabel(group.getName());
        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 28));
        name.setHorizontalAlignment(JLabel.CENTER);
        propertyPanel.add(name,BorderLayout.NORTH);
        Profile ownerp = getOwner(group);
        if(ownerp!=null) {
            JLabel owner = new JLabel("Owned by " + ownerp.getName());
            owner.setFont(new Font(owner.getFont().getName(), Font.PLAIN, 10));
            owner.setHorizontalAlignment(JLabel.CENTER);
            propertyPanel.add(owner, BorderLayout.SOUTH);
        }
        propertyPanel.setBackground(null);

        //create send button
        JPanel buttonPanel = new JPanel();
        text = new JTextField(30);
        addLimitToTextField(text,1000);
        buttonPanel.add(text);
        JButton send = new JButton("Send");
        establishServerCommunications(send);
        buttonPanel.add(send);
        buttonPanel.setBorder(null);
        //TODO change text box color
        text.setBackground(new Color(70,70,70));
        text.setForeground(new Color(255,255,255));
        text.setBorder(new EmptyBorder(4,10,4,10));
        send.setBackground(new Color(0,10,30));
        send.setForeground(new Color(255,255,255));
        send.setBorder(new EmptyBorder(4,10,4,10));

        //add stuff to frame
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        contentPane.setLayout(new BorderLayout());
        messagePanel.add(propertyPanel);
        //TODO set Background color for message container
        messagePanel.setBackground(new Color(0,0,0));
        messagePanel.setBorder(null);


        //messagePanel.setPreferredSize(new Dimension(messagePanel.getWidth(), 1500));
        scroll.setPreferredSize(new Dimension(500,500));
        scroll.setViewportView(messagePanel);
        scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        contentPane.add(scroll);
        contentPane.add(buttonPanel,BorderLayout.SOUTH);
        frame.setSize(500, 600);
        frame.setResizable(false);

        buttonPanel.setBackground(new Color(20,50,100));
    }

    /**
     * Disallows a text field to have more characters than the limit
     * @param tf
     * @param limit
     */
    private void addLimitToTextField(JTextField tf, int limit){

        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf.getText().length() > limit )
                    e.consume();
            }
        });
    }

    /**
     * Opens an output stream to notify server that a message was sent
     *      Adds listener for jbutton to send such notification
 *      Opens an input stream to receive notifications that a message was sent
     * @param jbutton
     */
    private void establishServerCommunications(JButton jbutton){
        //setup server interaction for live updates
        try{
            //connect and open streams
            Socket client = new Socket(Server.HOST,Server.PORT);
            PrintWriter out = new PrintWriter(client.getOutputStream());
            Scanner in = new Scanner(client.getInputStream());
            ServerListener sl = new ServerListener(in,this);
            Thread th = new Thread(sl);
            th.start();

            //tells the server you connected and what group you are chatting with
            out.println("welcome:"+groupid);
            out.flush();

            //Notify everyone in the group that you have messaged them
            jbutton.addActionListener(new ActionListener()
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
            if(App.DEV_MODE)
                e.printStackTrace();
        }
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
    public synchronized void getNewMessages(){
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
        List<Message> mList = new MessageController().getMessagesByGroupID(groupid);

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

        //build username and their message
        JLabel username = new JLabel(from);
        username.setFont(new Font(panel.getFont().getName(), Font.BOLD, 15));
        JTextArea body = new JTextArea(message);
        //TODO change username and body text color
        username.setForeground(new Color(255,255,255));
        body.setForeground(new Color(255,255,255));
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setBorder(null);
        body.setBackground(null);

        //TODO edit location of image?
        //add pic to row
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4,4,4,10);
        panel.add(picLabel,gbc);
        gbc.insets = new Insets(0,0,0,0);

        //TODO edit location of username?
        //Add username and their message
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(username,gbc);

        //TODO edit location of message?
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 4.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(body,gbc);

        //tidy up
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBorder(new EmptyBorder(5,10,5,10));

        //beautify
        //TODO edit color for box surrounding username and their message
        //row.setBackground(new Color(20,50,100));
        row.setBackground(new Color(0,0,0));
        //TODO edit color for box containing username and message
        panel.setBackground(new Color(0,10,30));
        row.add(panel,BorderLayout.CENTER);
        row.setSize(row.getWidth(),panel.getHeight());
        return row;
    }

    public Profile getOwner(Group g){
        ProfileMapper pm = new ProfileMapper();
        try {
            return pm.createObjectList("Select * from meetup.profile where id = " + g.getCreated_by()).get(0);
        }catch (Exception e ){
            return null;
        }
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
