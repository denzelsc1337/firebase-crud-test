package com.example.firebase_crud_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firebase_crud_test.model.Persona;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private List<Persona> lstPersona = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;
    Persona oPersonaD = new Persona();
    EditText nomP, apeP, correoP, passwordP;
    ListView listP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Persona oPersonaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomP = findViewById(R.id.txtnom);
        apeP = findViewById(R.id.txtape);
        correoP = findViewById(R.id.txtcor);
        passwordP = findViewById(R.id.txtpass);

        listP = findViewById(R.id.lstDatosPersonales);

        startfirebase();
        showData();


        listP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                oPersonaSelected = (Persona) parent.getItemAtPosition(position);
                nomP.setText(oPersonaSelected.getNombre());
                apeP.setText(oPersonaSelected.getApellidos());
                correoP.setText(oPersonaSelected.getCorreo());
                passwordP.setText(oPersonaSelected.getPassword());
            }
        });


    }

    private void startfirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    private void showData() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstPersona.clear();
                for (DataSnapshot oDataSnapshot : snapshot.getChildren()){
                    Persona oPersona = oDataSnapshot.getValue(Persona.class);
                    lstPersona.add(oPersona);

                    arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, lstPersona);
                    listP.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //inicializar firebase

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setText(View view){
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombre = nomP.getText().toString();
        String apellido = apeP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();

        switch (item.getItemId()){
            //Insercion de datos
            case R.id.icon_add:
                if (nombre.equals("") || apellido.equals("")|| correo.equals("")||password.equals("")){
                    validacion();
                }else {
                    oPersonaD.setUid(UUID.randomUUID().toString());
                    oPersonaD.setNombre(nombre);
                    oPersonaD.setApellidos(apellido);
                    oPersonaD.setCorreo(correo);
                    oPersonaD.setPassword(password);

                    databaseReference.child("Persona").child(oPersonaD.getUid()).setValue(oPersonaD);//Tabla con el nombre "Persona"

                    Toast.makeText(this, "Agregado Correctamente", Toast.LENGTH_SHORT).show();
                    clear();
                }
                break;
                //Actualizar Datos
            case R.id.icon_save:
                String nombre_u = nomP.getText().toString();
                String apellido_u = apeP.getText().toString();
                String correo_u = correoP.getText().toString();
                String password_u = passwordP.getText().toString();
                if (nombre_u.equals("") || apellido_u.equals("")|| correo_u.equals("")||password_u.equals("")){
                    validacion();
                }else {

                    oPersonaD.setUid(oPersonaSelected.getUid());
                    oPersonaD.setNombre(nomP.getText().toString().trim());
                    oPersonaD.setApellidos(apeP.getText().toString().trim());
                    oPersonaD.setCorreo(correoP.getText().toString().trim());
                    oPersonaD.setPassword(passwordP.getText().toString().trim());

                    databaseReference.child("Persona").child(oPersonaD.getUid()).setValue(oPersonaD);

                    Toast.makeText(this, "Actualizado correctamente!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
                break;
                //Eliminar Datos
            case R.id.icon_dlt:
                String nombre_d = nomP.getText().toString();
                String apellido_d = apeP.getText().toString();
                String correo_d = correoP.getText().toString();
                String password_d = passwordP.getText().toString();
                if (nombre_d.equals("") || apellido_d.equals("")|| correo_d.equals("")||password_d.equals("")){
                    validacion();
                }else {
                    oPersonaD.setUid(oPersonaSelected.getUid());
                    databaseReference.child("Persona").child(oPersonaD.getUid()).removeValue();
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    clear();
                }
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clear() {
        nomP.setText("");
        apeP.setText("");
        correoP.setText("");
        passwordP.setText("");
        nomP.requestFocus();
    }

    private void validacion() {
        String nombre = nomP.getText().toString();
        String apellido = apeP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();

        if (nombre.equals("")){
            nomP.setError("Required");
        }else if (apellido.equals("")){
            apeP.setError("Required");
        }else if (correo.equals("")){
            correoP.setError("Required");
        }else  if (password.equals("")){
            passwordP.setError("Required");
        }
    }
}