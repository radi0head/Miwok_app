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
public class NumbersFragment extends android.support.v4.app.Fragment {

    private MediaPlayer pronunciation;
    private AudioManager am;
    private AudioManager.OnAudioFocusChangeListener ears=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            switch (i){
                case AudioManager.AUDIOFOCUS_GAIN:  pronunciation.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:  releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:    pronunciation.pause();
                    pronunciation.seekTo(0);
                    break;
            }
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_numbers,container,false);
        if(getActivity().getActionBar()!=null)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        am=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> Words=new ArrayList<Word>();
        Words.add(new Word("one","lutti",R.drawable.number_one,R.raw.number_one));
        Words.add(new Word("two","otiiko",R.drawable.number_two,R.raw.number_two));
        Words.add(new Word("three","tolookosu",R.drawable.number_three,R.raw.number_three));
        Words.add(new Word("four","oyyisa",R.drawable.number_four,R.raw.number_four));
        Words.add(new Word("five","massokka",R.drawable.number_five,R.raw.number_five));
        Words.add(new Word("six","temmokka",R.drawable.number_six,R.raw.number_six));
        Words.add(new Word("seven","kenekaku",R.drawable.number_seven,R.raw.number_seven));
        Words.add(new Word("eight","kawinta",R.drawable.number_eight,R.raw.number_eight));
        Words.add(new Word("nine","wo'e",R.drawable.number_nine,R.raw.number_nine));
        Words.add(new Word("ten","na'aacha",R.drawable.number_ten,R.raw.number_ten));
        WordAdapter itemsAdapter=new WordAdapter(getActivity(),Words,R.color.category_numbers);
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
                    pronunciation.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            releaseMediaPlayer();
                        }
                    });
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
