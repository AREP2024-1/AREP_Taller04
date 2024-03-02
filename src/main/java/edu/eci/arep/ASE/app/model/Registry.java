package edu.eci.arep.ASE.app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import edu.eci.arep.ASE.app.http.HTTPMetodo;
import edu.eci.arep.ASE.app.http.exception.HTTPException;
import edu.eci.arep.ASE.app.model.Request;
import edu.eci.arep.ASE.app.model.Response;

public class Registry {

    private Map<String, Collection <HTTPMetodo>> registers;

    public Registry() {
        this.registers = new HashMap<>();
    }

    /*
     * Registra un handler para el método HTTP GET con un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param handler Función que se ejecutará cuando se haga una petición GET al endPoint.
     * @throws HTTPException Si el endPoint no es valido.
     */
    public <R> void get(String endPoint, BiFunction<Request, Response, R> handler){
        if (!validateEndPoint(endPoint, true)){
            throw new HTTPException();
        }

        if (!registers.containsKey(endPoint)) {
            registers.put(endPoint, new ArrayList<>());
            
        }
        registers.get(endPoint).add(new HTTPMetodo("GET", handler));
    }

    /*
     * Ejecuta el handler registrado para el método HTTP GET con un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param request Objeto request que contiene la información de la petición.
     * @param response Objeto response que contiene la información de la respuesta.
     * @throws HTTPException Si el endPoint no es valido.
     * @return R El resultado de ejecutar el handler.
     */
    public <R> R doGet(String endPoint, Request request, Response response){
        String endPointFinal = findEndPoint(endPoint);
        if (endPointFinal.equals("")){
            throw new HTTPException();
        }
        HTTPMetodo funcionMetodo = registers.get(endPointFinal).stream()
            .filter((httpMetodo) -> httpMetodo.validateMetodoHttp("GET"))
            .findFirst()
            .orElseThrow(HTTPException::new);
        return funcionMetodo.ejecutar(request, response);
    } 

    /*
     * Registra un handler para el método HTTP POST con un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param handler Función que se ejecutará cuando se haga una petición POST al endPoint.
     * @throws HTTPException Si el endPoint no es valido.
     */
    public <R> void post(String endPoint, BiFunction<Request, Response, R> handler){
        if (!validateEndPoint(endPoint, true)){
            throw new HTTPException();
        }

        if (!registers.containsKey(endPoint)) {
            registers.put(endPoint, new ArrayList<>());
            
        }
        registers.get(endPoint).add(new HTTPMetodo("POST", handler));
    }

    /*
     * Ejecuta el handler registrado para el método HTTP POST con un endPoint especifico.
     * @param endPoint Ruta del endPoint.
     * @param request Objeto request que contiene la información de la petición.
     * @param response Objeto response que contiene la información de la respuesta.
     * @throws HTTPException Si el endPoint no es valido.
     * @return R El resultado de ejecutar el handler.
     */
    public <R> R doPost(String endPoint, Request request, Response response){
        String endPointFinal = findEndPoint(endPoint);
        if (endPointFinal.equals("")){
            throw new HTTPException();
        }
        HTTPMetodo funcionMetodo = registers.get(endPointFinal).stream()
            .filter((httpMetodo) -> httpMetodo.validateMetodoHttp("POST"))
            .findFirst()
            .orElseThrow(HTTPException::new);
        return funcionMetodo.ejecutar(request, response);
    } 

    /*
     * Busca el endPoint especifico que coincida con el endPoint dado.
     * @param endPoint Ruta del endPoint.
     * @return String El endPoint especifico que coincide con el endPoint dado.
    */
    private String findEndPoint (String endPoint){
        if (!validateEndPoint(endPoint, false)) {
            throw new HTTPException();
        }

        String [] result = new String[0];
        String [] compareEndPoint = endPoint.substring(1).split("/");        
        String finalResult = "";
        for (String key : registers.keySet()) {
            String [] compareKey = key.substring(1).split("/");
            if(compareEndPoint.length != compareKey.length){
                continue;
            }

            boolean isEqual = true;
            for (int i = 0; i < compareEndPoint.length; i++) {

                if(!compareKey[i].startsWith(":") && !compareEndPoint[i].equals(compareKey[i])) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual && compareKey.length > result.length) {
                result = compareKey;
                finalResult = key;                
            }

        }
        return finalResult;
    }

    /*
     * Valida si el endPoint especificado es valido.
     * @param endPoint Ruta del endPoint.
     * @param isRegistry Indica si el endPoint es se debe registrar o si se debe buscar.
     * @return boolean retorna True si el endPoint es valido, de lo contrario retona False.
     */
    public boolean validateEndPoint(String endPoint, boolean isRegistry){
        if (!endPoint.startsWith("/")) {
            return false;            
        } else if (endPoint.equals("/")) {
            return true;
        }

        Set<Character> characters = Set.of(
            '*', '+', '^', '$', '#', ' '
        );

        return Arrays.asList(endPoint.substring(1).split("/")).parallelStream()
            .allMatch((seccion)->
                seccion != null && 
                !(seccion.equals("")) && 
                characters.stream().noneMatch((c)-> seccion.indexOf(c) != -1)&&
                (isRegistry || !seccion.startsWith(":"))
            );
    }

    /*
     * Valida si el endPoint especificado esta registrado.
     * @param endPoint Ruta del endPoint.
     * @return boolean retorna True si el endPoint esta registrado, de lo contrario retona False.
     */
    public boolean hasEndPoint(String endPoint){
        String resultFind = this.findEndPoint(endPoint); 
        return resultFind != null && !resultFind.equals("");
    }
    
}
