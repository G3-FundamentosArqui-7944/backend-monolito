package pe.edu.upc.bodymatch.shared.interfaces.rest;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden
@Controller
public class RootRedirectController {

    @GetMapping("/")
    public String redirectRootToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/api")
    public String redirectApiToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
