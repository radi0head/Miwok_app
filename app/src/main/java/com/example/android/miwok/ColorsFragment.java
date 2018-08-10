package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends android.support.v4.app.Fragment {

    private MediaPlayer pronunciation;
    private AudioManager am;
    private MediaPlayer.OnCompletionListener mOnCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener ears=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i==AudioManager.AUDIOFOCUS_GAIN){
                pronunciation.start();
            }
            else if(i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT||i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                pronunciation.pause();
                pronunciation.seekTo(0);
            }
            else if(i==AudioManager.AUDIOFOCUS_LOSS)
                releaseMediaPlayer();
        }
    };

    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_colors,container,false);
        if(getActivity().getActionBar()!=null)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        am= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> Words=new ArrayList<Word>();
        Words.add(new Word("red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        Words.add(new Word("green","chokokki",R.drawable.color_green,R.raw.color_green));
        Words.add(new Word("brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        Words.add(new Word("gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        Words.add(new Word("black","kululli",R.drawable.color_black,R.raw.color_black));
        Words.add(new Word("white","kelelli",R.drawable.color_white,R.raw.color_white));
        Words.add(new Word("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        Words.add(new Word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        WordAdapter itemsAdapter=new WordAdapter(getActivity(),Words,R.color.category_colors);
        ListView list=(ListView) rootView.findViewById(R.id.list);
        list.setAdapter(itemsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                releaseMediaPlayer();
                int result=am.requestAudioFocus(ears,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    pronunciation= MediaPlayer.create(getActivity(),Words.get(i).getSoundResourceID());
                    pronunciation.start();
                    pronunciation.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (pronunciation != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            pronunciation.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            pronunciation = null;
        }
        am.abandonAudioFocus(ears);
    }

}
