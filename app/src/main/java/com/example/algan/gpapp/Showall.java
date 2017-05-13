package com.example.algan.gpapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Showall extends AppCompatActivity  {
    Adapter college;
    ListView listCollege;
    ProgressBar proCollageList;
    String  mode,mode2,mode3,mode4,modeboth1,modeboth2, date,both1,both2,both3,both4,both5,both6,both7,both8,both9,both10,t3,t4, t1,t2, currenturl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showall);


        Intent intent=getIntent();
        Bundle options=intent.getExtras();
        mode = options.getString("mode");
        mode2 = options.getString("mode2");
        mode3 = options.getString("mode3");
        mode4 = options.getString("mode4");
        modeboth1 = options.getString("modeboth1");
        modeboth2 = options.getString("modeboth2");

        date = options.getString("date");
        t1 = options.getString("time1");
        t2 = options.getString("time2");
        t3 = options.getString("time3");
        t4 = options.getString("time4");

        both1 = options.getString("both1");
        both2 = options.getString("both2");
        both3 = options.getString("both3");
        both4 = options.getString("both4");
        both5 = options.getString("both5");
        both6 = options.getString("both6");
        both7 = options.getString("both7");
        both8 = options.getString("both8");
        both9 = options.getString("both9");
        both10 = options.getString("both10");


        listCollege = (ListView) findViewById(R.id.listcollege);
        proCollageList = (ProgressBar) findViewById(R.id.proCollageList);
        listCollege.setAdapter(college);


        new GetHttpResponse(this).execute();
    }


    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        private Context context;
        String result;
        List<cources> collegeList;
        List<cources> allCourses;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpService httpService = new HttpService("http://10.0.2.2:8080/GP/courses.php");
            try {
                httpService.ExecutePostRequest();

                if (httpService.getResponseCode() == 200) {
                    result = httpService.getResponse();
                    Log.d("Result", result);
                    if (result != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(result);

                            JSONObject object;
                            //JSONArray array;
                            cources college;
                            collegeList = new ArrayList<cources>();
                            allCourses=new ArrayList<cources>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                college = new cources();
                                object = jsonArray.getJSONObject(i);

                                college.Title = object.getString("Title");
                                college.URL = object.getString("URL");

                                try {
                                    allCourses.add(college);
                                    currenturl = college.URL;
                                    Connection connection2 = Jsoup.connect(currenturl);
                                    Document doc = connection2.get();
                                    String body = doc.text().toLowerCase();

                                    if(  body.contains(mode2)||body.contains(mode3)||body.contains(mode4)||body.contains(mode)||body.contains(modeboth1)||body.contains(modeboth2)
                                            ||body.contains(both1)||body.contains(both2)||body.contains(both3)||body.contains(both4)||body.contains(both5)
                                            ||body.contains(both6)||body.contains(both7)||body.contains(both8)||body.contains(both9)||body.contains(both10)  ) {

                                        if (body.contains(t1) || body.contains(t2) || body.contains(t3) || body.contains(t4)) {

                                            if(body.contains(date.toLowerCase()))
                                                collegeList.add(college);
                                        }
                                    }


                                } catch (IOException ioe) {
                                    Toast.makeText(context, httpService.getErrorMessage(), Toast.LENGTH_SHORT).show();

                                    // We were not successful in our HTTP request
                                }


                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                } else {
                    Toast.makeText(context, httpService.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            proCollageList.setVisibility(View.GONE);
            listCollege.setVisibility(View.VISIBLE);


            if (!collegeList.isEmpty() || collegeList!=null) {
                Adapter adapter = new Adapter(collegeList, context);
                listCollege.setAdapter(adapter);

            }else{
                Adapter adapter = new Adapter(allCourses, context);
                listCollege.setAdapter(adapter);

            }

        }
    }

    public class HttpService {
        private ArrayList<NameValuePair> params;
        private ArrayList<NameValuePair> headers;

        private String url;
        private int responseCode;
        private String message;
        private String response;

        public String getResponse() {
            return response;
        }

        public String getErrorMessage() {
            return message;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public HttpService(String url) {
            this.url = url;
            params = new ArrayList<NameValuePair>();
            headers = new ArrayList<NameValuePair>();
        }

        public void AddParam(String name, String value) {
            params.add(new BasicNameValuePair(name, value));
        }

        public void AddHeader(String name, String value) {
            headers.add(new BasicNameValuePair(name, value));
        }

        public void ExecuteGetRequest() throws Exception {
            String combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);
            for (NameValuePair h : headers) {
                request.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request, url);
        }

        public void ExecutePostRequest() throws Exception {
            HttpPost request = new HttpPost(url);
            for (NameValuePair h : headers) {
                request.addHeader(h.getName(), h.getValue());
            }

            if (!params.isEmpty()) {
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }

            executeRequest(request, url);
        }

        private void executeRequest(HttpUriRequest request, String url) {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse;
            try {
                httpResponse = client.execute(request);
                responseCode = httpResponse.getStatusLine().getStatusCode();
                message = httpResponse.getStatusLine().getReasonPhrase();

                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    response = convertStreamToString(instream);
                    instream.close();
                }
            } catch (ClientProtocolException e) {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            } catch (IOException e) {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            }
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
}







