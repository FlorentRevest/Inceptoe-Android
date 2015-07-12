package com.florentrevest.inceptoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends Activity
{
    private EditText mNicknameEditText, mChannelEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Button connectButton = (Button)findViewById(R.id.connectButton);
        mNicknameEditText = (EditText)findViewById(R.id.nicknameEditText);
        mChannelEditText = (EditText)findViewById(R.id.channelEditText);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        if(savedInstanceState != null)
        {
            mNicknameEditText.setText(savedInstanceState.getString("nickname"));
            mChannelEditText.setText(savedInstanceState.getString("channel"));
        }
        else
        {
            String channel = preferences.getString("channel", null);
            String nickname = preferences.getString("nickname", null);

            if(nickname != null)
                mNicknameEditText.setText(nickname);
            if(channel != null)
                mChannelEditText.setText(channel);
        }

        final SharedPreferences.Editor editor = preferences.edit();

        connectButton.setOnClickListener(new OnClickListener ()
        {
            @Override
            public void onClick(View arg0)
            {
                if(!mNicknameEditText.getText().toString().isEmpty())
                {
                    Intent gameIntent = new Intent(ConnectActivity.this, GameActivity.class);
                    gameIntent.putExtra("nickname", mNicknameEditText.getText().toString());
                    editor.putString("nickname", mNicknameEditText.getText().toString());
                    gameIntent.putExtra("channel", mChannelEditText.getText().toString());
                    editor.putString("channel", mChannelEditText.getText().toString());
                    ConnectActivity.this.startActivity(gameIntent);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("nickname", mNicknameEditText.getText().toString());
        outState.putString("channel", mChannelEditText.getText().toString());
    }
}
