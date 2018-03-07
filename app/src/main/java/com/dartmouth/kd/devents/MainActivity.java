package com.dartmouth.kd.devents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity  {

    private CampusEventDbHelper mEventDbHelper;
    private FilterDbHelper mFilterDbHelper;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private String userID;
    private ArrayAdapter<String> adapter;
    public static ArrayList<CampusEvent> eventlist;
    Context mcontext;
    static int user=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        checkPermissions();
        //hi
        mcontext = this;
        mEventDbHelper = new CampusEventDbHelper(mcontext);
        mEventDbHelper.deleteAllEvents();
        mFilterDbHelper = new FilterDbHelper(mcontext);
        mFilterDbHelper.deleteAllFilters();

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("masterSheet");
            //starting a thread to parse and load events from url onto sqlitedata base and firebase
//            Load_Urlevents jsoupAsyncTask = new Load_Urlevents(this);
//            jsoupAsyncTask.execute();
        if (user == 1) {
            Intent intent = new Intent(this, FunctionActivity.class);
            startActivity(intent);
            //if user isn't logged in, go to log in window
        }

    }

    @Override
    protected void onStart() {
            super.onStart();
        Load_Urlevents jsoupAsyncTask = new Load_Urlevents(this);
        jsoupAsyncTask.execute();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               //eventlist = new ArrayList<>();
            for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                Map<String,Object> singleRun =  (Map<String, Object>) eventSnapshot.getValue();

                CampusEvent event = new CampusEvent();
                event.setTitle(singleRun.get("title").toString());
                event.setLocation(singleRun.get("location").toString());
                String location = singleRun.get("location").toString();
                event.setstrDate(singleRun.get("dateInMillis").toString());
                event.setstrEnd(singleRun.get("endInMillis").toString());
                event.setstrStart(singleRun.get("startInMillis").toString());
                /*event.setDate(singleRun.get("dateInMillis").toString());
                event.setEnd(singleRun.get("endInMillis").toString());
                event.setStart(singleRun.get("startInMillis").toString());*/
                //event.setmDate(singleRun.get("Date").toString());
                //event.setEnd(singleRun.get("End").toString());
                //event.setStart(singleRun.get("Start").toString());
                event.setURL(singleRun.get("url").toString());
                event.setDescription(singleRun.get("description").toString());
                if (singleRun.get("latitude") == null) {
                    double lat = 43.7022;
                    double longi = -72.2896;
                    event.setLongitude(longi);
                    event.setLatitude(lat);
                }else{
                    double lat = ((double) singleRun.get("latitude"));
                    double longi = ((double) singleRun.get("longitude"));
                    if(lat == 43.7022){
                        lat = tryAndGetALat(lat, longi, location);
                        longi = tryAndGetALong(lat,longi,location);
                    }
                    event.setLongitude(longi);
                    event.setLatitude(lat);
                }

                event.setGreekSociety(0);
                event.setMajor(0);
                event.setGender(0);
                event.setYear(0);
                event.setProgramType(0);
                event.setEventType(0);
                event.setFood(2);
                mEventDbHelper = new CampusEventDbHelper(mcontext);

                new mInsertIntoDbTask().execute(event);
                //eventlist.add(event);
            }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        CampusEventDbHelper dbh = new CampusEventDbHelper(this);
        dbh.close();
        FilterDbHelper dbh3 = new FilterDbHelper(this);
        dbh3.close();
        super.onDestroy();
    }


    //called when login button is pressed
    public void login_button(View view)
    {
        Intent intent = new Intent(this, LogInActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    //called when signup button is pressed
    public void signup_button(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
              ) {


        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important for the app.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, 0);
                            }

                        }
                    });
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }else{
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
             firebaseAuth.signOut();
            Utils.showActivity(this, LogInActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }



    public class mInsertIntoDbTask extends AsyncTask<CampusEvent, Void, String> {
        @Override
        protected String doInBackground(CampusEvent... campusEvents) {
            long id = mEventDbHelper.insertEntry(campusEvents[0]);

            return ""+id;
            // Pop up a toast
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Event #" + result + " saved.", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    double tryAndGetALat(double lat,double longi,String location){
        double l= 43.7022;
        if (location.contains("Baker")){
            l = 43.7022;
        }else if(location.contains("Hop") || location.contains("HOP") || location.contains("Hopkins Center")){
            l = 43.702079;
        }else if(location.contains("Thayer") || location.contains("Maclean") || location.contains("Cummings")){
            l = 43.704453;
        }else if(location.contains("Tuck") || location.contains("Business") || location.contains("Byrne")){
            l = 43.705546;
        }
        else if(location.contains("Geisel") || location.contains("Life Science") || location.contains("Medicine")){
            l = 43.708865;
        }
        else if(location.contains("Occom") || location.contains("McLaughlin") || location.contains("Sudikoff")){
            l = 43.707223;
        }
        else if(location.contains("Wilder") || location.contains("Steele") || location.contains("Fairchild")|| location.contains("Burke")){
            l = 43.705750;
        }
        else if(location.contains("Lebanon")){
            l = 43.700960;
        }


        return l;
    }

    double tryAndGetALong(double lat,double longi,String location){
        double l = -72.2896;
        if (location.contains("Baker") || location.contains("Berry")){
            l = -72.2896;
        }else if(location.contains("Hop") || location.contains("HOP") || location.contains("Hopkins Center")){
            l = -72.287949;
        }else if(location.contains("Thayer") || location.contains("Maclean") || location.contains("Cummings")){
            l = -72.294636;
        }
        else if(location.contains("Tuck") || location.contains("Business") || location.contains("Byrne")){
            l = -72.294245;
        }
        else if(location.contains("Geisel") || location.contains("Life Science") || location.contains("Medicine")){
            l = -72.284718;
        }
        else if(location.contains("Occom") || location.contains("McLaughlin") || location.contains("Sudikoff")){
            l = -72.286563;
        }
        else if(location.contains("Wilder") || location.contains("Steele") || location.contains("Fairchild")|| location.contains("Burke")){
            l = -72.286403;
        }
        else if(location.contains("Lebanon")){
            l = -72.289077;
        }
        return l;
    }
}



