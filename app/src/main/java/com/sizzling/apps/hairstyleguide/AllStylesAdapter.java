package com.sizzling.apps.hairstyleguide;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by ahmed on 21/06/2017.
 */

public class AllStylesAdapter extends RecyclerView.Adapter<AllStylesAdapter.ViewHolder>{
private ArrayList<Integer> galleryList;
private Context context;

public AllStylesAdapter(Context context, ArrayList<Integer>galleryList){
        this.galleryList=galleryList;
        this.context=context;
        }

@Override
public AllStylesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item,viewGroup,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(AllStylesAdapter.ViewHolder viewHolder, final int i){
    RequestOptions glideOptions = new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    Glide.with(context)
            .load(galleryList.get(i)).apply(glideOptions)
            .into(viewHolder.img);
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,StepsActivity.class);
                intent.putExtra("stylenum",i+1);
                context.startActivity(intent);
            }
        });
//        viewHolder.img.setImageResource((galleryList.get(i)));
        }

@Override
public int getItemCount(){
        return galleryList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView name;
    public ViewHolder(View view) {
        super(view);
        img = (ImageView) view.findViewById(R.id.list_item_img);
        name = (TextView) view.findViewById(R.id.item_name_txt);
    }
}
}