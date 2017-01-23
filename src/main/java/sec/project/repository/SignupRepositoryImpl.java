package sec.project.repository;

import org.hibernate.internal.SessionImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sec.project.domain.Signup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupRepositoryImpl implements SignupRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public Signup customSave(Signup s) throws SQLException, NoSuchAlgorithmException {
        /*
        Working implementation
         */
        //em.persist(s);
        //return s;

        /*
        Insecure implementation
         */

        Connection conn = em.unwrap(SessionImpl.class).connection();
        String query = "insert into Signup (password, name, address) values ('" + s.getPassword()
                + "', '" + s.getName() + "', '" + s.getAddress() +"')";
        Statement statement = conn.createStatement();
        boolean success = statement.execute(query);
        ResultSet res = statement.executeQuery("call IDENTITY()");
        if(res.next()) {
            s.setId(res.getLong(1));
        }
        return s;

    }

    @Override
    public Signup findOne(String id) throws SQLException {
        try {
            Connection conn = em.unwrap(SessionImpl.class).connection();
            String query = "select id,name,address,password from SIGNUP where id = '" + id + "'";
            Statement statement = conn.createStatement();
            Boolean success = statement.execute(query);
            ResultSet res = statement.getResultSet();
            Signup s = null;
            if (res.next()) {
                s = new Signup(res.getLong("id"), res.getString("name"), res.getString("address"));
                s.setPassword(res.getString("password"));
            }
            return s;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
