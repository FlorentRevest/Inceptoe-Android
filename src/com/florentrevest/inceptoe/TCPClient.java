package com.florentrevest.inceptoe;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;
import org.msgpack.type.ValueFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TCPClient
{
    private Packer mPacker;
    private String mPseudo;
    private String match_id;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private Socket mSocket;

    public TCPClient(String channel, String pseudo, OnMessageReceived listener)
    {
        mPseudo = pseudo;
        match_id = channel;
        mMessageListener = listener;
    }

    public void handshake()
    {
        try
        {
            mPacker.writeMapBegin(3);
            mPacker.write("command");
            mPacker.write("handshake");
            
            mPacker.write("version");
            mPacker.write(2);
            
            mPacker.write("nickname");
            mPacker.write(mPseudo);
            mPacker.writeMapEnd();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void join_match()
    {
        try
        {
            mPacker.writeMapBegin(2);
            mPacker.write("command");
            mPacker.write("join_match");
            
            mPacker.write("match_id");
            mPacker.write(match_id);
            mPacker.writeMapEnd();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void make_move(int line, int column)
    {
        try
        {
            mPacker.writeMapBegin(4);
            mPacker.write("command");
            mPacker.write("make_move");

            mPacker.write("match_id");
            mPacker.write(match_id);

            mPacker.write("line");
            mPacker.write(line);

            mPacker.write("column");
            mPacker.write(column);
            mPacker.writeMapEnd();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void message(String message)
    {
        try
        {
            mPacker.writeMapBegin(3);
            mPacker.write("command");
            mPacker.write("message");
            
            mPacker.write("match_id");
            mPacker.write(match_id);
            
            mPacker.write("message");
            mPacker.write(message);
            mPacker.writeMapEnd();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void pong(String token)
    {
        try
        {
            mPacker.writeMapBegin(2);
            mPacker.write("command");
            mPacker.write("pong");

            mPacker.write("token");
            mPacker.write(token);
            mPacker.writeMapEnd();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void stopClient()
    {
        mRun = false;
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void run()
    {
        mRun = true;
        try {
            mSocket = new Socket(InetAddress.getByName("progval.net"), 8520);

            MessagePack msgpack = new MessagePack();
            OutputStream out = mSocket.getOutputStream();
            mPacker = msgpack.createPacker(out);
            InputStream in = mSocket.getInputStream();
            handshake();

            while(mRun)
            {
                MapValue mv = msgpack.read(in).asMapValue();
                String command = mv.get(ValueFactory.createRawValue("command")).asRawValue().getString();

                if(command.equals("handshake_reply"))
                {
                    Boolean accepted = mv.get(ValueFactory.createRawValue("accepted")).asBooleanValue().getBoolean();
                    String error_message = "";
                    if(!accepted)
                        error_message = mv.get(ValueFactory.createRawValue("error_message")).asRawValue().getString();
                    Integer version = mv.get(ValueFactory.createRawValue("version")).asIntegerValue().getInt();

                    mMessageListener.handshakeReply(accepted, error_message, version);
                }
                else if(command.equals("join_match_reply"))
                {
                    Boolean accepted = mv.get(ValueFactory.createRawValue("accepted")).asBooleanValue().getBoolean();
                    String error_message = "";
                    if(!accepted)
                        error_message = mv.get(ValueFactory.createRawValue("error_message")).asRawValue().getString();
                    String match_id = mv.get(ValueFactory.createRawValue("match_id")).asRawValue().getString();
                    ArrayValue av = mv.get(ValueFactory.createRawValue("users")).asArrayValue();
                    List<String> users = new ArrayList<String>();
                    for(int i = 0; i < av.size() ; i++)
                        users.add(av.get(i).toString());

                    mMessageListener.joinMatchReply(accepted, error_message, match_id, users);
                }
                else if(command.equals("user_joined_match"))
                {
                    String user = mv.get(ValueFactory.createRawValue("user")).asRawValue().getString();
                    String match_id = mv.get(ValueFactory.createRawValue("match_id")).asRawValue().getString();
                    
                    mMessageListener.userJoinedMatch(user, match_id);
                }
                else if(command.equals("new_game"))
                {
                    String match_id = mv.get(ValueFactory.createRawValue("match_id")).asRawValue().getString();
                    String your_char = mv.get(ValueFactory.createRawValue("your_char")).asRawValue().getString();
                    MapValue game = mv.get(ValueFactory.createRawValue("game")).asMapValue();
                    String current_player = game.get(ValueFactory.createRawValue("current_player")).asRawValue().getString();
                    Set<Map.Entry<Value, Value>> users_value = game.get(ValueFactory.createRawValue("users")).asMapValue().entrySet();
                    Iterator<Entry<Value, Value>> itr = users_value.iterator();
                    Map<String, String> users = new HashMap<String, String>();
                    while(itr.hasNext())
                    {
                        Entry<Value, Value> entry = itr.next();
                        users.put(entry.getKey().asRawValue().getString(), entry.getValue().asRawValue().getString());
                    }
                    ArrayValue array_value = game.get(ValueFactory.createRawValue("grid")).asArrayValue();
                    List<List<String>> grid = new ArrayList<List<String>>();
                    for(int k = 0 ; k < array_value.size() ; k++)
                    {
                        ArrayValue grid_array_value = array_value.get(k).asArrayValue();
                        List<String> line = new ArrayList<String>();
                        for(int l = 0 ; l < grid_array_value.size() ; l++)
                            line.add(grid_array_value.get(l).asRawValue().getString());
                        grid.add(line);
                    }
                    String last_player = null;
                    int last_line = 0;
                    int last_column = 0;
                    if(!game.get(ValueFactory.createRawValue("last_move")).isNilValue())
                    {
                        ArrayValue last_move = game.get(ValueFactory.createRawValue("last_move")).asArrayValue();
                        last_player = last_move.get(0).asRawValue().getString();
                        last_line = last_move.get(1).asIntegerValue().getInt();
                        last_column = last_move.get(2).asIntegerValue().getInt();
                    }
                    
                    mMessageListener.newGame(match_id, your_char, current_player, users, grid, last_player, last_line, last_column);
                }
                else if(command.equals("make_move"))
                {
                    String match_id = mv.get(ValueFactory.createRawValue("match_id")).asRawValue().getString();
                    Integer line = mv.get(ValueFactory.createRawValue("line")).asIntegerValue().getInt();
                    Integer column = mv.get(ValueFactory.createRawValue("column")).asIntegerValue().getInt();
                    
                    mMessageListener.makeMove(match_id, line, column);
                }
                else if(command.equals("message"))
                {
                    String match_id = mv.get(ValueFactory.createRawValue("match_id")).asRawValue().getString();
                    String from = mv.get(ValueFactory.createRawValue("from")).asRawValue().getString();
                    String message = mv.get(ValueFactory.createRawValue("message")).asRawValue().getString();

                    mMessageListener.message(match_id, from, message);
                }
                else if(command.equals("char_change"))
                {
                    String match_id = mv.get(ValueFactory.createRawValue("match_id")).asRawValue().getString();
                    String nick = mv.get(ValueFactory.createRawValue("nick")).asRawValue().getString();
                    String new_char = mv.get(ValueFactory.createRawValue("new_char")).asRawValue().getString();

                    mMessageListener.char_change(match_id, nick, new_char);
                }
                else if(command.equals("ping"))
                {
                    String token = mv.get(ValueFactory.createRawValue("token")).asRawValue().getString();

                    pong(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mMessageListener.networkError();
        }
    }

    public interface OnMessageReceived {
        public void handshakeReply(Boolean accepted, String error_message, Integer version);
        public void joinMatchReply(Boolean accepted, String error_message, String match_id, List<String> users);
        public void userJoinedMatch(String user, String match_id);
        public void newGame(String match_id, String your_char, String current_player, Map<String, String> users, List<List<String>> grid, String last_player, int last_line, int last_column);
        public void makeMove(String match_id, Integer line, Integer column);
        public void message(String match_id, String from, String message);
        public void char_change(String match_id, String nick, String new_char);
        public void networkError();
    }
}
