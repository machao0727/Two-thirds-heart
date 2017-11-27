package com.mc.devwithchao.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.mc.devwithchao.R;
import com.mc.devwithchao.view.bookview.BookView;
import com.mc.devwithchao.view.circledisperseview.CircleDisperseView;
import com.mc.devwithchao.view.tickingview.TickView;

public class AnimaActivity extends AppCompatActivity {
    private TickView tickview;
    private CircleDisperseView circlview;
    private BookView bookview;

    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima);
        tickview= (TickView) findViewById(R.id.tickview);
        circlview= (CircleDisperseView) findViewById(R.id.circlview);
        bookview= (BookView) findViewById(R.id.bookview);
        seekBar = (SeekBar) findViewById(R.id.seekbr);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                matchview.setProgress(progress * 1f / 100);
                bookview.updataProgress(progress*1f/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void startanim(View view){
        tickview.startAnim();
    }

    public void open(View view){
        circlview.open();
    }
    public void close(View view){
        circlview.close();
    }
}
