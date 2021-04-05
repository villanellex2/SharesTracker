package com.example.sharestracker.File;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LastSearchedStorage {
    static final String lastSearchedFile = "lastSearched";
    private static Set<String> lastSearchedStorage;

    public static boolean addToLastSearched(Context context, String share) {
        while (lastSearchedStorage.size() >= 16){
            lastSearchedStorage.remove(lastSearchedStorage.toArray()[0]);
        }
        lastSearchedStorage.add(share);
        return FileHandler.create(context, lastSearchedFile, getListAsString());
    }

    private static String getListAsString() {
        StringBuilder builder = new StringBuilder();
        for (String s : lastSearchedStorage) {
            builder.append(s);
            builder.append("&%1414]");
        }
        return builder.toString();
    }

    public static List<String> getLastSearched(Context context) {
        lastSearchedStorage = initializeFile(context, lastSearchedStorage);
        ArrayList<String> res = new ArrayList<>();
        for (String s : lastSearchedStorage) {
            if (!s.equals("")) {
                res.add(s);
            }
        }
        return res;
    }

    private static Set<String> initializeFile(Context context, Set<String> result) {
        if (!FileHandler.isFilePresent(context, lastSearchedFile)) {
            result = new LinkedHashSet<>();
            FileHandler.create(context, lastSearchedFile, "");
        } else {
            result = new LinkedHashSet<>(Arrays.asList
                    (FileHandler.read(context, lastSearchedFile).split("&%1414]")));
        }
        return result;
    }


}
