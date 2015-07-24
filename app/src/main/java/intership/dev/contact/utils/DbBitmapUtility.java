package intership.dev.contact.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Class contains methods that can convert between Bitmap and ByteArray
 */
public class DbBitmapUtility {

    /**
     * Convert from bitmap to byte array
     *
     * @param bitmap The Bitmap
     * @return The byte array
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * convert from byte array to bitmap
     *
     * @param image The byte array
     * @return The Bitmap
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
