package com.example.android.miwok;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    private int colorList;

    public WordAdapter(Context context, ArrayList<Word> list,int color){
        super(context,0,list);
        colorList=color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Word temp=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView def=(TextView)convertView.findViewById(R.id.default_text_view);
        TextView miw=(TextView)convertView.findViewById(R.id.miwok_text_view);
        ImageView img=(ImageView)convertView.findViewById(R.id.image_view);

        if(temp.getImageResourceID()!=0){
            def.setText(temp.getDefaultTranslation());
            miw.setText(temp.getMiwokTranslation());
            img.setImageResource(temp.getImageResourceID());
        }
        else{
            def.setText(temp.getDefaultTranslation());
            miw.setText(temp.getMiwokTranslation());
            img.setVisibility(View.GONE);
        }
        convertView.setBackgroundResource(colorList);
        return convertView;
    }
}
