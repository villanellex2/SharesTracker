package com.example.sharestracker.JSON;

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
    public static final String favoriteFile = "favoriteStorage";
    public static final String cache = "cacheMainMenu.json";
    private static List<String> favouriteShares;
    private final Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    public boolean isFavourite(String shareName) {
        initializeFavorite();
        return favouriteShares.contains(shareName);
    }

    private void initializeFavorite() {
        if (!isFilePresent(context, favoriteFile)) {
            favouriteShares = new ArrayList<>();
            create(context, favoriteFile, "");
        } else {
            favouriteShares = new ArrayList(Arrays.asList(read(context, favoriteFile).split(" ")));
        }
    }

    private String getListAsString() {
        StringBuilder builder = new StringBuilder();
        for (String s : favouriteShares) {
            builder.append(s);
            builder.append(" ");
        }
        return builder.toString();
    }

    public boolean deleteFromFavourite(String share) {
        if (isFavourite(share)) {
            favouriteShares.remove(share);
            return create(context, favoriteFile, getListAsString());
        }
        return true;
    }

    public boolean addToFavorite(String share) {
        if (!isFavourite(share)) {
            favouriteShares.add(share);
            return create(context, favoriteFile, getListAsString());
        }
        return true;
    }

    public List<String> getFavoritesList() {
        initializeFavorite();
        ArrayList<String> res = new ArrayList<>();
        for(String s: favouriteShares){
            if (!s.equals("")){
                res.add(s);
            }
        }
        return res;
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

    private String read(Context context, String fileName) {
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

    private boolean create(Context context, String fileName, String jsonString) {
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

    private boolean isFilePresent(Context context, String fileName) {
        synchronized (FileHandler.class) {
            String path = context.getFilesDir() + "/" + fileName;
            File file = new File(path);
            return file.exists();
        }
    }
}
