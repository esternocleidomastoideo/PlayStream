package madik.com.br.playstream;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
//import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    File myFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),"cbmn.mp3");
    public String StringUrl="http://stm3.srvstm.com:17938/mp3";
    //public String StringUrl="http://170.75.144.250:25740/mp3";
    public static URL url;
    public static BufferedInputStream bis;
    public static InputStream inputStream;
    public static OutputStream outputStream;
    public static FileOutputStream fileOutputStream;
    public static long bytesLidos=0;
    public static BufferedOutputStream boi;
    private Handler handler = new Handler();
    ProgressBar progressbar;
    MediaPlayer mPlayer;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.textView2);
        TextView tv2 = (TextView) findViewById(R.id.textViewTransferidos);

        int MaxSizeFile=2*1048576; //10 megabytes
        Drawable draw = getResources().getDrawable(R.drawable.my_progress_bar);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        progressbar.setProgress(0);
        progressbar.setSecondaryProgress(1);
        progressbar.setMax(MaxSizeFile);
        progressbar.setProgressDrawable(draw);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new PlayStreaming().execute();

       //new GravarMyStreaming().execute();

    }


        public void connectMyStreaming(){
                    try {
                        url = new URL(StringUrl);
                    } catch (MalformedURLException e){e.printStackTrace();}

                    final MediaPlayer mPlayer = new MediaPlayer();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    try{
                        mPlayer.setDataSource(StringUrl);
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                Toast.makeText(getApplicationContext(), "Tocando e gravando agora!", Toast.LENGTH_LONG).show();
                                mediaPlayer.start();
                                new GravarMyStreaming().execute();
                            }
                        });

                        mPlayer.prepare();

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

    }

    @Override
    public void onPrepared(MediaPlayer mPlayer) {
        if(mPlayer.isPlaying()){
        }else{
            //mPlayer.release();
            Log.i("#####","Player busy...");

        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) { return false; }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {  }

    @Override
    public void onCompletion(MediaPlayer mp) {  mp.release();  }

    public void updateProgress(long bytesLidos){

        final long progresso=progressbar.getMax()*bytesLidos/progressbar.getMax();
        progressbar.setProgress((int)progresso);
    }

    //TASK para tocar
    public class PlayStreaming extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {

            mPlayer = new MediaPlayer();


            try {
                url = new URL(StringUrl);
            } catch (MalformedURLException e){e.printStackTrace();}

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try{
                mPlayer.setDataSource(StringUrl);
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Toast.makeText(getApplicationContext(), "Tocando Rádio!", Toast.LENGTH_LONG).show();
                        mediaPlayer.start();
                    }
                });

                mPlayer.prepare();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            new GravarMyStreaming().execute();
            super.onPostExecute(aVoid);
        }

    }


    //


    //TASK para gravar o streaming
    public class GravarMyStreaming extends AsyncTask<Void, Integer, Long> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            long bytesLidos=0;
            //URL url;
            //BufferedInputStream bis;
            //InputStream inputStream;
            //OutputStream outputStream;
            //BufferedOutputStream boi;
            //progressbar.setMax(5000);
            //progressbar.setVisibility(View.INVISIBLE);
            FileOutputStream fileOutputStream;

        }

        @Override
        protected Long doInBackground(Void...params){

            try {
                //boi = new BufferedOutputStream(new FileOutputStream(myFile)); //deixando lento
                //fileOutputStream = new FileOutputStream(new File(root,"cbmn.mp3"));
                fileOutputStream = new FileOutputStream(myFile);
                //bis = new BufferedInputStream(inputStream);
                //bis = (BufferedInputStream) url.openStream();
                inputStream = url.openStream();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int c=0;

            try {

                while ((c = inputStream.read()) != -1 ) {

                    final TextView tv = (TextView)findViewById(R.id.textView2);
                    final TextView tv2 =(TextView)findViewById(R.id.textViewTransferidos);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            tv.setText((bytesLidos/1024)/1024+"");
                            tv2.setText(bytesLidos/2048+" Kb");
                        }
                    });

                    fileOutputStream.write(c);
                    //boi.write(c);
                    bytesLidos++;
                    publishProgress((int)bytesLidos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bytesLidos;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressbar.setProgress(values[0]);
            int status = (values[0]);
            super.onProgressUpdate(status/progressbar.getMax());
        }
    }

}
