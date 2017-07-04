package com.bba.ministries;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bba.ministries.Common.GlobalClass;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.bba.ministries.adapter.FB_CoversAdapter.NOTIFICATION_ID_BIG_IMAGE;

/**
 * Created by Prakash on 12/4/2016.
 */

public class PromiseCardViewPage extends AppCompatActivity {

    String title,image,description;
    int results=1;

    ImageView imageview,fb,twitter_image,whatsapp;
    TextView titles,desCription;
    ImageLoader loader;
    Toolbar toolbar;
    LinearLayout download;
    CallbackManager callbackmanager;
    /* Shared preference keys */
    private static final String PREF_NAME = "sample_twitter_pref";
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    private static final String PREF_USER_NAME = "twitter_user_name";

    /* Any number for uniquely distinguish your request */
    public static final int WEBVIEW_REQUEST_CODE = 100;

    private ProgressDialog pDialog;

    private static Twitter twitter;
    private static RequestToken requestToken;

    private static SharedPreferences mSharedPreferences;

    boolean isLoggedIn;

    private LoginManager loginManager;


    private String consumerKey = null;
    private String consumerSecret = null;
    private String callbackUrl = null;
    private String oAuthVerifier = null;

     ShareDialog shareDialog;

    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_promise);

        imageview=(ImageView)findViewById(R.id.image);
        fb=(ImageView)findViewById(R.id.fb);
        twitter_image=(ImageView)findViewById(R.id.twitter);
        whatsapp=(ImageView)findViewById(R.id.whatsapp);
        titles=(TextView)findViewById(R.id.title);
        desCription=(TextView)findViewById(R.id.description);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        download=(LinearLayout)findViewById(R.id.download);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.logos);
        getSupportActionBar().setTitle("  Promise Card");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loader=ImageLoader.getInstance();

        Intent i=getIntent();
        title=i.getStringExtra("title");
        image=i.getStringExtra("image");
        description=i.getStringExtra("description");

        loader.displayImage(image,imageview);
        titles.setText(title);
        desCription.setText(description);

        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
         isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);


        /* initializing twitter parameters from string.xml */
        initTwitterConfigs();

		/* Enabling strict mode */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //this loginManager helps you eliminate adding a LoginButton to your UI
        loginManager = LoginManager.getInstance();


         shareDialog = new ShareDialog(this);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), PromiseCardActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onFblogin();
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap=loader.loadImageSync(image);
                OutputStream fOut = null;
                File sdImageMainDirectory=null;
                Uri outputFileUri;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "BBA_Ministries" + File.separator+ "Share Image" + File.separator);
                    root.mkdirs();
                    if(sdImageMainDirectory!=null)
                    {
                        sdImageMainDirectory.delete();
                        sdImageMainDirectory = new File(root, "temporary"+".jpg");

                    }else
                    {
                        sdImageMainDirectory = new File(root, "temporary"+".jpg");

                    }
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();


                    Uri imageUri = Uri.parse(sdImageMainDirectory.getAbsolutePath());
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    //Target whatsapp:
                    shareIntent.setPackage("com.whatsapp");
                    //Add text and then Image URI
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared by BBA Ministries");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("*/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(shareIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),"Not installed",Toast.LENGTH_SHORT).show();
                    }




                  /*  Toast.makeText(context, "Successfully saved image.",
                            Toast.LENGTH_SHORT).show();*/
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });

        twitter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginToTwitter();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bm=loader.loadImageSync(image);
                //  ProgressDialog dialog=new ProgressDialog(context);
                // dialog.setTitle("Downloading.....");
                // dialog.show();
                //  holder.image.buildDrawingCache();
                //Bitmap bm=holder.image.getDrawingCache();
                OutputStream fOut = null;
                Uri outputFileUri;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "BBA_Ministries" + File.separator+ "Facebook Covers" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, title+".jpg");
                    Log.i("Check","Check"+sdImageMainDirectory.toString());
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);


                    showBigNotification(bm,"Successfully saved image",sdImageMainDirectory);
                    //dialog.dismiss();
                   /* Toast.makeText(context, "Successfully saved image.",
                            Toast.LENGTH_SHORT).show();*/
                } catch (Exception e) {
                    // dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                try {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(), PromiseCardActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

    private void showBigNotification(Bitmap bitmap, String message, File root) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(root), "image/*");


        //startActivity(intent);

        //startActivityForResult(intent, 1);
       // Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(message);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.logos).setTicker(message).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(message)
                .setContentIntent(resultPendingIntent)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.logos)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logos))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    // Private method to handle Facebook login and callback
    private void onFblogin()
    {
        callbackmanager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");


        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackmanager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {

                Toast.makeText(getApplicationContext(),"Login sucess",Toast.LENGTH_SHORT).show();

               // postPhoto();
               // sharePhotoToFacebook();
            }

            @Override
            public void onCancel()
            {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception)
            {
                System.out.println("onError");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // String resultdata=data.getExtras().getString("twitter");

        if(requestCode==100 && resultCode==Activity.RESULT_OK)
        {
            String verifier = data.getExtras().getString(oAuthVerifier);
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                long userID = accessToken.getUserId();
                final User user = twitter.showUser(userID);
                String username = user.getName();

                saveTwitterInfo(accessToken);
                new updateTwitterStatus().execute("Shared from BBA Ministries");


            } catch (Exception e) {
                Log.e("Twitter Login Failed", e.getMessage());
            }
        }

        else
        {
            callbackmanager.onActivityResult(requestCode, resultCode, data);


            if(results==1)
            {
                alertdialog();
            }else
            {
                alertDialog.dismiss();

                results=1;
            }



        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void postPhoto() {

        Bitmap icon = loader.loadImageSync(image);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(icon)
                .setCaption("Shared from BBA Ministries")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(),"Posting Promise card in Facebook",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    /* Reading twitter essential configuration parameters from strings.xml */
    private void initTwitterConfigs() {
        consumerKey = getString(R.string.twitter_consumer_key);
        consumerSecret = getString(R.string.twitter_consumer_secret);
        callbackUrl = getString(R.string.twitter_callback);
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);



    }


    private void loginToTwitter() {
         isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        if (isLoggedIn==false) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(consumerKey);
            builder.setOAuthConsumerSecret(consumerSecret);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(callbackUrl);

                /**
                 *  Loading twitter login page on webview for authorization
                 *  Once authorized, results are received at onActivityResult
                 *  */
                final Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {

            new updateTwitterStatus().execute("Shared from BBA Ministries");

        }
    }


    class updateTwitterStatus extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PromiseCardViewPage.this);
            pDialog.setMessage("Posting to twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(String... args) {

            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(consumerKey);
                builder.setOAuthConsumerSecret(consumerSecret);

                // Access Token
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                // Update status
                Bitmap bitmap=loader.loadImageSync(image);
                StatusUpdate statusUpdate = new StatusUpdate(status);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                //InputStream is = getResources().openRawResource(R.drawable.lakeside_view);
                statusUpdate.setMedia("test.jpg", bs);


                twitter4j.Status response = twitter.updateStatus(statusUpdate);

                Log.d("Status", response.getText());

            } catch (TwitterException e) {
                Log.d("Failed to post!", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

			/* Dismiss the progress dialog after sharing */
            pDialog.dismiss();

            Toast.makeText(PromiseCardViewPage.this, "Posted to Twitter!", Toast.LENGTH_SHORT).show();


        }

    }

    /**
     * Saving user information, after user is authenticated for the first time.
     * You don't need to show user to login, until user has a valid access toen
     */
    private void saveTwitterInfo(AccessToken accessToken) {

        long userID = accessToken.getUserId();

        User user;
        try {
            user = twitter.showUser(userID);

            String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e1) {
            e1.printStackTrace();
        }
    }
    public void alertdialog()
    {
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to post ?");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        sharePhotoToFacebook();


                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

         alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void sharePhotoToFacebook() {
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("BBA Ministries")
                .setImageUrl(Uri.parse(image))
                .build();
        shareDialog.show(linkContent);


        shareDialog.registerCallback(callbackmanager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

                results=0;

                Toast.makeText(getApplicationContext(),"Successfully Posted to Facebook",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Posting failed",Toast.LENGTH_SHORT).show();
                results=0;

            }

            @Override
            public void onError(FacebookException e) {

                results=0;
            }
        });
    }
}
