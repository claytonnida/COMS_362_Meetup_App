package app.serverclient;

import app.Controllers.MessageController;
import app.models.Message;
import app.models.mappers.MessageMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextGui {

    private JTextField text;
    private JScrollPane scroll;
    private JPanel messagePanel;
    private JFrame frame;
    public static void main(String[] args)throws IOException, SQLException {

        TextGui tg = new TextGui();

        List<Message> mList = new MessageController().getMessagesByGroupID(20);

        for(Message m: mList){
            tg.addMessage(m);
        }

        tg.frame.setVisible(true);

    }

    public TextGui(){
        frame = new JFrame("SpringLayout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        Container contentPane = frame.getContentPane();

        JPanel buttonPanel = new JPanel();
        text = new JTextField(30);
        buttonPanel.add(text);
        JButton next = new JButton("Send");
        buttonPanel.add(next);
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        contentPane.setLayout(new BorderLayout());


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

    public void open(){
        messagePanel.setVisible(true);
    }

    public void loadMessages(){

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
