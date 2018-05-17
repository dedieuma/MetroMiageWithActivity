package miage.metro.com.metromiagewithactivity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import API.Route;

/**
 * Created by Andréas on 16/05/2018.
 */

public class SelectionLigneAdapter extends ArrayAdapter<String>{

    List<Route> routeList;
    List<String> routesString;


    public SelectionLigneAdapter(@NonNull Context context, List<String> resource, List<Route> routeList) {
        super(context, 0, resource);
        this.routeList = routeList;
        this.routesString = resource;
    }






    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_selection_lignes, parent, false);
        }

        SelectionLigneViewHolder viewHolder = (SelectionLigneViewHolder) convertView.getTag();

        if(viewHolder==null){
            viewHolder = new SelectionLigneViewHolder();
            viewHolder.nomLigne = (TextView) convertView.findViewById(R.id.selection_ligne);

            convertView.setTag(viewHolder);
        }

        String ligne_selected = getItem(position);

        viewHolder.nomLigne.setText(ligne_selected);


        //View view = super.getView(position, convertView, parent);
        /*if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }
        */

        Route currentRoute = getRouteFromShortName(position, routesString, routeList);

        convertView.setBackgroundColor(Color.parseColor("#"+currentRoute.getColor()));
        viewHolder.nomLigne.setTextColor(Color.parseColor("#"+currentRoute.getTextColor()));
        viewHolder.nomLigne.setTypeface(Typeface.DEFAULT_BOLD);

        return convertView;

    }



    private class SelectionLigneViewHolder {
        public TextView nomLigne;

    }


    /***
     * getRouteFromShortName
     * retourne la route correspondante au string cliqué dans la listView
     * @param position
     * @param routes
     * @param listRoutes
     * @return
     */
    private Route getRouteFromShortName(int position, List<String> routes, List<Route> listRoutes) {
        String myShortName = routes.get(position);
        String[] evenShorter = myShortName.split(" ");
        for(Route rourou : listRoutes){
            if(rourou.getShortName().equals(evenShorter[1])){
                return rourou;
            }
        }
        return null;

    }



}
