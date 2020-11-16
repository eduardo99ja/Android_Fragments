package com.eduardo.dm_t2_2.ui.modificar;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.eduardo.dm_t2_2.R;
import com.eduardo.dm_t2_2.base.SQLite;
import com.eduardo.dm_t2_2.ui.listarActivos.ListarActivos;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Modificar extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    public SQLite sqLite;
   public String idBuscar=null;
    
    private Button btnGuarda, btnBusca, btnLimpia;
    String currentPath, sexo, destino, status;
    private Uri photoURI;
    public static final int REQUEST_TAKE_PHOTO = 1;
    private static int anio, mes, dia;
    Calendar c;
    DatePickerDialog dpd;
    private ImageButton btnCalendario;

    static int bnd = 0, idp;
    public static String i,n,d,s,f,dis,det, img, temp;
    private EditText etID, etNombre, etFecha, etDsiponibles,etDetalles,etDestino,etStatus;
    private Spinner spDestino, spStatus;
    private ImageView ivFoto;

    private ModificarViewModel mViewModel;

    public static Modificar newInstance() {
        return new Modificar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar, container, false);
        sqLite = new SQLite(getContext());
        componentes(root);
        return root;


    }


    private void componentes(View root) {

        editTextComponentes(root);
        botonesComponentes(root);
        SpinnerComponents(root);
        textViewComponentes(root);
        ivFoto = root.findViewById(R.id.ivFotomv);
        Bundle data= getArguments();
        if (data != null){
            idBuscar= data.getString("idEdit");
            etID.setEnabled(false);
            etID.setText(idBuscar);
            btnBusca.callOnClick();
            btnLimpia.setText("Cancelar");
        }

    }

    private void mLimpiar() {
        etID.setEnabled(true);
        etID.setText("");

        etNombre.setText("");
        etFecha.setText("");
        etDsiponibles.setText("");
        etDetalles.setText("");
       
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
        bnd = 0;
        ArrayAdapter<CharSequence> destinoAdapter, statusAdapter;
        destinoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opciones, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status, android.R.layout.simple_spinner_item);
        

        destino = "";
        status = "";
        sexo = "";

        spDestino.setAdapter(destinoAdapter);
        spStatus.setAdapter(statusAdapter);
        
    }

    private void SpinnerComponents(View root) {
        ArrayAdapter<CharSequence> destinoAdapter, statusAdapter;
        destinoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opciones, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status, android.R.layout.simple_spinner_item);
        
        spDestino = root.findViewById(R.id.spnDestinomv);
        spDestino.setAdapter(destinoAdapter);
        spStatus = root.findViewById(R.id.spnStatusmv);
        spStatus.setAdapter(statusAdapter);
        

        spDestino.setOnItemSelectedListener(this);
        spStatus.setOnItemSelectedListener(this);
        
    }

    public void cargaImagen(String imagen, ImageView iv) {
        try {
            File filePhoto = new File(imagen);
            Uri photUri = FileProvider.getUriForFile(getContext(), "com.eduardo.dm_t2_2", filePhoto);
            iv.setImageURI(photUri);
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Ocurrio un error en la carga de la imagen", Toast.LENGTH_LONG).show();
            Log.d("Carga Imagen", "Error al cargar la imagen" + imagen + "\n Mensaje" + ex.getMessage() + "\n Causa: " + ex.getCause());
        }
    }

    private void textViewComponentes(View root) {
        spDestino = root.findViewById(R.id.spnDestinomv);
        etNombre = root.findViewById(R.id.tIETNombremv);
        spStatus = root.findViewById(R.id.spnStatusmv);
        etFecha = root.findViewById(R.id.tIETFechamv);
        etDetalles = root.findViewById(R.id.tIETDetallesmv);
        etDsiponibles = root.findViewById(R.id.tIETDisponiblesmv);
        
    }

    private void editTextComponentes(View root) {
        etID = root.findViewById(R.id.tIETIDmv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ModificarViewModel.class);
        // TODO: Use the ViewModel
    }

    private void botonesComponentes(View root) {
        btnBusca = root.findViewById(R.id.btnBuscarmv);
        btnLimpia = root.findViewById(R.id.btnLimpiarmp);
        btnGuarda = root.findViewById(R.id.btnGuardarmv);
        btnCalendario = root.findViewById(R.id.btnCalendarioMV);
        ivFoto = root.findViewById(R.id.ivFotomv);



        btnBusca.setOnClickListener(this);
        btnLimpia.setOnClickListener(this);
        btnGuarda.setOnClickListener(this);
        btnCalendario.setOnClickListener(this);
        ivFoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarmv:

                if (etID.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Favor de ingresar un criterio de busqueda", Toast.LENGTH_LONG).show();
                    bnd = 0;
                } else {
                    Toast.makeText(getContext(), "ID del viaje es: " + etID.getText().toString(), Toast.LENGTH_LONG).show();
                    sqLite.abrirBase();
                    idp = Integer.parseInt(etID.getText().toString());
                    if (sqLite.getValor(idp).getCount() == 1) {
                        Cursor cursor = sqLite.getValor(idp);

                        if (cursor.moveToFirst()) {
                            do {

                                i = etID.getText().toString();
                                n = cursor.getString(1);
                                d = cursor.getString(2);
                                s = cursor.getString(3);
                                f = cursor.getString(4);
                                dis= cursor.getString(5);
                                det = cursor.getString(6);
                                img = cursor.getString(7);

                            } while (cursor.moveToNext());
                        }
                        Toast.makeText(getContext(), "ID del viaje: " + etID.getText().toString() , Toast.LENGTH_LONG).show();

                        cambioSpinnerDestino(d);
                        cambioSpinnerStatus(s);
                        temp = d;

                        etNombre.setText(n);
                        etFecha.setText(f);
                        etDetalles.setText(det);
                        etDsiponibles.setText(dis);
                        cargaImagen(img, ivFoto);
                        bnd = 1;
                    } else {
                        Toast.makeText(getContext(), "ID del viaje: " + etID.getText().toString() + " NO EXISTE", Toast.LENGTH_LONG).show();
                        bnd = 0;
                    }
                    sqLite.cerrarBase();
                    etID.setEnabled(false);
                }
                break;
            case R.id.btnGuardarmv:

                if (etID.getText().toString().equals("") || etNombre.getText().toString().equals("") || etFecha.getText().toString().equals("")
                        || etDetalles.getText().toString().equals("") || etDsiponibles.getText().toString().equals("")  || img.equals("")) {

                    Toast.makeText(getContext(), "Hay campos vacios, favor de llenar todos", Toast.LENGTH_LONG).show();
                } else {
                    int id = Integer.parseInt(etID.getText().toString());
                    String nom = etNombre.getText().toString().toUpperCase();
                    String fech = etFecha.getText().toString();
                    String dispo = etDsiponibles.getText().toString();
                    String detall = etDetalles.getText().toString();


                    sqLite.abrirBase();

                    Toast.makeText(getContext(), sqLite.updateRegistroViaje(id, nom, destino, status,  fech, dispo, detall,  img),Toast.LENGTH_LONG).show();

                    sqLite.cerrarBase();
                    mLimpiar();
                    etID.setEnabled(true);
                }
                if(idBuscar!=null){
                    ListarActivos listFragment = new ListarActivos();
                    FragmentManager manager= getFragmentManager();
                    manager.beginTransaction().replace(R.id.nav_host_fragment,listFragment,listFragment.getTag()).commit();
                }
                break;
            case R.id.btnLimpiarmp:
                mLimpiar();
                bnd = 0;
                if(idBuscar!=null){
                    ListarActivos listFragment = new ListarActivos();
                    FragmentManager manager= getFragmentManager();
                    manager.beginTransaction().replace(R.id.nav_host_fragment,listFragment,listFragment.getTag()).commit();
                }
                break;
            case R.id.btnCalendarioMV:
                c = Calendar.getInstance();
                anio = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(getContext(), this, anio, mes, dia);
                dpd.show();

                break;
            case R.id.ivFotomv:

                Intent tomaFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (tomaFoto.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImegeFile();
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Error al generar foto", Toast.LENGTH_LONG).show();
                    }

                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(getContext(), "com.eduardo.dm_t2_2", photoFile);
                        tomaFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(tomaFoto, REQUEST_TAKE_PHOTO);
                    }
                }
                break;

        }
    }

    public void cambioSpinnerDestino(String a) {
        ArrayAdapter<CharSequence> adapter;

        switch (a) {
            case "Nacional":
                spDestino.setSelection(1);
                break;
            case "Internacional":
                spDestino.setSelection(2);
                break;
        }
    }

    public void cambioSpinnerStatus(String a) {
        switch (a) {
            case "Activo":
                spStatus.setSelection(1);
                break;
            case "Inactivo":
                spStatus.setSelection(2);
                break;
        }
    }



    @NotNull
    private File createImegeFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, ".jpg", storageDir);

        currentPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnDestinomv:
                if (position != 0) {
                    destino = parent.getItemAtPosition(position).toString();
                } else {
                    destino = "";
                }

                break;
            case R.id.spnStatusmv:
                if (position != 0) {
                    status = parent.getItemAtPosition(position).toString();
                } else {
                    status = "";
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            ivFoto.setImageURI(photoURI);
            img = currentPath;
            Toast.makeText(getContext(), "Foto guardada en: " + img, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Algo fallo", Toast.LENGTH_LONG).show();
        }
    }

}