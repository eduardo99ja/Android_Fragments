package com.eduardo.dm_t2_2.ui.buscar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Calendar;

public class BuscarFragment extends Fragment  implements  View.OnClickListener  {

    private Button btnClean, btnSave,btnBuscar;
    private EditText etID, etNombre, etFecha, etDsiponibles,etDetalles,etDestino,etStatus;
    private ImageView ivFoto;
    static int bnd = 0, idp;

    public static String i,n,d,s,f,dis,det, img, temp;

    public SQLite sqLite;


    private BuscarViewModel buscarViewModel;
    public static BuscarFragment newInstance() {
        return new BuscarFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buscar, container, false);
        sqLite = new SQLite(getContext());
        Componentes(root);
        return root;
    }
    private void Componentes(View root) {
        EditTextComponentes(root);
        Botones(root);

    }
    private void EditTextComponentes(View root) {
        etID = (EditText) root.findViewById(R.id.tIETIDe);
        etNombre = (EditText) root.findViewById(R.id.tIETNombreev);
        etFecha = (EditText) root.findViewById(R.id.tIETFechaev);
        etDetalles = (EditText) root.findViewById(R.id.tIETDetallesev);
        etDsiponibles = (EditText) root.findViewById(R.id.tIETDisponiblesev);
        etDestino =(EditText) root.findViewById(R.id.tIETDestinoev);
        etStatus =(EditText) root.findViewById(R.id.tIETStatusev);


        etNombre.setEnabled(false);

        etFecha.setEnabled(false);
        etDetalles.setEnabled(false);
        etDsiponibles.setEnabled(false);
        etDestino.setEnabled(false);
        etStatus.setEnabled(false);




    }

    private void Botones(View root) {

        btnBuscar = root.findViewById(R.id.btnBuscare);
        btnClean = root.findViewById(R.id.btnLimpiarev);
        ivFoto = root.findViewById(R.id.ivFotoev);
        btnClean.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);

    }

    private void mLimpiar() {
        etID.setText("");
        etNombre.setText("");
        etFecha.setText("");
        etDsiponibles.setText("");
        etDetalles.setText("");
        etDestino.setText("");
        etStatus.setText("");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscare:
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
                        temp = d;

                        etNombre.setText(n);
                        etFecha.setText(f);
                        etDetalles.setText(det);
                        etDsiponibles.setText(dis);
                        etDestino.setText(d);
                        etStatus.setText(s);
                        cargaImagen(img, ivFoto);
                        bnd = 1;
                    } else {
                        Toast.makeText(getContext(), "ID del viaje: " + etID.getText().toString() + " NO EXISTE", Toast.LENGTH_LONG).show();
                        bnd = 0;
                    }
                    sqLite.cerrarBase();
                }
                break;

            case R.id.btnLimpiarev:
                mLimpiar();
                bnd = 0;
                break;


        }
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


}