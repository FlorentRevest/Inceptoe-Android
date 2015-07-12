package com.florentrevest.inceptoe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

public class Utilities {

    static public Bitmap generateGrid(Context c, String values) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Bitmap grid = BitmapFactory.decodeResource(c.getResources(), R.drawable.grid, opt);
        Bitmap cross = BitmapFactory.decodeResource(c.getResources(), R.drawable.cross, opt);
        Bitmap circle = BitmapFactory.decodeResource(c.getResources(), R.drawable.circle, opt);

        Canvas canvas = new Canvas(grid);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        if(values.charAt(0) == 'X')
            canvas.drawBitmap(cross, 18, 7, paint);
        else if(values.charAt(0) == 'O')
            canvas.drawBitmap(circle, 18, 7, paint);
        if(values.charAt(1) == 'X')
            canvas.drawBitmap(cross, 181, 7, paint);
        else if(values.charAt(1) == 'O')
            canvas.drawBitmap(circle, 181, 7, paint);
        if(values.charAt(2) == 'X')
            canvas.drawBitmap(cross, 343, 7, paint);
        else if(values.charAt(2) == 'O')
            canvas.drawBitmap(circle, 343, 7, paint);

        if(values.charAt(3) == 'X')
            canvas.drawBitmap(cross, 18, 141, paint);
        else if(values.charAt(3) == 'O')
            canvas.drawBitmap(circle, 18, 141, paint);
        if(values.charAt(4) == 'X')
            canvas.drawBitmap(cross, 181, 141, paint);
        else if(values.charAt(4) == 'O')
            canvas.drawBitmap(circle, 181, 141, paint);
        if(values.charAt(5) == 'X')
            canvas.drawBitmap(cross, 343, 141, paint);
        else if(values.charAt(5) == 'O')
            canvas.drawBitmap(circle, 343, 141, paint);

        if(values.charAt(6) == 'X')
            canvas.drawBitmap(cross, 18, 288, paint);
        else if(values.charAt(6) == 'O')
            canvas.drawBitmap(circle, 18, 288, paint);
        if(values.charAt(7) == 'X')
            canvas.drawBitmap(cross, 181, 288, paint);
        else if(values.charAt(7) == 'O')
            canvas.drawBitmap(circle, 181, 288, paint);
        if(values.charAt(8) == 'X')
            canvas.drawBitmap(cross, 343, 288, paint);
        else if(values.charAt(8) == 'O')
            canvas.drawBitmap(circle, 343, 288, paint);

        Bitmap largeCross = BitmapFactory.decodeResource(c.getResources(), R.drawable.large_cross, opt);
        Bitmap largeCircle = BitmapFactory.decodeResource(c.getResources(), R.drawable.large_circle, opt);

        if(whoWon(values) == 'X')
            canvas.drawBitmap(largeCross, 0, 0, paint);
        else if(whoWon(values) == 'O')
            canvas.drawBitmap(largeCircle, 0, 0, paint);

        return grid;
    }

    static public Character whoWon(String values)
    {
        if((values.charAt(0) == 'X' && values.charAt(1) == 'X' && values.charAt(2) == 'X') || //horizontal
           (values.charAt(3) == 'X' && values.charAt(4) == 'X' && values.charAt(5) == 'X') ||
           (values.charAt(6) == 'X' && values.charAt(7) == 'X' && values.charAt(8) == 'X') ||
           (values.charAt(0) == 'X' && values.charAt(3) == 'X' && values.charAt(6) == 'X') || // vertical
           (values.charAt(1) == 'X' && values.charAt(4) == 'X' && values.charAt(7) == 'X') ||
           (values.charAt(2) == 'X' && values.charAt(5) == 'X' && values.charAt(8) == 'X') ||
           (values.charAt(0) == 'X' && values.charAt(4) == 'X' && values.charAt(8) == 'X') || // diagonal
           (values.charAt(6) == 'X' && values.charAt(4) == 'X' && values.charAt(2) == 'X'))
            return 'X';
        else if((values.charAt(0) == 'O' && values.charAt(1) == 'O' && values.charAt(2) == 'O') || //horizontal
                (values.charAt(3) == 'O' && values.charAt(4) == 'O' && values.charAt(5) == 'O') ||
                (values.charAt(6) == 'O' && values.charAt(7) == 'O' && values.charAt(8) == 'O') ||
                (values.charAt(0) == 'O' && values.charAt(3) == 'O' && values.charAt(6) == 'O') || // vertical
                (values.charAt(1) == 'O' && values.charAt(4) == 'O' && values.charAt(7) == 'O') ||
                (values.charAt(2) == 'O' && values.charAt(5) == 'O' && values.charAt(8) == 'O') ||
                (values.charAt(0) == 'O' && values.charAt(4) == 'O' && values.charAt(8) == 'O') || // diagonal
                (values.charAt(6) == 'O' && values.charAt(4) == 'O' && values.charAt(2) == 'O'))
            return 'O';
        else
            return ' ';
    }

    static public class Comment {
        public boolean left;
        public String comment;

        public Comment(boolean left, String comment)
        {
            super();
            this.left = left;
            this.comment = comment;
        }
    }
}
