package es.uniovi.imovil.jcgranda.courses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OficinaListFragment extends Fragment implements AdapterView.OnItemClickListener {

	public interface Callbacks {
		public void onOficinaSelected(OficinasTurismo oficina);
	}
	private List<OficinasTurismo> listaOficinas = new ArrayList<OficinasTurismo>();
	private Map<Integer, OficinasTurismo> listaOficinasMap  = new HashMap<Integer, OficinasTurismo>();
	private OficinasTurismoAdapter mAdapter = null;
	private Callbacks mCallback = null;

	
	public static OficinaListFragment newInstance() {
		
		OficinaListFragment fragment = new OficinaListFragment();
		return fragment;
	}
	
	public OficinaListFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		
        super.onAttach(activity);
        try {
        	mCallback = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callbacks");
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView;
		rootView = inflater.inflate(R.layout.oficina_list_fragment, container, false);

		Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
		toolbar.setTitle((getActivity()).getTitle());

		createOficinaList();

		FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   Intent intent = new Intent(getActivity(), MapsActivity.class);
									   intent.putExtra("listaOficinasMap", (Serializable) listaOficinasMap);
									   intent.putExtra("listaOficinas", (Serializable) listaOficinas);
									   startActivity(intent);
								   }
							   }

		);


		return rootView;
	}

	private void createOficinaList() {
		String URL =
				"https://www.turismoasturias.es/open-data/catalogo-de-datos?p_p_id=opendata_WAR_importportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_resource_id=exportJson&p_p_cacheability=cacheLevelPage&p_p_col_id=column-1&p_p_col_count=1&_opendata_WAR_importportlet_structure=27532&_opendata_WAR_importportlet_robots=nofollow";
		final String CASO_VACIO = "{\"language-id\":\"es_ES\"}";


		RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject articles = response.getJSONObject("articles");
					JSONArray article = articles.getJSONArray("article");

					for (int i=0; i<article.length(); i++) {
						JSONObject oficina = article.getJSONObject(i);
						JSONArray arrayParametrosIniciales = oficina.getJSONArray("dynamic-element");


                        OficinasTurismo Oficina = new OficinasTurismo();
						Oficina.setId(String.valueOf(i));

						for (int j=0; j<4; j++) {   //arrayParametrosIniciales.length() en el 4 casca, sólo hace falta hasta el 3
							JSONObject obInicial = arrayParametrosIniciales.getJSONObject(j);
							JSONObject elementoValores;
							switch (j) {
								case 0:
									try {
										JSONObject joInterior = obInicial.getJSONObject("dynamic-content");
										Oficina.setNombre(joInterior.getString("content"));
									} catch (Exception e) {
										Oficina.setNombre(obInicial.getString("dynamic-content"));
									}
									break;
								case 1:
									JSONArray arrayParametros = obInicial.getJSONArray("dynamic-element");
									for (int k=0; k<arrayParametros.length(); k++) {
										elementoValores = arrayParametros.getJSONObject(k);
										switch (k) {
                                            /*
                                                A veces está directamente en el 'dynamic-content' y otras veces es un valor del objeto.
                                                Por eso el try-catch
                                             */
											case 0:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													Oficina.setConcejo(joInterior.getString("content"));
												} catch (Exception e) {
													Oficina.setConcejo(elementoValores.getString("dynamic-content"));
												}
												break;
											case 1:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													Oficina.setDireccion(joInterior.getString("content"));
												} catch (Exception e) {
													Oficina.setDireccion(elementoValores.getString("dynamic-content"));
												}
												break;
											case 2:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													Oficina.setTelefono(joInterior.getString("content"));
												} catch (Exception e) {
													Oficina.setTelefono(elementoValores.getString("dynamic-content"));
												}
												break;
											case 3:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													try {
														Oficina.setFax(joInterior.getString("content"));
													} catch (Exception e) {
														Oficina.setFax("");
													}
												} catch (Exception e) {
													Oficina.setFax(elementoValores.getString("dynamic-content"));
												}
												break;
											case 4:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													Oficina.setEmail(joInterior.getString("content"));
												} catch (Exception e) {
													Oficina.setEmail(elementoValores.getString("dynamic-content"));
												}
												break;
											case 5:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													Oficina.setWeb(joInterior.getString("content"));
												} catch (Exception e) {
													Oficina.setWeb(elementoValores.getString("dynamic-content"));
												}
												break;
											case 6:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													String horario1 = joInterior.getString("content");
													horario1 = horario1.replaceAll("<p>", "");
													horario1 = horario1.replaceAll("</p>", "");
													horario1 = horario1.replaceAll("<br />", "");
													horario1 = horario1.replaceAll("</strong>", "");
													horario1 = horario1.replaceAll("<strong>", "");
													horario1 = horario1.replaceAll("&nbsp;", "");
													horario1 = horario1.replaceAll("Horarios:","");
													horario1 = horario1.replaceAll("Horario:","");
													Oficina.setHorario(horario1);
												} catch (Exception e) {
													String horario2 = elementoValores.getString("dynamic-content");
													if (horario2.equals(CASO_VACIO)) {
														Oficina.setHorario("");
													} else {
														horario2 = horario2.replaceAll("<p>", "");
														horario2 = horario2.replaceAll("</p>", "");
														horario2 = horario2.replaceAll("<br />", "");
														horario2 = horario2.replaceAll("</strong>", "");
														horario2 = horario2.replaceAll("&nbsp;", "");
														horario2 = horario2.replaceAll("<strong>", "");
														horario2 = horario2.replaceAll("Horarios:","");
														horario2 = horario2.replaceAll("Horario:","");
														Oficina.setHorario(horario2);
													}
												}
												break;
											case 7:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													String masInfo1=joInterior.getString("content");
													masInfo1 = masInfo1.replaceAll("<p>", "");
													masInfo1 = masInfo1.replaceAll("</p>", "");
													masInfo1 = masInfo1.replaceAll("<br />", "");
													masInfo1 = masInfo1.replaceAll("</strong>", "");
													masInfo1 = masInfo1.replaceAll("<strong>", "");
													masInfo1 = masInfo1.replaceAll("&nbsp;", "");
													Oficina.setMasInformacion(masInfo1);
												} catch (Exception e) {
													String masInfo2 = elementoValores.getString("dynamic-content");

													//caso especial de 'Navia', que aunque parece que tiene 'dynamic-content' no tiene nada
													if (masInfo2.equals(CASO_VACIO)) {
														Oficina.setMasInformacion("");
													} else {
														masInfo2 = masInfo2.replaceAll("<p>", "");
														masInfo2 = masInfo2.replaceAll("</p>", "");
														masInfo2 = masInfo2.replaceAll("<br />", "");
														masInfo2 = masInfo2.replaceAll("</strong>", "");
														masInfo2 = masInfo2.replaceAll("&nbsp;", "");
														masInfo2 = masInfo2.replaceAll("<strong>", "");
														Oficina.setMasInformacion(masInfo2);
													}
												}
												break;
											case 9:
												try {
													JSONObject joInterior = elementoValores.getJSONObject("dynamic-content");
													String web2 =joInterior.getString("content");
													if (!web2.equals(CASO_VACIO) ){
														Oficina.setWeb2(web2);
													}
													Oficina.setWeb2("");
												} catch (Exception e) {
													String web2b = elementoValores.getString("dynamic-content");
													if (!web2b.equals(CASO_VACIO)){
														Oficina.setWeb2(web2b);
													}
													Oficina.setWeb2("");
												}
												break;
										}
									}
									break;
								case 2:
									JSONObject joCoordenadas = obInicial.getJSONObject("dynamic-element");
									String coordenadasString;
									try {
										JSONObject joInterior = joCoordenadas.getJSONObject("dynamic-content");
										coordenadasString = joInterior.getString("content");
									} catch (Exception e) {
										coordenadasString = joCoordenadas.getString("dynamic-content");
									}
									String[] parLineas = coordenadasString.split(",");
									Oficina.setMapaX(Float.valueOf(parLineas[0]));
									Oficina.setMapaY(Float.valueOf(parLineas[1]));
									break;

								case 3:
									JSONObject joImagen = obInicial.getJSONObject("dynamic-element");
									try {
										JSONObject joInterior = joImagen.getJSONObject("dynamic-content");
										Oficina.setImagen(joInterior.getString("content"));
									} catch (Exception e) {
										Oficina.setImagen(joImagen.getString("dynamic-content"));
									}
									break;
							}
						}

						listaOficinas.add(Oficina);
						listaOficinasMap.put(new Integer(Oficina.getId()), Oficina);
						Log.v(this.getClass().getName(), Oficina.toString() );

					}
					// Configurar la lista
					ListView lvOficinas = (ListView) OficinaListFragment.this.getView().findViewById(R.id.list_view_oficinas);

					mAdapter = new OficinasTurismoAdapter(OficinaListFragment.this.getContext(), listaOficinas);
					lvOficinas.setAdapter(mAdapter);
					lvOficinas.setOnItemClickListener(OficinaListFragment.this);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {}
		});
		mRequestQueue.add(jsonObjectRequest);

		//return listaOficinas;
	}


	


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		OficinasTurismo oficina = (OficinasTurismo) parent.getItemAtPosition(position);
		mCallback.onOficinaSelected(oficina);
	}
	
}
