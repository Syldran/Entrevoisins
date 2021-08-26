package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailNeighbourActivity extends AppCompatActivity {

    @BindView(R.id.detail_name)
    TextView mDetailName;
    @BindView(R.id.detail_avatar_name)
    TextView mDetailAvatarName;
    @BindView(R.id.detail_phone)
    TextView mDetailPhone;
    @BindView(R.id.detail_website)
    TextView mDetailWebsite;
    @BindView(R.id.detail_address)
    TextView mDetailAddress;
    @BindView(R.id.detail_aboutme)
    TextView mDetailAboutMe;
    @BindView(R.id.detail_avatar)
    ImageView mDetailAvatar;
    @BindView(R.id.detail_favorite)
    ImageButton mFavoriteButton;




    public static final String BUNDLE_FAVORITE="BUNDLE_FAVORITE";
    public static final String BUNDLE_POSITION="BUNDLE_POSITION";
    public static final String BUNDLE_STATE_FAVORITE="BUNDLE_STATE_FAVORITE";
    private boolean mIsFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);

        Neighbour mNeighbour=(Neighbour) getIntent().getSerializableExtra("EXTRA_NEIGHBOUR");
        mDetailAvatarName.setText(mNeighbour.getName());
        mDetailName.setText(mNeighbour.getName());
        mDetailPhone.setText(mNeighbour.getPhoneNumber());
        mDetailAddress.setText(mNeighbour.getAddress());
        mDetailAboutMe.setText(mNeighbour.getAboutMe());
        mDetailWebsite.append(mNeighbour.getName().toLowerCase());
        loadImage(mNeighbour.getAvatarUrl());

        if(savedInstanceState!=null){
            mIsFavorite=savedInstanceState.getBoolean(BUNDLE_STATE_FAVORITE);
        }else  mIsFavorite=mNeighbour.isFavorite();

        if (mIsFavorite) {
            //if is favorite
            mFavoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_white_24dp));
        }
        else {
            //if is not favorite
            mFavoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_border_white_24dp));
        }

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFavorite){
                    mFavoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_border_white_24dp));
                    mIsFavorite=false;
                }else{
                    mFavoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_white_24dp));
                    mIsFavorite=true;
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_FAVORITE,mIsFavorite);
        intent.putExtra(BUNDLE_POSITION,getIntent().getIntExtra("NEIGHBOUR_POSITION",-1) );
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_STATE_FAVORITE, mIsFavorite);
    }


    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, DetailNeighbourActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public void loadImage(String url)
    {
        Glide.with(getBaseContext()).load(url).apply(RequestOptions.centerCropTransform()).into(mDetailAvatar);
    }

}