package sec.project.repository;

import org.hibernate.internal.SessionImpl;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sec.project.domain.Signup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.MessageDigest;
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

        MessageDigest highlySecureEncryptor = MessageDigest.getInstance("MD5");
        Connection conn = em.unwrap(SessionImpl.class).connection();
        String insecurePassword = "fixme";
        highlySecureEncryptor.update(insecurePassword.getBytes());
        String securePassword = new String(Hex.encode(highlySecureEncryptor.digest()));
        String query = "insert into Signup (name, address, password) values ('" + s.getName() + "', '" + s.getAddress()
                + "', '" + securePassword + "')";
        Statement statement = conn.createStatement();
        boolean success = statement.execute(query);
        ResultSet res = statement.executeQuery("call IDENTITY()");
        if(res.next()) {
            s.setId(res.getLong(1));
        }
        s.setPassword(insecurePassword);
        return s;

    }

    @Override
    public Signup findOne(String id) throws SQLException {
        Connection conn = em.unwrap(SessionImpl.class).connection();
        String query = "select id,name,address,password from SIGNUP where id = '" + id + "'";
        Statement statement = conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        Signup s = null;
        if(res.next()) {
            s = new Signup(res.getLong(1), res.getString(2), res.getString(3));
            s.setPassword(res.getString(4));
        }
        return s;
    }
}
