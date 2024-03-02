package edu.eci.arep.ASE.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import edu.eci.arep.ASE.app.controller.SparkController;
import edu.eci.arep.ASE.app.http.HTTPConnection;
import edu.eci.arep.ASE.app.http.exception.HTTPException;
import edu.eci.arep.ASE.app.model.Registry;
import edu.eci.arep.ASE.app.scanner.Scanner;
import edu.eci.arep.ASE.app.services.ReadFile;

public class HTTPServer {

    /*
     * Inicia el servidor HTTP en el puerto 35000 y maneja las solicitudes entrantes.
     * Crea una instancia de la clase HTTPConnection para establecer una conexión con la API.
     * Lee multiples archivos como html, css, js e imagenes y los envia al cliente.
     * @throws IOException Si hay algún error de entrada/salida durante la ejecución.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HTTPConnection httpConnection = new HTTPConnection("https://www.omdbapi.com/", "d91dcd3");
        
        Scanner scanner = new Scanner();
        Registry registro = scanner.getRegistry();
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while(running){

            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            BufferedReader in = new BufferedReader(
                                 new InputStreamReader(
                                    clientSocket.getInputStream()));
            String inputLine;

            String firstLine = null;
                
            while ((inputLine = in.readLine()) != null) {
                if(firstLine == null){firstLine = inputLine;}

                System.out.println("Received: " + inputLine);
                
                if (!in.ready()) {
                    break;
                }
            }

            if(firstLine == null){continue;}

            String metodo = firstLine.split(" ")[0];
            String path = firstLine.split(" ")[1];
            ReadFile readFile = new ReadFile();

            try{
                if(registro.hasEndPoint(path)){
                    readFile.lecturaController(clientSocket, registro, path, metodo);
                }else if(path.equals("/favicon.ico")){
                    readFile.lecturaImagen(clientSocket, path);
                }else if(path.startsWith("/movie")){
                    readFile.getMovieData(clientSocket,httpConnection, path.split("/")[2]);
                }else if(path.equals("/")){
                    readFile.lecturaArchivo(clientSocket, "/index.html");
                }else if(path.endsWith(".html") || path.endsWith(".js")){
                    readFile.lecturaArchivo(clientSocket , path);
                }else if(path.endsWith(".css")){
                    readFile.lecturaEstilos(clientSocket, path);
                }else if(path.endsWith(".png")){
                    readFile.lecturaImagen(clientSocket, path);
                }else{
                    throw new FileNotFoundException("El archivo no ha sido encontrado");
                }
     
                in.close();
                clientSocket.close();
            }catch(HTTPException h){
                readFile.archivoNoEncontrado(clientSocket);
            }catch(FileNotFoundException e){
                readFile.archivoNoEncontrado(clientSocket);
            }

            
        }
        serverSocket.close();
    
    }
}
