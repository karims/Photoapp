package com.mintash.photofun;

/**
 * Created by karimn on 11/1/14.
 */
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "fpvG3SBSIMDbggmqyh0Pg6mjVJNmrKFqtaLOJ3Pd", "H0zLjZ9Pa9IQWmciRZCLf9h1Hk2oQOT2H9AAnvLt");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}
