package sg.xs_tech.mybadmintonscores;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {
    private String id;
    private String fname;
    private String email;
    private String profile_picture_url;

    public Friend(String fname) {
        this.fname = fname;
    }

    public Friend(String id, String fname) {
        this.id = id;
        this.fname = fname;
    }

    protected Friend(Parcel in) {
        id = in.readString();
        fname = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fname);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    protected String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }
}
