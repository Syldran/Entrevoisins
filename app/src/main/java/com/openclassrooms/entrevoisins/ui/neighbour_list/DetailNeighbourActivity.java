package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailNeighbourActivity extends AppCompatActivity {

    @BindView(R.id.detail_name)
    TextView mDetailName;
    @BindView(R.id.detail_avatar_name)
    TextView mDetailAvatarName;
    @BindView(R.id.detail_phone)
    TextView mDetailPhone;
    @BindView(R.id.detail_address)
    TextView mDetailAddress;
    @BindView(R.id.detail_aboutme)
    TextView mDetailAboutMe;
    @BindView(R.id.detail_avatar)
    ImageView mDetailAvatar;
    @BindView(R.id.detail_favorite)
    ImageButton mFavoriteButton;
    @BindView(R.id.detail_return)
    ImageButton mReturnButton;


    public static final String BUNDLE_FAVORITE="BUNDLE_FAVORITE";
    public static final String BUNDLE_POSITION="BUNDLE_POSITION";
    public static final String BUNDLE_STATE_FAVORITE="BUNDLE_STATE_FAVORITE";
    private boolean mIsFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);

        mDetailAvatarName.setText((String) getIntent().getStringExtra("EXTRA_NAME"));
        mDetailName.setText((String) getIntent().getStringExtra("EXTRA_NAME"));
        mDetailPhone.setText((String) getIntent().getStringExtra("EXTRA_PHONE"));
        mDetailAddress.setText((String) getIntent().getStringExtra("EXTRA_ADDRESS"));
        mDetailAboutMe.setText((String) getIntent().getStringExtra("EXTRA_ABOUT"));
        loadImage((String) getIntent().getStringExtra("EXTRA_PHOTO"));

        if(savedInstanceState!=null){
            mIsFavorite=savedInstanceState.getBoolean(BUNDLE_STATE_FAVORITE);
        }else  mIsFavorite=(boolean) getIntent().getBooleanExtra("EXTRA_FAVORITE", false);

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
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(BUNDLE_FAVORITE,mIsFavorite);
                intent.putExtra(BUNDLE_POSITION,(long) getIntent().getLongExtra("EXTRA_ID",-1));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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
        Glide.with(getBaseContext()).load(url).into(mDetailAvatar);
    }

}