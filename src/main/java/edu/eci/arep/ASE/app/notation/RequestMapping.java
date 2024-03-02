package edu.eci.arep.ASE.app.notation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edu.eci.arep.ASE.app.model.MetodoHttp;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String path();
    MetodoHttp metodoHttp(); 
}
