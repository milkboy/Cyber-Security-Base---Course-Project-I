package sec.project.repository;

import org.hibernate.internal.SessionImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sec.project.domain.Signup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupRepositoryImpl implements SignupRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public Signup customSave(Signup s) throws SQLException {
        /*
        Working implementation
         */
        //em.persist(s);

        /*
        Insecure implementation
         */

        Connection conn = em.unwrap(SessionImpl.class).connection();
        String query = "insert into Signup (name, address) values ('" + s.getName() + "', '" + s.getAddress() + "')";
        Statement statement = conn.createStatement();
        boolean res = statement.execute(query);
        return s;
    }

    @Override
    public Signup findOne(String id) throws SQLException {
        Connection conn = em.unwrap(SessionImpl.class).connection();
        String query = "select * from SIGNUP where id = '" + id + "'";
        Statement statement = conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        Signup s = null;
        if(res.next()) {
            s = new Signup(res.getLong(1), res.getString(2), res.getString(3));
        }
        return s;
    }
}
