package com.ninedash.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Sing on 14年4月27日.
 */
public class ReportTimeFragment extends Fragment {

    final int REQUEST_IMAGE_CAPTURE = 1;
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    final int MEDIA_TYPE_IMAGE = 1;
    private String restaurantId;
    private Bitmap imageBitmap;
    EditText waitingTimeField;
    EditText waitingPositionField;
    EditText waitingPeopleField;

    public ReportTimeFragment(String id){
        restaurantId = id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.report_time,container,false);
        waitingTimeField = (EditText) view.findViewById(R.id.waiting_time_field);
        waitingPositionField = (EditText) view.findViewById(R.id.waiting_position_field);
        waitingPeopleField = (EditText) view.findViewById(R.id.waiting_people_field);
        final ImageView imgView = (ImageView) view.findViewById(R.id.imageView);
        Button btn = (Button) view.findViewById(R.id.button);
        Button camera_btn = (Button) view.findViewById(R.id.enableCamera);
        btn.setOnClickListener(new SubmitReportListener(this));
        camera_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                enableCamera();
                imgView.setImageBitmap(imageBitmap);
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setRestaurantId(String id) {
        restaurantId = id;
    }

    public void enableCamera(){
        //Camera cam = Camera.open();
        //private void dispatchTakePictureIntent() {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //PackageManager  pm = getPackageManager();
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        // start the image capture Intent
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, 1, takePictureIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    private class SubmitReportListener implements View.OnClickListener {

        private ReportTimeFragment hostFragment;

        public SubmitReportListener(ReportTimeFragment fragment) {
            hostFragment = fragment;
        }

        @Override
        public void onClick(View view) {
            new HTTPHandler() {
                @Override
                public HttpUriRequest getHttpRequest() {
                    HttpPost postRequest = null;
                    String restaurantId = ((MainActivity) hostFragment.getActivity()).getSelectedRestaurant();
                    try {
                        URI uri = new URI(MainActivity.HOST + "restaurants/" + restaurantId + "/report");
                        postRequest = new HttpPost(uri);
                        postRequest.setHeader("Content-Type", "application/json");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    String waitingTime = waitingTimeField.getText().toString();
                    String waitingPosition = waitingPositionField.getText().toString();
                    String waitingPeople = waitingPeopleField.getText().toString();
                    String fbId = ((MainActivity) hostFragment.getActivity()).getFbId();
                    String jsonString = "{\"time\":" + waitingTime + ",\"pos\":" + waitingPosition + ",\"ppl\":" + waitingPeople + ",\"user\":" + fbId + "}";
                    try {
                        StringEntity params = new StringEntity(jsonString);
                        postRequest.setEntity(params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return postRequest;
                }

                @Override
                public void onResponse(JSONObject res) {
                    try {
                        if (res.getString("OK").equals("OK")) {
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction transaction = fm.beginTransaction();
                            transaction.remove(hostFragment).commit();
                            Toast.makeText(hostFragment.getActivity(), "Submission success!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(hostFragment.getActivity(), "Submission error! Pleas retry!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
