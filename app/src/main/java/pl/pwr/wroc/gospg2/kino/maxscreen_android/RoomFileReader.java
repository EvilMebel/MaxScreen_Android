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
    static public void openFile(RoomView roomView){//}, String is ) {

		//InputStream is = context.getResources().openRawResource(R.raw.testowy);
        //BufferedReader br = new BufferedReader(is);*/
        String is = "0011111111111111111111111111111\n" +
                "0011111111111111111111111111111111\n" +
                "0011111111111111111111111111111111\n" +
                "0011111111111111111111111111111111\n" +
                "0011111111111111111111111111111111\n" +
                "0011111111111111111111111111111111\n" +
                "001111111111111111111111111111111100011111\n" +
                "001111111111111111111111111111111100011111\n" +
                "001111111111111111111111111111111100011111\n" +
                "001111111111111111111111111111111100011111\n" +
                "11111111111111111111111111111111111111111";


        Context context = MaxScreen.getContext();

        BufferedReader br = new BufferedReader(new StringReader(is));
/*

        Log.d("Path",FilePath);
        File f = new File(FilePath);
        try {
            Log.d(name,"otwieram plik");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
*/

        int maxX =0;
        int maxY =0;

        boolean firstFrame = true;
        try {
            String test = null;
            int row = 1;
            int col = 1;

            int x = 1;
            int y = 1;
            char c;

            for(int i = 0; (test = br.readLine()) != null; i++) {
                Log.d("reader", "reading: " + test);
                //numbers from left to right
                col = 1;
                x=1;

                boolean first = true;
                for(int j = 0; j< test.length(); j++) {

                    c = test.charAt(j);
                    if(c == '1') {
                        //add seat
                        Log.d("read","row="+row + " col=" + col);
                        roomView.addView(new SeatView(context,x,y,row,col));
                        //if first add ui
                        if(first) {
                            first = false;
                            SeatView ui = new SeatView(context,x-1,y,row,col);
                            ui.setStatus(SeatView.SeatStat.ONLY_VISUALIZATION);
                            roomView.addView(ui);
                        }

                        //new seat is a new column
                        col++;
                    } else {
                        //do nthg
                    }
                    //from left to right
                    x++;


                }


                if(x>maxX) {
                    maxX = x;
                }

                //next row of seats
                row++;
                y++;

                if(y>maxY) {
                    maxY = y;
                }
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

        roomView.setMaxValues(maxX,maxY);

//		InputStream is =  getResources().openRawResource(R.raw.testowy);
    }
}

