package miage.metro.com.metromiagewithactivity;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andréas on 17/05/2018.
 */

public class SelectionArretAdapter extends ArrayAdapter<String> {


    public SelectionArretAdapter(@NonNull Context context, List<String> resource) {
        super(context, 0, resource);
    }






    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_selection_lignes, parent, false);
        }

        SelectionArretViewHolder viewHolder = (SelectionArretViewHolder) convertView.getTag();

        if(viewHolder==null){
            viewHolder = new SelectionArretViewHolder();
            viewHolder.nomLigne = (TextView) convertView.findViewById(R.id.selection_ligne);

            convertView.setTag(viewHolder);
        }

        String ligne_selected = getItem(position);

        viewHolder.nomLigne.setText(ligne_selected);


        //View view = super.getView(position, convertView, parent);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }



        return convertView;

    }



    private class SelectionArretViewHolder {
        public TextView nomLigne;

    }



}
