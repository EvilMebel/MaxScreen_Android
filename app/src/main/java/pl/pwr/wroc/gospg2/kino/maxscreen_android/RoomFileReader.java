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

import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.RoomView;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.SeatView;

/**
 * Created by Evil on 2015-05-08.
 */
public class RoomFileReader {


    private static final String hall1 = "00111111111111111111111111\n" +
            "00111111111111111111111111111\n" +
            "00111111111111111111111111111\n" +
            "00111111111111111111111111111\n" +
            "00111111111111111111111111111\n" +
            "00111111111111111111111111111\n" +
            "0011111111111111111111111111100011111\n" +
            "0011111111111111111111111111100011111\n" +
            "0011111111111111111111111111100011111\n" +
            "0011111111111111111111111111100011111\n" +
            "111111111111111111111111111111111111";
    private static final String hall2 = "11111111111111111111111111111111111\n" +
            "001111111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111100000111111111111\n" +
            "0\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "00011111111111111111111111111111\n" +
            "0000111111111111111111111111111";
    private static final String hall3 = "111111001111111111111111100111111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111111111111111001111\n" +
            "0011110011111100001111111001111\n" +
            "0\n" +
            "000111111111111111111111111111\n" +
            "000111111111100000011111111111\n" +
            "00001111111110000001111111111\n" +
            "000000111111100000011111111";

    static public void openFile(RoomView roomView, String input){//}, String is ) {

		//InputStream is = context.getResources().openRawResource(R.raw.testowy);
        //BufferedReader br = new BufferedReader(is);*/
        String is = input.replace("a","\n");


        Context context = MaxScreen.getContext();

        BufferedReader br = new BufferedReader(new StringReader(is));

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

    static public void openTheDoor(RoomView roomView,Halls hall){//}, String is ) {
        String is = hall.getStructureFile();
        String[] rows = is.split("a");

        Log.d("read","rows ="+rows.length + " str=" + is);


        Context context = MaxScreen.getContext();

        BufferedReader br = new BufferedReader(new StringReader(is));

        int maxX =0;
        int maxY =0;

        boolean firstFrame = true;
        try {
            //String test = null;
            int row = 1;
            int col = 1;

            int x = 1;
            int y = 1;
            char c;

            String currentRow = "";
            int rowsCount = rows.length;
            for(int i = 0; i < rowsCount; i++) {
                currentRow = rows[i];
                Log.d("reader", "reading: " + currentRow);
                //numbers from left to right
                col = 1;
                x=1;

                boolean first = true;
                for(int j = 0; j< currentRow.length(); j++) {

                    c = currentRow.charAt(j);
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

