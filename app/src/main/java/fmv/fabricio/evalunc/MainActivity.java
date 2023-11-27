package fmv.fabricio.evalunc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Mqtt mqttManager;
    EditText texto;
    Button btnEnviar, btnvolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto     = findViewById(R.id.txtMessage);
        btnEnviar = findViewById(R.id.btnPublish);
        btnvolver = findViewById(R.id.btnvolver);

        mqttManager = new Mqtt(getApplicationContext());
        mqttManager.connectToMqttBroker();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar si el campo de texto esta vacio
                if(texto.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "!ERROR¡ Ingrese el estado", Toast.LENGTH_SHORT).show();
                }else{
                    // Publicar en MQTT
                    mqttManager.publishMessage(texto.getText().toString());
                    Toast.makeText(MainActivity.this, "Mensaje Publicado En MQTT", Toast.LENGTH_SHORT).show();
                    texto.setText("");
                }
            }
        });

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Volver a la actividad anterior
                startActivity(new Intent(MainActivity.this, principalRP.class));
            }
        });
    }
}