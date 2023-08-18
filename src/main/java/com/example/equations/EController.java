package com.example.equations;

import org.hibernate.dialect.Sybase11Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
class EController {
    @Autowired
    EquationService equationService;
    @Autowired
    RootService rootService;

    @GetMapping("/equations")
    public String getEquations(Model model){
        model.addAttribute("equations", equationService.findAll());
        return "equations";
    }

    @PostMapping("/equations")
    public String getSearched(Model model,
                              @RequestParam double number){
        model.addAttribute("equations", equationService.findEquationsByRoot(number));
        return "equations";
    }

    @GetMapping("/input")
    public String inputForm() {
        return "submit_equation";
    }

    @PostMapping("/process")
    public String processInput(@RequestParam("equation") String equation, Model model) {
        if (EquationService.isValidFullEquation(equation)){
            equationService.save(new Equation(equation));
        }
        return "redirect:/input";
    }

    @GetMapping("/check_root/{id}")
    public String getCheck(@PathVariable long id,
                        Model model) {
        model.addAttribute("equation", equationService.findById(id));
        return "check_root";
    }
    @PostMapping("/check_root/{id}")
    public String postCheck(@PathVariable long id,
                            @RequestParam double number) {
        Equation equation = equationService.findById(id);

        if (EquationService.checkRoot(equation.getEquationString(), number)){
            rootService.save(new Root(number, equation));
        }
        return "redirect:/check_root/" + id;
    }
}
