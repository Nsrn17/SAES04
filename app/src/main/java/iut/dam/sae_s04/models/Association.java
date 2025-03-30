package iut.dam.sae_s04.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Association implements Parcelable {
    private String title;
    private String description;
    private int logoResId; // ID de l'image drawable
    private String cat;
    private String acro;

    public Association(String title, int logoResId, String description, String cat,String acro) {
        this.title = title;
        this.description = description;
        this.logoResId = logoResId;
        this.cat = cat;
        this.acro=acro;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getLogoResId() { return logoResId; }
    public String getCat() { return cat; }

    public String getAcro() {
        return acro;
    }

    @Override
    public String toString() {
        return title;
    }

    protected Association(Parcel in) {
        title = in.readString();
        logoResId = in.readInt();
        description = in.readString();
        cat = in.readString();
    }

    public static final Creator<Association> CREATOR = new Creator<Association>() {
        @Override
        public Association createFromParcel(Parcel in) {
            return new Association(in);
        }

        @Override
        public Association[] newArray(int size) {
            return new Association[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(logoResId);
        dest.writeString(description);
        dest.writeString(cat);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}