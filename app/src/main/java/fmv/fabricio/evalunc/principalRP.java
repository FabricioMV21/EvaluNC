package fmv.fabricio.evalunc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class principalRP extends AppCompatActivity {
    Button btnRegistros, btnPublicarR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_rp);

        btnRegistros = findViewById(R.id.btnRegistros);
        btnPublicarR = findViewById(R.id.btnPublicarR);

        btnRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(principalRP.this, registroPT.class));
            }
        });

        btnPublicarR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(principalRP.this, MainActivity.class));
            }
        });
    }
}