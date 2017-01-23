package sec.project.controller;

import org.apache.commons.lang3.RandomStringUtils;
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
        model.addAttribute("redirect", cleanRedirect(redirect));

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
            String password = getPassword();

            Signup secureSignup = new Signup();
            secureSignup.setAddress(address);
            secureSignup.setName(name);
            secureSignup.setPassword(encodePassword(password));
            signupRepository.save(secureSignup);
            secureSignup.setPassword(password); //Need to send clear text password to the user
            model.addAttribute("signup", secureSignup);

            if (redirect != null) {
                model.addAttribute("redirect", cleanRedirect(redirect));
            }
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "done";
        }
        return "done";
    }

    private String getPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String encodePassword(String password) throws NoSuchAlgorithmException {
        //Secure version:
        return passwordEncoder.encode(password);
    }

    private String cleanRedirect(String redirect) {
        if(redirect.matches("(/[a-z0-9]*)*"))
            return redirect;
        else
            return null;
    }
}
