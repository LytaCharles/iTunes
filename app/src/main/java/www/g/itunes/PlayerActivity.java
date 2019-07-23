package www.g.itunes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {

    Toolbar playerToolbar;

    CircleImageView mcoverart;

    Button btn_playnext, btn_playprevious, btn_pause, btn_shuffle, btn_repeat;

    TextView songTextLabel;
    String sname;
    SeekBar songSeekBar;
    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerToolbar = (Toolbar)findViewById(R.id.playerToolbar_id);
        setSupportActionBar(playerToolbar);
        getSupportActionBar().setTitle("Now Playing...");
        //getSupportActionBar().setSubtitle("Now Playing...");
        playerToolbar.setNavigationIcon(R.drawable.ic_navigationback);



        mcoverart = (CircleImageView)findViewById(R.id.iv_coverart);
        mcoverart.setBorderOverlay(true);
        mcoverart.setBorderWidth(1);
        mcoverart.setImageResource(R.drawable.coverart);
        mcoverart.setBorderColor(Color.RED);

        btn_playnext = (Button)findViewById(R.id.bt_playnext);
        btn_playprevious = (Button)findViewById(R.id.bt_playprevious);
        btn_pause = (Button)findViewById(R.id.bt_pause);
        btn_repeat = (Button)findViewById(R.id.bt_repeat) ;
        btn_shuffle = (Button)findViewById(R.id.bt_shuffle);


        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayerActivity.this, "This feature is currently unavailable", Toast.LENGTH_SHORT).show();
            }
        });


        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayerActivity.this, "This feature is currently unavailable", Toast.LENGTH_SHORT).show();
            }
        });


        songTextLabel = (TextView)findViewById(R.id.tv_songnamedisplay);
        songSeekBar = (SeekBar) findViewById(R.id.song_seekBar_id);

  //This code updates the seekBar when music is playing
        updateSeekBar = new Thread(){

            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){

                    try{
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);

                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };
         if (myMediaPlayer!=null){
             myMediaPlayer.stop();
             myMediaPlayer.release();
         }
        Intent i = getIntent();
         Bundle bundle = i.getExtras();

         mySongs = (ArrayList)bundle.getParcelableArrayList("songs");


         //This code sets the song name on the Text view for song label

        sname = mySongs.get(position).toString();
        String  songName = i.getStringExtra("songname");
        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position = bundle.getInt("pos", 0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        songSeekBar.setMax(myMediaPlayer.getDuration());
        updateSeekBar.start();

        songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorWhite),PorterDuff.Mode.SRC_IN);

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        //This code deals with the play and pause button
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songSeekBar.setMax(myMediaPlayer.getDuration());

                if (myMediaPlayer.isPlaying()){

                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();

                }
                else {
                    btn_pause.setBackgroundResource(R.drawable.ic_pause);
                    myMediaPlayer.start();
                }

            }
        });

        //This code  deals with the button for playing next song
        btn_playnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
            }
        });

        //This code deals with the button for playing previous song
        btn_playprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
            }
        });





    }




}
