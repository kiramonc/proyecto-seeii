package Pojo;
// Generated 19/11/2015 09:00:12 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Administrador generated by hbm2java
 */
public class Administrador  implements java.io.Serializable {


     private int idAdmin;
     private Usuario usuario;
     private Set unidadensenianzas = new HashSet(0);

    public Administrador() {
    }

	
    public Administrador(int idAdmin, Usuario usuario) {
        this.idAdmin = idAdmin;
        this.usuario = usuario;
    }
    public Administrador(int idAdmin, Usuario usuario, Set unidadensenianzas) {
       this.idAdmin = idAdmin;
       this.usuario = usuario;
       this.unidadensenianzas = unidadensenianzas;
    }
   
    public int getIdAdmin() {
        return this.idAdmin;
    }
    
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Set getUnidadensenianzas() {
        return this.unidadensenianzas;
    }
    
    public void setUnidadensenianzas(Set unidadensenianzas) {
        this.unidadensenianzas = unidadensenianzas;
    }




}


