package app.serverclient;

import app.App;
import app.Controllers.MessageController;
import app.MySQL.MySQLHelper;
import app.models.Group;
import app.models.Message;
import app.models.mappers.MessageMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextGui {

    private JTextField text;
    private JScrollPane scroll;
    private JPanel messagePanel;
    private JFrame frame;
    private int groupid;
    private String lastUpdate = "00000000 00:00:00.000";
    public static void main(String[] args)throws IOException, SQLException {

        Group g = new Group();
        g.setId(20);
        g.setName("Maverick");
        TextGui tg = new TextGui(g);

        tg.loadMessages();

        tg.frame.setVisible(true);

    }

    public TextGui(Group group){
        this.groupid = group.getId();


        frame = new JFrame(group.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        Container contentPane = frame.getContentPane();

        JLabel name = new JLabel(group.getName());
        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 24));

        JPanel buttonPanel = new JPanel();
        text = new JTextField(30);
        buttonPanel.add(text);
        JButton send = new JButton("Send");

        send.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // display/center the jdialog when the button is pressed
                Message m = new Message();
                MessageMapper mm = new MessageMapper();
                m.setTo_id(groupid);
                m.setFrom_id(3);
                m.setBody(text.getText());
                m.setImage(null);
                MySQLHelper.executeUpdate(mm.toInsertQueryQuery(m));
                text.setText("");

                getNewMessages();
            }
        });

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

    public void getNewMessages(){
        //2019-04-19 16:22:58.0
        String newUpdate = getTime();
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
        lastUpdate = newUpdate;
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    public void open(){
        frame.setVisible(true);
    }

    public void loadMessages()throws SQLException{
        List<Message> mList = new MessageController().getMessagesByGroupID(20);

        for(Message m: mList){
            addMessage(m);
        }
        lastUpdate = getTime();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    private void addMessage(Message m){
        JPanel message = generateRow(m.getFrom_pic(),m.getSender_name(),m.getBody());
        addMessage(message);
    }
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

    public JPanel generateRow(BufferedImage image,String from, String message){
        JPanel row = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        //BufferedImage myPicture = ImageIO.read(new File("path-to-file"));
        Image scaledImage = image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4,4,4,10);
        panel.add(picLabel,gbc);
        gbc.insets = new Insets(0,0,0,0);

        JLabel n = new JLabel(from);
        n.setFont(new Font(panel.getFont().getName(), Font.BOLD, 15));
        JTextArea m = new JTextArea(message);
        m.setLineWrap(true);
        m.setWrapStyleWord(true);
        m.setEditable(false);
        m.setBorder(null);
        m.setBackground(null);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(n,gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 4.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(m,gbc);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBorder(new EmptyBorder(5,10,5,10));

        row.setBackground(new Color(255,255,255));
        row.add(panel,BorderLayout.CENTER);
        row.setSize(row.getWidth(),panel.getHeight());
        return row;
    }

}
