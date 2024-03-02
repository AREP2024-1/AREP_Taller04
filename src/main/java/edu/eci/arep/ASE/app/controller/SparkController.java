package edu.eci.arep.ASE.app.controller;

import java.util.function.BiFunction;
import edu.eci.arep.ASE.app.model.Request;
import edu.eci.arep.ASE.app.model.Response;
import edu.eci.arep.ASE.app.model.Registry;


public class SparkController {
    private Registry registry = new Registry();

    /*
     * Registra un handler para el método GET en un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param handler Función que se ejecutará cuando se haga una petición GET al endPoint.
     */
    public void get(String endPoint, BiFunction<Request, Response, ?> handler){
        registry.get(endPoint, handler);
    }

    /*
     * Ejecuta el handler registrado para el método GET en un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param request Objeto request que contiene la información de la petición.
     * @param response Objeto response que contiene la información de la respuesta.
     * @return R El resultado de ejecutar el handler.
     */
    public <R> R doGet(String endPoint, Request request, Response response){
        return registry.doGet(endPoint, request, response);
    }

    /*
     * Registra un handler para el método POST en un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param handler Función que se ejecutará cuando se haga una petición POST al endPoint.
     */
    public void post(String endPoint, BiFunction<Request, Response, ?> handler){
        registry.post(endPoint, handler);
    }

    /*
     * Ejecuta el handler registrado para el método POST en un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param request Objeto request que contiene la información de la petición.
     * @param response Objeto response que contiene la información de la respuesta.
     * @return R El resultado de ejecutar el handler.
     */
    public <R> R doPost(String endPoint, Request request, Response response){
        return registry.doPost(endPoint, request, response);
    }

    /*
     * Valida si el endPoint especificado existe.
     * @param endPoint Ruta del endPoint.
     * @return boolean retorna True si el endPoint existe, de lo contrario retorna false.
     */
    public boolean hasEndPoint(String endPoint){
        return registry.hasEndPoint(endPoint);
    }
    
}
