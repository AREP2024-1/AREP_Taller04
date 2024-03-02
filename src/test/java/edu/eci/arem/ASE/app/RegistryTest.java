package edu.eci.arem.ASE.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.eci.arep.ASE.app.http.exception.HTTPException;
import edu.eci.arep.ASE.app.model.Registry;

public class RegistryTest {
    
    @Test
    public void deberiaRegistrarPeticionGet() throws HTTPException{
        Registry registry = new Registry();

        registry.get("/unitTestGet", (req,res)->{
            return "Unit test peticion GET";
        });

        var result = registry.doGet("/unitTestGet", null, null);
 
        assertTrue(registry.hasEndPoint("/unitTestGet"));
        assertNotNull(result);
        assertEquals("Unit test peticion GET", result);
    }
 
    @Test
    public void deberiaRegistrarPeticionesPost() throws HTTPException{
        Registry registry = new Registry();
        registry.post("/unitTestPost", (req,res)->{
            return "Unit test peticion POST";
        });

        var result = registry.doPost("/unitTestPost", null, null);
 
        assertTrue(registry.hasEndPoint("/unitTestPost"));
        assertNotNull(result);
        assertEquals("Unit test peticion POST", result);
    }
   
    @Test
    public void deberiaLanzarExcepcionCuandoNoTieneEndPointDelGet(){
        Registry registry = new Registry();

        assertThrows(HTTPException.class, 
                        ()-> registry.doGet("/unitTestGet", null, null));

        registry.get("/unitTestGet", (req,res)->{
            return "Unit test excepcion peticion GET";
        });

        var result = registry.doGet("/unitTestGet", null, null);
 
        assertTrue(registry.hasEndPoint("/unitTestGet"));
        assertNotNull(result);
        assertEquals("Unit test excepcion peticion GET", result);
 
    }
 
    @Test
    public void deberiaLanzarExcepcionCuandoNoTieneEndPointDelPost(){
        Registry registry = new Registry();

        assertThrows(HTTPException.class, 
                        ()-> registry.doPost("/unitTestPost", null, null));

        registry.post("/unitTestPost", (req,res)->{
            return "Unit test excepcion peticion POST";
        });

        var result = registry.doPost("/unitTestPost", null, null);
 
        assertTrue(registry.hasEndPoint("/unitTestPost"));
        assertNotNull(result);
        assertEquals("Unit test excepcion peticion POST", result);
 
    }
 
    @Test
    public void noDeberiaAceptarEndPointsInvalidos(){
        Registry registry = new Registry();
        assertTrue(registry.validateEndPoint("/unitTest",false));
        assertTrue(registry.validateEndPoint("/:danielaTest",true));
        assertTrue(registry.validateEndPoint("/",false));
        assertTrue(registry.validateEndPoint("/taller04",false));
 
        assertFalse(registry.validateEndPoint("/#ni+Tes+",false));
        assertFalse(registry.validateEndPoint("*",false));
        assertFalse(registry.validateEndPoint("/:",false));
        assertFalse(registry.validateEndPoint("Hola$imon",false));
        assertFalse(registry.validateEndPoint("^+*",false));
    }
   
}

