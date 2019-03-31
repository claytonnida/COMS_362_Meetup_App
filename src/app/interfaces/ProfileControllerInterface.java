package app.interfaces;

import app.models.Profile;

import java.util.List;

//TODO: Add comments
public interface ProfileControllerInterface {

    void changeStatus(Profile p);

    void setPicture(String filename);
}
