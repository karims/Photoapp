package com.mintash.photofun;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GridFragment extends Fragment implements AbsListView.OnScrollListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String KEY_URL = "picasso:url";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<PhotoData> photoDataList;
    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public GridFragment() {
        // Required empty public constructor
    }

    public void setPhotoDataList(List<PhotoData> photoDataListSent){
        this.photoDataList = photoDataListSent;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        final PhotoGridAdapter gridAdapter = new PhotoGridAdapter(getActivity(), photoDataList);
        final GridView photoGrid = (GridView) view.findViewById(R.id.gridViewFragment);
        photoGrid.setAdapter(gridAdapter);
        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String currentUrl = gridAdapter.getItem(i);
                // showDetails(currentUrl);
                //onFragmentInteraction(currentUrl);

                Intent imgIntent = new Intent(getActivity(), DetailActivity.class);
                imgIntent.putExtra(KEY_URL, currentUrl);

                startActivity(imgIntent);
            }
        });
        photoGrid.setOnScrollListener(new GridScrollListener(8, gridAdapter, getActivity()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        Log.i("Scrolling~~~", "In scrolllistener()");
        if (scrollState == SCROLL_STATE_IDLE) {

            Log.i("Scrolling~~~", "In scrolllistener(): " + scrollState + ", skip: " + GlobalInits.skip + ", last: "+ absListView.getLastVisiblePosition());
            if (absListView.getLastVisiblePosition() >= 7) {
                // Execute LoadMoreDataTask AsyncTask
//                GlobalInits.skip = GlobalInits.skip+8;
//                RemoteDataTask subTaskScroll = new RemoteDataTask(getActivity(), "latest", GlobalInits.skip);
//                subTaskScroll.execute();


            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        Log.i("Scrolling~~~", "In onScroll()");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



}
