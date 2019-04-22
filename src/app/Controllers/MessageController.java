package app.Controllers;

import app.models.Message;
import app.models.Profile;
import app.models.mappers.MessageMapper;
import app.models.mappers.ProfileMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MessageController {
    public List<Message> getMessagesByGroupID(int groupid)throws SQLException{
        return getMessagesByGroupID(groupid,"0000-00-00 00:00:00.0");
    }
    public List<Message> getMessagesByGroupID(int groupid,String date)throws SQLException {
        MessageMapper mm = new MessageMapper();
        ProfileMapper pm = new ProfileMapper();
        HashMap<Integer, List<Message>> map = new HashMap<>();

        List<Message> messages = mm.createObjectList(
                "select to_id, body, image, time, name , profile_pic "+
                        " from (" +
                        "select * from meetup.message as a inner join meetup.profile as b " +
                        "on a.from_id = b.id ) as c "
               +"where to_id = "+groupid
               +" and time >= '"+date+"'"
        );
//        for(Message m: messages){
//            if(map.containsKey(m.getFrom_id())){
//                map.get(m.getFrom_id()).add(m);
//            }else{
//                map.put(m.getFrom_id(),new ArrayList<>());
//                map.get(m.getFrom_id()).add(m);
//            }
//        }
//
//        Iterator<Integer> iter = map.keySet().iterator();
//        while(iter.hasNext()){
//            int id = iter.next();
//            Profile p = pm.createObjectList("Select profile_pic, name from meetup.profile where " +
//                    "id = "+id).get(0);
//
//            for(Message m: map.get(id)){
//                m.setSender_name(p.getName());
//                m.setFrom_pic(p.getProfile_pic());
//
//            }
//        }

        return messages;
    }
}
