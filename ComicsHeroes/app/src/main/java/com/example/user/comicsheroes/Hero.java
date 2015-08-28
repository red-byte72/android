package com.example.user.comicsheroes;

/**
 * Created by User on 25.08.2015.
 */
public class Hero {
    private int code;
    private String status;
    private String copyright;
    private String attributionText;
    private String attributionHTML;
    private String etag;
    private Data data;
         class Data{
            private int offset;
            private int limit;
            private int total;
            private int count;
            private Results[] results;
             class Results{
                private String id;
                private String name;
                private String description;
                private String modified;
                private Thumbnail thumbnail;
                 class Thumbnail{
                    private String path;
                    private String extension;
                    public String getPath(){return path;}
                    public String getExtension(){return extension;}
                }
                 public String getId(){return id;}
                 public String getName(){return name;}
                 public String getDescription(){return description;}
                 public String getModified(){return modified;}
                 public Thumbnail getThumbnail(){return thumbnail;}

            }
        public int getOffset(){return offset;}
        public int getLimit(){return limit;}
        public int getTotal(){return total;}
        public int getCount(){return count;}
        public Results[] getResults(){return results;}
    }
    public Data getData(){return data;}
    public int getCode(){return code;}
    public String getStatus(){return status;}
    public String getCopyright(){return copyright;}
    public String getAttributionText(){return attributionText;}
    public String getAttributionHTML(){return attributionHTML;}
    public String getEtag(){return etag;}
    private String heroFace;
    private String heroName;
    private String heroInfo;

    Hero(String heroFace, String heroName, String heroInfo){
        this.heroFace = heroFace;
        this.heroName = heroName;
        this.heroInfo = heroInfo;

    }
    public String getHeroFace(){return heroFace;}
    public String getHeroName(){return heroName;}
    public String getHeroInfo(){return heroInfo;}


}
