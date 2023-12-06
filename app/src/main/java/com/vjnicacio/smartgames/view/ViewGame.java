package com.vjnicacio.smartgames.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.vjnicacio.smartgames.adapter.PlatformAdapter;
import com.vjnicacio.smartgames.adapter.StoreAdapter;
import com.vjnicacio.smartgames.model.PlatformModel;
import com.vjnicacio.smartgames.model.StoreModel;
import com.vjnicacio.smartgames.util.ImageUtils;
import com.vjnicacio.smartgames.model.GameModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGame extends AppCompatActivity {

    // URLs das APIs
    static String uriListAllGames = (ListAllGames.ip + "/rest-api-smart-games/rest/services/data/getGame");
    static String uriGetPlatform = (ListAllGames.ip + "/rest-api-smart-games/rest/services/data/getPlatform");
    static String uriGetStore = (ListAllGames.ip + "/rest-api-smart-games/rest/services/data/getStore");
    static String uriAddCart = "http://192.168.0.23/rest-api-smart-games/rest/services/data/addCart";

    private RequestQueue rQueue;

    private TextView textViewGameTitle, textViewGameDescription, textViewGamePrice;
    private ImageView imageViewGame;
    private ListView listViewPlatform;
    private Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        // Obtém os dados do Intent
        Intent intent = getIntent();
        String game_id = intent.getStringExtra("game_id");

        // Inicializa os elementos da UI
        textViewGameTitle = findViewById(R.id.textViewGameTitle);
        textViewGameDescription = findViewById(R.id.textViewGameDescription);
        textViewGamePrice = findViewById(R.id.textViewGamePrice);
        imageViewGame = findViewById(R.id.imageViewGame);
        listViewPlatform = findViewById(R.id.listViewPlatform);
        btnBuy = findViewById(R.id.btnBuy);

        // Configura o listener do botão de compra
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Realiza a chamada para adicionar ao carrinho
                    volleyaddCart(getApplicationContext(), uriListAllGames, game_id);
                } catch (Exception e) {
                    // Trata erros
                    Log.e("Aqui", "Erro ao adicionar ao carrinho: " + e.getMessage());
                }
            }
        });

        // Carrega a imagem do jogo
        try {
            // Utilize o ID correto do ImageView
            ImageView imageView = findViewById(R.id.imageViewGame);
            String filePath = intent.getStringExtra("IMAGE_FILE_PATH");
            if (filePath != null && !filePath.isEmpty()) {
                // Carrega a imagem no ImageView a partir do arquivo
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Aqui", "Erro ao carregar imagem do jogo: " + e.getMessage());
        }

        // Realiza chamadas para obter detalhes do jogo, lojas e plataformas
        try {
            volleyGetGame(getApplicationContext(), uriListAllGames, game_id);
            volleyGetStore(getApplicationContext(), uriGetStore, game_id);
            volleyGetPlatform(getApplicationContext(), uriGetPlatform, game_id);
        } catch (Exception e) {
            Log.e("Aqui", "Erro ao obter informações: " + e.getMessage());
        }
    }

    public void volleyGetGame(final Context ctx, String url, String id_game) {
        // Cria a fila de requisições se ainda não existir
        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        // Parâmetros da requisição
        Map<String, String> params = new HashMap<>();
        params.put("id", id_game);

        // Cria a requisição POST
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.e("Tartaruga", "result " + result);

                        if (result != null) {
                            Log.e("RESULT: ", "*" + result + "*");

                            try {
                                // Converte a resposta para um objeto JSON
                                JSONObject object = new JSONObject(result);

                                // Cria um objeto GameModel
                                GameModel gameModel = new GameModel();

                                // Preenche o objeto GameModel com os dados da resposta
                                gameModel.setId(object.optInt("id"));
                                gameModel.setGame_name(object.optString("game_name"));
                                gameModel.setGame_description(object.optString("game_description"));

                                // Corrige a obtenção da imagem em base64
                                String base64Image = object.optString("photo");
                                gameModel.setPhoto(base64Image);

                                // Continua preenchendo o objeto GameModel
                                gameModel.setGame_price(object.optDouble("game_price"));

                                // Atualiza os elementos da UI com os dados do jogo
                                textViewGameTitle.setText(gameModel.getGame_name());
                                textViewGameDescription.setText(gameModel.getGame_description());
                                textViewGamePrice.setText(String.valueOf(gameModel.getGame_price()));

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                                // Trata erros ao processar a resposta JSON
                                Log.e("ERRO", "Erro ao processar resposta JSON: " + ex.getMessage());
                            }
                        } else {
                            // Trata caso a resposta seja nula
                            Log.e("Aqui", "Erro: Resposta nula");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Exibe mensagens de erro para o usuário
                Toast.makeText(ctx.getApplicationContext(), "Houve um Erro: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Retorna os parâmetros da requisição
                return params;
            }
        };

        // Define a política de tentativas da requisição
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adiciona a requisição à fila
        rQueue.add(request);
    }


    public void volleyGetStore(final Context ctx, String url, String id_game) {
        // Cria a fila de requisições se ainda não existir
        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        // Parâmetros da requisição
        Map<String, String> params = new HashMap<>();
        params.put("id", id_game);

        // Cria a requisição POST
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.e("GetStore", "result " + result);

                        if (result != null) {
                            Log.e("RESULT: ", "*" + result + "*");

                            try {
                                // Converte a resposta para um array JSON
                                JSONArray json = new JSONArray(result);

                                // Lista para armazenar os objetos StoreModel
                                List<StoreModel> list = new ArrayList<>();

                                // Itera sobre o array JSON
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject object = json.getJSONObject(i);

                                    // Cria um objeto StoreModel
                                    StoreModel storeModel = new StoreModel();

                                    // Preenche o objeto StoreModel com os dados da resposta
                                    storeModel.setId(object.optInt("id"));
                                    storeModel.setStore(object.optString("store"));
                                    storeModel.setLat(object.optString("lat"));
                                    storeModel.setLon(object.optString("lon"));

                                    // Adiciona o objeto à lista
                                    list.add(storeModel);
                                }

                                // Cria um adapter e configura a ListView
                                StoreAdapter storeAdapter = new StoreAdapter(ctx.getApplicationContext(), list);
                                ListView listViewStore = findViewById(R.id.listViewStore);
                                listViewStore.setAdapter(storeAdapter);

                            } catch (JSONException ex) {
                                ex.printStackTrace();

                                // Trata erros ao processar a resposta JSON
                                Toast.makeText(ctx.getApplicationContext(), "Houve um Erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Trata caso a resposta seja nula
                            Toast.makeText(ctx.getApplicationContext(), "Houve um erro, resposta nula.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Trata erros na requisição

                // Exibe mensagens de erro para o usuário
                Toast.makeText(ctx.getApplicationContext(), "Houve um Erro: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Retorna os parâmetros da requisição
                return params;
            }
        };

        // Define a política de tentativas da requisição
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adiciona a requisição à fila
        rQueue.add(request);
    }


    public void volleyGetPlatform(final Context ctx, String url, String id_game) {
        // Cria a fila de requisições se ainda não existir
        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        // Parâmetros da requisição
        Map<String, String> params = new HashMap<>();
        params.put("id", id_game);

        // Cria a requisição POST
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.e("GetPlatform", "result " + result);

                        if (result != null) {
                            Log.e("RESULT: ", "*" + result + "*");

                            try {
                                // Converte a resposta para um array JSON
                                JSONArray json = new JSONArray(result);

                                // Lista para armazenar os objetos PlatformModel
                                List<PlatformModel> list = new ArrayList<>();

                                // Itera sobre o array JSON
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject object = json.getJSONObject(i);

                                    // Cria um objeto PlatformModel
                                    PlatformModel platformModel = new PlatformModel();

                                    // Preenche o objeto PlatformModel com os dados da resposta
                                    platformModel.setId(object.optInt("id"));
                                    platformModel.setPlatform(object.optString("platform"));

                                    // Adiciona o objeto à lista
                                    list.add(platformModel);
                                }

                                // Cria um adapter e configura a ListView
                                PlatformAdapter platformAdapter = new PlatformAdapter(ctx.getApplicationContext(), list);
                                ListView listViewPlatform = findViewById(R.id.listViewPlatform);
                                listViewPlatform.setAdapter(platformAdapter);

                            } catch (JSONException ex) {
                                ex.printStackTrace();

                                // Trata erros ao processar a resposta JSON
                                Toast.makeText(ctx.getApplicationContext(), "Houve um Erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Trata caso a resposta seja nula
                            Toast.makeText(ctx.getApplicationContext(), "Erro: Resposta nula", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Trata erros na requisição
                Toast.makeText(ctx.getApplicationContext(), "Houve um Erro: " + volleyError.getCause(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Retorna os parâmetros da requisição
                return params;
            }
        };

        // Define a política de tentativas da requisição
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adiciona a requisição à fila
        rQueue.add(request);
    }



    public void volleyaddCart(final Context ctx, String url, String game_id) {
        // Cria a fila de requisições se ainda não existir
        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        // Parâmetros da requisição
        Map<String, String> params = new HashMap<>();
        params.put("id", game_id);

        // Cria a requisição POST
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        // Exibe mensagem de sucesso ao usuário
                        Toast.makeText(ctx.getApplicationContext(), "Adicionado ao carrinho.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Exibe mensagem de erro ao usuário
                Toast.makeText(ctx.getApplicationContext(), "Houve um Erro: " + volleyError.getCause(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Retorna os parâmetros da requisição
                return params;
            }
        };

        // Define a política de tentativas da requisição
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adiciona a requisição à fila
        rQueue.add(request);
    }

}