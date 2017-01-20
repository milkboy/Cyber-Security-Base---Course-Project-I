package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.sql.SQLException;

@Controller()
public class AdminController {

    private final SignupRepository signupRepository;

    @Autowired
    public AdminController(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.GET)
    public String listSignup(Model model, @PathVariable String id) throws SQLException {
        Signup s = signupRepository.findOne(id);
        model.addAttribute("s", s);
        return "single";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String listSignups(Model model, @RequestParam(required = false) String id) throws SQLException {
        if(id != null) {
            Signup s = signupRepository.findOne(id);
            if(s != null) {
                model.addAttribute("s", s);
                return "single";
            }
        }
        model.addAttribute("ppl", signupRepository.findAll());
        return "list";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        //signupRepository.save(new Signup(name, address));
        return "list";
    }

}
