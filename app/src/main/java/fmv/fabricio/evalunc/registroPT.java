package fmv.fabricio.evalunc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class registroPT extends AppCompatActivity {
    private List<Paciente> listapaciente = new ArrayList<Paciente>();
    ArrayAdapter<Paciente> arrayAdapterPaciente;
    private ListView listaP; // Lista pacientes
    private EditText editTextRut, editTextNombre, editTextTelefono, editTextAlergia, editTextEstado;
    private Button btnRegistrar, btnActualizar, btnEliminar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pt);

        // Variables
        editTextRut      = findViewById(R.id.editTextRut);
        editTextNombre   = findViewById(R.id.editTextNombre);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextAlergia  = findViewById(R.id.editTextAlergias);
        editTextEstado   = findViewById(R.id.editTextEstado);
        listaP           = findViewById(R.id.listaPacientes);
        btnRegistrar     = findViewById(R.id.btnRegistrar);
        btnActualizar    = findViewById(R.id.btnActualizar);
        btnEliminar      = findViewById(R.id.btnEliminar);

        // Click Botones CRUD
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarPaciente();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPaciente();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPaciente();
            }
        });

        // Cargar los datos al darle CLICK al ListView
        listaP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el paciente seleccionado de la lista
                Paciente pacienteSc = listapaciente.get(position);

                // Cargar los datos del paciente en los EditText
                editTextRut.setText(pacienteSc.getRut());
                editTextNombre.setText(pacienteSc.getNombre());
                editTextTelefono.setText(pacienteSc.getTelefono());
                editTextAlergia.setText(pacienteSc.getAlergia());
                editTextEstado.setText(pacienteSc.getEstado());
            }
        });


        // Metodos al iniciar la aplicacion
        inicializarFirebase();
        listarpacientes();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarpacientes() {
        // Referecia de la base de datos
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
                // Adaptador para la lista  pacientes
                arrayAdapterPaciente = new ArrayAdapter<>(registroPT.this, android.R.layout.simple_list_item_1, listapaciente);
                listaP.setAdapter(arrayAdapterPaciente);

                // Actualiza el adaptador de pacientes
                arrayAdapterPaciente.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(registroPT.this, "Error al obtener datos de la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void registrarPaciente() {
        // Referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("Pacientes");

        String rut = editTextRut.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String alergia = editTextAlergia.getText().toString().trim();
        String estado = editTextEstado.getText().toString().trim();

        if (rut.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || alergia.isEmpty() || estado.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Objeto Paciente
            Paciente p = new Paciente();
            p.setRut(rut);
            p.setNombre(nombre);
            p.setTelefono(telefono);
            p.setAlergia(alergia);
            p.setEstado(estado);
            // Guardar paciente en la base de datos FIREBASE
            databaseReference.child(p.getNombre()).setValue(p);
            Toast.makeText(this, "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show();
            LimpiarCampos();
        }
    }
    private void actualizarPaciente() {
        String rutAActualizar = editTextRut.getText().toString();
        if(rutAActualizar.isEmpty()){
            Toast.makeText(this, "Ingrese El Rut", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference pacientesReference = databaseReference.child("Pacientes");
            Query query = pacientesReference.orderByChild("rut").equalTo(rutAActualizar);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String keyPaciente = snapshot.getKey();
                        // Objeto paciente con los nuevos datos
                        Paciente pacienteActualizado = new Paciente();
                        pacienteActualizado.setRut(editTextRut.getText().toString());
                        pacienteActualizado.setNombre(editTextNombre.getText().toString());
                        pacienteActualizado.setTelefono(editTextTelefono.getText().toString());
                        pacienteActualizado.setAlergia(editTextAlergia.getText().toString());
                        pacienteActualizado.setEstado(editTextEstado.getText().toString());

                        // Actualizar todos los campos en la base de datos
                        DatabaseReference pacienteRef = pacientesReference.child(keyPaciente);
                        pacienteRef.setValue(pacienteActualizado)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(registroPT.this, "Registro Actualizado", Toast.LENGTH_SHORT).show();
                                        LimpiarCampos();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(registroPT.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(registroPT.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void eliminarPaciente() {

        String rutAEliminar = editTextRut.getText().toString();
        if(rutAEliminar.isEmpty()){
            Toast.makeText(this, "Ingrese El Rut", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference pacientesReference = FirebaseDatabase.getInstance().getReference("Pacientes");

            // Buscar paciente por el rut
            Query query = pacientesReference.orderByChild("rut").equalTo(rutAEliminar);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String keyAEliminar = snapshot.getKey();
                        // Utilizar la key encontrada para eliminar al paciente
                        DatabaseReference pacienteEliminar = pacientesReference.child(keyAEliminar);
                        pacienteEliminar.removeValue();
                        Toast.makeText(registroPT.this, "Paciente Elimindao", Toast.LENGTH_SHORT).show();
                        LimpiarCampos();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(registroPT.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }
    public void LimpiarCampos(){
        editTextEstado.setText("");
        editTextTelefono.setText("");
        editTextNombre.setText("");
        editTextRut.setText("");
        editTextAlergia.setText("");
    }
}