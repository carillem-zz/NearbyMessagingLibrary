
package com.cmendoza;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class RNNearbyMessagingLibraryModule extends ReactContextBaseJavaModule implements LifecycleEventListener
{
  private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(10)
            .build();

  @NonNull private MessageListener _messageListener = new MessageListener()
  {
    @Override
    public void onFound(Message message)
    {
      super.onFound(message);
      String messageContent = NearbyMessage.readSubscribedMessage(message).getMessage();
      Log.i(getName(), messageContent);
    }

    @Override
    public void onLost(Message message)
    {
      super.onLost(message);
      String messageString = new String(message.getContent());
      Log.i(getName(), "Lost: " + messageString);
    }
  };

  public RNNearbyMessagingLibraryModule(ReactApplicationContext reactContext)
  {
    super(reactContext);
  }

  private PublishOptions createPublishOptions()
  {
    return new PublishOptions.Builder()
            .setStrategy(PUB_SUB_STRATEGY)
            .setCallback(new PublishCallback(){
              @Override
              public void onExpired()
              {
                super.onExpired();
                Log.i(getName(), "End publish");
              }
            }).build();
  }

  private SubscribeOptions createForegroundSubscribeOptions()
  {
    return new SubscribeOptions.Builder()
            .setStrategy(PUB_SUB_STRATEGY)
            .setCallback(new SubscribeCallback(){
              @Override
              public  void onExpired()
              {
                super.onExpired();
                Log.i(getName(), "End foreground subscribe");
              }
            }).build();
  }

  private SubscribeOptions createBackgroundSubscribeOptions()
  {
    return new SubscribeOptions.Builder()
            .setStrategy(Strategy.BLE_ONLY)
            .setCallback(new SubscribeCallback(){
              @Override
              public void onExpired()
              {
                super.onExpired();
                Log.i(getName(), "End background subscribe");
              }
            }).build();
  }

  @ReactMethod
  public void foregroundSubscribe()
  {
    SubscribeOptions subscribeOptions = createForegroundSubscribeOptions();

    if(getCurrentActivity() != null)
    {
      Nearby.getMessagesClient(getCurrentActivity())
              .subscribe(_messageListener, subscribeOptions)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Log.i(getName(), "Subscribing..");
                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.e(getName(), "Subscribe failed");
                }
              })
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull  Task<Void> task)
                  {
                      Log.i(getName(), "Subscribed successfully");
                  }
              });
    } else {
      Log.e(getName(), "No activity found for foreground subscribe");
    }
  }

    @ReactMethod
    public void backgroundSubscribe()
    {
        Log.i(getName(), "in background subscribe");
        SubscribeOptions subscribeOptions = createBackgroundSubscribeOptions();
        if(getCurrentActivity() != null)
        {
            Nearby.getMessagesClient(getCurrentActivity())
                    .subscribe(getPendingIntent(), subscribeOptions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(getName(), "Background subscribing..");
                            Intent intent = new Intent(getCurrentActivity(), NearbyService.class);
                            getCurrentActivity().startService(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(getName(), "Background subscribe failed");
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i(getName(), "Background subscribe completed");
                        }
                    });
        } else {
            Log.e(getName(), "No activity found for background subscribe");
        }
    }

    private PendingIntent getPendingIntent()
    {
        Intent intent = createNearbyService();
        intent.setClass(getCurrentActivity(), NearbyService.class);
        return PendingIntent.getService(getCurrentActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent createNearbyService()
    {
        return new Intent(NearbyService.FOREGROUND);
    }

  @ReactMethod
  public void publish(final String msgToPublish)
  {
    PublishOptions publishOptions = createPublishOptions();
    Message publishMessage = NearbyMessage.createMessageToPublish(msgToPublish);

    if(getCurrentActivity() != null)
    {
      Nearby.getMessagesClient(getCurrentActivity())
              .publish(publishMessage, publishOptions)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Log.i(getName(), "Publishing..." + msgToPublish);
                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.e(getName(), "Publish failed: " + e.toString());
                }
              })
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  Log.i(getName(), "Published.");
                }
              });
    } else {
      Log.e(getName(), "No activity found for publishing");
    }
  }

  @ReactMethod
  public void checkLibraryConnection()
  {
    Toast.makeText(getCurrentActivity(), "Connected to NearbyMessaging Library.", Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void backgroundUnsubscribe()
  {
    if(getCurrentActivity() != null)
    {
      Nearby.getMessagesClient(getCurrentActivity())
              .unsubscribe(getPendingIntent())
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Log.i(getName(), "Unsubscribe successful");
                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.e(getName(), "Unsubscribe failed");
                }
              })
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  Log.i(getName(), "Unsubscribe completed");
                }
              });
    } else {
      Log.e(getName(), "No activity found");
    }
  }

  @ReactMethod
  public void foregroundUnsubscribe()
  {
    if(getCurrentActivity() != null)
    {
      Nearby.getMessagesClient(getCurrentActivity())
              .unsubscribe(_messageListener)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Log.i(getName(), "Unsubscribe successful");
                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.e(getName(), "Unsubscribe failed");
                }
              })
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  Log.i(getName(), "Unsubscribe completed");
                }
              });
    } else {
      Log.e(getName(), "No activity found");
    }
  }

  @Override
  public void onHostResume()
  {
    Log.i(getName(), "On host resume");
  }

  @Override
  public void onHostPause()
  {
    Log.i(getName(), "On host pause");
  }

  @Override
  public void onHostDestroy()
  {
    Log.i(getName(), "On host destroy");
  }

  @Override
  public String getName() {
    return "RNNearbyMessagingLibrary";
  }
}