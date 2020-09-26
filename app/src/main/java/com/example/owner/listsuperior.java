package com.example.owner;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class listsuperior extends ArrayAdapter<String> {
    private Integer[] image;
    private String[] name;
    private Activity context;
    public listsuperior(Activity context,Integer[] image,String[] name) {
        super(context,R.layout.listview,name);
        this.context=context;
        this.name=name;
        this.image=image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder=null;
        if (r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview,null);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }
        viewHolder.imageView.setImageResource(image[position]);
        viewHolder.t1.setText(name[position]);
        return r;
    }
    class ViewHolder {
        TextView t1;
        ImageView imageView;

        ViewHolder(View view) {
            t1 = (TextView) view.findViewById(R.id.textview1);
            imageView = (ImageView) view.findViewById(R.id.imageview1);
        }
    }
}
