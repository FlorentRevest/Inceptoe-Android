package com.florentrevest.inceptoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class GridActivity extends Activity {
    ImageView mIV;
    int mWidth, mHeight, mX, mY;
    String mValues;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
        {
            mValues = savedInstanceState.getString("values");
            mX = savedInstanceState.getInt("x");
            mY = savedInstanceState.getInt("y");
        }
        else
        {
            mValues = getIntent().getStringExtra("values");
            mX = getIntent().getIntExtra("x", -1);
            mY = getIntent().getIntExtra("y", -1);
        }
        mIV = new ImageView(this);
        mIV.setImageBitmap(Utilities.generateGrid(this, mValues));
        mIV.setAdjustViewBounds(true);
        mIV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.setContentView(mIV);

        mWidth =  getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        if(e.getAction() == MotionEvent.ACTION_UP)
        {
            float x = e.getX();
            float y = e.getY();
            int caseX = -1;
            int caseY = -1;

            int size = mWidth;
            boolean vertical = true;
            if(mWidth > mHeight)
            {
                size = mHeight;
                vertical = false;
            }

            if(vertical)
            {
                if(x < size/3)
                    caseX = 0;
                else if(x < 2*size/3)
                    caseX = 1;
                else
                    caseX = 2;

                int gridY = mHeight/2-size/2;
                if(y > gridY && y < gridY + size/3)
                    caseY = 0;
                else if(y > gridY + size/3 && y < gridY + 2*size/3)
                    caseY = 1;
                else if(y > gridY + 2*size/3 && y < gridY + size)
                    caseY = 2;
            }
            else
            {
                if(y < size/3)
                    caseY = 0;
                else if(y < 2*size/3)
                    caseY = 1;
                else
                    caseY = 2;

                int gridX = mWidth/2-size/2;
                if(x > gridX && x < gridX + size/3)
                    caseX = 0;
                else if(x > gridX + size/3 && x < gridX + 2*size/3)
                    caseX = 1;
                else if(x > gridX + 2*size/3 && x < gridX + size)
                    caseX = 2;
            }
            if(caseX != -1 && caseY != -1 && mX != -1 && mY != -1)
            {
                Intent result = new Intent("com.florentrevest.inceptoe.GridActivity.RESULT_ACTION");
                result.putExtra("x", caseX + mX*3);
                result.putExtra("y", caseY + mY*3);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("values", mValues);
        outState.putInt("x", mX);
        outState.putInt("y", mY);
    }
}