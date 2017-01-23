package sec.project.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.security.MessageDigest;
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
            String password = getPassword();

            /*
            Signup secureSignup = new Signup();
            secureSignup.setAddress(address);
            secureSignup.setName(name);
            //secureSignup.setPassword(passwordEncoder.encode(getPassword()));
            secureSignup.setPassword(encodePassword(getPassword()));
            signupRepository.save(secureSignup);
            secureSignup.setPassword(password); //Need to send clear text password to the user
            model.addAttribute("signup", secureSignup);
            */

            //BEGIN insecure
            Signup s = new Signup(name, address);
            s.setPassword(encodePassword(password));
            s = signupRepository.customSave(s);
            s.setPassword(password); //Need to send clear text password to the user
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

    private String getPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String encodePassword(String password) throws NoSuchAlgorithmException {
        //Secure version:
        //return passwordEncoder.encode(password);

        //Insecure version
        MessageDigest highlySecureEncryptor = MessageDigest.getInstance("MD5");
        highlySecureEncryptor.update(password.getBytes());
        return new String(Hex.encode(highlySecureEncryptor.digest()));
    }

    private String cleanRedirect(String redirect) {
        if(redirect.matches("(/[a-z0-9]*)*"))
            return redirect;
        else
            return null;
    }
}
