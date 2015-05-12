package org.wb.hust.dongguess.modal;

/**
 * Created by Administrator on 2014/12/27.
 */
public class Song {
    private String mSongName;
    private String mFileName;
    private int mNameLength;

    public String getSongName() {
        return mSongName;
    }

    public char[] getNameCharacters(){
        return mSongName.toCharArray();
    }

    public void setSongName(String songName) {
        this.mSongName = songName;
        this.mNameLength = songName.length();
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public int getNameLength() {
        return mNameLength;
    }
}
