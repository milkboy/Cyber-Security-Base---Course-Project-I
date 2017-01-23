package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.security.NoSuchAlgorithmException;

@Controller
public class SignupController {

    private final SignupRepository signupRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignupController(SignupRepository signupRepository, PasswordEncoder passwordEncoder) {
        this.signupRepository = signupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("*")
    public String defaultMapping() {
        return "index";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(
            Model model,
            @RequestParam(required = false) String redirect,
            @RequestHeader(name = "Referer", required = false) String referer) {
        if (redirect == null) {
            redirect = referer;
        }
        /* correct
           model.addAttribute("redirect", cleanRedirect(redirect));
        */
        //Insecure
        model.addAttribute("redirect", redirect);

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(
            @RequestParam String name,
            @RequestParam String address,
            Model model,
            @RequestParam(required = false) String redirect
    ) throws NoSuchAlgorithmException {

        try {
            /*
            Signup secureSignup = new Signup();
            secureSignup.setAddress(address);
            secureSignup.setName(name);
            secureSignup.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphabetic(10)));
            signupRepository.save(secureSignup);
            model.addAttribute("signup", secureSignup);
            */

            //BEGIN insecure
            Signup s = signupRepository.customSave(new Signup(name, address));
            model.addAttribute("signup", s);
            //END

            if (redirect != null) {
                /*
                 * correct code
                model.addAttribute("redirect", cleanRedirect(redirect));
                */
                //BEGIN insecure
                model.addAttribute("redirect", redirect);
                //END
            }
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "done";
        }
        return "done";
    }

    private String cleanRedirect(String redirect) {
        if(redirect.matches("(/[a-z0-9]*)*"))
            return redirect;
        else
            return null;
    }
}
