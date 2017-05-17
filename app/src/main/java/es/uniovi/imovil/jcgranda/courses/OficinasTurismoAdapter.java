package es.uniovi.imovil.jcgranda.courses;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OficinasTurismoAdapter extends BaseAdapter {
	
	static class ViewHolder {
		public TextView mOficinaNombre;

	}
	
	private List<OficinasTurismo> mOficinas;
	public LayoutInflater mInflater;

	
	public OficinasTurismoAdapter(Context context, List<OficinasTurismo> oficinas) {

		if (context == null || oficinas == null ) {
			throw new IllegalArgumentException();
		}
			
		this.mOficinas = oficinas;
		this.mInflater = LayoutInflater.from(context);
	}
		
	@Override
	public int getCount() {

		return mOficinas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mOficinas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		ViewHolder viewHolder;
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.list_item_oficinas, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mOficinaNombre = (TextView) rowView.findViewById(R.id.nombreOficina);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}
		
		OficinasTurismo oficina = (OficinasTurismo) getItem(position);
		Log.v(this.getClass().getName(), "Hola");
		viewHolder.mOficinaNombre.setText(oficina.getNombre());

		
		return rowView;
	}

}
