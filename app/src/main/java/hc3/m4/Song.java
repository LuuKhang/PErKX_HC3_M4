package hc3.m4;

public class Song {

    private long id;
    private R.drawable art;
    private String title;
    private String artist;
    private String album;
    private String genre;

    public Song(long songID, R.drawable songArt, String songTitle, String songArtist, String songAlbum, String songGenre) {
        id = songID;
        art = songArt;
        title = songTitle;
        artist = songArtist;
        album = songAlbum;
        genre = songGenre;
    }

    public long getID(){return id;}
    public R.drawable getArt(){return art;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum(){return album;}
    public String getGenre(){return genre;}

    public void setID(long id){this.id = id;}
    public void setArt(R.drawable art){this.art = art;}
    public void setTitle(String title){this.title = title;}
    public void setArtist(String artist){this.artist = artist;}
    public void getAlbum(String album){this.album = album;}
    public void getGenre(String genre){this.genre = genre;}
}
