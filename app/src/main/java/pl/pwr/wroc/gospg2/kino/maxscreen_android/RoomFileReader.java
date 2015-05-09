package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.RoomView;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.SeatView;

/**
 * Created by Evil on 2015-05-08.
 */
public class RoomFileReader {
    String res;
    static public void openFile(Context context, Intent data, RoomView roomView){//}, String is ) {

		//InputStream is = context.getResources().openRawResource(R.raw.testowy);
        //BufferedReader br = new BufferedReader(is);*/
        String is = "001111100\n"+
                                        "001111100\n"+
                                        "001101101\n"+
                                        "001101101";

        BufferedReader br = new BufferedReader(new StringReader(is));
/*

        Log.d("Path",FilePath);
        File f = new File(FilePath);
        try {
            Log.d(name,"otwieram plik");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
*/

        boolean firstFrame = true;
        try {
            String test = null;
            int row = 1;
            int col = 1;

            int x = 0;
            int y = 0;
            char c;

            for(int i = 0; (test = br.readLine()) != null; i++) {
                Log.d("reader", "reading: " + test);
                //numbers from left to right
                col = 1;
                y=0;

                for(int j = 0; j< test.length(); j++) {

                    c = test.charAt(j);
                    if(c == '1') {
                        //add seat
                        roomView.addView(new SeatView(context,x,y,row,col));
                        //new seat is a new column
                        col++;
                    } else {
                        //do nthg
                    }
                    //from left to right
                    y++;
                }






                //next row of seats
                row++;
                x++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



//		InputStream is =  getResources().openRawResource(R.raw.testowy);
    }
}

