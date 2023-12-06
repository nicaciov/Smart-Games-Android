package com.vjnicacio.smartgames.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vjnicacio.smartgames.R;
import com.vjnicacio.smartgames.model.GameModel;
import com.vjnicacio.smartgames.util.ImageUtils;
import com.vjnicacio.smartgames.view.ViewGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<GameModel> gameList;
    private Context context;
    private Activity activity;
    private RequestQueue requestQueue;

    // URL para adicionar itens ao carrinho
    private static final String URI_ADD_CART = "http://192.168.0.23/rest-api-smart-games/rest/services/data/addCart";

    // Construtor da classe
    public GameAdapter(List<GameModel> gameList, Context context, Activity activity) {
        this.gameList = gameList;
        this.context = context;
        this.activity = activity;
    }

    // Cria novas instâncias de ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    // Associa os dados aos elementos visuais do ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final GameModel game = gameList.get(position);

        // Exibe o nome do jogo
        holder.textViewGameName.setText(game.getGame_name());

        // Formata o preço do jogo para exibição
        double price = game.game_price;
        Locale brLocale = new Locale("pt", "BR");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(brLocale);
        currencyFormat.setCurrency(Currency.getInstance("BRL"));
        String formattedValue = currencyFormat.format(price);
        holder.textViewPrice.setText(formattedValue);

        // Exibe a imagem do jogo usando Base64
        String base64Image = game.getPhoto();
        ImageUtils.setBase64Image(holder.imageViewGame, base64Image);

        // Configura o clique no item para visualizar detalhes do jogo
        holder.linearLayoutCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                // Converte a imagem Base64 em um arquivo temporário
                String base64Image = game.getPhoto();
                byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                File tempFile = null;

                try {
                    tempFile = File.createTempFile("temp_image", null, activity.getCacheDir());
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(decodedBytes);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Inicia a atividade de visualização do jogo
                Intent intent = new Intent(context, ViewGame.class);
                intent.putExtra("game_id", game.getId() + "");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Se existir um arquivo temporário, passa o caminho para a atividade
                if (tempFile != null) {
                    intent.putExtra("IMAGE_FILE_PATH", tempFile.getAbsolutePath());
                }

                context.startActivity(intent);
            }
        });

        // Configura o clique no botão "Adicionar ao Carrinho"
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                try {
                    Log.e("Aqui", "Try");
                    // Realiza a chamada para adicionar o item ao carrinho
                    volleyAddToCart(activity.getApplicationContext(), URI_ADD_CART, game.getId());
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), "Houve um erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Retorna o número total de itens na lista
    @Override
    public int getItemCount() {
        return gameList.size();
    }

    // Classe ViewHolder que representa os elementos visuais do item na lista
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewGame;
        TextView textViewGameName, textViewPrice;
        LinearLayout linearLayoutCard;
        Button btnAddToCart;

        // Construtor da classe ViewHolder
        ViewHolder(View itemView) {
            super(itemView);
            textViewGameName = itemView.findViewById(R.id.textViewGameName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            linearLayoutCard = itemView.findViewById(R.id.listview);
            imageViewGame = itemView.findViewById(R.id.imageViewGame);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    // Método para enviar uma solicitação para adicionar o item ao carrinho usando Volley
    public void volleyAddToCart(final Context context, String url, long gameId) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        }

        // Parâmetros JSON da solicitação
        Map<String, String> jsonParams = new HashMap<>();
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                // Exibe uma mensagem quando o item é adicionado ao carrinho com sucesso
                Toast.makeText(activity.getApplicationContext(), "Adicionado ao carrinho.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Exibe uma mensagem de erro em caso de falha na solicitação
                Toast.makeText(context, "Houve um erro: " + volleyError.getCause(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Parâmetros a serem enviados no corpo da solicitação POST
                Map<String, String> parameters = new HashMap<>();
                parameters.put("id", gameId + "");
                return parameters;
            }
        };
        // Configuração de política de retentativa para a solicitação Volley
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}