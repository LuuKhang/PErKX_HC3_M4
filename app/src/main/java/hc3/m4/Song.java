package hc3.m4;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private long id;
    private String art;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int local;
    private boolean selected = false;

    public Song() {
        id = -1;
        art = "";
        title = "";
        artist = "";
        album = "";
        genre = "";
        local = 0;
    }

    public Song(long songID, String songTitle, String songArtist, String songAlbum, String songArt, String songGenre) {
        id = songID;
        art = songArt;
        title = songTitle;
        artist = songArtist;
        album = songAlbum;
        genre = songGenre;
        local = 0;
    }

    public Song(String songTitle, String songArtist, String songAlbum, String songArt, String songGenre) {
        art = songArt;
        title = songTitle;
        artist = songArtist;
        album = songAlbum;
        genre = songGenre;
        local = 0;
    }

    public Song(long songID, String songTitle, String songArtist, String songAlbum, String songArt, String songGenre, int songLocal) {
        id = songID;
        art = songArt;
        title = songTitle;
        artist = songArtist;
        album = songAlbum;
        genre = songGenre;
        local = songLocal;
    }

    public Song(String songTitle, String songArtist, String songAlbum, String songArt, String songGenre, int songLocal) {
        art = songArt;
        title = songTitle;
        artist = songArtist;
        album = songAlbum;
        genre = songGenre;
        local = songLocal;
    }

    public long getID(){return id;}
    public String getArt(){return art;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum(){return album;}
    public String getGenre(){return genre;}
    public int getLocal(){return local;}

    public void setID(long id){this.id = id;}
    public void setArt(String art){this.art = art;}
    public void setTitle(String title){this.title = title;}
    public void setArtist(String artist){this.artist = artist;}
    public void setAlbum(String album){this.album = album;}
    public void setGenre(String genre){this.genre = genre;}
    public void setLocal(int local){this.local = local;}

    public boolean isSelected() { return this.selected; }
    public void setSelected(boolean selected) { this.selected = selected; }



    // Functions, creators, and a bunch of stuff copy pasted for Parcelable
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) { return new Song(source); }
        @Override
        public Song[] newArray(int size) { return new Song[size]; }
    };
    public Song(Parcel in) {
        id = in.readLong();
        art = in.readString();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        genre = in.readString();
        local = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(art);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(genre);
        dest.writeInt(local);
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
