package sec.project.repository;

import sec.project.domain.Signup;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface SignupRepositoryCustom {

    Signup customSave(Signup s) throws SQLException, NoSuchAlgorithmException;

    Signup findOne(String id) throws SQLException;
}
