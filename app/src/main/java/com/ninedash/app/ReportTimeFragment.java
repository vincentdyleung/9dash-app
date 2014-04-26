package com.ninedash.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Sing on 14年4月27日.
 */
public class ReportTimeFragment extends Fragment {

    private String nameOfRestaurant;
    private int numberOfTables;

    public ReportTimeFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.report_time,container,false);
        final TextView textView = (TextView) view.findViewById(R.id.textView);
        Button btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                textView.setVisibility(textView.VISIBLE);
                getData(view, textView);
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getData(View view, TextView textView){
        // Retrieve the users' data
        final EditText table_Data = (EditText) view.findViewById(R.id.editText_numberOfTables);
        final EditText table_Name = (EditText) view.findViewById(R.id.editText_name);
        numberOfTables = Integer.parseInt(table_Data.getText().toString());
        nameOfRestaurant = table_Name.getText().toString();
        textView.setText(Integer.toString(numberOfTables) + nameOfRestaurant);
    }
/*
    //Send the information to the server side
    private void sendPost() throws Exception {

            String url = "";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

        }*/
}
