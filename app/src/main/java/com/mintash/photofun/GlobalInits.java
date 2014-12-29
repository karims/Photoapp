package com.mintash.photofun;

/**
 * Created by karimn on 11/15/14.
 */
public class GlobalInits {
    private static GlobalInits ourInstance = new GlobalInits();

    public static int skip =0;
    public static String category_selected = "latest";
    public static GlobalInits getInstance() {
        return ourInstance;
    }

    private GlobalInits() {
    }
}
