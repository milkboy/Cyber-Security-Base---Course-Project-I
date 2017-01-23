package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.security.NoSuchAlgorithmException;

@Controller
public class EventController {

    private final SignupRepository signupRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EventController(SignupRepository signupRepository, PasswordEncoder passwordEncoder) {
        this.signupRepository = signupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/event/{id}/{password}")
    public String defaultMapping(@PathVariable Long id, @PathVariable String password, Model model) throws NoSuchAlgorithmException {
        Signup res = signupRepository.findOne(id);
        //Verify password here!
        //Bcrypt
        if(res == null || !passwordEncoder.matches(password, res.getPassword())) {
            return "redirect:/";
        }

        model.addAttribute("signup", res);
        model.addAttribute("password", password);
        return "event";
    }
}
