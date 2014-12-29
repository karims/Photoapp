package com.mintash.photofun;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class DetailActivity extends Activity {


    private static final String KEY_URL = "picasso:url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment().newInstance(getIntent().getStringExtra(KEY_URL)))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        private Button saveButton;

        public static PlaceholderFragment newInstance(String url) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(KEY_URL, url);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            final Activity activity = getActivity();

            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.fragment_detail_img, container, false);

            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            saveButton = new Button(activity);
            saveButton.setText("Save Image");
            saveButton.setBackgroundColor(Color.WHITE);
            saveButton.setTextColor(Color.BLACK);
            saveButton.setVisibility(View.GONE);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("saving image~~~", " clicked");
                    imageView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = imageView.getDrawingCache();

                    String root = Environment.getExternalStorageDirectory().toString();
                    File newDir = new File(root + "/photofun");
                    if(!newDir.exists()) {
                        newDir.mkdirs();
                    }
                    Random gen = new Random();
                    int n = 10000;
                    n = gen.nextInt(n);
                    String fotoname = "Photo-"+ n +".jpg";
                    File file = new File (newDir, fotoname);
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        Toast.makeText(activity, "Saved to your folder", Toast.LENGTH_SHORT).show();
                        MediaScannerConnection.scanFile(activity, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
                    } catch (Exception e) {

                    }

                }

            });
            FrameLayout.LayoutParams lpView = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lpView.gravity = Gravity.CENTER;
            activity.addContentView(saveButton, lpView);

            imageView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    Log.i("Longclick~~~", "long clicked");
                    saveButton.setVisibility(View.VISIBLE);

                    return true;
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        saveButton.setVisibility(View.GONE);

                }
            });
            Transformation cropPosterTransformation = new Transformation() {

                @Override public Bitmap transform(Bitmap source) {
                    int targetWidth = container.getWidth();
                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    int targetHeight = (int) (targetWidth * aspectRatio);
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                }

                @Override public String key() {
                    return "cropPosterTransformation" + container.getWidth();
                }
            };

            Bundle arguments = getArguments();
            String url = arguments.getString(KEY_URL);


            Picasso.with(activity)
                    .load(url)
                    .placeholder(R.drawable.placeholder) //
                    .error(R.drawable.error) //
                    .into(imageView);

            return view;
        }
    }
}
