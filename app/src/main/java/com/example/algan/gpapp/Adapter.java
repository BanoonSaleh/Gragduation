package com.example.algan.gpapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.algan.gpapp.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by algan on 4/8/2017.
 */public class Adapter extends BaseAdapter {
        Context context;
        List<cources> valueList;

        public Adapter(List<cources> listValue, Context context) {
                this.context = context;
                this.valueList = listValue;
        }

        public Adapter(Showall listValue, ListView listCollege) {

        }

        @Override
        public int getCount() {
                return this.valueList.size();
        }

        @Override
        public Object getItem(int position) {
                return this.valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                ViewItem viewItem = null;
                getItem(position);
                if (convertView == null) {
                        viewItem = new ViewItem();
                        LayoutInflater layoutInfiater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        //    LayoutInflater layoutInfiter = LayoutInflater.from(context);
                        convertView = layoutInfiater.inflate(R.layout.list_adapter_view, null);

                        viewItem.txtTitle = (TextView) convertView.findViewById(R.id.adapter_text_title);
                        viewItem.txtDescription = (TextView) convertView.findViewById(R.id.adapter_text_description);
                        viewItem.favor=(Button)convertView.findViewById(R.id.setFavorite);
                        viewItem.favor.setVisibility(View.INVISIBLE);
                        SessionManager session=new SessionManager(convertView.getContext());
                        if(session.isLoggedIn()==true)
                        {
                                viewItem.favor.setVisibility(View.VISIBLE);
                        }

                        viewItem.txtDescription.setMovementMethod(LinkMovementMethod.getInstance());
                        convertView.setTag(viewItem);
                } else {
                        viewItem = (ViewItem) convertView.getTag();
                }

                viewItem.txtTitle.setText(valueList.get(position).Title);
                viewItem.txtDescription.setText(valueList.get(position).URL);
                viewItem.favor.setTag(position);
                viewItem.favor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                int position = (Integer) view.getTag();
                                // Access the row position here to get the correct data item
                                cources crs = valueList.get(position);
                                updateCourses(position, crs, view.getContext(), view);
                        }
                });

                return convertView;
        }

        private void updateCourses(final int position, final cources selectedCrs, final Context ctx, final View view) {
                // Tag used to cancel the request
                String tag_string_req = "req_courses";

                final ProgressDialog pDialog = new ProgressDialog(ctx);
                pDialog.setMessage("Loading courses ...");
                showDialog(pDialog);

                RequestQueue queue = Volley.newRequestQueue(ctx);
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_COURSES(), new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
//                Log.d(TAG, "Register Response: " + response.toString());
                                hideDialog(pDialog);

                                try {
                                        JSONObject jObj = new JSONObject(response);
                                        boolean error = jObj.getBoolean("error");
                                        if (!error) {

                                                // We get weather info (This is an array)
//                        JSONArray jArr = jObj.getJSONArray("data");
                                                if (!selectedCrs.isFavorite) {
                                                        view.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                                                        Toast.makeText(ctx, "Courses set as favorite", Toast.LENGTH_LONG).show();
                                                } else {
                                                        view.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                                                        Toast.makeText(ctx, "Courses unset as favorite", Toast.LENGTH_LONG).show();
                                                }
                                                valueList.get(position).isFavorite=!selectedCrs.isFavorite;
                                        } else {

                                                // Error occurred in registration. Get the error
                                                // message
                                                String errorMsg = jObj.getString("error_msg");
                                                Toast.makeText(ctx,
                                                        "Unable to update", Toast.LENGTH_LONG).show();
                                        }
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }

                        }
                }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ctx,
                                        error.getMessage(), Toast.LENGTH_LONG).show();
                                hideDialog(pDialog);
                        }
                }) {

                        @Override
                        protected Map<String, String> getParams() {
                                // Posting params to register url
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("uid", DashboardActivity.uuid);
                                params.put("email", DashboardActivity.uemail);
                                params.put("set", String.valueOf(selectedCrs.id));
                                params.put("currentState", (selectedCrs.isFavorite) ? "set" : "unset");
                                return params;
                        }

                };
                queue.add(strReq);
        }

        private void showDialog(ProgressDialog pDialog) {
                if (!pDialog.isShowing())
                        pDialog.show();
        }

        private void hideDialog(ProgressDialog pDialog) {
                if (pDialog.isShowing())
                        pDialog.dismiss();
        }
}


class ViewItem
{
        TextView txtTitle;
        TextView txtDescription;
        Button favor;

}