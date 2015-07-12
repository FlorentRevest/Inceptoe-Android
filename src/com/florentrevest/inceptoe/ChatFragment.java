package com.florentrevest.inceptoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment
{
    private DiscussArrayAdapter adapter;
    private EditText editText;
    private ListView chatView;
    private OnMessageSent mMessageListener = null;
    private Bundle mSavedInstanceState;
    private String mNickname;

    public ChatFragment()
    {}

    public void setNickname(String nickname)
    {
        mNickname = nickname;
    }

    public void setOnMessageSent(OnMessageSent listener)
    {
        mMessageListener = listener;
    }

    public interface OnMessageSent
    {
        public void sendMessage(String message);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edittext", editText.getText().toString());
        outState.putStringArrayList("messages", adapter.save());
        mSavedInstanceState = outState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        editText = (EditText)view.findViewById(R.id.chatEditText);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String message = editText.getText().toString();
                    if(!message.isEmpty())
                    {
                        editText.setText("");
                        if(mMessageListener != null)
                            mMessageListener.sendMessage(message);
                    }

                    return true;
                }
                return false;
            }
        });
        chatView = (ListView)view.findViewById(R.id.chatListView);
        adapter = new DiscussArrayAdapter(getActivity(), R.layout.listitem_discuss);
        chatView.setAdapter(adapter);
        final ImageButton sendButton = (ImageButton)view.findViewById(R.id.chatSendButton);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                String message = editText.getText().toString();
                if(!message.isEmpty())
                {
                    editText.setText("");
                    if(mMessageListener != null)
                        mMessageListener.sendMessage(message);
                }
            }
        });

        if(mSavedInstanceState != null)
        {
            editText.setText(mSavedInstanceState.getString("edittext"));
            adapter.restore(mSavedInstanceState.getStringArrayList("messages"));
        }
        return view;
    }
    
    public void receiveMessage(String from, String message)
    {
        Boolean left = true;
        if(from.equals(mNickname))
            left = false;
        adapter.add(new Utilities.Comment(left, message));
        chatView.setSelection(adapter.getCount() - 1);
    }

    public class DiscussArrayAdapter extends ArrayAdapter<Utilities.Comment>
    {
        private List<Utilities.Comment> comments = new ArrayList<Utilities.Comment>();

        @Override
        public void add(Utilities.Comment object) {
            comments.add(object);
            super.add(object);
        }

        public DiscussArrayAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public int getCount() {
            return comments.size();
        }

        public Utilities.Comment getItem(int index) {
            return comments.get(index);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.listitem_discuss, parent, false);
            }

            LinearLayout wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

            Utilities.Comment com = getItem(position);

            TextView text = (TextView) row.findViewById(R.id.comment);

            text.setText(com.comment);

            text.setBackgroundResource(com.left ? R.drawable.bubble_left : R.drawable.bubble_right);
            wrapper.setGravity(com.left ? Gravity.LEFT : Gravity.RIGHT);

            return row;
        }

        public Bitmap decodeToBitmap(byte[] decodedByte) {
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }

        public void restore(ArrayList<String> msgs)
        {
            for(int i = 0; i < msgs.size() ; i++)
            {
                String msg = msgs.get(i);
                Boolean left = false;
                if(msg.substring(0, 1).equals("1"))
                    left = true;
                msg = msg.substring(1);

                add(new Utilities.Comment(left, msg));
            }
        }

        public ArrayList<String> save()
        {
            ArrayList<String> strs = new ArrayList<String>();
            for(int i = 0; i < comments.size() ; i++)
            {
                Utilities.Comment c = comments.get(i);

                if(c.left == true)
                    strs.add("1" + c.comment);
                else
                    strs.add("0" + c.comment);
            }
            return strs;
        }
    }
}
