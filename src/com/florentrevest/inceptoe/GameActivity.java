package com.florentrevest.inceptoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class GameActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ChatFragment.OnMessageSent, BoardFragment.OnMakeMove, BoardFragment.OnActionbarChange {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static private TCPClient mTcpClient = null;
    static private Thread mThread = null;
    static private ChatFragment cf;
    static private BoardFragment bf;
    private String mNickname, mChannel;
    private PagerTitleStrip mPts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mNickname = getIntent().getStringExtra("nickname");
        mChannel = getIntent().getStringExtra("channel");

        if(savedInstanceState != null)
        {
            mNickname = savedInstanceState.getString("nickname");
            getActionBar().setTitle(savedInstanceState.getString("actionbartitle"));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if(bf == null && cf == null)
        {
            bf = (BoardFragment)mSectionsPagerAdapter.getItem(0);
            bf.setRetainInstance(true);
            bf.setOnMakeMove(this);
            bf.setOnActionbarChange(this);
            bf.setNickname(mNickname);

            cf = (ChatFragment)mSectionsPagerAdapter.getItem(1);
            cf.setRetainInstance(true);
            cf.setOnMessageSent(this);
            cf.setNickname(mNickname);
        }

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        mPts = (PagerTitleStrip)findViewById(R.id.pager_title_strip);

        if(mTcpClient == null)
        {
            mThread = new Thread(new Runnable() {
                public void run() {
                    mTcpClient = new TCPClient(mChannel, mNickname, new TCPClient.OnMessageReceived()
                    {
                        @Override
                        public void handshakeReply(Boolean accepted, final String error_message, Integer version)
                        {
                            if(accepted)
                                mTcpClient.join_match();
                            else
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();

                                        if(mTcpClient != null)
                                            mTcpClient.stopClient();
                                        mTcpClient = null;
                                        bf = null;
                                        cf = null;
                                        if(mThread != null)
                                            mThread.interrupt();
                                        finish();
                                    }
                                });
                            }
                        }

                        @Override
                        public void joinMatchReply(final Boolean accepted, final String error_message, String match_id, List<String> users)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!accepted)
                                    {
                                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();

                                        if(mTcpClient != null)
                                            mTcpClient.stopClient();
                                        mTcpClient = null;
                                        bf = null;
                                        cf = null;
                                        if(mThread != null)
                                            mThread.interrupt();
                                        finish();                                    }
                                }
                            });
                        }

                        @Override
                        public void userJoinedMatch(final String user, String match_id)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), user + " est connecté!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void newGame(String match_id, final String your_char, final String current_player, final Map<String, String> users, final List<List<String>> grid, final String last_player, final int last_line, final int last_column)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bf.init(your_char, current_player, users, grid, last_player, last_line, last_column);
                                }
                            });
                        }

                        @Override
                        public void makeMove(String match_id, final Integer line, final Integer column)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bf.receiveMove(line, column);
                                    if(mViewPager.getCurrentItem() == 1)
                                        mPts.setBackgroundResource(R.color.red);
                                }
                            });
                        }

                        @Override
                        public void message(String match_id, final String from, final String message)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cf.receiveMessage(from, message);
                                    if(mViewPager.getCurrentItem() == 0)
                                        mPts.setBackgroundResource(R.color.red);
                                }
                            });
                        }

                        @Override
                        public void char_change(String match_id, final String nick, final String new_char)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bf.char_change(nick, new_char);
                                }
                            });
                        }

                        @Override
                        public void networkError()
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();

                                    if(mTcpClient != null)
                                        mTcpClient.stopClient();
                                    mTcpClient = null;
                                    bf = null;
                                    cf = null;
                                    if(mThread != null)
                                        mThread.interrupt();
                                    finish();
                                }
                            });
                        }
                    });
                    mTcpClient.run();
                }
            });
            mThread.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("nickname", mNickname);
        outState.putString("actionbartitle", getActionBar().getTitle().toString());
    }

    @Override
    public void onBackPressed()
    {
        if(mTcpClient != null)
            mTcpClient.stopClient();
        mTcpClient = null;
        bf = null;
        cf = null;
        if(mThread != null)
            mThread.interrupt();
        finish();
    }

    public void sendMessage(String message) // From ChatFragment
    {
        mTcpClient.message(message);
    }

    public void makeMove(Integer line, Integer column) // From BoardFragment
    {
        mTcpClient.make_move(line, column);
    }

    @Override
    public void changeActionbar(String title)
    {
        getActionBar().setTitle(title);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2)
    { }

    @Override
    public void onPageSelected(int i)
    {
        mPts.setBackgroundResource(R.color.blue);
    }

    @Override
    public void onPageScrollStateChanged(int i)
    { }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        BoardFragment boardf;
        ChatFragment chatf;
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            boardf = new BoardFragment();
            chatf = new ChatFragment();
        }

        @Override
        public Fragment getItem(int position)
        {
            if(position == 0)
                return boardf;
            else
                return chatf;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
                case 0:
                    return "Grille";
                case 1:
                    return "Chat";
            }
            return null;
        }
    }
}
