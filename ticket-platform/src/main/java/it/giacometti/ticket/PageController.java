package it.giacometti.ticket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class PageController {

    @GetMapping("/pagina")
    public String mostraPagina(@RequestParam(name = "param", required = false, defaultValue = "Valore predefinito") String param, Model model) {
        model.addAttribute("param", param);
        return "admin/pagina";
    }
}