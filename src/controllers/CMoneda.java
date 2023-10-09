package controllers;

import core.Ex;
import core.JAbstractController;
import core.JAbstractModelBD;
import modelbd.Moneda;
import modelbd.SimpleModelo;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Ferz
 */
public class CMoneda extends JAbstractController implements Serializable{
    
    private Moneda predeterminado;
    @Override
    public ArrayList getRegistros() {
         return this.getRegistros(new String[]{},null,null);
    }
    public ArrayList<SimpleModelo> getRegistros(String[] columna,Object[] valor) {
        return getRegistros(new String[]{},columna,valor);
    }
    @Override
    public ArrayList getRegistros(Object[] cvl) {
        return this.getRegistros(new String[]{}, cvl!=null?new String[]{SimpleModelo.ACTIVO}:null,cvl);
    }
    
    @Override
    public JAbstractModelBD getRegistro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JAbstractModelBD getRegistro(JAbstractModelBD mdl, boolean opcion) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JAbstractModelBD buscarRegistro(Object cvl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean guardarRegistro(JAbstractModelBD mdl) {
        Moneda sm = (Moneda)mdl;
        int gravado;
        String campos = "nombre, simbolo, activo,isdefault";        
        Object[] valores = {sm.getNombre(),sm.getSimbolo(),sm.getActivo(),sm.getPredeterminado()};
       
         gravado = this.agregarRegistroPs(sm.getNombreTabla(), this.stringToArray(campos, ","), valores);
       
        return gravado==1;
    }

    @Override
    public int actualizarRegistro(JAbstractModelBD mdl) {
         Moneda sm = (Moneda)mdl;
        int gravado;
        String campos = "nombre, simbolo, activo,isdefault";        
        Object[] valores = {sm.getNombre(),sm.getSimbolo(),sm.getActivo(),sm.getPredeterminado(),sm.getPrimaryKey()};
        
        gravado = this.actualizarRegistroPs(sm.getNombreTabla(), this.adjuntarSimbolo(campos, ",", "?")+Ex.WHERE+mdl.getCampoClavePrimaria()+" = ? ", valores);
         System.out.println("grabadoo "+gravado); 
        return gravado;
    }
    
     public Moneda getRegistroPorPk(Integer id)
    {
        Moneda sm = null;
        try {            
            rs = this.selectPorPk(Moneda.TABLA, Moneda.PK_COLUMNA, id);
            
            while(rs.next())
            {
                 sm = new Moneda();
                 sm.setPrimaryKey(rs.getInt(1));                 
                 sm.setNombre(rs.getString(2));
                 sm.setSimbolo(rs.getString(3));
                 sm.setActivo(rs.getInt(4));
                 sm.setPredeterminado(rs.getInt(5));
                 if(sm.getPredeterminadoBool())
                 {
                     this.predeterminado = sm;
                 }
                 
            }
        } catch (SQLException ex) {
        }
        return sm;
    }

    @Override
    public ArrayList getRegistros(String[] campos, String[] columnaId, Object[] id) {
        ArrayList<Moneda> rgs = new ArrayList<>();        
        try {
            
            if(id != null)
                this.numRegistros = this.getNumeroRegistros(Moneda.TABLA, SimpleModelo.ACTIVO, SimpleModelo.ACTIVO, id);
            else
            {
               this.numRegistros = this.getNumeroRegistros(Moneda.TABLA, SimpleModelo.ACTIVO);               
               if(rs.isClosed())
                   System.out.println("resultset esta cerrado...");
            }
            rs = this.getRegistros(Moneda.TABLA,campos,columnaId,id);
            if(this.numRegistros<finalPag)
           {
              finalPag =  this.numRegistros;
           }
            if(finalPag>inicioPag)
            {
                inicioPag -= (finalPag-inicioPag)-1;
            }
            Moneda sm;
            while(rs.next())
            {
                 sm = new Moneda();
                 sm.setPrimaryKey(rs.getInt(1));                 
                 sm.setNombre(rs.getString(2));
                 sm.setSimbolo(rs.getString(3));
                 sm.setActivo(rs.getInt(4));
                 sm.setPredeterminado(rs.getInt(5));
                 if(sm.getPredeterminadoBool())
                 {
                     this.predeterminado = sm;
                 }
                 rgs.add(sm);
            }
        } catch (SQLException ex) {
        }
        return rgs;
    }

    public Moneda getPredeterminado() {
        return predeterminado;
    }
    
}
