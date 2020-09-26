package com.example.owner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class maildialog extends AppCompatDialogFragment {
    SharedPreferences sharedPreferences;
    public final String mypreference="mypref";
    public final String name="namekey";
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    //private Maildialoglistner listner;
    //public EaxampleDialogListner listner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialogemail,null);
        builder.setView(view)
                .setTitle("EMail")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
         //           listner.apllytext();
                    }
                });
        imageView=(ImageView)view.findViewById(R.id.one);
        imageView1=(ImageView)view.findViewById(R.id.two);
        imageView2=(ImageView)view.findViewById(R.id.three);
        imageView4=(ImageView)view.findViewById(R.id.five);

        return builder.create();
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
       try{
       //    listner=(EaxampleDialogListner)context;
       }catch (ClassCastException e)
       {
           throw new ClassCastException(context.toString() +
                   "must implement ExampleDialogListner");
       }
    }*/

/*    public interface EaxampleDialogListner{
        void apllytext();
    }*/
}
