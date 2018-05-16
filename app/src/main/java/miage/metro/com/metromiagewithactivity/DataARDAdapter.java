package miage.metro.com.metromiagewithactivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import Persistance.Data_Arret_Route_Direction;

/**
 * Created by Andréas on 20/04/2018.
 */
//http://tutos-android-france.com/listview-afficher-une-liste-delements/
public class DataARDAdapter extends ArrayAdapter<Data_Arret_Route_Direction> {

    public DataARDAdapter(Context context, List<Data_Arret_Route_Direction> dataARDs){
        super(context, 0, dataARDs );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_saved_lignes, parent, false);
        }

        DataARDViewHolder viewHolder = (DataARDViewHolder) convertView.getTag();
        if(viewHolder==null){
            viewHolder = new DataARDViewHolder();
            viewHolder.nom_Arret = (TextView) convertView.findViewById(R.id.nom_Arret);
            viewHolder.nom_Ligne = (TextView) convertView.findViewById(R.id.nom_Ligne);
            viewHolder.nom_Direction = (TextView) convertView.findViewById(R.id.nom_Direction);

            convertView.setTag(viewHolder);
        }

        Data_Arret_Route_Direction dataARD = getItem(position);

        viewHolder.nom_Arret.setText(dataARD.getArret().getName());

        if (dataARD.getNameDirection() != ""){
            viewHolder.nom_Ligne.setText(dataARD.getRoute().getType()+" "+dataARD.getRoute().getShortName());
            viewHolder.nom_Direction.setText("Direction : "+dataARD.getNameDirection());
        }



        return convertView;
    }

    private class DataARDViewHolder{
        public TextView nom_Arret;
        public TextView nom_Ligne;
        public TextView nom_Direction;
    }
}
