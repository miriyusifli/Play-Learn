package com.example.miriyusifli.cardgame.models.Buttons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.example.miriyusifli.cardgame.R;
import com.example.miriyusifli.cardgame.models.DAO.Topic;

public class RootTopicButton extends android.support.v7.widget.AppCompatButton {


    String text;
    Context context;

    public RootTopicButton(Context context, String text, float scale) {
        super(context);
        this.text = text;
        this.context = context;



        /*GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = (int) (140 * scale);
        params.height = (int) (140 * scale);
        params.setMargins((int) (20 * scale), 0, 0, (int) (20 * scale));*/


       /* Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/Lobster.otf");

        setTypeface(type);
*/
        setTypeface(null, Typeface.BOLD_ITALIC);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) scale * 140, (int) scale * 140);


        params.width = (int) (140 * scale);
        params.height = (int) (140 * scale);

        params.setMargins((int) (20 * scale), 0, 0, (int) (20 * scale));

        // setPadding(0, (int) (20 * scale), 0, 0);


        modifyText();
        setText(text);
        setTextColor(Color.WHITE);
        setEnabled(true);
        setLayoutParams(params);
        setResource();

    }


    public void setImage(Bitmap image, int imageWidth, int imageHeight) {


        Bitmap bitmapResized = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, false);


        Drawable d = new BitmapDrawable(getResources(), bitmapResized);
        setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);


    }

    private void modifyText() {
        if (text.length() > 11) {
            String array[] = text.split(" ");
            this.text = "";
            for (int j = 0; j < array.length; j++) {
                this.text = this.text + array[j];
                if (j != array.length - 1) {
                    this.text = text + "\n";
                }
            }
        }


    }

    public void setResource() {
        if (!isEnabled()) setBackgroundResource(R.drawable.disable_circle);
        else setBackgroundResource(R.drawable.enable_circle);

    }


}
