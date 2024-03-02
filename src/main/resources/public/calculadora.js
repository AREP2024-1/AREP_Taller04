let numero1;
let numero2;

function operaciones(){
    let botonSuma = document.getElementById("botonSuma");
    let botonResta = document.getElementById("botonResta");
    let botonMultiplicacion = document.getElementById("botonMultiplicacion");
    let botonDivision = document.getElementById("botonDivision");

    botonSuma.addEventListener("click", sumar);
    botonResta.addEventListener("click", restar);
    botonMultiplicacion.addEventListener("click", multiplicar);
    botonDivision.addEventListener("click", dividir);
}

function sumar() {
    numero1 = document.getElementById("primerNumero").value;
    numero2 = document.getElementById("segundoNumero").value;
    let resultado = parseInt(numero1) + parseInt(numero2);
    mensajeResultado(resultado);

}

function restar() {
    numero1 = document.getElementById("primerNumero").value;
    numero2 = document.getElementById("segundoNumero").value;
    let resultado = parseInt(numero1) - parseInt(numero2);
    mensajeResultado(resultado);
    
}

function multiplicar() {
    numero1 = document.getElementById("primerNumero").value;
    numero2 = document.getElementById("segundoNumero").value;
    let resultado = parseInt(numero1) * parseInt(numero2);
    mensajeResultado(resultado);
    
}

function dividir() {
    numero1 = document.getElementById("primerNumero").value;
    numero2 = document.getElementById("segundoNumero").value;
    let resultado = parseInt(numero1) / parseInt(numero2);
    mensajeResultado(resultado);
    
}

function mensajeResultado(resultadoOperacion){
    let seccionMensaje = document.getElementById("mensajeResultado");
    let parrafo = document.createElement("p");
    parrafo.innerHTML = "El resultado es: " + resultadoOperacion;
    seccionMensaje.appendChild(parrafo);
}

window.addEventListener("load", operaciones);