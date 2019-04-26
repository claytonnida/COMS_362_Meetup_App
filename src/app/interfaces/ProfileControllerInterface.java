package app.interfaces;

import app.models.Group;
import app.models.Profile;

import java.sql.SQLException;
import java.util.List;

public interface ProfileControllerInterface {

    /**
     * Creates instance of a {@link Profile} and prompts users to fill out its fields.
     *
     * @return
     *      Instance of {@link Profile}.
     */
    Profile createProfile();

    /**
     * A series of prompts to guide user through editing their profile.
     *
     * @param p {@link Profile} to edit the fields of.
     */
    void editProfileFields(Profile p);

    /**
     * A series of prompts to guide user through editing their online status.
     *
     * @param p {@link Profile} to edit the online status of.
     */
    void editOnlineStatus(Profile p);

    /**
     * A series of prompts to guide user through editing their zodiac sign.
     *
     * @param p {@link Profile} to set the zodiac sign of.
     */
    void editZodiacSign(Profile p);

    /**
     * A series of prompts to guide user through editing their spirit animal.
     *
     * @param p {@link Profile} to set the spirit animal of.
     */
    void editSpiritAnimal(Profile p);

    /**
     * A series of prompts to guide user through editing their name.
     *
     * @param p {@link Profile} to set the name of.
     */
    void editName(Profile p);

    /**
     * A series of prompts to guide user through editing their major.
     *
     * @param p {@link Profile} to set the major of.
     */
    void editMajor(Profile p);

    /**
     * A series of prompts to guide user through editing their sexual preference.
     *
     * @param p {@link Profile} to set the sexual preferences of.
     */
    void editSexPref(Profile p);

    /**
     * A series of prompts to guide user through editing their about me section.
     *
     * @param p {@link Profile} to set the "about me" section of.
     */
    void editAboutMe(Profile p);

    /**
     * A series of prompts to guide user through editing their gender identity.
     *
     * @param p {@link Profile} to set the gender identity of.
     */
    void editGenderId(Profile p);

    /**
     * A series of prompts to guide user through editing their age.
     *
     * @param p {@link Profile} to set the age of.
     */
    void editAge(Profile p);

    /**
     * Update the {@link Profile} as a row in the database.
     *
     * @param p {@link Profile} to update.
     *
     * @throws SQLException
     */
    void updateProfile(Profile p) throws SQLException;

    /**
     * Insert the {@link Profile} as a row to the database.
     *
     * @param p {@link Profile} to insert.
     *
     * @throws SQLException
     */
    void insertProfile(Profile p)throws SQLException;

    /**
     * Lists {@link Profile}s.
     */
    void listProfiles();

    /**
     * Sends the profile to database.
     * If profile.id == 0, this method will create an Insert query
     * else this method will create an Update query
     *
     * @param p {@link Profile} to save in the database.
     *
     * @return The ID of the {@link Profile} that has been saved or updated in the database.
     *
     * @throws SQLException
     */
    int saveProfile(Profile p)throws SQLException;

    /**
     * Returns a version of the given {@link List<Integer>} with "offline" connections removed.
     *
     * @param pidList {@link List<Integer>} of {@link Profile} IDs to filter.
     *
     * @return
     *      {@link List<Integer>} of IDs associated with online {@link Profile}s.
     */
    List<Integer> filterOnlineConnections(List<Integer> pidList);

    /**
     * Queries the database for the "appearsOffline" column for the given Profile ID.
     *
     * @param pid The ID of the {@link Profile} to query the database for.
     *
     * @return
     *      {@code true} if the "appearsOffline" column for given {@link Profile} in the database is set to {@code true}. {@code false}, otherwise.
     */
    boolean appearsOffline(int pid);

    /**
     * Pings the server to see if {@link Profile} associated with given ID is online.
     *
     * @param pid The ID of the {@link Profile} to ping the server for.
     *
     * @return
     *      {@code true} if the given {@link Profile} is online. {@code false}, otherwise.
     */
    boolean checkOnlineStatus(int pid);

	void editInterests(Profile p);

    /**
     * Lists the invites sent to a {@link Profile} and allows a user to respond to them.
     *
     * @param profileId The ID associated with a {@link Profile} to get the invites of.
     */
    void viewInvitations(int profileId);

    /**
     * Prompts user to accept or reject an invitation to a {@link app.models.Group}.
     *
     * @param profileId The ID of the {@code Profile} attached to the user that is responding to the invite.
     * @param groupId The ID of the {@link app.models.Group} that the user is being invited to.
     */
    void respondToGroupInvite(int profileId, int groupId);

	void rateProfile(Profile p);

}
