package com.mintash.photofun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AbsListView;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karimn on 11/15/14.
 */
public class GridScrollListener implements AbsListView.OnScrollListener {

    private int bufferItemCount = 10;
    private int currentPage = 0;
    private int itemCount = 0;
    private boolean isLoading = true;
    PhotoGridAdapter gridAdapter;
    Activity a;

    public GridScrollListener(int bufferItemCount, PhotoGridAdapter gridAdapter, Activity activity) {
        this.bufferItemCount = bufferItemCount;
        this.gridAdapter = gridAdapter;
        this.a = activity;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Do Nothing
        Log.i("Scrolling~~~", "in gridscrolllistener onScrollStateChanged " + scrollState + " ,count: "+ view.getCount()+" ,last visible: "+ view.getLastVisiblePosition());
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            if (view.getLastVisiblePosition()+1 >= view.getCount()) {
                GlobalInits.skip = GlobalInits.skip + 8;
                RemoteDataTaskGrid subTaskScroll = new RemoteDataTaskGrid(a, GlobalInits.category_selected, GlobalInits.skip);
                subTaskScroll.execute();
                gridAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        if (totalItemCount < itemCount) {
            this.itemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.isLoading = true;
            }
        }

        if (isLoading && (totalItemCount > itemCount)) {
            isLoading = false;
            itemCount = totalItemCount;
            currentPage++;
        }

        if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + bufferItemCount)) {
            //loadMore(currentPage + 1, totalItemCount);
            isLoading = true;
            Log.i("Scrolling~~~", "in gridscrolllistener");
        }
    }

    //
    private class RemoteDataTaskGrid extends AsyncTask<Void, Void, Void> {

        private static final String KEY_URL = "picasso:url";
        private ProgressDialog mProgressDialog;
        Context context;
        RemoteDataTask.OnAsyncRequestComplete caller;
        private String categoryParse;
        private List<PhotoData> photoDataList = null;
        private int limitParse=8;
        private List<ParseObject> ob;

        public RemoteDataTaskGrid(Activity a, String category, int limit) {
            caller = (RemoteDataTask.OnAsyncRequestComplete) a;
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
            gridAdapter.upDateItems(photoDataList);
            mProgressDialog.dismiss();
        }
    }
}