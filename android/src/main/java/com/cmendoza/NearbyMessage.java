package com.cmendoza;

import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;

import java.nio.charset.Charset;

public class NearbyMessage
{
    private static final Gson gson = new Gson();
    private String mMessage;

    public NearbyMessage(String message)
    {

        mMessage = message;
    }

    protected String getMessage()
    {
        return mMessage;
    }

    public static NearbyMessage readSubscribedMessage(Message message)
    {
        String nearbyMessageString = new String(message.getContent()).trim();
        return gson.fromJson(
                (new String(nearbyMessageString.getBytes(Charset.forName("UTF-8")))),
                NearbyMessage.class);
    }

    public static Message createMessageToPublish(String message)
    {
        NearbyMessage nearbyMessage = new NearbyMessage(message);
        return new Message(gson.toJson(nearbyMessage).getBytes(Charset.forName("UTF-8")));
    }
}
