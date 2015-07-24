/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Test;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceTest {
    
    public boolean registrar(Session session, Test test) throws Exception;
    public List<Test> verTodo();
    public List<Test> verTodo(Session session) throws Exception;
    public Test verPorTema(Session session, int tema) throws Exception;
    public Test verPorCodigoTest(Session session, int idTest) throws Exception;
    public boolean actualizar(Session session,Test test) throws Exception;
    public boolean eliminar(Session session,Test test)throws Exception;
    
    
}
