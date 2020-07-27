package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            else if (i==AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
            }
            else if (i==AudioManager.AUDIOFOCUS_LOSS){
                mediaPlayer.release();
            }
        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener= new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list,container,false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word(R.drawable.color_red, R.raw.color_red,"red", "weṭeṭṭi"));
        words.add(new Word(R.drawable.color_green, R.raw.color_green,"green", "chokokki"));
        words.add(new Word(R.drawable.color_brown, R.raw.color_brown,"brown", "ṭakaakki"));
        words.add(new Word(R.drawable.color_gray, R.raw.color_gray,"gray", "ṭopoppi"));
        words.add(new Word(R.drawable.color_black, R.raw.color_black,"black", "kululli"));
        words.add(new Word(R.drawable.color_white, R.raw.color_white,"white", "kelelli"));
        words.add(new Word(R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow,"dusty yellow", "ṭopiisә"));
        words.add(new Word(R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow,"mustard yellow", "chiwiiṭә"));
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                releaseMediaPlayer();
                Word current = words.get(i);
                int result = audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), current.getPronunciationResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
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
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
}
