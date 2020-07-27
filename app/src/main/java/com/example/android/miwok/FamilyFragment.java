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
public class FamilyFragment extends Fragment {

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


    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list,container,false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word(R.drawable.family_father, R.raw.family_father,"father", "әpә"));
        words.add(new Word(R.drawable.family_mother, R.raw.family_mother,"mother", "әṭa"));
        words.add(new Word(R.drawable.family_son, R.raw.family_son,"son", "angsi"));
        words.add(new Word(R.drawable.family_daughter,R.raw.family_daughter, "daughter", "tune"));
        words.add(new Word(R.drawable.family_older_brother, R.raw.family_older_brother,"older brother", "taachi"));
        words.add(new Word(R.drawable.family_younger_brother,R.raw.family_younger_brother ,"younger brother", "chalitti"));
        words.add(new Word(R.drawable.family_older_sister, R.raw.family_older_sister,"older sister", "teṭe"));
        words.add(new Word(R.drawable.family_younger_sister, R.raw.family_younger_sister,"younger sister", "kolliti"));
        words.add(new Word(R.drawable.family_grandmother, R.raw.family_grandmother,"grandmother", "ama"));
        words.add(new Word(R.drawable.family_grandfather, R.raw.family_grandfather,"grandfather", "paapa"));
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_family);
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
