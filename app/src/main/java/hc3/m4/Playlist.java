package hc3.m4;

public class Playlist {

    private long id;
    private String name;
    private boolean selected = false;

    public Playlist() {
        id = -1;
        name = "";
    }

    public Playlist(long songID, String playlistName) {
        id = songID;
        name = playlistName;
    }

    public Playlist(String playlistName) {
        name = playlistName;
    }

    public long getID(){return id;}
    public String getName(){return name;}

    public void setID(long id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() {return selected;}
}
