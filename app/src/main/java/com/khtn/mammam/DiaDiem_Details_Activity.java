package com.khtn.mammam;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.khtn.mammam.pojo.Comment;
import com.khtn.mammam.pojo.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiaDiem_Details_Activity extends AppCompatActivity {

    private ViewGroup scrollViewgroup;
    private CallbackManager callbackManager;
    private ImageView imgShare, imgDirection, imgComment;
    private Restaurant restaurant;
    private TextView txtRestName, txtRestAddrr;
    private ListView lvListComment;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Comment> listComment;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_diem_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundlerest");
        restaurant = (Restaurant) bundle.getSerializable("resttrans");

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        txtRestName = (TextView) findViewById(R.id.txtRestName);
        txtRestAddrr = (TextView) findViewById(R.id.txtRestAdrr);

        txtRestName.setText(restaurant.getRestName().toString());
        txtRestAddrr.setText("Địa chỉ : "+restaurant.getRestAddr().toString());

        scrollViewgroup = (ViewGroup) findViewById(R.id.viewgroup);
        for (int i = 0; i < 6; i++) {
            final View singleFrame = getLayoutInflater().inflate(R.layout.frame_icon_caption, null);
            singleFrame.setId(i);
            ImageView icon = (ImageView) singleFrame.findViewById(R.id.icon);
            Picasso.with(getApplicationContext()).load(restaurant.getRestImg()).into(icon);
            scrollViewgroup.addView(singleFrame);
        }

        imgShare = (ImageView) findViewById(R.id.imgViewShare);
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(restaurant.getRestDetailLink()+""))
                .build();

                ShareDialog shareDialog = new ShareDialog(DiaDiem_Details_Activity.this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            }
        });

        imgDirection = (ImageView) findViewById(R.id.imgViewDirection);
        imgDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+restaurant.getRestName());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        imgComment = (ImageView) findViewById(R.id.imgViewComment);
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(DiaDiem_Details_Activity.this,Comment_Evaluate_Activity.class);
                startActivityForResult(in,88);
            }
        });

        lvListComment = (ListView) findViewById(R.id.lvListComment);
        listComment = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<Comment>(this,android.R.layout.simple_list_item_1,listComment);
        lvListComment.setAdapter(arrayAdapter);
        BindingDataToListComment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==88 && data!=null)
        {
            String noidung = data.getStringExtra("noidungbinhluan");
        }
    }

    private void BindingDataToListComment()
    {
        String[] listCommenters = restaurant.getRestTopCommenter().split(";");
        String[] listComments = restaurant.getRestTopComment().split(";");

        for(int i=0;i<listComments.length;i++)
        {
            listComment.add(new Comment(listCommenters[i]+"",listComments[i]+""));
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
