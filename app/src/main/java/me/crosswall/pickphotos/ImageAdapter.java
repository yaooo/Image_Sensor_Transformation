package me.crosswall.pickphotos;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.crosswall.photo.pick.util.UriUtil;

/**
 * Created by yuweichen on 15/12/10.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>{
    private Context context;
    private ArrayList<String> images = new ArrayList<>();
    private int widget;
    public ImageAdapter(Context context){
        this.context = context;
        widget = context.getResources().getDisplayMetrics().widthPixels / 3;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(new ImageView(context));
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.setData(getItem(position));
    }

    public void addData(ArrayList<String> images){
        this.images.addAll(images);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
        this.images.clear();
        notifyDataSetChanged();
    }

    public String getItem(int position){
        return this.images.get(position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }

        public void setData(String folderPath){

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widget,widget);
            params.setMargins(5,5,5,5);
            imageView.setLayoutParams(params);
            Uri uri = UriUtil.generatorUri(folderPath,UriUtil.LOCAL_FILE_SCHEME);
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .placeholder(imageView.getDrawable())
                    .thumbnail(0.3f)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(imageView);
        }
    }
}
