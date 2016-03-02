package gestion;

import hibernate.HibernateUtil;
import hibernate.Usuario;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

public class GestorUsuario {
    
    public static JSONObject getLogin(String usuario, String pass){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        
        String hql = "from Usuario where login=:log and pass=:pass";
        Query q = sesion.createQuery(hql);
        q.setString("log", usuario);
        q.setString("pass", pass);
        List<Usuario> usuarios = q.list();
        
        JSONObject obj = new JSONObject();
        if(usuarios.isEmpty()){
            obj.put("r", false);
        } else{
            obj.put("r", true);
        }
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        return obj;
    }
    
    public static Usuario getUserbyName(String login){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        Usuario u= (Usuario) sesion.get(Usuario.class, login);
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        return u;
    }
}
