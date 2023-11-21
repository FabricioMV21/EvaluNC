package fmv.fabricio.evalunc;

public class Paciente {
    private String rut;
    private String nombre;
    private String telefono;
    private String alergia;
    private String estado;

    public Paciente(){

    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getAlergia() {
        return alergia;
    }

    public void setAlergia(String alergia) {
        this.alergia = alergia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Paciente: " +
                "RUT= " + rut +
                ", NOMBRE= " + nombre +
                ", TELEFONO= " + telefono +
                ", ALERGIA= " + alergia +
                ", ESTADO= " + estado;
    }
}
