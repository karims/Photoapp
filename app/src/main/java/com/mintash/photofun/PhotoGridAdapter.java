package com.mintash.photofun;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by karimn on 10/29/14.
 */
public class PhotoGridAdapter extends BaseAdapter {

    private static int STATE_OF_VIEW = 1;
    private final Context context;
    private final List<String> urls = new ArrayList<String>();
    private List<PhotoData> photoDataList;
    private ArrayList<PhotoData> arraylist;
    private OkHttpDownloader okHttpClient;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.placeholder)
            .showImageForEmptyUri(R.drawable.placeholder)
            .showImageOnFail(R.drawable.error)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public PhotoGridAdapter(Context context) {
        this.context = context;
        urls.add("http://33.media.tumblr.com/7a5f6e5d04b8ef530b26cd658746fbde/tumblr_ne4d4qm8GO1tf0cv0o6_500.png");
        urls.add("http://38.media.tumblr.com/aef3e7319ec099e829169f196341a00c/tumblr_ne75pr35fT1u1968fo1_500.jpg");
        urls.add("http://33.media.tumblr.com/12a1e9a5399aca7c0ef4560d7be6fcfd/tumblr_ne75tnZVGN1u1968fo1_500.jpg");
        urls.add("http://40.media.tumblr.com/6098b090d758308b30fa832f445775d3/tumblr_nbtwl8HWgX1s0oildo1_500.png");
        urls.add("http://41.media.tumblr.com/1d6a3aa65bd61b7cc108ea2cbdd4c3cd/tumblr_n0t4iawczh1sq3g2zo1_500.png");
        urls.add("http://33.media.tumblr.com/a52da7eb152447137602b350a92d8f47/tumblr_ndhrkmnpdn1rcdaero1_500.jpg");
        urls.add("http://38.media.tumblr.com/0dc8c4899e4a65dfa0ef9c0f022ffce5/tumblr_ndaoetbr821u16ixdo1_500.jpg");
    }
    public PhotoGridAdapter(Context context, List<PhotoData> photoDataList) {
        this.context = context;
        this.photoDataList = photoDataList;
        this.arraylist = new ArrayList<PhotoData>();
//        this.arraylist.addAll(photoDataList);
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        inflater = LayoutInflater.from(context);
    }

    public void upDateItems(List<PhotoData> photoDataList){
        this.photoDataList.addAll(photoDataList);
    }
    @Override
    public int getCount() {

        return photoDataList.size();
    }

    @Override
    public String getItem(int i) {

        return photoDataList.get(i).getPhoto_url();
    }

    public String getCategory(int i) {

        return photoDataList.get(i).getCategory();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView gridImg;

        if(v == null)
        {
            v = inflater.inflate(R.layout.grid_view_item, viewGroup, false);
            v.setTag(R.id.gridImageView, v.findViewById(R.id.gridImageView));
        }
//        if (gridImg == null) {
//            gridImg = new ImageView(context);
//        }
        gridImg = (ImageView)v.getTag(R.id.gridImageView);
        Transformation cropPosterTransformation = new Transformation() {

            @Override public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;
                Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
                if (result != source) {
                    source.recycle();
                }
                return result;
            }

            @Override public String key() { return "square()"; }
        };

        // Get the image URL for the current position.
        //String url = getItem(position);

        String url = photoDataList.get(position).getPhoto_url();

        final ImageView finalGridImg = gridImg;


        imageLoader.displayImage(url, gridImg, options, new SimpleImageLoadingListener() {

        });

        return v;
    }

    private static class MyTransformation extends BitmapTransformation {

        public MyTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source,
                                   int outWidth, int outHeight) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String getId() {
            // Return some id that uniquely identifies your transformation.
            return "com.example.myapp.MyTransformation";
        }
    }
}
