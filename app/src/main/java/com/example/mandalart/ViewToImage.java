package com.example.mandalart;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewToImage {

    public interface SaveImageCallback{
        void imageResult(int status, Uri uri);
    }

    SaveImageCallback saveImageCallback;

    public void setSaveImageCallback(SaveImageCallback saveImageCallback) {
        this.saveImageCallback = saveImageCallback;
    }

    public void saveBitMap(Context context, Bitmap bitmap) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated) {
                if(saveImageCallback != null){
                    saveImageCallback.imageResult(400, null);
                }
                return;
            }
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            if(saveImageCallback != null){
                saveImageCallback.imageResult(400, null);
            }
        }
        scanGallery(context, pictureFile.getAbsolutePath());
    }

    private void scanGallery(Context context, String path) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    if(saveImageCallback != null){
                        saveImageCallback.imageResult(200, uri);
                    }

                }
            });
        } catch (Exception e) {
            if(saveImageCallback != null){
                saveImageCallback.imageResult(400, null);
            }
        }
    }
}
