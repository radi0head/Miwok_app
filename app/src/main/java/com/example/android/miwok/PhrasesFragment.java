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
public class PhrasesFragment extends android.support.v4.app.Fragment {

    private MediaPlayer pronunciation;
    private AudioManager am;
    private MediaPlayer.OnCompletionListener mOnCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i==AudioManager.AUDIOFOCUS_GAIN)
                pronunciation.start();
            else if(i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT||i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                pronunciation.pause();
                pronunciation.seekTo(0);
            }
            else if(i==AudioManager.AUDIOFOCUS_LOSS)
                releaseMediaPlayer();
        }
    };

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_phrases,container,false);
        if(getActivity().getActionBar()!=null)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        am= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> Words=new ArrayList<Word>();
        Words.add(new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        Words.add(new Word("What is your name?","tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        Words.add(new Word("My name is...","oyaaset...",R.raw.phrase_my_name_is));
        Words.add(new Word("How are you feeling?","michәksәs?",R.raw.phrase_how_are_you_feeling));
        Words.add(new Word("I’m feeling good.","kuchi achit",R.raw.phrase_im_feeling_good));
        Words.add(new Word("Are you coming?","әәnәs'aa?",R.raw.phrase_are_you_coming));
        Words.add(new Word("Yes, I’m coming.","hәә’ әәnәm",R.raw.phrase_yes_im_coming));
        Words.add(new Word("I’m coming.","әәnәm",R.raw.phrase_im_coming));
        Words.add(new Word("Let’s go","yoowutis",R.raw.phrase_lets_go));
        Words.add(new Word("Come here","әnni'nem",R.raw.phrase_come_here));
        WordAdapter itemsAdapter=new WordAdapter(getActivity(),Words,R.color.category_phrases);
        ListView list=(ListView) rootView.findViewById(R.id.list);
        list.setAdapter(itemsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                releaseMediaPlayer();
                int result=am.requestAudioFocus(mOnAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    pronunciation=MediaPlayer.create(getActivity(),Words.get(i).getSoundResourceID());
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
        am.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

}
