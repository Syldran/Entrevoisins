package com.openclassrooms.entrevoisins.ui.neighbour_list;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.utils.ItemClickSupport;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;

    private static final int REQUEST_CODE_DETAIL = 7;
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_PHONE = "EXTRA_PHONE";
    private static final String EXTRA_PHOTO = "EXTRA_PHOTO";
    private static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    private static final String EXTRA_ABOUT = "EXTRA_ABOUT";
    private static final String EXTRA_FAVORITE = "EXTRA_FAVORITE";


    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance() {
        NeighbourFragment fragment = new NeighbourFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
        mApiService.getNeighbours().get(4).setFavorite(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        configureOnClickRecyclerView();
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        mNeighbours = mApiService.getNeighbours();
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours));

    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void configureOnClickRecyclerView(){

        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_neighbour)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                        Log.e("TAG", "Id à la position "+position+" est : "+mNeighbours.get(position).getId());
                        Intent intent = new Intent(getActivity(), DetailNeighbourActivity.class);
                        intent.putExtra(EXTRA_ID, mNeighbours.get(position).getId());
                        intent.putExtra(EXTRA_NAME, mNeighbours.get(position).getName());
                        intent.putExtra(EXTRA_PHOTO, mNeighbours.get(position).getAvatarUrl());
                        intent.putExtra(EXTRA_PHONE, mNeighbours.get(position).getPhoneNumber());
                        intent.putExtra(EXTRA_ADDRESS, mNeighbours.get(position).getAddress());
                        intent.putExtra(EXTRA_ABOUT, mNeighbours.get(position).getAboutMe());
                        intent.putExtra(EXTRA_FAVORITE, mNeighbours.get(position).isFavorite());

                        //DetailNeighbourActivity.navigate(getActivity());
                        startActivityForResult(intent,REQUEST_CODE_DETAIL);

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_DETAIL == requestCode && RESULT_OK == resultCode) {
            initList();
            // Fetch the score from the Intent
            boolean favorite=data.getBooleanExtra(DetailNeighbourActivity.BUNDLE_FAVORITE,false);
            int pos = ((int) data.getLongExtra(DetailNeighbourActivity.BUNDLE_POSITION,-1))-1;
            mNeighbours.get(pos).setFavorite(favorite);
        }
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        initList();
    }
}
