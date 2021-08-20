package com.openclassrooms.entrevoisins.ui.neighbour_list;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private List<Neighbour> mFavorites;
    private RecyclerView mRecyclerView;
    private static final String NEIGHBOUR_POSITION="NEIGHBOUR_POSITION";
    private static final int REQUEST_CODE_DETAIL = 7;
    private static final String EXTRA_NEIGHBOUR = "EXTRA_NEIGHBOUR";
    private static final String TAB_POSITION = "TAB_POSITION";


    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(int position) {
        NeighbourFragment fragment = new NeighbourFragment();
        Bundle args=new Bundle();
        args.putInt(TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
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
        mFavorites = new ArrayList<Neighbour>(0);
        if(getArguments().getInt(TAB_POSITION)==1)
        {
            for (int i = 0; i < mApiService.getNeighbours().size(); i++) {
                if (mApiService.getNeighbours().get(i).isFavorite()) {
                    mFavorites.add(mApiService.getNeighbours().get(i));
                }
            }

            mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mFavorites));
        }else {
            mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours));
        }

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
                        Intent intent = new Intent(getActivity(), DetailNeighbourActivity.class);
                        if(getArguments().getInt(TAB_POSITION)==1)
                            intent.putExtra(EXTRA_NEIGHBOUR,mFavorites.get(position));
                        else
                            intent.putExtra(EXTRA_NEIGHBOUR,mNeighbours.get(position));
                        intent.putExtra(NEIGHBOUR_POSITION,position);
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
            boolean favorite=data.getBooleanExtra(DetailNeighbourActivity.BUNDLE_FAVORITE,false);
            int pos = data.getIntExtra(DetailNeighbourActivity.BUNDLE_POSITION,-1);//pas bon !
            if(getArguments().getInt(TAB_POSITION)==1)
                mFavorites.get(pos).setFavorite(favorite);
            else
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
