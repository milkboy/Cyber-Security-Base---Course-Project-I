package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class EventController {

    private final SignupRepository signupRepository;

    @Autowired
    public EventController(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    @RequestMapping("/event/{id}/{password}")
    public String defaultMapping(@PathVariable Long id, @PathVariable String password, Model model) {
        Signup res = signupRepository.findOne(id);
        model.addAttribute("signup", res);
        model.addAttribute("password", password);
        return "event";
    }


}
