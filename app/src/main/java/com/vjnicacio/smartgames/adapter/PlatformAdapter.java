package com.vjnicacio.smartgames.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vjnicacio.smartgames.model.PlatformModel;

import java.util.List;

public class PlatformAdapter extends ArrayAdapter<PlatformModel> {

    // Construtor da classe
    public PlatformAdapter(Context context, List<PlatformModel> platformList) {
        super(context, 0, platformList);
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

        // Obtém o objeto PlatformModel na posição atual
        PlatformModel platformModel = getItem(position);

        // Verifica se o objeto PlatformModel não é nulo
        if (platformModel != null) {
            // Obtém a referência ao TextView dentro do layout do item da lista
            TextView textView = convertView.findViewById(android.R.id.text1);

            // Define o texto do TextView com o nome da plataforma a partir do PlatformModel
            textView.setText(platformModel.getPlatform());
        }

        // Retorna a exibição atualizada para a lista
        return convertView;
    }
}