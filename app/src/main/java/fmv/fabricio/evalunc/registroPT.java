package fmv.fabricio.evalunc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class registroPT extends AppCompatActivity {
    private List<Paciente> listapaciente = new ArrayList<Paciente>();
    ArrayAdapter<Paciente> arrayAdapterPaciente;
    private EditText editTextRut, editTextNombre, editTextTelefono, editTextAlergia, editTextEstado;
    private Button btnRegistrar;
    private ListView listaP;
    // Lista Paciente
    // Obtén una referencia a la base de datos
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pt);

        editTextRut = findViewById(R.id.editTextRut);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextAlergia = findViewById(R.id.editTextAlergias);
        editTextEstado = findViewById(R.id.editTextEstado);
        listaP = findViewById(R.id.listaPacientes);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarPaciente();
            }
        });
        inicializarFirebase();
        // Llamada a listarpacientes después de configurar el OnClickListener
        listarpacientes();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarpacientes() {
        databaseReference.child("Pacientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listapaciente.clear();

                for (DataSnapshot objSnaptshot : snapshot.getChildren()) {
                    Paciente p = objSnaptshot.getValue(Paciente.class);
                    if (p != null) {
                        listapaciente.add(p);
                    }
                }

                Log.d("DEBUG", "Número de pacientes: " + listapaciente.size());

                // Mover la creación del ArrayAdapter aquí
                arrayAdapterPaciente = new ArrayAdapter<>(registroPT.this, android.R.layout.simple_list_item_1, listapaciente);
                listaP.setAdapter(arrayAdapterPaciente);

                // Actualiza el ArrayAdapter con la nueva lista de pacientes
                arrayAdapterPaciente.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "Error al obtener datos de la base de datos: " + error.getMessage());
            }
        });
    }





    // Metodo para registrar pacientes
    private void registrarPaciente() {
        // Inicializa la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("Pacientes");

        String rut = editTextRut.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String alergia = editTextAlergia.getText().toString().trim();
        String estado = editTextEstado.getText().toString().trim();

        if (rut.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Crea un objeto Paciente
            Paciente p = new Paciente();
            p.setRut(rut);
            p.setNombre(nombre);
            p.setTelefono(telefono);
            p.setAlergia(alergia);
            p.setEstado(estado);
            // Guarda el paciente en la base de datos usando la clave única
            databaseReference.child(p.getNombre()).setValue(p);
            Toast.makeText(this, "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show();
            LimpiarCampos();
        }
    }
    // Metodo para limpiar los campos
    public void LimpiarCampos(){
        editTextEstado.setText("");
        editTextTelefono.setText("");
        editTextNombre.setText("");
        editTextRut.setText("");
        editTextAlergia.setText("");
    }
}