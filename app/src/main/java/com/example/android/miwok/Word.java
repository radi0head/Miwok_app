package com.example.android.miwok;

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int mImageResourceID;
    private int mSoundResourceID;

    public Word(String defaultTranslation,String miwokTranslation,int soundResource){
        mDefaultTranslation=defaultTranslation;
        mMiwokTranslation=miwokTranslation;
        mSoundResourceID=soundResource;
    }
    public Word(String defaultTranslation,String miwokTranslation,int imageResource,int soundResource){
        mDefaultTranslation=defaultTranslation;
        mMiwokTranslation=miwokTranslation;
        mImageResourceID=imageResource;
        mSoundResourceID=soundResource;
    }
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceID() {
        return mImageResourceID;
    }

    public int getSoundResourceID() {
        return mSoundResourceID;
    }
}
