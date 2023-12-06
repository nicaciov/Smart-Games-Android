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

    static public String ip = "http://192.168.0.23";

    static String uriListAllGames = (ip + "/rest-api-smart-games/rest/services/data/listAllGames");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_games);

        // Tenta fazer a requisição para listar todos os jogos
        try {
            volleyListAllGames(getApplicationContext(), uriListAllGames);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Houve um erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Inicializa os componentes da interface do usuário
        swipeRefreshlayout = findViewById(R.id.swipeRefreshlayout);
        swipeRefreshlayout.setRefreshing(true);

        int c1 = getResources().getColor(R.color.colorPurple);
        int c2 = getResources().getColor(R.color.colorPurple);
        int c3 = getResources().getColor(R.color.colorPurple);

        // Configura o esquema de cores para o SwipeRefreshLayout
        swipeRefreshlayout.setColorSchemeColors(c1, c2, c3);
        swipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Atualiza a lista de jogos ao puxar para baixo
                swipeRefreshlayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), "Carregando . . .", Toast.LENGTH_SHORT).show();

                try {
                    volleyListAllGames(getApplicationContext(), uriListAllGames);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Houve um Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                swipeRefreshlayout.setRefreshing(false);
            }
        });

    }

    // Método para fazer a requisição para listar todos os jogos
    public void volleyListAllGames(final Context ctx, String url) {

        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Map<String, String> jsonParams = new HashMap<String, String>();
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                Log.e("Tartaruga", "result " + result);

                // Finaliza o indicador de carregamento
                swipeRefreshlayout.setRefreshing(false);
                if (result != null) {

                    Log.e("RESULT: ", "*" + result + "*");

                    // Lista para armazenar os modelos de jogo
                    List<GameModel> list = new ArrayList<GameModel>();
                    GameModel gameModel = null;

                    try {

                        // Converte a resposta JSON em objetos GameModel
                        JSONArray json = new JSONArray(result);
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject object = json.getJSONObject(i);
                            gameModel = new GameModel();

                            // Tenta obter e definir os atributos do jogo
                            try {
                                gameModel.setId(object.getInt("id"));
                            } catch (Exception e) {
                            }

                            try {
                                gameModel.setGame_name(object.getString("game_name"));
                            } catch (Exception e) {
                            }

                            try {
                                // Se a foto estiver na resposta, remove a parte inicial
                                gameModel.setPhoto(object.getString("photo").substring(object.getString("photo").indexOf(","), object.getString("photo").length()));
                            } catch (Exception e) {
                            }

                            try {
                                gameModel.setGame_price(object.getDouble("game_price"));
                            } catch (Exception e) {
                            }

                            // Adiciona o modelo de jogo à lista
                            list.add(gameModel);
                        }

                        // Configura o RecyclerView para exibir a lista de jogos
                        rvArticle = findViewById(R.id.recyclerView);
                        rvArticle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rvAdapter = new GameAdapter(list, getApplicationContext(), ListAllGames.this);
                        rvArticle.setAdapter(rvAdapter);

                        // Finaliza o indicador de carregamento
                        swipeRefreshlayout.setRefreshing(false);

                    } catch (Exception ex) {
                        ex.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Houve um Erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        // Finaliza o indicador de carregamento
                        swipeRefreshlayout.setRefreshing(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Houve um Erro: ", Toast.LENGTH_SHORT).show();
                    // Finaliza o indicador de carregamento
                    swipeRefreshlayout.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Houve um Erro: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                // Finaliza o indicador de carregamento
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

    // Método para lidar com a seleção de itens no menu de opções
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Abre a atividade do carrinho quando o ícone do carrinho é selecionado
        startActivity(new Intent(ListAllGames.this, Cart.class));
        return false;
    }

    // Método para inflar o menu de opções na barra de ação
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
