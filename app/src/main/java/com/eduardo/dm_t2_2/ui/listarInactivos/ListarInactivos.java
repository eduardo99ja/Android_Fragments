package com.eduardo.dm_t2_2.ui.listarInactivos;

import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eduardo.dm_t2_2.R;
import com.eduardo.dm_t2_2.base.SQLite;
import com.eduardo.dm_t2_2.ui.listarActivos.ListarActivosViewModel;

import java.io.File;
import java.util.ArrayList;

public class ListarInactivos extends Fragment {
    ArrayList<String> registros, imagenes,ids;

    private ListarInactivosViewModel mViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(ListarInactivosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listar_inactivos, container, false);
        final TextView textView = root.findViewById(R.id.text_listarInactivos);
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        ListView list = root.findViewById(R.id.lvListadoInactivosV);
        SQLite sqLite;

        sqLite = new SQLite(getContext());
        sqLite.abrirBase();

        Cursor cursor = sqLite.getRegistrosInactivos();

        registros = sqLite.getViajes(cursor);
        imagenes = sqLite.getImagenes(cursor);
        ids= sqLite.getIds(cursor);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, registros);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_viaje, null);
                ((TextView) dialogView.findViewById(R.id.tvInfoViaje)).setText(registros.get(position));
                ImageView ivImagen = dialogView.findViewById(R.id.ivFotoViaje);
                cargaImagen(imagenes.get(position), ivImagen);
                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Viaje");
                dialogo.setView(dialogView);

                dialogo.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aceptarCambio(ids.get(position));
                    }
                });
                dialogo.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Registro aun activo", Toast.LENGTH_LONG).show();
                    }
                });
                dialogo.show();
            }
        });


        sqLite.cerrarBase();

        return root;
    }
    public void aceptarCambio(String ide){
        int idCambio= Integer.parseInt(ide.toString());
        String Status = "Activo";
        SQLite sqLite;

        sqLite = new SQLite(getContext());
        sqLite.abrirBase();
        sqLite.updateStatus(idCambio,Status);
        Toast.makeText(getContext(), "Status cambiado", Toast.LENGTH_LONG).show();
        sqLite.cerrarBase();

        getFragmentManager().beginTransaction().detach(ListarInactivos.this).attach(ListarInactivos.this).commit();



    }
    public void cargaImagen(String imagen, ImageView iv){
        try {
            File filePhoto = new File(imagen);
            Uri photUri = FileProvider.getUriForFile(getContext(), "com.eduardo.dm_t2_2", filePhoto);
            iv.setImageURI(photUri);
        }catch(Exception ex){
            Toast.makeText(getContext(), "Ocurrio un error en la carga de la imagen", Toast.LENGTH_LONG).show();
            Log.d("Carga Imagen", "Error al cargar la imagen" + imagen + "\n Mensaje" + ex.getMessage() + "\n Causa: " + ex.getCause());
        }
    }
    }



