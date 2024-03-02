package edu.eci.arep.ASE.app.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

import edu.eci.arep.ASE.app.model.Registry;
import edu.eci.arep.ASE.app.model.Request;
import edu.eci.arep.ASE.app.model.Response;
import edu.eci.arep.ASE.app.notation.Controller;
import edu.eci.arep.ASE.app.notation.RequestMapping;

public class Scanner {

    private Registry registro;

    /*
     * Inicializa el escaneo de los directorios en la ruta especificada.
     * @param controller la ruta del controlador para iniciar el escaneo.
     * @throws FileNotFoundException si no se encuentra el archivo.
     * @throws ClassNotFoundException si no se encuentra la clase.
     */
    public Scanner(String controller) throws FileNotFoundException, ClassNotFoundException{
        String rutaEjecucion = System.getProperty("user.dir");
        String rutaPartida = rutaEjecucion + "/target/classes/edu";

        this.registro = new Registry();

        try {
            searchDirectories(new File(rutaPartida));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*
     * Inicializa el escaneo de los directorios sin especificar un controlador.
     * @throws FileNotFoundException si no se encuentra el archivo.
     * @throws ClassNotFoundException si no se encuentra la clase.
     */
    public Scanner() throws FileNotFoundException, ClassNotFoundException{
        this("");        
    }

    public Registry getRegistry(){
        return registro;
    }

    private void searchDirectories(File archivoRaiz) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException{
        
        if (archivoRaiz== null) throw new  FileNotFoundException();                 
        
        if (archivoRaiz.isDirectory()){
            for(File file: archivoRaiz.listFiles()){
                searchDirectories(file);
            }

        }else{
            String nombreArchivo = archivoRaiz.getAbsolutePath();
            String classpath = System.getProperty("java.class.path");

            Class clase = Class.forName(obtenerNombreClase(nombreArchivo, classpath));

            try{
                registrarMetodo(clase);

            }catch(NoSuchMethodException e){

            }          

        }        

    }

    private<T> void registrarMetodo(Class<T> clase) throws NoSuchMethodException {

        Constructor<T> constructor = clase.getConstructor();
            
        if (clase.isAnnotationPresent(Controller.class)) {
            for(Method method: clase.getDeclaredMethods()){
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    Class<?> tipoRetorno = method.getReturnType();

                    BiFunction<Request, Response, ?> funcion = (req, res)-> {
                        try {
                            Object result = method.invoke(constructor.newInstance(), (Object[])method.getParameterTypes());
                            return tipoRetorno.cast(result);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                | InstantiationException e) {
                            e.printStackTrace();
                            return null;
                        }
                       
                    };

                    switch (requestMapping.metodoHttp()) {
                        case GET -> registro.get(requestMapping.path(), funcion);
                        case POST -> registro.post(requestMapping.path(), funcion);
                        default -> throw new NoSuchMethodException();
                    }
 
                }
            }                
        } 
    }

    private String obtenerNombreClase(String rutaArchivo, String rutaClase) {
        String separar = File.separator;
        String relativePath = rutaArchivo.substring(rutaClase.length() + 1);
        return relativePath.replace(separar, ".").replace(".class", "");
    }
    
}
