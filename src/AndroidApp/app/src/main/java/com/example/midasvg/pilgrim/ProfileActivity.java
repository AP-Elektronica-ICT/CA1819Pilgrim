package com.example.midasvg.pilgrim;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //De button wordt ge-enabled op de Action Bar
        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);
        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        final TextView accInfo = (TextView) findViewById(R.id.txtAcc);
        accInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        final TextView history = (TextView) findViewById(R.id.txtPilgrimages);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CompletedPilgrimagesActivity.class);
                startActivity(intent);
            }
        });

        final TextView viewCollection = (TextView) findViewById(R.id.txtCollection);
        viewCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CollectionActivity.class);
                startActivity(intent);
            }
        });

        final Button editProfile = (Button) findViewById(R.id.btn_EditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (nToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public byte[] getBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1){
            byteBuffer.write(buffer,0,len);
        }
        return  byteBuffer.toByteArray();
    }

    public void Save() throws  IOException{

        String finalBase64 = "";

        if(imageURI == null)
        {
            base64Image = "";
            Log.d("Test", "Save: empty");
        }
        else{
            InputStream inputStream = getContentResolver().openInputStream(imageURI);
            byte[] imageArray = getBytes(inputStream);
            base64Image = Base64.encodeToString(imageArray, Base64.NO_WRAP);
            Log.d("Test", "Save:" + base64Image);
            finalBase64 = base64Image;
        }

        Log.d("profileCall", "Nickname: " + nickName.getText() + ", FirstName: " + firstName.getText() + ", LastName: " + lastName.getText() + ", DateOfBirth: " + date.getText() + ", UID: " + UID);
        sendPost();
        /*RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "https://cloud.requestcatcher.com/";
        StringRequest MyStringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //test
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){protected Map<String, String> getParams(){
            Map<String,String> myData = new HashMap<String, String>();
            myData.put("FirstName", firstName.getText().toString());
            myData.put("LastName", lastName.getText().toString());
            myData.put("NickName", nickName.getText().toString());
            myData.put("Age", "18");
            myData.put("Country", "Belgium");
            myData.put("fireBaseID", UID);

            Log.d("myData", "getParams: " + myData);
            return myData;


        }
        };
        MyRequestQueue.add(MyStringReq);
        */
    }

    public void sendPost(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("https://api20181128095534.azurewebsites.net/api/profiles");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("FirstName", firstName.getText().toString());
                    jsonObject.put("LastName", lastName.getText().toString());
                    jsonObject.put("NickName", nickName.getText().toString());
                    jsonObject.put("Age", 18);
                    jsonObject.put("Country", "Belgium");
                    jsonObject.put("Base64", base64Image);
                    jsonObject.put("fireBaseID", UID);
                    // Need to delete \ from string in backend !!!

                    Log.i("image", base64Image);
                    Log.i("json", jsonObject.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonObject.toString());

                    os.flush();
                    os.close();
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}
