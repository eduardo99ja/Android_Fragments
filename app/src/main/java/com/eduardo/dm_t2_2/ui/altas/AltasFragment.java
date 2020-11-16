package com.eduardo.dm_t2_2.ui.altas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eduardo.dm_t2_2.R;
import com.eduardo.dm_t2_2.base.SQLite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AltasFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnClean, btnSave;
    private ImageButton btnDate;
    private EditText etID, etNombre, etFecha, etDsiponibles,etDetalles;
    private Spinner spDestino, spStatus;
    private ImageView ivFoto;

    private Uri photoURI;

    public SQLite sqLite;

    public static final int REQUEST_TAKE_PHOTO = 1;
    private static int anio, mes, dia;
    String currentPath, img = "", a, b,destino,status;

    DatePickerDialog dpd;
    Calendar c;

    private AltasViewModel mViewModel;
    private AltasViewModel altasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_altas, container, false);

        sqLite = new SQLite(getContext());

        Componentes(root);

        return root;

    }

        private void Componentes(View root) {
            EditTextComponentes(root);
            Botones(root);
            SpinnerComponents(root);
        }
        private void EditTextComponentes(View root) {
            etID = (EditText) root.findViewById(R.id.tIETIDnp);
            etNombre = (EditText) root.findViewById(R.id.tIETNombrenv);
            etFecha = (EditText) root.findViewById(R.id.tIETFechanv);
            etDetalles = (EditText) root.findViewById(R.id.tIETDetallesnv);
            etDsiponibles = (EditText) root.findViewById(R.id.tIETDisponiblesnv);

        }

    private void Botones(View root) {

        btnDate = root.findViewById(R.id.btnCalendarioNV);
        btnSave = root.findViewById(R.id.btnGuardarnv);
        btnClean = root.findViewById(R.id.btnLimpiarnp);
        ivFoto = root.findViewById(R.id.ivFotonv);

        btnDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClean.setOnClickListener(this);
        ivFoto.setOnClickListener(this);

    }
    private void SpinnerComponents(View root) {
        ArrayAdapter<CharSequence> destinoAdapter, statusAdapter;
        destinoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opciones, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status, android.R.layout.simple_spinner_item);
        
        spDestino = root.findViewById(R.id.spnDestinonv);
        spDestino.setAdapter(destinoAdapter);
        spStatus = root.findViewById(R.id.spnStatusnv);
        spStatus.setAdapter(statusAdapter);
        

        spDestino.setOnItemSelectedListener(this);
        spStatus.setOnItemSelectedListener(this);
        
    }
    private void mLimpiar() {
        etID.setText("");
        etNombre.setText("");
        etFecha.setText("");
        etDsiponibles.setText("");
        etDetalles.setText("");


        ArrayAdapter<CharSequence> destinoAdapter, statusAdapter;
        destinoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opciones, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status, android.R.layout.simple_spinner_item);

        destino = "";
        status = "";


        spDestino.setAdapter(destinoAdapter);
        spStatus.setAdapter(statusAdapter);


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spnDestinonv:
                if (position != 0) {
                    destino = parent.getItemAtPosition(position).toString();
                } else {
                    destino = "";
                }

                break;
            case R.id.spnStatusnv:
                if (position != 0) {
                    status = parent.getItemAtPosition(position).toString();
                } else {
                    status = "";
                }
                break;

        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLimpiarnp:
                mLimpiar();
                break;
            case R.id.btnCalendarioNV:
                c = Calendar.getInstance();
                anio = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(getContext(), this, anio, mes, dia);
                dpd.show();

                break;
            case R.id.ivFotonv:

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
            case R.id.btnGuardarnv:

                if (etID.getText().toString().equals("") || etNombre.getText().toString().equals("") || etFecha.getText().toString().equals("")
                        || etDetalles.getText().toString().equals("") || etDsiponibles.getText().toString().equals("") ) {

                    Toast.makeText(getContext(), "Hay campos vacios, favor de llenar todos", Toast.LENGTH_LONG).show();
                } else {
                    int id = Integer.parseInt(etID.getText().toString());
                    String nom = etNombre.getText().toString().toUpperCase();
                    String fech = etFecha.getText().toString();
                    String disp = etDsiponibles.getText().toString();
                    String det = etDetalles.getText().toString();

                    sqLite.abrirBase();

                    if (sqLite.addViaje(id, destino, status, nom, fech,disp,det, img)) {
                        Toast.makeText(getContext(), "Datos alamcenados", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Error en almacenamiento", Toast.LENGTH_LONG).show();
                    }
                    sqLite.cerrarBase();
                }
                break;
        }

    }
    private File createImegeFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, ".jpg", storageDir);

        currentPath = image.getAbsolutePath();
        return image;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AltasViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}