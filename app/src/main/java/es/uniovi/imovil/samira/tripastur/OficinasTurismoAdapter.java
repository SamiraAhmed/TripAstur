package es.uniovi.imovil.samira.tripastur;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

//Adaptador para inflar la lsita con todas las oficinas
public class OficinasTurismoAdapter extends BaseAdapter implements SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener {


	//Metodos que se implementan de la interfaz del searchview para los eventos de busqueda
	@Override
	public boolean onQueryTextSubmit(String query) {
		return true;
	}

	//Vamos buscando según lo que se introduzca en searchview partiendo de todas las oficinas e ir filtrando
	@Override
	public boolean onQueryTextChange(String newText) {
		Log.v(this.getClass().getCanonicalName(),newText);
		newText = newText.toLowerCase();
		mOficinas.clear();
		if (newText.length() == 0) {
			mOficinas.addAll(OficinasBuscadas);

		} else {
			for (Oficina ofi : OficinasBuscadas) {
				if (ofi.getNombre().toLowerCase().contains(newText)) {
					mOficinas.add(ofi);

					notifyDataSetChanged();
				}
			}
		}
		notifyDataSetChanged();
		return true;
	}

	//Metodo para el evento del checkbox de favoritos, obtenemos la oficina en el que el checkbox a cambiado de estado
	//y le cambiamos el estado aqui para luego pasarlo a la lista
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CheckBox cb =(CheckBox) buttonView;
		Oficina ofi=this.getItem(Integer.parseInt(cb.getContentDescription().toString()));
		ofi.setOficinaFavorita(isChecked);

	}

	//elementos que se iflaran en cada item de la lista
	static class ViewHolder {
		public TextView mOficinaNombre;
		public CheckBox esFavorita;

	}

	private ArrayList<Oficina> mOficinas;
	private ArrayList<Oficina> OficinasBuscadas;
	public LayoutInflater mInflater;

	//constructor con la lista
	public OficinasTurismoAdapter(Context context, ArrayList<Oficina> oficinas) {

		if (context == null || oficinas == null ) {
			throw new IllegalArgumentException();
		}

		this.mInflater = LayoutInflater.from(context);

		this.mOficinas = oficinas;
		this.OficinasBuscadas = new ArrayList<Oficina>();
		this.OficinasBuscadas.addAll(mOficinas);


	}

	//constructor solo con el contexto
	public OficinasTurismoAdapter(Context context){
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


	//instanciamos los elemntos que queremos mostrar en la lista
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		ViewHolder viewHolder;
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.list_item_oficinas, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mOficinaNombre = (TextView) rowView.findViewById(R.id.nombreOficina);
			viewHolder.esFavorita = (CheckBox) rowView.findViewById(R.id.check_fav);

			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}

		Oficina oficina = (Oficina) getItem(position);
		viewHolder.mOficinaNombre.setText(oficina.getNombre());
		viewHolder.esFavorita.setContentDescription(position+"");
		viewHolder.esFavorita.setChecked(oficina.isOficinaFavorita());

		viewHolder.esFavorita.setOnCheckedChangeListener(this);
		return rowView;
	}






}
