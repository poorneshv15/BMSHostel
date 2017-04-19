package com.psps.projects.bmshostel;

/**
 * Created by Pratik on 10-04-2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

class AndroidImageAdapter extends PagerAdapter {
    private Context mContext;

    AndroidImageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return sliderImagesId.length;
    }

    private int[] sliderImagesId = new int[]{
            R.drawable.farouche, R.drawable.hostel, R.drawable.savepaper,
            R.drawable.foodfiesta, R.drawable.raggin, R.drawable.crickettournament,
    };

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ( obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageResource(sliderImagesId[i]);
        ( container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ( container).removeView((ImageView) obj);
    }
}