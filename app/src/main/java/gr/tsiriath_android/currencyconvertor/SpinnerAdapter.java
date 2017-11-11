package gr.tsiriath_android.currencyconvertor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class SpinnerAdapter extends ArrayAdapter {
    private final int groupid;
    private final ArrayList<ItemData> list;
    private final LayoutInflater inflater;

    SpinnerAdapter(Activity context, ArrayList<ItemData> list){
        super(context, R.id.spin_img,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid= R.layout.spinner_item_currencies;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);

        ImageView imageView=itemView.findViewById(R.id.spin_img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView=itemView.findViewById(R.id.spin_txt);
        textView.setText(list.get(position).getText());
        TextView textView2=itemView.findViewById(R.id.spin_des);
        textView2.setText(list.get(position).getDescr());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);

    }
}



