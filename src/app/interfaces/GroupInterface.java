package app.interfaces;

public interface GroupInterface {

   int getCreated_by();

   void setCreated_by(int created_by);

   int getId();

   void setId(int id);

   String getName();

   void setName(String name);

   String getIsPublic();

   void setIsPublic(String isPublic);

   double getRankAvg();
   
   void setRankAvg(double rankAvg);
}
