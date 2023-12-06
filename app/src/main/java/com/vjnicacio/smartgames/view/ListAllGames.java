package com.vjnicacio.smartgames.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vjnicacio.smartgames.R;
import com.vjnicacio.smartgames.adapter.GameAdapter;
import com.vjnicacio.smartgames.model.GameModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAllGames extends AppCompatActivity {

    private RequestQueue rQueue;
    private LinearLayout linearLayoutHomeDEPA;
    private RecyclerView rvArticle;
    private RecyclerView.Adapter rvAdapter;
    private SwipeRefreshLayout swipeRefreshlayout;
    private ProgressDialog dialog;

    static String uriListAllGames = "http://192.168.0.23/rest-api-smart-games/rest/services/data/listAllGames";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_games);

        try {
            Log.e("Aqui", "Try");
            volleyListAllGames(getApplicationContext(), uriListAllGames);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Aqui", "TryErro");
        }

        swipeRefreshlayout = findViewById(R.id.swipeRefreshlayout);
        swipeRefreshlayout.setRefreshing(true);

        int c1 = getResources().getColor(R.color.colorPurple);
        int c2 = getResources().getColor(R.color.colorPurple);
        int c3 = getResources().getColor(R.color.colorPurple);

        swipeRefreshlayout.setColorSchemeColors(c1, c2, c3);
        swipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshlayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), "Carregando . . .", Toast.LENGTH_SHORT).show();

                try {
                    Log.e("Aqui", "Try");
                    volleyListAllGames(getApplicationContext(), uriListAllGames);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Aqui", "TryErro");
                }

                swipeRefreshlayout.setRefreshing(false);
            }
        });

    }

    public void volleyListAllGames(final Context ctx, String url) {

        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Map<String, String> jsonParams = new HashMap<String, String>();
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                Log.e("Tartaruga", "result " + result);

                swipeRefreshlayout.setRefreshing(false);
                if (result != null) {

                    Log.e("RESULT: ", "*" + result + "*");

                    List<GameModel> list = new ArrayList<GameModel>();
                    GameModel gameModel = null;

                    try {

                        JSONArray json = new JSONArray(result);
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject object = json.getJSONObject(i);
                            gameModel = new GameModel();


                            try {
                                gameModel.setId(object.getInt("id"));
                            } catch (Exception e) {
                            }

                            try {
                                gameModel.setGame_name(object.getString("game_name"));
                            } catch (Exception e) {
                            }

                            try {
                                gameModel.setPhoto(object.getString("photo").substring(object.getString("photo").indexOf(","), object.getString("photo").length()));
                            } catch (Exception e) {
                            }

                            try {
                                gameModel.setGame_price(object.getDouble("game_price"));
                            } catch (Exception e) {
                            }

                            list.add(gameModel);
                        }

                        rvArticle = findViewById(R.id.recyclerView);
                        rvArticle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rvAdapter = new GameAdapter(list, getApplicationContext(), ListAllGames.this);
                        rvArticle.setAdapter(rvAdapter);

                        swipeRefreshlayout.setRefreshing(false);

                    } catch (Exception ex) {
                        ex.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ERRO", ex.getMessage());
                        swipeRefreshlayout.setRefreshing(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro: "  , Toast.LENGTH_SHORT).show();
                    swipeRefreshlayout.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Erro: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Erro: " + volleyError.getCause(), Toast.LENGTH_SHORT).show();
                Log.e("Aqui", "ErrorResponse: " + volleyError.getMessage());
                Log.e("Aqui", "ErrorResponse: " + volleyError.getCause());
                swipeRefreshlayout.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", 1 + "");
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue.add(request);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(ListAllGames.this, ViewGame.class));

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}