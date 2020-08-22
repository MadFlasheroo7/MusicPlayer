package com.example.musicplayer;

public class MusicFiles {
    private String path;
    private String album;
    private String title;
    private String artist;
//    private String albumID;
    private String duration;
    private String id;

    public MusicFiles(String path, String title, String artist, String album, String duration, String id) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
//        this.albumArtist = albumArtist;
        this.duration = duration;
        this.id = id;
    }

//    public String getAlbumID() {
//        return albumID;
//    }
//
//    public void setAlbumID(String albumID) {
//        this.albumID = albumID;
//    }
//    public String getAlbumArtist() {
//        return albumArtist;
//    }
//
//    public void setAlbumArtist(String albumArtist) {
//        this.albumArtist = albumArtist;
//    }

    public MusicFiles() {
    }

//    public String getAlbumArtist() {
//        return albumArtist;
//    }
//
//    public void setAlbumArtist(String albumArtist) {
//        this.albumArtist = albumArtist;
//    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
