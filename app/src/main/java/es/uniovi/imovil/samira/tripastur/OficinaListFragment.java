package es.uniovi.imovil.samira.tripastur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OficinaListFragment extends Fragment implements  AdapterView.OnItemClickListener{



	public interface Callbacks {
		public void onOficinaSelected(Oficina oficina);
	}

	public static ArrayList<Oficina> listaOficinas ;
	public static Map<Integer, Oficina> listaOficinasMap  = new HashMap<Integer, Oficina>();
	private OficinasTurismoAdapter mAdapter = null;
	private OficinasTurismoFavoritasAdapter fAdapter = null;
	private Callbacks mCallback = null;

	
	public static OficinaListFragment newInstance() {
		
		OficinaListFragment fragment = new OficinaListFragment();
		return fragment;
	}
	
	public OficinaListFragment() {
	}

	//guardar el estado de las listas para poder recuperarlo ante cambios
    @Override
    public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

        outState.putSerializable("lista", (Serializable) listaOficinas);
		outState.putSerializable("listamap", (Serializable) listaOficinasMap);

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

        //Inflar el layout con las tabs y las listas
		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.tabs, container, false);

		if (OficinaListFragment.listaOficinas==null){
			OficinaListFragment.listaOficinas= new ArrayList<Oficina>();
		}


		TabHost tabs=(TabHost) rootView.findViewById(android.R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec=tabs.newTabSpec(getString(R.string.tab_todas));
		spec.setContent(R.id.Oficinastab);
		spec.setIndicator(getString(R.string.tab_todas));
		tabs.addTab(spec);

		spec=tabs.newTabSpec(getString(R.string.tab_favoritas));
		spec.setContent(R.id.OficinasFavtab);
		spec.setIndicator(getString(R.string.tab_favoritas));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);

        //si restauramos desde un estado anterior

		if( savedInstanceState != null ){

			listaOficinas = (ArrayList<Oficina>)savedInstanceState.getSerializable("lista");
			listaOficinasMap = (HashMap<Integer,Oficina>)savedInstanceState.getSerializable("listamap");
			Log.d("d","oficinas instanciadas ");
			mAdapter=new OficinasTurismoAdapter(getContext(),listaOficinas);

			ListView lvOficinas = (ListView) rootView.findViewById(R.id.list_view_oficinas);
			lvOficinas.setAdapter(mAdapter);
			lvOficinas.setOnItemClickListener(this);

		}else {
            //si se crea

			mAdapter = new OficinasTurismoAdapter(OficinaListFragment.this.getContext());

			createOficinaList();


		}
            tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    if(tabId==getString(R.string.tab_todas)){
                        OficinaListFragment ofilistfrag = OficinaListFragment.this;
                        ofilistfrag.mAdapter=new OficinasTurismoAdapter(ofilistfrag.getContext(),OficinaListFragment.listaOficinas);
						MainActivity.searchView.setOnQueryTextListener(mAdapter);
                        ListView lv=(ListView) ofilistfrag.getView().findViewById(R.id.list_view_oficinas);
                        lv.setAdapter(mAdapter);

                    }

                    if(tabId==getString(R.string.tab_favoritas)){
                        OficinaListFragment ofilistfrag = OficinaListFragment.this;
                        ofilistfrag.fAdapter=new OficinasTurismoFavoritasAdapter(ofilistfrag.getContext(),OficinaListFragment.listaOficinas);
                        ListView lv=(ListView) ofilistfrag.getView().findViewById(R.id.list_favorites);
                        lv.setAdapter(fAdapter);
                        lv.setOnItemClickListener(OficinaListFragment.this);

                    }
                }
            });



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


	//Metodo para obtener los datos abiertos desde la pagina del principado
    //Utiliza volley para realizar la petición y obtener los datos
    //Luego realizo el parseo onteniendo los value object ya sean array o object
    //try/catch para poder obtener los datos dependiendo de las distintas estructuras posibles
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


                        Oficina Oficina = new Oficina();
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

						if (!listaOficinas.contains(Oficina)){
							listaOficinas.add(Oficina);
						}

						listaOficinasMap.put(new Integer(Oficina.getId()), Oficina);

					}
					Collections.sort(listaOficinas, new Comparator<Oficina>() {
						@Override
						public int compare(Oficina o1, Oficina o2) {
							return o1.getNombre().compareTo(o2.getNombre());
						}
					});

					// Configurar la lista
					ListView lvOficinas = (ListView) OficinaListFragment.this.getView().findViewById(R.id.list_view_oficinas);
					mAdapter.setOficinas(listaOficinas);
					lvOficinas.setAdapter(mAdapter);
					lvOficinas.setOnItemClickListener(OficinaListFragment.this);


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override

			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});

		RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonObjectRequest.setRetryPolicy(policy);
		mRequestQueue.add(jsonObjectRequest);

		//return listaOficinas;
	}

	//evento en el que se selecciona una lista para mostrar su descripción
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Oficina oficina = (Oficina) parent.getItemAtPosition(position);
		mCallback.onOficinaSelected(oficina);
	}

	//Metodos para acceder a las listas y al adpatador desde otras clases
	public OficinasTurismoAdapter getmAdapter() {
		return mAdapter;
	}

    public ArrayList<Oficina> getListaOficinas() {
        return listaOficinas;
    }

    public Map<Integer, Oficina> getListaOficinasMap() {
        return listaOficinasMap;
    }




}
