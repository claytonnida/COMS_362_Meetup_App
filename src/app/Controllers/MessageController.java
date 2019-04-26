package app.Controllers;

import app.MySQL.MySQLHelper;
import app.models.Message;
import app.models.mappers.MessageMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MessageController {
    // TODO: Javadocs
    public List<Message> getMessagesByGroupID(int groupid)throws SQLException{
        return getMessagesByGroupID(groupid,"0000-00-00 00:00:00.0");
    }

    public List<Message> getMessagesByGroupID(int groupid,String date)throws SQLException {
        MessageMapper mm = new MessageMapper();

        List<Message> messages = mm.createObjectList(
                "select to_id, body, image, time, name , profile_pic "+
                        " from (" +
                        "select * from meetup.message as a inner join meetup.profile as b " +
                        "on a.from_id = b.id ) as c "
                        +"where to_id = "+groupid
                        +" and time > '"+date+"'"
        );

        return messages;
    }

    /**
     * This method will send messages in the chat to the database. This is mostly used for sending Images to the database but it also maintains
     * normal messages as well. This method is the implementation of the sendPicture Use Case
     * @param msg Message to be sent to the database
     */
    public static void sendMessageToDB(Message msg){
        try {
            if (msg.getImage() != null) {
                int newh = msg.getImage().getHeight();
                int neww = msg.getImage().getWidth();
                while ((newh > 100) && (neww > 100)) {
                    newh = (int) (newh * .99);
                    neww = (int) (neww * .99);
                }
                msg.setImage(resize(msg.getImage(), neww, newh));
                //msg.setImage(Scalr.resize(msg.getImage(), 100, 100));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(msg.getImage(), "png", out);
                byte[] buf = out.toByteArray();
                // setup stream for blob
                ByteArrayInputStream inStream = new ByteArrayInputStream(buf);

                MessageMapper mm = new MessageMapper();
                //String query = rm.toInsertQueryQuery(msg);
                String query = null;
                query = mm.toInsertQuery(msg, true);
                PreparedStatement ps = MySQLHelper.getConnection().prepareStatement(query);
                ps.setBinaryStream(1, inStream, inStream.available());
                ps.executeUpdate();
            }
            else{
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(msg.getImage(), "png", out);
                byte[] buf = out.toByteArray();
                // setup stream for blob
                ByteArrayInputStream inStream = new ByteArrayInputStream(buf);

                MessageMapper mm = new MessageMapper();
                String query = mm.toInsertQueryQuery(msg);
                PreparedStatement ps = MySQLHelper.getConnection().prepareStatement(query);
                ps.setBinaryStream(1, inStream, inStream.available());
                ps.executeUpdate();
            }
        }
        catch(Exception e){
            //System.out.println("Exception found in sendMessageToDB()");
            e.printStackTrace();
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
