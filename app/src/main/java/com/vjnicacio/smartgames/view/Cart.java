package com.vjnicacio.smartgames.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vjnicacio.smartgames.R;

import androidx.annotation.Nullable;

public class Cart extends AppCompatActivity {

    // Botão para escanear cupom
    private ImageButton btnScanCoupon;

    // Campo de texto para inserir código do cupom
    private EditText editTextCouponCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da atividade
        setContentView(R.layout.activity_cart);

        // Inicializa os componentes da interface do usuário
        btnScanCoupon = findViewById(R.id.btnScanCoupon);
        editTextCouponCode = findViewById(R.id.editTextCouponCode);

        // Adiciona um OnClickListener para o botão de escanear cupom
        btnScanCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adicione a lógica aqui para lidar com o escaneamento do cupom
            }
        });
    }
}