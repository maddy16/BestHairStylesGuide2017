package com.sizzling.apps.hairstyleguide;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by ahmed on 20/07/2017.
 */

public class StepsAdapter extends PagerAdapter {
    Context context;
    private ArrayList<Integer> list;
    StepsAdapter(Context context, ArrayList<Integer> list){
        this.context=context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.steps_item, container,false);
        GestureImageView imageView  =  (GestureImageView) view.findViewById(R.id.stepImage);
        RequestOptions glideOptions = new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Glide.with(context)
                .load(list.get(position)).apply(glideOptions)
                .into(imageView);
//        ImageView imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setImageResource(list.get(position));
        imageView.getController().enableScrollInViewPager((ViewPager) container);
        imageView.getController().getSettings().setMaxZoom(8.0f);
        imageView.getController().getSettings().setZoomEnabled(true);
        TextView stepName = view.findViewById(R.id.text_step);
        stepName.setText("Step # "+(position+1));
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
