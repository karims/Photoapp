package com.mintash.photofun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karimn on 11/7/14.
 */
public class RemoteDataTask extends AsyncTask<Void, Void, Void> {

    private static final String KEY_URL = "picasso:url";
    private ProgressDialog mProgressDialog;
    Context context;
    OnAsyncRequestComplete caller;
    private String categoryParse;
    private List<PhotoData> photoDataList = null;
    private int limitParse=8;
    private List<ParseObject> ob;

    public RemoteDataTask(Activity a, String category, int limit) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
        categoryParse = category;
        limitParse = limit;


    }

    public void setCategory(String cat){
        this.categoryParse = cat;

    }

    public void setLimit(int limit){
        this.limitParse = limit;

    }
    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(List<PhotoData> photoDataList);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create a progressdialog
        mProgressDialog = new ProgressDialog(context);
        // Set progressdialog title
        mProgressDialog.setTitle("Parse.com Load More Tutorial");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();
        Parse.initialize(context, "fpvG3SBSIMDbggmqyh0Pg6mjVJNmrKFqtaLOJ3Pd", "H0zLjZ9Pa9IQWmciRZCLf9h1Hk2oQOT2H9AAnvLt");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        photoDataList = new ArrayList<PhotoData>();

        try {
            // Locate the class table named "TestLimit" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "socialimgs");
            query.orderByDescending("createdAt");
            String cat = categoryParse.toLowerCase().replace("#","");
            if(!categoryParse.toLowerCase().contains("latest")){
                query.whereEqualTo("category", cat);
            }

            // Set the limit of objects to show
            query.setLimit(8);
            query.setSkip(GlobalInits.skip);
            ob = query.find();
            Log.i("query results ~~~", String.valueOf(ob.size()) + " :skip: "+ GlobalInits.skip + " :cat is: "+cat);

            for (ParseObject num : ob) {
                PhotoData map = new PhotoData();
                //map.setNum((String) num.get("number"));
                map.setPhoto_url((String) num.get("url"));

                map.setCategory((String) num.get("category"));

                map.setPhoto_source((String) num.get("source"));

                photoDataList.add(map);
            }

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        caller.asyncResponse(photoDataList);
        mProgressDialog.dismiss();
    }
}
