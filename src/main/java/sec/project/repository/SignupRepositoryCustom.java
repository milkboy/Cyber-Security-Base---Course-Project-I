package sec.project.repository;

import sec.project.domain.Signup;

import java.sql.SQLException;

public interface SignupRepositoryCustom {

    Signup customSave(Signup s) throws SQLException;

    Signup findOne(String id) throws SQLException;
}
