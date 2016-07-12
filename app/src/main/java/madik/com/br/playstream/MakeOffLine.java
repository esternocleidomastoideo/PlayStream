package madik.com.br.playstream;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marcelo on 04/07/2016.
 */
public class MakeOffLine extends AsyncTask<String, Integer, Long> {

    public static long bytesLidos=0;

    @Override
    protected Long doInBackground(String... stringUrl) {


        InputStream inputStream = null;
        FileOutputStream  fileOutputStream = null;
        URL url= null;

        try {
            url = new URL(stringUrl[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        File root = Environment.getExternalStorageDirectory();

        try {
            inputStream = url.openStream();
        } catch (IOException e) {e.printStackTrace(); }

        try {
            fileOutputStream = new FileOutputStream(new File(root,"cbmn.mp3"));
        } catch (FileNotFoundException e) {e.printStackTrace(); }


        int c;


        try {
            while ((c = inputStream.read()) != -1) {

                fileOutputStream.write(c);

                bytesLidos++;

                if((bytesLidos)==500){
                    c=-1;
                }else{
                    bytesLidos++;

                }

                Log.i("### INFO ######", "############### "+MakeOffLine.bytesLidos+" ########");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
