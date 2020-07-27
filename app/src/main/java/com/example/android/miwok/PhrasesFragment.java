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
public class PhrasesFragment extends Fragment {

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


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list,container,false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word(R.raw.phrase_where_are_you_going,"Where are you going?", "minto wuksus"));
        words.add(new Word(R.raw.phrase_what_is_your_name,"What is your name?", "tinnә oyaase'nә"));
        words.add(new Word(R.raw.phrase_my_name_is,"My name is...", "oyaaset..."));
        words.add(new Word(R.raw.phrase_how_are_you_feeling,"How are you feeling?", "michәksәs?"));
        words.add(new Word(R.raw.phrase_im_feeling_good,"I’m feeling good.", "kuchi achit"));
        words.add(new Word(R.raw.phrase_are_you_coming,"Are you coming?", "әәnәs'aa?"));
        words.add(new Word(R.raw.phrase_yes_im_coming,"Yes, I’m coming.", "hәә’ әәnәm"));
        words.add(new Word(R.raw.phrase_im_coming,"I’m coming.", "әәnәm"));
        words.add(new Word(R.raw.phrase_lets_go,"Let’s go.", "yoowutis"));
        words.add(new Word(R.raw.phrase_come_here,"Come here.", "әnni'nem"));
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);
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
