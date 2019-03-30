package app.interfaces;

import app.models.Profile;

import java.util.List;

public interface ProfileControllerInterface {

    List<Integer> filterOnlineConnections(List<Integer> pidList);

    void changeStatus(Profile p);

    void setPicture(String filename);

}
