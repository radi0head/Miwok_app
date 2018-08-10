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
public class FamilyFragment extends android.support.v4.app.Fragment {

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

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_family,container,false);
        if(getActivity().getActionBar()!=null)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        am= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> Words=new ArrayList<Word>();
        Words.add(new Word("father","әpә",R.drawable.family_father,R.raw.family_father));
        Words.add(new Word("mother","әṭa",R.drawable.family_mother,R.raw.family_mother));
        Words.add(new Word("son","angsi",R.drawable.family_son,R.raw.family_son));
        Words.add(new Word("daughter","tune",R.drawable.family_daughter,R.raw.family_daughter));
        Words.add(new Word("older brother","taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        Words.add(new Word("younger brother","chalitti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        Words.add(new Word("older sister","teṭe",R.drawable.family_older_sister,R.raw.family_older_sister));
        Words.add(new Word("younger sister","kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        Words.add(new Word("grandmother","ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        Words.add(new Word("grandfather","paapa",R.drawable.family_grandfather,R.raw.family_grandfather));
        WordAdapter itemsAdapter=new WordAdapter(getActivity(),Words,R.color.category_family);
        ListView list=(ListView) rootView.findViewById(R.id.list);
        list.setAdapter(itemsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                releaseMediaPlayer();
                int result=am.requestAudioFocus(mOnAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
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
        am.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

}
