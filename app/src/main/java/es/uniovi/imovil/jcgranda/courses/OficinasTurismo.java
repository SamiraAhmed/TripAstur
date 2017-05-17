package es.uniovi.imovil.jcgranda.courses;

import java.io.Serializable;

/**
 * Created by pioli on 13/05/2017.
 */

public class OficinasTurismo implements Serializable {

    private String id;
    private String nombre;
    private String concejo;
    private String direccion;
    private String telefono;
    private String fax;
    private String email;
    private String web;
    private String web2;
    private String masInformacion;
    private String imagen;
    private String horario;
    private float mapaX;
    private float mapaY;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getMapaX() {
        return mapaX;
    }

    public void setMapaX(float mapaX) {
        this.mapaX = mapaX;
    }

    public float getMapaY() {
        return mapaY;
    }

    public void setMapaY(float mapaY) {
        this.mapaY = mapaY;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public String getWeb2() {
        return web2;
    }

    public void setWeb2(String web2) {
        this.web2 = web2;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getConcejo() {
        return concejo;
    }

    public void setConcejo(String concejo) {
        this.concejo = concejo;
    }

    public String getMasInformacion() {
        return masInformacion;
    }

    public void setMasInformacion(String masInformacion) {
        this.masInformacion = masInformacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return getConcejo();
    }
}
