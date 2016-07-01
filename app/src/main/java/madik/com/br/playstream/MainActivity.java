package madik.com.br.playstream;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    public String StringUrl="http://stm3.srvstm.com:17938/mp3";
    InputStream inputStream;
    FileOutputStream fileOutputStream;

    public URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            URL url=new URL(StringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        final MediaPlayer mPlayer =new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try{

            mPlayer.reset();
            mPlayer.setDataSource(StringUrl);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                @Override
                public void onPrepared(MediaPlayer mediaPlayer){
                    Log.i("#STATUS#","#Starting CBMN...");
                    mediaPlayer.start();

                    Log.i("#GRAVANDO#","Recording CBMN radio...");
   }
            });

            mPlayer.prepare();
            Log.i("##########","Ligando...");
            Log.i("MediaPlayer ","--> "+mPlayer.isPlaying());
            //mPlayer.start();
            Log.i("MediaPlayer ","--> "+mPlayer.isPlaying());

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }



    @Override
    public void onPrepared(MediaPlayer mPlayer) {

        if(mPlayer.isPlaying()){

            Log.i("# status #","Estava tocando...");

            mPlayer.stop();
            mPlayer.release();

        }else{
            Log.i("# status #","Não está tocando..."+mPlayer.isPlaying());
        }
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("Error","MediaPlayer ");
        return false;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i("BUFFER: ", "---> "+mp.MEDIA_INFO_BUFFERING_START);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("###########", "Vai caralho!!");
        mp.release();

    }
}
