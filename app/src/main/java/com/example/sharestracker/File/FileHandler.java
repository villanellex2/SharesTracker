package com.example.sharestracker.File;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {
    static final String lastSearchedFile = "lastSearched";
    private static List<String> lastSearched;
    private Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    List<String> initializeFile(String fileName, List<String> result) {
        if (!isFilePresent(context, fileName)) {
            result = new ArrayList<>();
            create(context, fileName, "");
        } else {
            result = new ArrayList(Arrays.asList(read(context, fileName).split("&%1414]")));
        }
        return result;
    }


    public boolean isCached(String share) {
        return isFilePresent(context, share + ".json");
    }

    public boolean cacheShare(String companyInfo, String share) {
        return create(context, share + ".json", companyInfo);
    }

    public String getCompanyInfo(String share) {
        return read(context, share + ".json");
    }

    public void storeImage(BitmapDrawable image, String share) {
        try {
            FileOutputStream fos = context.openFileOutput(share+".png", Context.MODE_PRIVATE);
            image.getBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BitmapDrawable readDrawable(Resources res, String share){
        try {
            FileInputStream fis = context.openFileInput(share+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            BitmapDrawable drawable = new BitmapDrawable(res, bitmap);
            fis.close();
            return drawable;
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String read(Context context, String fileName) {
        synchronized (FileHandler.class) {
            try {
                FileInputStream fis = context.openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (IOException fileNotFound) {
                return "";
            }
        }
    }

    static boolean create(Context context, String fileName, String jsonString) {
        synchronized (FileHandler.class) {
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                if (jsonString != null) {
                    fos.write(jsonString.getBytes());
                }
                fos.close();
                return true;
            } catch (IOException fileNotFound) {
                return false;
            }
        }
    }

    static boolean isFilePresent(Context context, String fileName) {
        synchronized (FileHandler.class) {
            String path = context.getFilesDir() + "/" + fileName;
            File file = new File(path);
            return file.exists();
        }
    }
}
