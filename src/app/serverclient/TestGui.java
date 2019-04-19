package app.serverclient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestGui {

    private JTextField text;
    private JScrollPane scroll;
    private JPanel messagePanel;
    private JFrame frame;
    public static void main(String[] args)throws IOException {

        TestGui tg = new TestGui();
        BufferedImage myPicture = ImageIO.read(
                new File("C:\\Users\\nkallen\\IdeaProjects\\COMS_362_Meetup_App\\sobble_is_best.png"));
        JPanel p = tg.generateRow(myPicture,"Me","Go f yourself\non\ntop\nof\tthat\nhill there");

        tg.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tg.addMessage(tg.generateRow(myPicture,"Me","Go f yourself\non\ntop\nof\tthat\nhill there"));
        tg.addMessage(tg.generateRow(myPicture,"Me","ok"));
        tg.addMessage(tg.generateRow(myPicture,"Me","mmm\nhmmm"));
        tg.addMessage(tg.generateRow(myPicture,"Me","Go f yourself\non\ntop\nof\tthat\nhill there"));
        tg.frame.setVisible(true);

    }

    public TestGui(){
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
        messagePanel = new JPanel(new GridLayout(0,1));
        contentPane.setLayout(new BorderLayout());


        messagePanel.setPreferredSize(new Dimension(messagePanel.getWidth(), 1500));
        scroll.setPreferredSize(new Dimension(500,500));
        scroll.setViewportView(messagePanel);
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

    private void addMessage(JPanel row){

        GridLayout gl = (GridLayout)messagePanel.getLayout();
        gl.setRows(gl.getRows()+1);
        messagePanel.add(row);

        int size = 0;
        for(Component c: messagePanel.getComponents()){
            size += c.getHeight();
            size += 8;
        }

        System.out.println(size);
        messagePanel.setPreferredSize(new Dimension(messagePanel.getWidth(),size*2));
    }

    private void addImage(){
        messagePanel.repaint();

    }

    private JPanel generateRow(BufferedImage image,String from, String message){
        JPanel row = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new GridLayout(1, 2));
        //BufferedImage myPicture = ImageIO.read(new File("path-to-file"));
        Image scaledImage = image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        panel.add(picLabel);


        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        JLabel n = new JLabel(from);
        n.setFont(new Font(panel.getFont().getName(), Font.BOLD, 15));
        JTextArea m = new JTextArea(message);
        m.setEditable(false);
        m.setBorder(null);
        m.setBackground(null);
        textPanel.add(n);
        textPanel.add(m);

        int lines = 1;
        Matcher matcher = Pattern.compile("\n").matcher(message);
        while(matcher.find()){
            lines++;
        }


        panel.add(textPanel);
        row.add(panel);
        row.setBackground(new Color(255,255,255));


        return row;
    }


}
