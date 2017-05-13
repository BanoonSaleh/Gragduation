package com.example.algan.gpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.algan.gpapp.app.AppConfig;
import com.example.algan.gpapp.helper.SQLiteHandler;
import com.example.algan.gpapp.helper.SessionManager;
import com.example.algan.gpapp.models.Course;
import com.example.algan.gpapp.models.CourseAdapter;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    public static String uemail, uuid;

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private TextView txtName;
    private TextView txtEmail, txtSubtitle;
    private Button btnLogout, btnSetReminder, btnProfile;
    private LinearLayout setReminderView;
    private ListView listCourses;
    private EditText selectCourse;

    private SQLiteHandler db;
    private SessionManager session;

    public static ArrayList<Course> listSearched, listLoaded;
    private boolean courseLoaded = false;
    private ProgressDialog pDialog;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        txtName = (TextView) findViewById(R.id.name);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        listCourses = (ListView) findViewById(R.id.listCourses);

        // load the courses
        listLoaded = new ArrayList<Course>();
        listSearched = new ArrayList<Course>();


        // Create the adapter to convert the array to views
        CourseAdapter adapter = new CourseAdapter(this, listSearched);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listCourses);
        listView.setAdapter(adapter);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String uid = user.get("uid");
        this.uemail = email;
        this.uuid = uid;

        loadCourses(email, uid);

        // Displaying the user details on the screen
        txtName.setText(name);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Profile button click event
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        Intent i = new Intent(getApplicationContext(),
                MainActivity.class);
        startActivity(i);
        finish();
    }

    private void loadCourses(final String email, final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_courses";

        pDialog.setMessage("Loading courses ...");
        showDialog();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_COURSES(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        courseLoaded = true;
                        // We get weather info (This is an array)
                        JSONArray jArr = jObj.getJSONArray("data");

                        for (int i = 0; i < jArr.length(); i++) {
                            JSONObject _jobj = jArr.getJSONObject(i);
                            listSearched.add(new Course(_jobj.getString("Title"), _jobj.getString("URL"), _jobj.getInt("id"), _jobj.getBoolean("isFav")));
                        }

                        Toast.makeText(getApplicationContext(), "Courses loaded", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "Unable to load courses", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Course Load Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("email", email);
                params.put("get", "true");
                return params;
            }

        };
        queue.add(strReq);
    }

    public void addReminder(String title){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rule", "FREQ=DAILY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", getString( R.string.prefix_reminder_title) +" " + title);
        startActivity(intent);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
