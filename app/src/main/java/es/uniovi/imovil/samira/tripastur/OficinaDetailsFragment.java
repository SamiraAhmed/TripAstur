package es.uniovi.imovil.samira.tripastur;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.net.URL;

public class OficinaDetailsFragment extends Fragment {
	
	private View rootView;
	private Oficina oficina;


	public static OficinaDetailsFragment newInstance(Oficina oficina) {

		//Crear un fragmento y pasarle las oficinas
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


		rootView = inflater.inflate(R.layout.oficina_details_fragment, container, false);


		Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarDetalleFragment);


		FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							Intent intent = new Intent(getContext(), MapsActivity.class);
							intent.putExtra(OficinaDetailsActivity.OFICINA, OficinaDetailsFragment.this.oficina);
							startActivity(intent);

							}
		}

		);


		// Si estamos restaurando desde un estado previo no hacemos nada
		if (savedInstanceState != null) {

			return  rootView ;
		}

		//si hay oficinas las pasamos al metodo setoficina para poder instanciar los view del layout y mostrarlos en la vista
		Bundle args = getArguments();
		if (args != null) {
			Oficina Oficina = (Oficina) args.getSerializable(OficinaDetailsActivity.OFICINA);
			this.setOficina(Oficina);
		}
		return rootView;
	}

	//Metodo para enlazar los views del layout con la vista
	public void setOficina(Oficina oficina) {

		this.oficina=oficina;

		//ejemplo de imagen
		//"https://www.turismoasturias.es/documents/11022/84753/OficinaTurismo1.jpg";
		String RUTA_IMAGEN = "https://www.turismoasturias.es";
		URL url = null;
		String rutaCompleta = RUTA_IMAGEN + oficina.getImagen();
		ImageView imagen=((ImageView) rootView.findViewById(R.id.imagen));
		Glide.with(this).load(rutaCompleta).into(imagen);



		TextView tvNombre = (TextView) rootView.findViewById(R.id.nombre);
		tvNombre.setText(oficina.getNombre());

		TextView tvTituloUbicacion = (TextView) rootView.findViewById(R.id.tituloUbicacion);
		tvTituloUbicacion.getText();
		TextView tvUbicacion = (TextView) rootView.findViewById(R.id.ubicacion);
		tvUbicacion.setText("Concejo: "+oficina.getConcejo()+ "\n" + "Dirección: "+oficina.getDireccion());

		TextView tvTituloContacto = (TextView) rootView.findViewById(R.id.tituloContacto);
		tvTituloContacto.getText();
		TextView tvContacto = (TextView) rootView.findViewById(R.id.contacto);
		tvContacto.setText("Teléfono: "+ oficina.getTelefono()+ "\nFax: " + oficina.getFax());

		TextView tvTitulocontactoWeb = (TextView) rootView.findViewById(R.id.titulocontactoWeb);
		tvTitulocontactoWeb.getText();
		TextView tvContactoWeb = (TextView) rootView.findViewById(R.id.contactoWeb);
		tvContactoWeb.setText("Email: "+oficina.getEmail()+ "\nWeb: " + oficina.getWeb());
		TextView tvTituloHorario = (TextView) rootView.findViewById(R.id.tituloHorario);
		tvTituloHorario.getText();

		TextView tvHorarios= (TextView) rootView.findViewById(R.id.horario);
		tvHorarios.setText(oficina.getHorario() + "\n" + oficina.getMasInformacion());





	}



}