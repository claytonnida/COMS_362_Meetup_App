package app.serverclient;

import app.App;
import app.Controllers.MessageController;
import app.MySQL.MySQLHelper;
import app.models.Group;
import app.models.Message;
import app.models.Profile;
import app.models.mappers.MessageMapper;
import app.models.mappers.ProfileMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.*;

public class ChatGUI {

    private JTextField text;
    private JScrollPane scroll;
    private JPanel messagePanel;
    private JFrame frame;
    private int groupid;
    private Profile profile;
    private String lastUpdate = "00000000 00:00:00.000";
    private PrintWriter out;

    public static void main(String[] args)throws Exception{
        ProfileMapper pm = new ProfileMapper();
        Profile me = pm.createObjectList("Select * from meetup.profile where id = 7").get(0);
        //Profile me = new Profile();
        Group g = new Group();
        g.setId(23);
        g.setName("Maverick");

        ChatGUI tg = new ChatGUI(g, me);
        tg.loadMessages();
        tg.open();
        tg.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        scroll.addMouseListener(new ThemeMouseListener(scroll));

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Container contentPane = frame.getContentPane();
        scroll.setBorder(null);

        //Display name of the group
        JPanel propertyPanel = new JPanel(new BorderLayout());

        //Display name of the group
        JLabel name = new JLabel(group.getName());
        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 28));
        name.setHorizontalAlignment(JLabel.CENTER);
        name.addMouseListener(new ThemeMouseListener(name));
        propertyPanel.add(name,BorderLayout.NORTH);

        //Display owner of the group
        Profile ownerp = getOwner(group);
        if(ownerp!=null) {
            JLabel owner = new JLabel("Owned by " + ownerp.getName());
            owner.setFont(new Font(owner.getFont().getName(), Font.PLAIN, 10));
            owner.setHorizontalAlignment(JLabel.CENTER);
            owner.addMouseListener(new ThemeMouseListener(owner));
            propertyPanel.add(owner, BorderLayout.SOUTH);
        }
        propertyPanel.setBackground(null);

        //create send button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        text = new JTextField(30);
        addLimitToTextField(text,1000);
        text.addKeyListener(new EnterListener(this));
        buttonPanel.addMouseListener(new ThemeMouseListener(buttonPanel));
        text.addMouseListener(new ThemeMouseListener(text));
        buttonPanel.add(text,BorderLayout.CENTER);

        //create send button
        JButton send = new JButton("Send");
        establishServerCommunications(send);
        send.addMouseListener(new ThemeMouseListener(send));
        buttonPanel.add(send,BorderLayout.EAST);
        buttonPanel.setBorder(null);
        buttonPanel.setFont(new Font(buttonPanel.getFont().getName(),Font.PLAIN,30));

        text.setBackground(new Color(70,70,70));
        text.setForeground(new Color(255,255,255));
        text.setBorder(new EmptyBorder(4,10,4,10));

        send.setBackground(new Color(0,10,30));
        send.setForeground(new Color(255,255,255));
        send.setBorder(new EmptyBorder(4,10,4,10));

        // Add button to access changing of window theme
//        JButton themeButton = new JButton("Change Colors");
//        themeButton.setBackground(new Color(0,10,30));
//        themeButton.setForeground(new Color(255,255,255));
//        themeButton.setBorder(new EmptyBorder(4,10,4,10));
//        buttonPanel.add(themeButton, BorderLayout.WEST);

        JPopupMenu themeMenu = new JPopupMenu("Theme Settings");
        JMenuItem changeForegroundColor = new JMenuItem("Change Foreground Color");
        themeMenu.add(changeForegroundColor);

        text.addMouseListener(new ThemeMouseListener(text));

        text.add(themeMenu);



        //add stuff to frame
        messagePanel = new JPanel();
        messagePanel.addMouseListener(new ThemeMouseListener(messagePanel));
        messagePanel.setLayout(new GridBagLayout());
        contentPane.setLayout(new BorderLayout());
        messagePanel.add(propertyPanel);

        messagePanel.setBackground(new Color(0,0,0));
        messagePanel.setBorder(null);


        //messagePanel.setPreferredSize(new Dimension(messagePanel.getWidth(), 1500));
        scroll.setPreferredSize(new Dimension(500,500));
        scroll.setViewportView(messagePanel);
        scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

        contentPane.add(scroll);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

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
            out = new PrintWriter(client.getOutputStream());
            Scanner in = new Scanner(client.getInputStream());
            ServerListener sl = new ServerListener(in,this);
            Thread th = new Thread(sl);
            th.start();

            //tells the server you connected and what group you are chatting with
            out.println("welcome:"+groupid);
            out.flush();

            //Notify everyone in the group that you have messaged them
            jbutton.addActionListener(e -> sendAndNotify());

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
        vertical.addMouseListener(new ThemeMouseListener(vertical));

        vertical.setValue( vertical.getMaximum() );
        scroll.revalidate();
    }

    /**
     * Add a message to the GUI
     * @param m
     */
    private void addMessage(Message m){
        JPanel message = generateRow(m.getFrom_pic(),m.getSender_name(),m.getBody(),m.getTime(), m.getImage());
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
            messagePanel.add(row, gbc);

    }

    /**
     * Create a row for message in the GUI
     * @param image
     * @param from
     * @param message
     * @return
     */
    public JPanel generateRow(BufferedImage image,String from, String message,String time, BufferedImage picture){
        //Create Containers
        JPanel row = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());

        //Scale user's pic
        Image scaledImage = image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        picLabel.addMouseListener(new ThemeMouseListener(picLabel));

        GridBagConstraints gbc = new GridBagConstraints();

        //build username and their message
        time = convertTime(time,true);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,10));
        timeLabel.setForeground(new Color(90,170,255));
        timeLabel.addMouseListener(new ThemeMouseListener(timeLabel));

        //build username and their message
        JPanel header = new JPanel(new BorderLayout());
        JPanel text = new JPanel(new BorderLayout());
        Image scaledPic = null;
        JLabel pic = null;
        if(picture != null) {
            scaledPic = picture.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            pic = new JLabel(new ImageIcon(scaledPic));
        }
        /*
        if(picture != null){
            pic.add(header, BorderLayout.NORTH);
            pic.add(body, BorderLayout.SOUTH);

        }
        */
        JLabel username = new JLabel(from);

        text.addMouseListener(new ThemeMouseListener(text));
        username.addMouseListener(new ThemeMouseListener(username));

        header.add(username,BorderLayout.WEST);
        header.add(timeLabel,BorderLayout.EAST);
        header.setBackground(null);
        header.addMouseListener(new ThemeMouseListener(header));

        username.setFont(new Font(panel.getFont().getName(), Font.BOLD, 15));
        JTextArea body = new JTextArea(message);
        body.addMouseListener(new ThemeMouseListener(body));

        username.setForeground(new Color(255,255,255));
        body.setForeground(new Color(255,255,255));
        text.setBackground(null);
        body.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setBorder(null);
        body.setBackground(null);
        text.add(header,BorderLayout.NORTH);
        text.add(body,BorderLayout.SOUTH);
        text.add(body,BorderLayout.CENTER);
        //TODO change box containing username and message
        text.setBackground(null);
        text.setBorder(new EmptyBorder(5,5,5,5));


        //display picture
        if(picture != null) {
            text.add(pic, BorderLayout.SOUTH);
        }

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
        panel.setBackground(new Color(0,10,30));
        LineBorder lb = new LineBorder(null,2,true);
        panel.setBorder(lb);
        row.add(panel,BorderLayout.CENTER);
        row.setSize(row.getWidth(),panel.getHeight());
        return row;
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
        try {
            File file = new File(text.getText());
            BufferedImage img = ImageIO.read(file);
            m.setImage(img);
            m.setBody(text.getText()
                    .replaceAll("\\w:","")
                    .replaceAll("([\\s\\w]+[\\\\/])",""));
            text.setText("");
            MessageController.sendMessageToDB(m);
        }
        catch(Exception e) {
            m.setBody(text.getText().trim());
            m.setImage(null);
            text.setText("");
            MySQLHelper.executeUpdate(mm.toInsertQueryQuery(m));
        }

        //ping other members
        out.println("rec:"+groupid);
        out.flush();

    }

    class EnterListener implements KeyListener{

        ChatGUI cg;
        public EnterListener(ChatGUI b){
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

    static class ThemeMouseListener implements MouseListener {

        private JComponent parentComponent;

        private JPopupMenu themeMenu = new JPopupMenu("Change Color");
        private static Map<String, Color> colorChoices = new HashMap<>();

        static {
            // Whatever colors you add will appear as choices for the user
            colorChoices.put("Red", new Color(.5f, .1f, .1f));
            colorChoices.put("Orange", new Color(.8f, .3f, .1f));
            colorChoices.put("Yellow", new Color(.8f, .8f, 0f));
            colorChoices.put("Green", new Color(.1f, .5f, .1f));
            colorChoices.put("Blue", new Color(.1f, .1f, .5f));
            colorChoices.put("Purple", new Color(.3f, .1f, .3f));
            colorChoices.put("Cyan", new Color(.15f, .7f, .7f));
            colorChoices.put("White", new Color(1f, 1f, 1f));
            colorChoices.put("Grey", new Color(.3f, .3f, .3f));
            colorChoices.put("Blue-ish Black", new Color(10, 10, 50));
            colorChoices.put("Black", new Color(0f, 0f, 0f));
        }

        public ThemeMouseListener(JComponent jComponent) {
            parentComponent = jComponent;

            JMenu foregroundMenu = new JMenu("Foreground Color");
            JMenu backgroundMenu = new JMenu("Background Color");

            for(String colorName : colorChoices.keySet()) {
                JMenuItem foregroundMenuItem = new JMenuItem(colorName);
                JMenuItem backgroundMenuItem = new JMenuItem(colorName);

                foregroundMenu.add(foregroundMenuItem);
                backgroundMenu.add(backgroundMenuItem);

                foregroundMenuItem.setForeground(colorChoices.get(colorName));
                backgroundMenuItem.setBackground(colorChoices.get(colorName));
                foregroundMenuItem.addActionListener(e -> parentComponent.setForeground(foregroundMenuItem.getForeground()));
                backgroundMenuItem.addActionListener(e -> parentComponent.setBackground(backgroundMenuItem.getBackground()));
            }

            String name = parentComponent.getName();
            if(name == null) {
                themeMenu.add("Change Color").setEnabled(false);
            } else {
                themeMenu.add(String.format("Change %s Color", parentComponent.getName())).setEnabled(false);
            }

            themeMenu.addSeparator();
            themeMenu.add(foregroundMenu);
            themeMenu.add(backgroundMenu);

            parentComponent.add(themeMenu);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3) {
                themeMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }


    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
