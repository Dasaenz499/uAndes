package co.edu.uniandes.fuse.api.consulta.entidad.empleado.models.respuesta;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Modelo de Entidad Empleado")
public class EntidadEmpleado {

    @JsonProperty
    private String segundo_apellido;
    @JsonProperty
    private String fecha_fin_contrato;
    @JsonProperty
    private String documento;
    @JsonProperty
    private String primer_apellido;
    @JsonProperty
    private String login;
    @JsonProperty
    private String area_personal;
    @JsonProperty
    private String nombre_centro_costo;
    @JsonProperty
    private String estado_laboral;
    @JsonProperty
    private String descripcion_area_personal;
    @JsonProperty
    private String dependencia;
    @JsonProperty
    private String primer_nombre;
    @JsonProperty
    private String posicion;
    @JsonProperty
    private String descripcion_funcion;
    @JsonProperty
    private String centro_costo;
    @JsonProperty
    private String tipo_documento;
    @JsonProperty
    private String fecha_inicio_contrato;
    @JsonProperty
    private String descripcion_posicion;
    @JsonProperty
    private String funcion;
    @JsonProperty
    private String tipo_contrato;
    @JsonProperty
    private String segundo_nombre;
    @JsonProperty
    private String fecha_ingreso;
    @JsonProperty
    private String tipo_nomina;
    @JsonProperty
    private String descripcion_dependencia;
    @JsonProperty
    private String tipo_sueldo;

    @ApiModelProperty(value = "Usuario", required = true, example = "usuario@uniandes.edu.co")
    public String getSegundo_apellido() {
        return segundo_apellido;
    }

    public void setSegundo_apellido(String segundo_apellido) {
        this.segundo_apellido = segundo_apellido;
    }

    public String getFecha_fin_contrato() {
        return fecha_fin_contrato;
    }

    public void setFecha_fin_contrato(String fecha_fin_contrato) {
        this.fecha_fin_contrato = fecha_fin_contrato;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPrimer_apellido() {
        return primer_apellido;
    }

    public void setPrimer_apellido(String primer_apellido) {
        this.primer_apellido = primer_apellido;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getArea_personal() {
        return area_personal;
    }

    public void setArea_personal(String area_personal) {
        this.area_personal = area_personal;
    }

    public String getNombre_centro_costo() {
        return nombre_centro_costo;
    }

    public void setNombre_centro_costo(String nombre_centro_costo) {
        this.nombre_centro_costo = nombre_centro_costo;
    }

    public String getEstado_laboral() {
        return estado_laboral;
    }

    public void setEstado_laboral(String estado_laboral) {
        this.estado_laboral = estado_laboral;
    }

    public String getDescripcion_area_personal() {
        return descripcion_area_personal;
    }

    public void setDescripcion_area_personal(String descripcion_area_personal) {
        this.descripcion_area_personal = descripcion_area_personal;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public String getPrimer_nombre() {
        return primer_nombre;
    }

    public void setPrimer_nombre(String primer_nombre) {
        this.primer_nombre = primer_nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getDescripcion_funcion() {
        return descripcion_funcion;
    }

    public void setDescripcion_funcion(String descripcion_funcion) {
        this.descripcion_funcion = descripcion_funcion;
    }

    public String getCentro_costo() {
        return centro_costo;
    }

    public void setCentro_costo(String centro_costo) {
        this.centro_costo = centro_costo;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getFecha_inicio_contrato() {
        return fecha_inicio_contrato;
    }

    public void setFecha_inicio_contrato(String fecha_inicio_contrato) {
        this.fecha_inicio_contrato = fecha_inicio_contrato;
    }

    public String getDescripcion_posicion() {
        return descripcion_posicion;
    }

    public void setDescripcion_posicion(String descripcion_posicion) {
        this.descripcion_posicion = descripcion_posicion;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getTipo_contrato() {
        return tipo_contrato;
    }

    public void setTipo_contrato(String tipo_contrato) {
        this.tipo_contrato = tipo_contrato;
    }

    public String getSegundo_nombre() {
        return segundo_nombre;
    }

    public void setSegundo_nombre(String segundo_nombre) {
        this.segundo_nombre = segundo_nombre;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getTipo_nomina() {
        return tipo_nomina;
    }

    public void setTipo_nomina(String tipo_nomina) {
        this.tipo_nomina = tipo_nomina;
    }

    public String getDescripcion_dependencia() {
        return descripcion_dependencia;
    }

    public void setDescripcion_dependencia(String descripcion_dependencia) {
        this.descripcion_dependencia = descripcion_dependencia;
    }

    public String getTipo_sueldo() {
        return tipo_sueldo;
    }

    public void setTipo_sueldo(String tipo_sueldo) {
        this.tipo_sueldo = tipo_sueldo;
    }

}
