package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Controller
public class SignupController {

    private final SignupRepository signupRepository;

    @Autowired
    public SignupController(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    @RequestMapping("*")
    public String defaultMapping() {
        return "index";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model, @RequestParam(required = false) String redirect, @RequestHeader(name = "Referer", required = false) String referer) {
        if(redirect != null) {
        } else {
            redirect = referer;
        }
        model.addAttribute("redirect", redirect);
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(
            @RequestParam String name,
            @RequestParam String address,
            Model model,
            @RequestParam(required = false) String redirect
    ) throws SQLException, NoSuchAlgorithmException {

        Signup s = signupRepository.customSave(new Signup(name, address));
        model.addAttribute("signup", s);
        if(redirect != null) {
            model.addAttribute("redirect", redirect);
        }
        return "done";
    }

}
