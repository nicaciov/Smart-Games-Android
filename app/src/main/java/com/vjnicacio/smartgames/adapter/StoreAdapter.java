package com.vjnicacio.smartgames.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vjnicacio.smartgames.model.StoreModel;

import java.util.List;

public class StoreAdapter extends ArrayAdapter<StoreModel> {

    private Context context;

    // Construtor da classe
    public StoreAdapter(Context context, List<StoreModel> storeList) {
        super(context, 0, storeList);
        this.context = context;
    }

    // Método chamado para obter a exibição de um item na posição especificada
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Verifica se a exibição atual (convertView) é nula
        if (convertView == null) {
            // Se nula, infla um novo layout de item de lista simples do Android
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Obtém o objeto StoreModel na posição atual
        final StoreModel storeModel = getItem(position);

        // Verifica se o objeto StoreModel não é nulo
        if (storeModel != null) {
            // Obtém a referência ao TextView dentro do layout do item da lista
            TextView textView = convertView.findViewById(android.R.id.text1);

            // Define o texto do TextView com o nome da loja a partir do StoreModel
            textView.setText(storeModel.getStore());

            // Adiciona um OnClickListener para cada item da lista
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Obtém a latitude e longitude da loja
                    double latitude = Double.parseDouble(storeModel.getLat());
                    double longitude = Double.parseDouble(storeModel.getLon());

                    // Cria uma URI para abrir o Google Maps com a localização e um marcador
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Marcador)");

                    // Cria uma Intent com a ação VIEW para abrir o Google Maps
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Especifica que o aplicativo do Google Maps deve ser usado
                    mapIntent.setPackage("com.google.android.apps.maps");

                    // Verifica se há aplicativos disponíveis para lidar com a Intent
                    context.startActivity(mapIntent);

                }
            });
        }

        // Retorna a exibição atualizada para a lista
        return convertView;
    }
}