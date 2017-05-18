package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.net.URL;

public class OficinaDetailsFragment extends Fragment {
	


	public static OficinaDetailsFragment newInstance(OficinasTurismo oficina) {
		
		OficinaDetailsFragment fragment = new OficinaDetailsFragment();
		
		Bundle args = new Bundle();
        args.putSerializable(OficinaDetailsActivity.OFICINA, (Serializable) oficina);
        fragment.setArguments(args);
        
		return fragment;
	}
	
	public OficinaDetailsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView;
		rootView = inflater.inflate(R.layout.oficina_details_fragment, container, false);

		FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);





		// Si estamos restaurando desde un estado previo no hacemos nada
		if (savedInstanceState != null) {
			return rootView;
		}
					
		Bundle args = getArguments();


		if (args != null) {
			OficinasTurismo Oficina = (OficinasTurismo) args.getSerializable(OficinaDetailsActivity.OFICINA);

			this.setOficina(Oficina);

		}
		return rootView;
	}

	public void setOficina(OficinasTurismo oficina) {

		//ejemplo de imagen
		//"https://www.turismoasturias.es/documents/11022/84753/OficinaTurismo1.jpg";
		String RUTA_IMAGEN = "https://www.turismoasturias.es";
  		ImageView imagen = ((ImageView) getView().findViewById(R.id.imagen));
		URL url = null;
		String rutaCompleta = RUTA_IMAGEN + oficina.getImagen();

		TextView tvNombre = (TextView) getView().findViewById(R.id.nombre);
		tvNombre.setText(oficina.getNombre());
		TextView tvConcejo = (TextView) getView().findViewById(R.id.concejo);
		tvConcejo.setText(oficina.getConcejo());
		TextView tvTelefono = (TextView) getView().findViewById(R.id.telefono);
		tvTelefono.setText(oficina.getTelefono());
		TextView tvDireccion = (TextView) getView().findViewById(R.id.direccion);
		tvDireccion.setText(oficina.getDireccion());
		TextView tvFax = (TextView) getView().findViewById(R.id.fax);
		tvFax.setText(oficina.getFax());
		TextView tvEmail = (TextView) getView().findViewById(R.id.email);
		tvEmail.setText(oficina.getEmail());
		TextView tvWeb = (TextView) getView().findViewById(R.id.web);
		tvWeb.setText(oficina.getWeb());
		TextView tvWeb2 = (TextView) getView().findViewById(R.id.web2);
		tvWeb2.setText(oficina.getWeb2());
		TextView tvMasInfo = (TextView) getView().findViewById(R.id.masinfo);
		tvMasInfo.setText(oficina.getMasInformacion());
		TextView tvHorario = (TextView) getView().findViewById(R.id.horario);
		tvHorario.setText(oficina.getHorario());



	}
}
