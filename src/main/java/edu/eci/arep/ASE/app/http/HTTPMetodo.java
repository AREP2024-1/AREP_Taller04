package edu.eci.arep.ASE.app.http;

import java.util.function.BiFunction;

import edu.eci.arep.ASE.app.model.Request;
import edu.eci.arep.ASE.app.model.Response;

public class HTTPMetodo {

    private String metodo;
    private BiFunction<Request, Response, ?> handler;

    /*
     * Constructor de la clase HTTPMetodo.
     * @param metodo Método HTTP que se va a manejar en la peticion.
     * @param handler Función que se ejecutará cuando se haga una petición al método HTTP.
     */
    public HTTPMetodo(String metodo, BiFunction<Request, Response, ?> handler) {
        this.metodo = metodo;
        this.handler = handler;
    }

    /*
     * Ejecuta el handler registrado para el método HTTP.
     * @param request Objeto request que contiene la información de la petición.
     * @param response Objeto response que contiene la información de la respuesta.
     * @return R El resultado de ejecutar el handler.
     */
    public <R> R ejecutar(Request request, Response response) {
        return (R) handler.apply(request, response);
    }

    /*
     * Valida si el método HTTP asociado es igual al metodo HTTP especificado.
     * @param metodoHttp Método HTTP que se va a comparar.
     * @return boolean retorna True si el método HTTP es igual al especificado, de lo contrario retorna false.
     */
    public boolean validateMetodoHttp(String metodoHttp) {
        return this.metodo.equals(metodoHttp);
    }
    
}
