package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Configuration
public class MiscConfig {
    private final
    SignupRepository signupRepository;

    @Autowired
    public MiscConfig(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
        Signup demoSignup = new Signup("John Doe", "Sample address");
        demoSignup.setPassword("d3b07384d113edec49eaa6238ad5ff00");
        this.signupRepository.saveAndFlush(demoSignup);
    }

}
