package com.example.sharestracker.File;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FavouriteStorage {
    static final String favouriteFile = "favoriteStorage";
    private static List<String> favouriteShares;

    public static boolean isFavourite(Context context, String shareName) {
        favouriteShares = initializeFile(context, favouriteShares);
        return favouriteShares.contains(shareName);
    }

    public static boolean deleteFromFavourite(Context context, String share) {
        if (isFavourite(context, share)) {
            favouriteShares.remove(share);
            return FileHandler.create(context, favouriteFile, getListAsString());
        }
        return true;
    }

    public static boolean addToFavorite(Context context, String share) {
        if (!isFavourite(context, share)) {
            favouriteShares.add(share);
            return FileHandler.create(context, favouriteFile, getListAsString());
        }
        return true;
    }

    private static String getListAsString() {
        StringBuilder builder = new StringBuilder();
        for (String s : favouriteShares) {
            builder.append(s);
            builder.append("&%1414]");
        }
        return builder.toString();
    }

    public static List<String> getFavoritesList(Context context) {
        favouriteShares = initializeFile(context, favouriteShares);
        ArrayList<String> res = new ArrayList<>();
        for (String s : favouriteShares) {
            if (!s.equals("")) {
                res.add(s);
            }
        }
        return res;
    }

    private static List<String> initializeFile(Context context, List<String> result) {
        if (!FileHandler.isFilePresent(context, FavouriteStorage.favouriteFile)) {
            result = new ArrayList<>();
            FileHandler.create(context, FavouriteStorage.favouriteFile, "");
        } else {
            result = Collections.synchronizedList(new ArrayList<>(Arrays.asList
                    (FileHandler.read(context, FavouriteStorage.favouriteFile).split("&%1414]"))));
        }
        return result;
    }

}
