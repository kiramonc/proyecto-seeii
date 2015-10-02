/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
"use strict"; // no se puede utilizar variables no declaradas
//cargamos las funciones de annyang

if (annyang) {
    //Define las funciones de nuestras órdenes que se ejecutarán.
    var respuesta = "down";
    var verificar = function(palabra) {
        //#hola es una etiqueta css
        if (palabra == respuesta) {
            $("#hola").slideDown("slow");
            //eso nos lleva a la seccion con clase css #section_hello
            scrollTo("#section_hello");
            responsiveVoice.speak(palabra);
        } else {
            if (palabra == "up" || palabra == "app") {
                $("#hola").slideUp("slow");
                //eso nos lleva a la seccion con clase css #section_hello
                scrollUp("#section_hello");
            } else {
                responsiveVoice.speak(palabra);
                alert("Palabra diferente " + palabra);
            }
        }
    }
        ;

        // Define nuestros comandos.
        var commands = {
            ':palabraEscuchada': verificar, //indicando :palabraEscuchada captura cualquier cosa y llama a la función hola
        };

        annyang.addCommands(commands);
        annyang.debug(); // para saber si hay un error en la consola del navegador
        annyang.start(); // Annyang y la Cual solicita permiso al usuario para usar el micrófono
    }
    ;

    var scrollTo = function(identifier, speed) {
        $('html, body').animate({
            scrollTop: $(identifier).offset().top
        }, speed || 1000);
    }

    var scrollUp = function(identifier, speed) {
        $('html, body').animate({
            scrollTop: $(identifier).offset().bottom
        }, speed || 1000);
}