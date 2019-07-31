package com.example.miriyusifli.cardgame.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.view.Display;
import android.widget.Button;
import android.widget.GridLayout;

/**
 * Created by miriyusifli on 6/15/17.
 */

public class MyButton extends Button{
    public MyButton(Context context) {
        super(context);
    }

    public  void setSizes(Activity activity, int columnCount, int widthDivisor){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point screen = new Point();
        display.getSize(screen);
        int width = screen.x;
        setWidth(width/columnCount-width/widthDivisor);

        GridLayout.LayoutParams params=new GridLayout.LayoutParams();

        params.setMargins(width/54,width/54,width/54,width/54);
        setLayoutParams(params);


    }

    public void setColors(String textColour,String backroundColour){
        setTextColor(Color.parseColor(textColour));
        setBackgroundColor(Color.parseColor(backroundColour));



    }

    public void setGradientColor(String beginColour, String endColour, GradientDrawable.Orientation direction){
        int[] colors = {Color.parseColor(beginColour),Color.parseColor(endColour)};

        //create a new gradient color

        GradientDrawable gd = new GradientDrawable(direction, colors);

        gd.setCornerRadius(14);
        //apply the button background to newly created drawable gradient
        setBackground(gd);
    }

    public void SetBorderColor(){

    }




}
