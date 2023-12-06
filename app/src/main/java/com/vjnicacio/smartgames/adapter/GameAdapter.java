package com.vjnicacio.smartgames.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.vjnicacio.smartgames.view.ListAllGames;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<GameModel> listItems;
    private Context context;
    private Activity activity;

    private RequestQueue rQueue;

    static String uriListAllGames = "http://192.168.0.23/rest-api-smart-games/rest/services/data/addCart";

    public GameAdapter(List<GameModel> listItems, Context ctx, Activity act) {
        this.listItems = listItems;
        this.context = ctx;
        this.activity = act;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final GameModel game = listItems.get(position);

        holder.textViewGameName.setText(game.game_name.toString());

        double valor = game.game_price;
        Locale brLocale = new Locale("pt", "BR");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(brLocale);

        currencyFormat.setCurrency(Currency.getInstance("BRL"));

        String formattedValue = currencyFormat.format(valor);

        holder.textViewPrice.setText(formattedValue);

        String base64Image = game.photo;
        ImageUtils.setBase64Image(holder.imageViewGame, base64Image);

        holder.linearLayoutCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                try {
                    Log.e("Aqui", "Try");
                    volleyaddCart(activity.getApplicationContext(), uriListAllGames, game.id);
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Aqui", "TryErro");
                }

            }
        });

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                try {
                    Log.e("Aqui", "Try");
                    volleyaddCart(activity.getApplicationContext(), uriListAllGames, game.id);
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Aqui", "TryErro");
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewGame;

        TextView textViewGameName, textViewPrice;

        LinearLayout linearLayoutCard;

        Button btnAddToCart;

        ViewHolder(View itemView) {
            super(itemView);

            textViewGameName = itemView.findViewById(R.id.textViewGameName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            linearLayoutCard = itemView.findViewById(R.id.listview);
            imageViewGame = itemView.findViewById(R.id.imageViewGame);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);

        }
    }

    public void volleyaddCart(final Context ctx, String url, long game_id) {

        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(activity.getApplicationContext());
        }

        Map<String, String> jsonParams = new HashMap<String, String>();
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                Toast.makeText(activity.getApplicationContext(), "Adicionado ao carrinho.", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Erro: " + volleyError.getCause(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", game_id + "");
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue.add(request);
    }

}