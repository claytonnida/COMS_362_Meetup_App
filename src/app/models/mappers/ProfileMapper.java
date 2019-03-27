package app.models.mappers;

import app.models.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMapper extends ResultMapper<Profile> {


    @Override
    public Profile createObject(ResultSet rs) throws SQLException {
        Profile p = new Profile();
        p.setProfileid(rs.getString("profileid"));
        p.setAge(rs.getInt("age"));
        p.setSexualpref(rs.getString("sexualpref"));
        p.setSpiritanimal(rs.getString("spiritanimal"));
        p.setGenderid(rs.getString("genderid"));
        p.setMajor(rs.getString("major"));
        p.setZodiac(rs.getString("zodiac"));

        return p;
    }

    @Override
    public String toUpdateQueryQuery(Profile object) {
        return String.format("UPDATE meetup.profile " +
                        "SET aboutme = '%s', age = %d, sexualpref = '%s', spiritanimal = '%s', genderid = '%s', " +
                        "major = '%s', zodiac = '%s' " +
                        "WHERE profileid = %s",
                object.getAboutme(),object.getAge(), object.getSexualpref(),
                object.getSpiritanimal(), object.getGenderid(),
                object.getMajor(), object.getZodiac());
    }

    @Override
    public String toInsertQueryQuery(Profile object) {
        return String.format("INSERT INTO meetup.profile " +
                        "(aboutme, age , sexualpref , spiritanimal, genderid, " +
                        "major , zodiac ) " +
                        "VALUES ('%s', %d, '%s', '%s', '%s', '%s', '%s')",
                object.getAboutme(),object.getAge(), object.getSexualpref(),
                object.getSpiritanimal(), object.getGenderid(),
                object.getMajor(), object.getZodiac());
    }
}