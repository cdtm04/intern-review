package intership.dev.contact;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Class manage information of a user
 */
public class Contact implements Serializable {

    private int mId;
    private String mName, mDecription;
    private Bitmap mAvatar;

    public Contact(int id, String mName, String mDecription, Bitmap mAvatar) {
        this.mId = id;
        this.mName = mName;
        this.mDecription = mDecription;
        this.mAvatar = mAvatar;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDecription() {
        return mDecription;
    }

    public void setDecription(String decription) {
        this.mDecription = decription;
    }

    public Bitmap getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.mAvatar = avatar;
    }
}
