package intership.dev.contact;

import android.graphics.Bitmap;

/**
 * Class manage information of a user
 */
public class Contact {

    private String mName, mDecription;
    private Bitmap mAvatar;

    public Contact(String mName, String mDecription, Bitmap mAvatar) {
        this.mName = mName;
        this.mDecription = mDecription;
        this.mAvatar = mAvatar;
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
