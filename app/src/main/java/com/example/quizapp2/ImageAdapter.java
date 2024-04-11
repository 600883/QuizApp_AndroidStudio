package com.example.quizapp2;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import com.example.quizapp2.room.QuizAppEntity;

import java.util.List;
import java.util.zip.Inflater;

public class ImageAdapter extends BaseAdapter {

    private Context context;

    private List<QuizAppEntity> list;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, List<QuizAppEntity> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public ImageAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            Log.e("ImageAdapter", "The list is null");
            return 0; // Or any default value you prefer
        }
    }


    @Override
    public QuizAppEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textView = convertView.findViewById(R.id.textView3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QuizAppEntity image = list.get(position);
        String imageUriOrName = image.getUri(); // Assuming this will contain either URI or a resourceName

        if (imageUriOrName != null)  {
            // it is a file uri
            if (imageUriOrName.contains("/")) {
                Glide.with(context).load(Uri.parse(imageUriOrName)).into(holder.imageView);
            } else {
                // it is a resource name
                int resourceId = context.getResources().getIdentifier(imageUriOrName, "drawable", context.getPackageName());
                if (resourceId != 0) {
                    holder.imageView.setImageResource(resourceId);
                } else {
                    holder.imageView.setImageResource(R.drawable.haaland);
                }
            }
            holder.textView.setText(image.getName());
        } else {
            holder.imageView.setImageResource(R.drawable.haaland);
            holder.textView.setText(image.getName());
        }

        return convertView;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void setList(List<QuizAppEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<QuizAppEntity> getList() {
        return list;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}