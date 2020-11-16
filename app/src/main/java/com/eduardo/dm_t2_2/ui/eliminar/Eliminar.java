package com.eduardo.dm_t2_2.ui.eliminar;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eduardo.dm_t2_2.R;
import com.eduardo.dm_t2_2.base.SQLite;
import com.eduardo.dm_t2_2.ui.listarActivos.ListarActivos;

import java.io.File;

public class Eliminar extends Fragment implements  View.OnClickListener {
    private Button btnElimiar,btnBuscar,btnCambioStatus;
    private EditText etID, etNombre, etFecha, etDsiponibles,etDetalles,etDestino,etStatus;
    private ImageView ivFoto;
    static int bnd = 0, idp;
    public String idEliminar=null;

    public static String i,n,d,s,f,dis,det, img, temp;

    public SQLite sqLite;
    private EliminarViewModel mViewModel;

    public static Eliminar newInstance() {
        return new Eliminar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eliminar, container, false);
        sqLite = new SQLite(getContext());
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliminarViewModel.class);
        // TODO: Use the ViewModel
    }
    private void Componentes(View root) {
        EditTextComponentes(root);
        Botones(root);
        Bundle data= getArguments();
        if (data != null){
            idEliminar= data.getString("idEliminar");
            etID.setEnabled(false);
            etID.setText(idEliminar);
            btnBuscar.callOnClick();

        }

    }
    private void EditTextComponentes(View root) {
        etID = (EditText) root.findViewById(R.id.tIETIDdv);
        etNombre = (EditText) root.findViewById(R.id.tIETNombredv);
        etFecha = (EditText) root.findViewById(R.id.tIETFechadv);
        etDetalles = (EditText) root.findViewById(R.id.tIETDetallesdv);
        etDsiponibles = (EditText) root.findViewById(R.id.tIETDisponiblesdv);
        etDestino =(EditText) root.findViewById(R.id.tIETDestinodv);
        etStatus =(EditText) root.findViewById(R.id.tIETStatusdv);


        etNombre.setEnabled(false);
        etFecha.setEnabled(false);
        etDetalles.setEnabled(false);
        etDsiponibles.setEnabled(false);
        etDestino.setEnabled(false);
        etStatus.setEnabled(false);

    }

    private void Botones(View root) {
        btnCambioStatus = root.findViewById(R.id.btnCambioStatusdv);

        btnBuscar = root.findViewById(R.id.btnBuscardv);
      //  btnClean = root.findViewById(R.id.btnLimpiardv);
        btnElimiar = root.findViewById(R.id.btnEliminardv);


        ivFoto = root.findViewById(R.id.ivFotodv);
        //btnClean.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
        btnElimiar.setOnClickListener(this);
        btnCambioStatus.setOnClickListener(this);

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
            case R.id.btnBuscardv:
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

            case R.id.btnEliminardv:
                if(bnd == 1){
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_viaje, null);
                    ((TextView) dialogView.findViewById(R.id.tvInfoViaje)).setText("¿Esta seguro de eliminar?\n" +
                            "ID: [" + i + "]\n" +
                            "Nombre destino: [" + n + "]\n" +
                            "TIPO DESTINO: [" + d + "]\n" +
                            "STATUS: [" + s + "]\n" +
                            "FECHA SALIDA: ["+ f + "]\n" +
                            "LUGARES DISPONIBLES: ["+ dis +"]\n" +
                            "DETALLES: ["+ det +"]\n");

                    ImageView image = dialogView.findViewById(R.id.ivFotoViaje);
                    cargaImagen(img, image);
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                    dialogo.setTitle("Importante");
                    dialogo.setView(dialogView);
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            aceptar();
                            mLimpiar();
                            if(idEliminar!=null){
                                ListarActivos listFragment = new ListarActivos();
                                FragmentManager manager= getFragmentManager();
                                manager.beginTransaction().replace(R.id.nav_host_fragment,listFragment,listFragment.getTag()).commit();
                            }
                        }
                    });
                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Registro aun activo", Toast.LENGTH_LONG).show();
                        }
                    });

                    dialogo.show();
                    bnd = 0;

                }else{
                    Toast.makeText(getContext(), "Favor de ingresar un criterio de busqueda", Toast.LENGTH_LONG).show();
                    bnd = 0;
                }

                break;
            case R.id.btnCambioStatusdv:
                if(bnd == 1){
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_viaje, null);
                    ((TextView) dialogView.findViewById(R.id.tvInfoViaje)).setText("¿Confirmar cambio de status?\n" +
                            "ID: [" + i + "]\n" +
                            "Nombre destino: [" + n + "]\n" +
                            "TIPO DESTINO: [" + d + "]\n" +
                            "STATUS: [" + s + "]\n" +
                            "FECHA SALIDA: ["+ f + "]\n" +
                            "LUGARES DISPONIBLES: ["+ dis +"]\n" +
                            "DETALLES: ["+ det +"]\n");

                    ImageView image = dialogView.findViewById(R.id.ivFotoViaje);
                    cargaImagen(img, image);
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                    dialogo.setTitle("Importante");
                    dialogo.setView(dialogView);
                    dialogo.setCancelable(false);
                    if(s.equals("Activo")){
                        dialogo.setPositiveButton("Desactivar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aceptarCambio(1);
                                mLimpiar();
                                if(idEliminar!=null){
                                    ListarActivos listFragment = new ListarActivos();
                                    FragmentManager manager= getFragmentManager();
                                    manager.beginTransaction().replace(R.id.nav_host_fragment,listFragment,listFragment.getTag()).commit();
                                }
                            }
                        });
                    }else{
                        dialogo.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aceptarCambio(2);
                                mLimpiar();

                            }
                        });
                    }

                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "El registro no cambio", Toast.LENGTH_LONG).show();
                        }
                    });

                    dialogo.show();
                    bnd = 0;

                }else{
                    Toast.makeText(getContext(), "Favor de ingresar un criterio de busqueda", Toast.LENGTH_LONG).show();
                    bnd = 0;
                }

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
    public void aceptar(){
        sqLite.abrirBase();
        sqLite.eliminar(etID.getText());
        Toast.makeText(getContext(), "Registro eliminado", Toast.LENGTH_LONG).show();
        sqLite.cerrarBase();
    }
    public void aceptarCambio(int id){
        String  Status;
        if(id == 1){
            Status = "Inactivo";

        }else{
            Status = "Activo";
        }
        sqLite.abrirBase();
        sqLite.updateStatus(Integer.parseInt(etID.getText().toString()) ,Status);
        Toast.makeText(getContext(), "Status cambiado", Toast.LENGTH_LONG).show();
        sqLite.cerrarBase();


    }

}