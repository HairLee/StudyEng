package com.hailpt.com.youtubeeng.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hailpt.com.youtubeeng.R;
import com.hailpt.com.youtubeeng.adater.CaterogyAdapter;
import com.hailpt.com.youtubeeng.app.AppController;
import com.hailpt.com.youtubeeng.model.Caterogy;
import com.hailpt.com.youtubeeng.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaterogyActivity extends AppCompatActivity {

    private static final String TAG = CaterogyActivity.class.getSimpleName();

    private static final String CHANNEL_ID = "UCDIJPT98nv7gcWgQ8TeJmrg";
    private static final int MAX_RESULT = 20;
    private static final String KEY = "AIzaSyAKOtDCpaGQ9624yS8-9pADYH0qFvmK6f8";
    private static final String url = "https://www.googleapis.com/youtube/v3/playlists?part=contentDetails&channelId=" + CHANNEL_ID + "&maxResults=" + MAX_RESULT + "&key=" + KEY;

    private RecyclerView recyclerView;
    private CaterogyAdapter likeContentAdapter;
    private Caterogy caterogy = new Caterogy();
    private List<Caterogy> caterogyList = new ArrayList<>();
    private CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeJsonObjectRequest();

    }

    private String jsonResponse;
    private void makeJsonObjectRequest() {

        customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String name = response.getString("kind");
                    String email = response.getString("etag");
//                    String nextPageToken = response.getString("nextPageToken");

                    JSONObject phone = response.getJSONObject("pageInfo");
                    int totalResults = phone.getInt("totalResults");
                    int resultsPerPage = phone.getInt("resultsPerPage");

                    JSONArray items = response.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        Caterogy caterogy = new Caterogy();
                        JSONObject item = items.getJSONObject(i);
                        JSONObject snippet = item.getJSONObject("contentDetails");
                        Log.d("hailpt ======", "itemCount = " + snippet.getString("itemCount"));
                        Log.d("hailpt ======", "Id = " + item.getString("id"));
                        caterogy.setId(item.getString("id"));
                        caterogy.setItemCount(snippet.getString("itemCount"));
                        if (!item.getString("id").equals("PL7YqrWXed1aaFMn8Rakj0OQb0_dkw71hs")&&!item.getString("id").equals("PL7YqrWXed1aZMHsToX32rksxdvVtczdFN")&&!item.getString("id").equals("PL7YqrWXed1aZMHsToX32rksxdvVtczdFN")&&!item.getString("id").equals("PL7YqrWXed1abbjqN85ERxqsHUHHIxfiKF")) {
                            caterogyList.add(caterogy);
                        }
                    }

                    Log.d("hailpt ======", "SIZE = " + caterogyList.size());
                    Gson gson = new Gson();
                    String json = gson.toJson(caterogyList);

                    Log.d("hailpt", "json ======" + json);

//
//                    jsonResponse = totalResults + " === " + resultsPerPage;
//                    jsonResponse += "Name: " + name + "\n\n";
//                    jsonResponse += "Email: " + email + "\n\n";
//
//                    Toast.makeText(getApplicationContext(),
//                            jsonResponse,
//                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: ------------ " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                updateLayout();
                hidePDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void updateLayout() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        likeContentAdapter = new CaterogyAdapter(this, caterogyList, new CaterogyAdapter.OnRemoveCallBack() {
            @Override
            public void remove(final int pos) {

            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CaterogyActivity.this, VlogListActitivty.class);
                intent.putExtra("CHANNEL_ID", caterogyList.get(position).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                startActivity(intent);


                Log.d(TAG, "CHANNEL_ID CHANNEL_ID == " + caterogyList.get(position).getId());
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(likeContentAdapter);
        likeContentAdapter.notifyDataSetChanged();

    }

    private void hidePDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            customProgressDialog = null;
        }
    }
}
