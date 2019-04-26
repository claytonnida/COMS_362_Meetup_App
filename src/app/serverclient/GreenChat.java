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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class GreenChat {

    private JTextField text;
    private JScrollPane scroll;
    private JPanel messagePanel;
    private JFrame frame;
    private int groupid;
    private Profile profile;
    private String lastUpdate = "00000000 00:00:00.000";
    private PrintWriter out;

	private static Map<String, Color> colorMap = new HashMap<>();

	static {
		colorMap.put("Red", new Color(.5f, .1f, .1f));
		colorMap.put("Orange", new Color(.8f, .3f, .1f));
		colorMap.put("Yellow", new Color(.8f, .8f, 0f));
		colorMap.put("Green", new Color(.1f, .5f, .1f));
		colorMap.put("Blue", new Color(.1f, .1f, .5f));
		colorMap.put("Purple", new Color(.5f, .1f, .5f));
		colorMap.put("White", new Color(1f, 1f, 1f));
		colorMap.put("Black", new Color(0f, 0f, 0f));
	}


    //TODO change name and owner text color
    //TODO change text box color
    //TODO set Background color for message container
    //TODO change username and body text color

    //TODO change box containing username and message
    //TODO edit color for box surrounding username and their message

    public static void main(String[] args)throws Exception{
        ProfileMapper pm = new ProfileMapper();
        Profile me = pm.createObjectList("Select * from meetup.profile where id = 3").get(0);
        //Profile me = new Profile();
        Group g = new Group();
        g.setId(21);
        g.setCreated_by(3);
        g.setName("Maverick");


        GreenChat tg = new GreenChat(g,me);
        System.out.println(tg.getTime());
        tg.loadMessages();
        tg.open();
        tg.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Setup a chat interface for communicating with the group
     * @param group
     * @param profile
     */
    public GreenChat(Group group, Profile profile){

        this.groupid = group.getId();
        this.profile = profile;

        //setup main frame
        frame = new JFrame(group.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //setup scroll pane for messages
        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Container contentPane = frame.getContentPane();
        scroll.setBorder(null);
        //Display name of the group
        //TODO change name and owner text color
        JPanel propertyPanel = new JPanel(new BorderLayout());
        JLabel name = new JLabel(group.getName());
        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 28));
        name.setHorizontalAlignment(JLabel.CENTER);
        propertyPanel.add(name,BorderLayout.NORTH);
	    propertyPanel.setForeground(colorMap.get("White"));
        name.setForeground(new Color(70,120,90));
        Profile ownerp = getOwner(group);
        if(ownerp!=null) {
            JLabel owner = new JLabel("Owned by " + ownerp.getName());
            owner.setFont(new Font(owner.getFont().getName(), Font.PLAIN, 10));
            owner.setHorizontalAlignment(JLabel.CENTER);
            propertyPanel.add(owner, BorderLayout.SOUTH);
            owner.setForeground(new Color(70,120,90));
        }
        propertyPanel.setBackground(null);

        //create send button
        JPanel buttonPanel = new JPanel();
        text = new JTextField(30);
        addLimitToTextField(text,1000);
        buttonPanel.add(text);
        JButton send = new JButton("Send");
        establishServerCommunications(send);
        text.addKeyListener(new EnterListener(this));
        buttonPanel.add(send);
        buttonPanel.setBorder(null);
        //TODO change text box color
        text.setBackground(new Color(20,50,20));
	    text.setForeground(colorMap.get("White"));
        text.setBorder(new EmptyBorder(4,10,4,10));
        send.setBackground(new Color(50,100,50));
	    send.setForeground(colorMap.get("White"));
        send.setBorder(new EmptyBorder(4,10,4,10));

        //add stuff to frame
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        contentPane.setLayout(new BorderLayout());
        messagePanel.add(propertyPanel);
        //TODO set Background color for message container
        messagePanel.setBackground(new Color(30,60,30));
        messagePanel.setBorder(null);


        //messagePanel.setPreferredSize(new Dimension(messagePanel.getWidth(), 1500));
        scroll.setPreferredSize(new Dimension(500,500));
        scroll.setViewportView(messagePanel);
        scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        contentPane.add(scroll);
        contentPane.add(buttonPanel,BorderLayout.SOUTH);
        frame.setSize(500, 600);
        frame.setResizable(false);

        buttonPanel.setBackground(new Color(50,130,80));
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
            out = new PrintWriter(client.getOutputStream());
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
                    sendAndNotify();
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


        //get messages we haven't seen yet
        MessageController mc = new MessageController();
        try {
            List<Message> ml = mc.getMessagesByGroupID(groupid, lastUpdate);
            for (Message m : ml) {
                addMessage(m);
                lastUpdate = m.getTime();
            }
        }catch (Exception e){
            if(App.DEV_MODE)
                e.printStackTrace();
        }

        //update state
        scroll.setViewportView(messagePanel);
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
        scroll.revalidate();

        synch = 0;
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


        scroll.setViewportView(messagePanel);
        lastUpdate = getTime();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
        scroll.revalidate();
    }

    /**
     * Add a message to the GUI
     * @param m
     */
    private void addMessage(Message m){
        JPanel message = generateRow(m.getFrom_pic(),m.getSender_name(),m.getBody(),m.getTime());
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


    }

    /**
     * Create a row for message in the GUI
     * @param image
     * @param from
     * @param message
     * @return
     */
    public JPanel generateRow(BufferedImage image,String from, String message,String time){
        //Create Containers
        JPanel row = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());

        //Scale user's pic
        Image scaledImage = image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        GridBagConstraints gbc = new GridBagConstraints();


        //Time
        time = convertTime(time,false);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,10));
	    timeLabel.setBackground(colorMap.get("White"));

        //build username and their message
        JPanel header = new JPanel(new BorderLayout());
        JPanel text = new JPanel(new BorderLayout());
        JLabel username = new JLabel(from);
        header.add(username,BorderLayout.WEST);
        header.add(timeLabel,BorderLayout.EAST);
        header.setBackground(null);
        username.setFont(new Font(panel.getFont().getName(), Font.BOLD, 15));
        JTextArea body = new JTextArea(message);
        //TODO change username and body text color
	    username.setForeground(colorMap.get("White"));
	    body.setForeground(colorMap.get("White"));
        text.setBackground(null);
        body.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setBorder(null);
        body.setBackground(null);
        text.add(header,BorderLayout.NORTH);
        text.add(body,BorderLayout.SOUTH);
        //TODO change box containing username and message
        text.setBackground(new Color(50,150,70));
        text.setBorder(new EmptyBorder(5,5,5,5));


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
        //panel.add(username,gbc);

        //TODO edit location of message?
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 4.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(text,gbc);

        //tidy up
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBorder(new EmptyBorder(5,10,5,10));

        //beautify
        row.setBackground(null);
        //TODO edit color for box surrounding username and their message
        panel.setBackground(new Color(50,120,70));
        panel.setBorder(new LineBorder(new Color(20,50,20),2,false));
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


    private String convertTime(String dt){
        return convertTime(dt,true);
    }
    private String convertTime(String dt,boolean includeDate){
        String date = dt.replaceAll("\\d+:\\d+:\\d+.\\d+","").trim();
        String time = dt.replaceAll("\\d+-\\d+-\\d+","").trim();
        String[] split = time.split(":");
        if(split[0].startsWith("0"))
            split[0] = split[0].substring(1);


        int hour = (Integer.parseInt(split[0]))-5;//UTC is 5 hours ahead of us
        String half;
        if(hour < 12 || hour == 24){
            half = "am";
            if(hour < 0){
                hour += 12;
                half = "pm";
            }
            if(hour == 0)
                hour = 12;
        }else {
            half = "pm";
            hour-=5;
        }

        String dd = includeDate?date.replaceAll("-","/"):"";
        return hour+":"+split[1]+" "+half+"  "+dd;
    }

    /**
     * Listens to server for when to update the GUI
     */
    static class ServerListener implements Runnable{

        GreenChat tg;
        Scanner in ;
        public ServerListener(Scanner in, GreenChat tg){
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

    /**
     * Tell stuff to the server
     */
    private int synch = 0;
    private void sendAndNotify(){
        if(synch==0){
            synch = 1;
        }else return;
        // Build message
        if(text.getText().trim().equals("")){
            text.setText("");
            return;
        }
        Message m = new Message();
        MessageMapper mm = new MessageMapper();
        m.setTo_id(groupid);
        m.setFrom_id(profile.getId());
        m.setBody(text.getText().trim());
        m.setImage(null);

        //post message on server
        text.setText("");
        MySQLHelper.executeUpdate(mm.toInsertQueryQuery(m));
        //ping other members
        out.println("rec:"+groupid);
        out.flush();

    }

    class EnterListener implements KeyListener{

        GreenChat cg;
        public EnterListener(GreenChat b){
            cg = b;
        }
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==10 && cg.synch==0){
                cg.sendAndNotify();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
