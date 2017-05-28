package es.uniovi.imovil.samira.tripastur;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

//Adaptador para inflar la lista con las oficinas favoritas, se realizan las mismas operaciones que en el adaptador
//de la lista total de oficinas
public class OficinasTurismoFavoritasAdapter extends BaseAdapter implements SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener{

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        Log.v(this.getClass().getCanonicalName(),newText);
        newText = newText.toLowerCase();
        mOficinas.clear();
        if (newText.length() == 0) {
            mOficinas.addAll(OficinasBuscadas);
            Log.d("d","añadidas todas");
        } else {
            for (Oficina ofi : OficinasBuscadas) {
                if (ofi.getNombre().toLowerCase().contains(newText)) {
                    mOficinas.add(ofi);
                    Log.d("d","añadida "+ofi.getNombre());
                    notifyDataSetChanged();
                }
            }
        }
        notifyDataSetChanged();
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox cb =(CheckBox) buttonView;
        Oficina ofi=this.getItem(Integer.parseInt(cb.getContentDescription().toString()));
        ofi.setOficinaFavorita(isChecked);



    }

    static class ViewHolder {
        public TextView mOficinaNombre;
        public CheckBox esFavorita;

    }

    private ArrayList<Oficina> mOficinas;
    private ArrayList<Oficina> OficinasBuscadas;
    public LayoutInflater mInflater;


    public OficinasTurismoFavoritasAdapter(Context context, ArrayList<Oficina> oficinas) {

        if (context == null || oficinas == null ) {
            throw new IllegalArgumentException();
        }

        this.mInflater = LayoutInflater.from(context);

        this.mOficinas = new ArrayList<Oficina>();
        for (Oficina ofi:
             oficinas) {
            if(ofi.isOficinaFavorita()){
                mOficinas.add(ofi);
            }

        }
        this.OficinasBuscadas = new ArrayList<Oficina>();
        this.OficinasBuscadas.addAll(mOficinas);


    }

    public OficinasTurismoFavoritasAdapter(Context context){
        if (context == null  ) {
            throw new IllegalArgumentException();
        }

        this.mInflater = LayoutInflater.from(context);
    }

    public void setOficinas(ArrayList<Oficina> oficinas){
        if (oficinas == null  ) {
            throw new IllegalArgumentException();
        }

        this.mOficinas = oficinas;
        this.OficinasBuscadas = new ArrayList<Oficina>();
        this.OficinasBuscadas.addAll(mOficinas);

    }

    @Override
    public int getCount() {

        return mOficinas.size();
    }

    @Override
    public Oficina getItem(int position) {

        return mOficinas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        OficinasTurismoAdapter.ViewHolder viewHolder;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_oficinas, parent, false);
            viewHolder = new OficinasTurismoAdapter.ViewHolder();
            viewHolder.mOficinaNombre = (TextView) rowView.findViewById(R.id.nombreOficina);
            viewHolder.esFavorita = (CheckBox) rowView.findViewById(R.id.check_fav);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (OficinasTurismoAdapter.ViewHolder) rowView.getTag();
        }

        Oficina oficina = (Oficina) getItem(position);
        viewHolder.mOficinaNombre.setText(oficina.getNombre());
        viewHolder.esFavorita.setContentDescription(position+"");
        viewHolder.esFavorita.setChecked(oficina.isOficinaFavorita());


        viewHolder.esFavorita.setOnCheckedChangeListener(this);
        return rowView;
    }
}
