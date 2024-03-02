package edu.eci.arep.ASE.app.controller;

import edu.eci.arep.ASE.app.model.MetodoHttp;
import edu.eci.arep.ASE.app.notation.Controller;
import edu.eci.arep.ASE.app.notation.RequestMapping;

@Controller
public class Controlador {

    @RequestMapping(path = "/test", metodoHttp = MetodoHttp.GET)
    public String getMapping(){
        return "Peticion GET!";
    }

    @RequestMapping(path = "/testPost", metodoHttp = MetodoHttp.POST)
    public String postMapping(){
        return "Peticion POST!";
    }
    
    
}
