package com.netlynxtech.gdsfileupload.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.netlynxtech.gdsfileupload.Consts;
import com.securepreferences.SecurePreferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Probook2 on 30/12/2014.
 */
public class Utils {

    private ApiService apiService;
    private Context context;

    public Utils(Context con) {
        this.context = con;
    }

    public String getPhoneNumber() {
        if (Consts.DEBUG) {
            return "97307191";
        }
        SecurePreferences sp = new SecurePreferences(context);
        return sp.getString(Consts.REGISTER_MOBILE_NUMBER, "0");
    }

    public void storeSecurePreferenceValue(String key, String value) {
        SecurePreferences sp = new SecurePreferences(context);
        sp.edit().putString(key, value).commit();
    }

    public boolean checkIfRegistered() {
        SecurePreferences sp = new SecurePreferences(context);
        Log.e(Consts.REGISTER_LOGIN_ID, sp.getString(Consts.REGISTER_LOGIN_ID, "0"));
        Log.e(Consts.REGISTER_LOGIN_ID, sp.getString(Consts.REGISTER_MOBILE_NUMBER, "0"));
        Log.e(Consts.REGISTER_LOGIN_ID, sp.getString(Consts.REGISTER_PASSWORD, "0"));
        Log.e(Consts.REGISTER_LOGIN_ID, sp.getString(Consts.REGISTER_UDID, "0"));
        Log.e(Consts.REGISTER_LOGIN_ID, sp.getString(Consts.REGISTER_USER_GROUP, "0"));
        Log.e(Consts.REGISTER_LOGIN_ID, sp.getString(Consts.REGISTER_USER_NAME, "0"));
        if (!sp.getString(Consts.REGISTER_LOGIN_ID, "0").equals("0") && !sp.getString(Consts.REGISTER_USER_GROUP, "0").equals("0") && !sp.getString(Consts.REGISTER_MOBILE_NUMBER, "0").equals("0") && !sp.getString(Consts.REGISTER_USER_NAME, "0").equals("0") && !sp.getString(Consts.REGISTER_PASSWORD, "0").equals("0") && !sp.getString(Consts.REGISTER_UDID, "0").equals("0")) {
            return true;
        }
        return false;
    }

    public void storeUnique(String gcmid) {
        SecurePreferences sp = new SecurePreferences(context);
        sp.edit().putString(Consts.REGISTER_UDID, gcmid).commit();
    }

    public String getUnique() {
        SecurePreferences sp = new SecurePreferences(context);
        if (Consts.DEBUG) {
            sp.edit().putString(Consts.REGISTER_UDID, "1111111").commit();
            return "1111111";
        }
        if (!sp.getString(Consts.REGISTER_UDID, "0").equals("0")) {
            return sp.getString(Consts.REGISTER_UDID, "0");
        }
        return "";
    }

    public String convertBitmapToString(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public String createFolder() {
        String directory = "";
        File folder = new File(Environment.getExternalStorageDirectory() + "/gdsupload");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        if (success) {
            directory = Environment.getExternalStorageDirectory() + "/gdsupload";
        }
        return directory;
    }

    public String createThumbnailFolder() {
        String directory = "";
        File folder = new File(Environment.getExternalStorageDirectory() + "/gdsupload/.thumbnails");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            directory = Environment.getExternalStorageDirectory() + "/gdsupload/.thumbnails";
        }
        return directory;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(File file) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath().toString(), options);

        // Calculate inSampleSize
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        options.inSampleSize = calculateInSampleSize(options, imageWidth, imageHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath().toString(), options);
    }

    public void saveImageToFolder(Bitmap bitmap, String name) {
        Log.e("Creating Thumbnail", "Creating Thumbnail");

        FileOutputStream out = null;
        try {
            String dir = createThumbnailFolder();
            if (!dir.equals("")) {
                Log.e("Creating Thumbnail", dir + "/" + name);
                out = new FileOutputStream(dir + "/" + name);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            } else {
                Log.e("ERROR", "ERROR MAKING THUMBNAIL FOLDER!!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
