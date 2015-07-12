package com.florentrevest.inceptoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BoardFragment extends Fragment
{
    private OnMakeMove mMovementListener = null;
    private OnActionbarChange mActionbarListener = null;
    private ImageView grid00, grid01, grid02, grid10, grid11, grid12, grid20, grid21, grid22, lastSelectedGrid;
    private ProgressBar progressbar;
    private String mNickname, mChar, mAdversaryNickname = null, mAdversaryChar = null;
    private Boolean your_turn = false;
    private Bundle mSavedInstanceState = null;
    private List<List<String>> mGrid;
    public Boolean grid00isWon = false, grid01isWon = false, grid02isWon = false, grid10isWon = false,
            grid11isWon = false, grid12isWon = false, grid20isWon = false, grid21isWon = false, grid22isWon = false;

    public BoardFragment()
    {}

    public interface OnMakeMove
    {
        public void makeMove(Integer line, Integer column);
    }

    public interface OnActionbarChange
    {
        public void changeActionbar(String title);
    }

    public void setNickname(String nickname)
    {
        mNickname = nickname;
    }

    public void setOnMakeMove(OnMakeMove listener)
    {
        mMovementListener = listener;
    }

    public void setOnActionbarChange(OnActionbarChange listener)
    {
        mActionbarListener = listener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if(progressbar.getVisibility() == View.INVISIBLE)
            outState.putBoolean("progressHidden", true);
        if(mChar != null)
            outState.putString("mChar", mChar);
        if(mNickname != null)
            outState.putString("mNickname", mNickname);
        if(mAdversaryChar != null)
            outState.putString("mAdversaryChar", mAdversaryChar);
        if(mAdversaryNickname != null)
            outState.putString("mAdversaryNickname", mAdversaryNickname);

        outState.putStringArray("line0", mGrid.get(0).toArray(new String[0]));
        outState.putStringArray("line1", mGrid.get(1).toArray(new String[0]));
        outState.putStringArray("line2", mGrid.get(2).toArray(new String[0]));
        outState.putStringArray("line3", mGrid.get(3).toArray(new String[0]));
        outState.putStringArray("line4", mGrid.get(4).toArray(new String[0]));
        outState.putStringArray("line5", mGrid.get(5).toArray(new String[0]));
        outState.putStringArray("line6", mGrid.get(6).toArray(new String[0]));
        outState.putStringArray("line7", mGrid.get(7).toArray(new String[0]));
        outState.putStringArray("line8", mGrid.get(8).toArray(new String[0]));

        if(lastSelectedGrid == grid00)
            outState.putBoolean("highlightGrid00", true);
        if(lastSelectedGrid == grid01)
            outState.putBoolean("highlightGrid01", true);
        if(lastSelectedGrid == grid02)
            outState.putBoolean("highlightGrid02", true);
        if(lastSelectedGrid == grid10)
            outState.putBoolean("highlightGrid10", true);
        if(lastSelectedGrid == grid11)
            outState.putBoolean("highlightGrid11", true);
        if(lastSelectedGrid == grid12)
            outState.putBoolean("highlightGrid12", true);
        if(lastSelectedGrid == grid20)
            outState.putBoolean("highlightGrid20", true);
        if(lastSelectedGrid == grid21)
            outState.putBoolean("highlightGrid21", true);
        if(lastSelectedGrid == grid22)
            outState.putBoolean("highlightGrid22", true);
        outState.putBoolean("your_turn", your_turn);
        mSavedInstanceState = outState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.board_fragment, container, false);

        grid00 = (ImageView)view.findViewById(R.id.grid00);
        grid00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid00) && !grid00isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 0);
                    gridIntent.putExtra("y", 0);

                    gridIntent.putExtra("values", mGrid.get(0).get(0) + mGrid.get(0).get(1) + mGrid.get(0).get(2) +
                                                  mGrid.get(1).get(0) + mGrid.get(1).get(1) + mGrid.get(1).get(2) +
                                                  mGrid.get(2).get(0) + mGrid.get(2).get(1) + mGrid.get(2).get(2));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid10 = (ImageView)view.findViewById(R.id.grid10);
        grid10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid10) && !grid10isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 1);
                    gridIntent.putExtra("y", 0);

                    gridIntent.putExtra("values", mGrid.get(0).get(3) + mGrid.get(0).get(4) + mGrid.get(0).get(5) +
                                                  mGrid.get(1).get(3) + mGrid.get(1).get(4) + mGrid.get(1).get(5) +
                                                  mGrid.get(2).get(3) + mGrid.get(2).get(4) + mGrid.get(2).get(5));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid20 = (ImageView)view.findViewById(R.id.grid20);
        grid20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid20) && !grid20isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 2);
                    gridIntent.putExtra("y", 0);

                    gridIntent.putExtra("values", mGrid.get(0).get(6) + mGrid.get(0).get(7) + mGrid.get(0).get(8) +
                                                  mGrid.get(1).get(6) + mGrid.get(1).get(7) + mGrid.get(1).get(8) +
                                                  mGrid.get(2).get(6) + mGrid.get(2).get(7) + mGrid.get(2).get(8));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid01 = (ImageView)view.findViewById(R.id.grid01);
        grid01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid01) && !grid01isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 0);
                    gridIntent.putExtra("y", 1);

                    gridIntent.putExtra("values", mGrid.get(3).get(0) + mGrid.get(3).get(1) + mGrid.get(3).get(2) +
                                                  mGrid.get(4).get(0) + mGrid.get(4).get(1) + mGrid.get(4).get(2) +
                                                  mGrid.get(5).get(0) + mGrid.get(5).get(1) + mGrid.get(5).get(2));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid11 = (ImageView)view.findViewById(R.id.grid11);
        grid11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid11) && !grid11isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 1);
                    gridIntent.putExtra("y", 1);

                    gridIntent.putExtra("values", mGrid.get(3).get(3) + mGrid.get(3).get(4) + mGrid.get(3).get(5) +
                                                  mGrid.get(4).get(3) + mGrid.get(4).get(4) + mGrid.get(4).get(5) +
                                                  mGrid.get(5).get(3) + mGrid.get(5).get(4) + mGrid.get(5).get(5));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid21 = (ImageView)view.findViewById(R.id.grid21);
        grid21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid21) && !grid21isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 2);
                    gridIntent.putExtra("y", 1);

                    gridIntent.putExtra("values", mGrid.get(3).get(6) + mGrid.get(3).get(7) + mGrid.get(3).get(8) +
                                                  mGrid.get(4).get(6) + mGrid.get(4).get(7) + mGrid.get(4).get(8) +
                                                  mGrid.get(5).get(6) + mGrid.get(5).get(7) + mGrid.get(5).get(8));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid02 = (ImageView)view.findViewById(R.id.grid02);
        grid02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid02) && !grid02isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 0);
                    gridIntent.putExtra("y", 2);

                    gridIntent.putExtra("values", mGrid.get(6).get(0) + mGrid.get(6).get(1) + mGrid.get(0).get(2) +
                                                  mGrid.get(7).get(0) + mGrid.get(7).get(1) + mGrid.get(1).get(2) +
                                                  mGrid.get(8).get(0) + mGrid.get(8).get(1) + mGrid.get(2).get(2));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid12 = (ImageView)view.findViewById(R.id.grid12);
        grid12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid12) && !grid12isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 1);
                    gridIntent.putExtra("y", 2);

                    gridIntent.putExtra("values", mGrid.get(6).get(3) + mGrid.get(6).get(4) + mGrid.get(0).get(5) +
                                                  mGrid.get(7).get(3) + mGrid.get(7).get(4) + mGrid.get(1).get(5) +
                                                  mGrid.get(8).get(3) + mGrid.get(8).get(4) + mGrid.get(2).get(5));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        grid22 = (ImageView)view.findViewById(R.id.grid22);
        grid22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(your_turn && (lastSelectedGrid == null || lastSelectedGrid == grid22) && !grid22isWon)
                {
                    Intent gridIntent = new Intent(getActivity(), GridActivity.class);
                    gridIntent.putExtra("x", 2);
                    gridIntent.putExtra("y", 2);

                    gridIntent.putExtra("values", mGrid.get(6).get(6) + mGrid.get(6).get(7) + mGrid.get(0).get(8) +
                                                  mGrid.get(7).get(6) + mGrid.get(7).get(7) + mGrid.get(1).get(8) +
                                                  mGrid.get(8).get(6) + mGrid.get(8).get(7) + mGrid.get(2).get(8));
                    startActivityForResult(gridIntent, 0);
                }
            }
        });
        mGrid = new ArrayList<List<String>>();
        List<String> line = new ArrayList<String>();
        line.add(" ");
        line.add(" ");
        line.add(" ");
        line.add(" ");
        line.add(" ");
        line.add(" ");
        line.add(" ");
        line.add(" ");
        line.add(" ");

        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        mGrid.add(line);
        progressbar = (ProgressBar)view.findViewById(R.id.progressBar);

        if(mSavedInstanceState != null)
        {
            if(mSavedInstanceState.getBoolean("progressHidden", false));
                progressbar.setVisibility(View.INVISIBLE);
            mChar = mSavedInstanceState.getString("mChar");
            mNickname = mSavedInstanceState.getString("mNickname");
            mAdversaryChar = mSavedInstanceState.getString("mAdversaryChar");
            mAdversaryNickname = mSavedInstanceState.getString("mAdversaryNickname");
            your_turn = mSavedInstanceState.getBoolean("your_turn");

            String[] line0array = mSavedInstanceState.getStringArray("line0");
            List<String> line0list = new ArrayList<String>();
            for(String s : line0array)
                line0list.add(s);
            mGrid.add(line0list);
            String[] line1array = mSavedInstanceState.getStringArray("line1");
            List<String> line1list = new ArrayList<String>();
            for(String s : line1array)
                line1list.add(s);
            mGrid.add(line1list);
            String[] line2array = mSavedInstanceState.getStringArray("line2");
            List<String> line2list = new ArrayList<String>();
            for(String s : line2array)
                line2list.add(s);
            mGrid.add(line2list);
            String[] line3array = mSavedInstanceState.getStringArray("line3");
            List<String> line3list = new ArrayList<String>();
            for(String s : line3array)
                line3list.add(s);
            mGrid.add(line3list);
            String[] line4array = mSavedInstanceState.getStringArray("line4");
            List<String> line4list = new ArrayList<String>();
            for(String s : line4array)
                line4list.add(s);
            mGrid.add(line4list);
            String[] line5array = mSavedInstanceState.getStringArray("line5");
            List<String> line5list = new ArrayList<String>();
            for(String s : line5array)
                line5list.add(s);
            mGrid.add(line5list);
            String[] line6array = mSavedInstanceState.getStringArray("line6");
            List<String> line6list = new ArrayList<String>();
            for(String s : line6array)
                line6list.add(s);
            mGrid.add(line6list);
            String[] line7array = mSavedInstanceState.getStringArray("line7");
            List<String> line7list = new ArrayList<String>();
            for(String s : line7array)
                line7list.add(s);
            mGrid.add(line7list);
            String[] line8array = mSavedInstanceState.getStringArray("line8");
            List<String> line8list = new ArrayList<String>();
            for(String s : line8array)
                line8list.add(s);
            mGrid.add(line8list);
            updateGrids();

            if(mSavedInstanceState.getBoolean("highlightGrid00", false))
            {
                lastSelectedGrid = grid00;
                grid00.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid01", false))
            {
                lastSelectedGrid = grid01;
                grid01.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid02", false))
            {
                lastSelectedGrid = grid02;
                grid02.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid10", false))
            {
                lastSelectedGrid = grid10;
                grid10.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid11", false))
            {
                lastSelectedGrid = grid11;
                grid11.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid12", false))
            {
                lastSelectedGrid = grid12;
                grid12.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid20", false))
            {
                lastSelectedGrid = grid20;
                grid20.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid21", false))
            {
                lastSelectedGrid = grid21;
                grid21.setBackgroundColor(getResources().getColor(R.color.blue));
            }
            if(mSavedInstanceState.getBoolean("highlightGrid22", false))
            {
                lastSelectedGrid = grid22;
                grid22.setBackgroundColor(getResources().getColor(R.color.blue));
            }

        }

        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    public void init(final String your_char, final String current_player, final Map<String, String> users, final List<List<String>> grid, final String last_player, final int last_line, final int last_column)
    {
        progressbar.setVisibility(View.INVISIBLE);
        mGrid = grid;
        mChar = your_char;
        if(!mChar.isEmpty())
        {
            if(mChar.equals("X"))
                mAdversaryChar = "O";
            else
                mAdversaryChar = "X";

            Iterator<String> usersIterator = users.keySet().iterator();
            while(usersIterator.hasNext())
            {
                String user = usersIterator.next();
                String user_char = users.get(user);

                if(user_char.equals(mAdversaryChar))
                    mAdversaryNickname = user;
            }
        }

        if(mAdversaryChar == null)
            return;

        if(last_player != null)
        {
            if((last_line == 0 || last_line == 3 || last_line == 6) && (last_line == 0 || last_line == 3 || last_line == 6)) {
                grid00.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid00;
            }
            else if((last_column == 0 || last_column == 3 || last_column == 6) && (last_line == 1 || last_line == 4 || last_line == 7)) {
                grid01.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid01;
            }
            else if((last_column == 0 || last_column == 3 || last_column == 6) && (last_line == 2 || last_line == 5 || last_line == 8)) {
                grid02.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid02;
            }

            else if((last_column == 1 || last_column == 4 || last_column == 7) && (last_line == 0 || last_line == 3 || last_line == 6)) {
                grid10.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid10;
            }
            else if((last_column == 1 || last_column == 4 || last_column == 7) && (last_line == 1 || last_line == 4 || last_line == 7)) {
                grid11.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid11;
            }
            else if((last_column == 1 || last_column == 4 || last_column == 7) && (last_line == 2 || last_line == 5 || last_line == 8)) {
                grid12.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid12;
            }

            else if((last_column == 2 || last_column == 5 || last_column == 8) && (last_line == 0 || last_line == 3 || last_line == 6)) {
                grid20.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid20;
            }
            else if((last_column == 2 || last_column == 5 || last_column == 8) && (last_line == 1 || last_line == 4 || last_line == 7)) {
                grid21.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid21;
            }
            else if((last_column == 2 || last_column == 5 || last_column == 8) && (last_line == 2 || last_line == 5 || last_line == 8)) {
                grid22.setBackgroundColor(getResources().getColor(R.color.blue));
                lastSelectedGrid = grid22;
            }
        }

        if(mActionbarListener != null)
        {
            if(current_player.equals(mChar))
            {
                mActionbarListener.changeActionbar("C'est votre tour !");
                your_turn = true;
            }
            else
            {
                mActionbarListener.changeActionbar("C'est au tour de " + mAdversaryNickname + " !");
                your_turn = false;
            }
        }
        updateGrids();
    }

    public void char_change(String nick, String new_char)
    {
        if(mAdversaryNickname.equals(nick))
            mAdversaryChar = new_char;
        else if(mNickname.equals(nick))
            mChar = new_char;
        else if(mAdversaryChar.equals(new_char))
            mAdversaryNickname = nick;
    }

    public void receiveMove(Integer line, Integer column)
    {
        if(your_turn)
        {
            mGrid.get(line).set(column, mChar);
            mActionbarListener.changeActionbar("C'est au tour de " + mAdversaryNickname + " !");
            your_turn = false;
        }
        else
        {
            mGrid.get(line).set(column, mAdversaryChar);
            mActionbarListener.changeActionbar("C'est votre tour !");
            your_turn = true;
        }

        if(lastSelectedGrid != null)
            lastSelectedGrid.setBackgroundResource(0);

        lastSelectedGrid = null;

        if((column == 0 || column == 3 || column == 6) && (line == 0 || line == 3 || line == 6) && !grid00isWon)
        {
            grid00.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid00;
        }
        else if((column == 0 || column == 3 || column == 6) && (line == 1 || line == 4 || line == 7) && !grid01isWon)
        {
            grid01.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid01;
        }
        else if((column == 0 || column == 3 || column == 6) && (line == 2 || line == 5 || line == 8) && !grid02isWon)
        {
            grid02.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid02;
        }

        else if((column == 1 || column == 4 || line == 7) && (line == 0 || line == 3 || line == 6) & !grid10isWon)
        {
            grid10.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid10;
        }
        else if((column == 1 || column == 4 || column == 7) && (line == 1 || line == 4 || line == 7) && !grid11isWon)
        {
            grid11.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid11;
        }
        else if((column == 1 || column == 4 || column == 7) && (line == 2 || line == 5 || line == 8) && !grid12isWon)
        {
            grid12.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid12;
        }

        else if((column == 2 || column == 5 || column == 8) && (line == 0 || line == 3 || line == 6) && !grid20isWon)
        {
            grid20.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid20;
        }
        else if((column == 2 || column == 5 || column == 8) && (line == 1 || line == 4 || line == 7) && !grid21isWon)
        {
            grid21.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid21;
        }
        else if((column == 2 || column == 5 || column == 8) && (line == 2 || line == 5 || line == 8) && !grid22isWon)
        {
            grid22.setBackgroundColor(getResources().getColor(R.color.blue));
            lastSelectedGrid = grid22;
        }

        updateGrids();
    }

    void updateGrids()
    {
        if(getActivity() != null)
        {
            String grid00values = mGrid.get(0).get(0) + mGrid.get(0).get(1) + mGrid.get(0).get(2) +
                    mGrid.get(1).get(0) + mGrid.get(1).get(1) + mGrid.get(1).get(2) +
                    mGrid.get(2).get(0) + mGrid.get(2).get(1) + mGrid.get(2).get(2);
            grid00.setImageBitmap(Utilities.generateGrid(getActivity(), grid00values));
            grid00isWon = (Utilities.whoWon(grid00values) == 'O' || Utilities.whoWon(grid00values) == 'X');
            String grid10values = mGrid.get(0).get(3) + mGrid.get(0).get(4) + mGrid.get(0).get(5) +
                    mGrid.get(1).get(3) + mGrid.get(1).get(4) + mGrid.get(1).get(5) +
                    mGrid.get(2).get(3) + mGrid.get(2).get(4) + mGrid.get(2).get(5);
            grid10isWon = (Utilities.whoWon(grid10values) == 'O' || Utilities.whoWon(grid10values) == 'X');
            grid10.setImageBitmap(Utilities.generateGrid(getActivity(), grid10values));
            String grid20values = mGrid.get(0).get(6) + mGrid.get(0).get(7) + mGrid.get(0).get(8) +
                    mGrid.get(1).get(6) + mGrid.get(1).get(7) + mGrid.get(1).get(8) +
                    mGrid.get(2).get(6) + mGrid.get(2).get(7) + mGrid.get(2).get(8);
            grid20isWon = (Utilities.whoWon(grid20values) == 'O' || Utilities.whoWon(grid20values) == 'X');
            grid20.setImageBitmap(Utilities.generateGrid(getActivity(), grid20values));

            String grid01values = mGrid.get(3).get(0) + mGrid.get(3).get(1) + mGrid.get(3).get(2) +
                    mGrid.get(4).get(0) + mGrid.get(4).get(1) + mGrid.get(4).get(2) +
                    mGrid.get(5).get(0) + mGrid.get(5).get(1) + mGrid.get(5).get(2);
            grid01isWon = (Utilities.whoWon(grid01values) == 'O' || Utilities.whoWon(grid01values) == 'X');
            grid01.setImageBitmap(Utilities.generateGrid(getActivity(), grid01values));
            String grid11values = mGrid.get(3).get(3) + mGrid.get(3).get(4) + mGrid.get(3).get(5) +
                    mGrid.get(4).get(3) + mGrid.get(4).get(4) + mGrid.get(4).get(5) +
                    mGrid.get(5).get(3) + mGrid.get(5).get(4) + mGrid.get(5).get(5);
            grid11.setImageBitmap(Utilities.generateGrid(getActivity(), grid11values));
            grid11isWon = (Utilities.whoWon(grid11values) == 'O' || Utilities.whoWon(grid11values) == 'X');
            String grid21values = mGrid.get(3).get(6) + mGrid.get(3).get(7) + mGrid.get(3).get(8) +
                    mGrid.get(4).get(6) + mGrid.get(4).get(7) + mGrid.get(4).get(8) +
                    mGrid.get(5).get(6) + mGrid.get(5).get(7) + mGrid.get(5).get(8);
            grid21isWon = (Utilities.whoWon(grid21values) == 'O' || Utilities.whoWon(grid21values) == 'X');
            grid21.setImageBitmap(Utilities.generateGrid(getActivity(), grid21values));

            String grid02values = mGrid.get(6).get(0) + mGrid.get(6).get(1) + mGrid.get(6).get(2) +
                    mGrid.get(7).get(0) + mGrid.get(7).get(1) + mGrid.get(7).get(2) +
                    mGrid.get(8).get(0) + mGrid.get(8).get(1) + mGrid.get(8).get(2);
            grid02isWon = (Utilities.whoWon(grid02values) == 'O' || Utilities.whoWon(grid02values) == 'X');
            grid02.setImageBitmap(Utilities.generateGrid(getActivity(), grid02values));
            String grid12values = mGrid.get(6).get(3) + mGrid.get(6).get(4) + mGrid.get(6).get(5) +
                    mGrid.get(7).get(3) + mGrid.get(7).get(4) + mGrid.get(7).get(5) +
                    mGrid.get(8).get(3) + mGrid.get(8).get(4) + mGrid.get(8).get(5);
            grid12isWon = (Utilities.whoWon(grid12values) == 'O' || Utilities.whoWon(grid12values) == 'X');
            grid12.setImageBitmap(Utilities.generateGrid(getActivity(), grid12values));
            String grid22values = mGrid.get(6).get(6) + mGrid.get(6).get(7) + mGrid.get(6).get(8) +
                    mGrid.get(7).get(6) + mGrid.get(7).get(7) + mGrid.get(7).get(8) +
                    mGrid.get(8).get(6) + mGrid.get(8).get(7) + mGrid.get(8).get(8);
            grid22isWon = (Utilities.whoWon(grid22values) == 'O' || Utilities.whoWon(grid22values) == 'X');
            grid22.setImageBitmap(Utilities.generateGrid(getActivity(), grid22values));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
        {
            int x = data.getIntExtra("x", -1);
            int y = data.getIntExtra("y", -1);
            if(x != -1 && y != -1 && mMovementListener != null)
                mMovementListener.makeMove(y, x);
        }
    }
}
