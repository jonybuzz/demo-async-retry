package com.demo.asyncretry.service;

import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NegocioService {

    @Async
    public void terminaloCuandoQuieras(String tarea) throws InterruptedException {
        log.info("Ejecutando en hilo: " + Thread.currentThread().getName());
        log.warn("Haciendo {}...", tarea);
        Thread.sleep(1000);
        log.warn("Terminado {}...", tarea);
    }

    @Async
    public Future<String> saludameQueTeEspero(String nombre) throws InterruptedException {
        log.info("Ejecutando en hilo: " + Thread.currentThread().getName());
        log.warn("Preparando saludo");
        Thread.sleep(4000);
        String saludo = "HOLA '" + nombre + "' ¯\\_ツ_/¯";
        return new AsyncResult<String>(saludo);
    }

    @Autowired
    RetryTemplate miRetryTemplate;

    public void hastaQueAnde(Integer parametro) {
        log.info("Iniciando tarea hasta que ande...");
        Double resultado = miRetryTemplate.execute(contexto -> this.tarea(parametro));
        System.out.println("Resultado: " + resultado);
    }

    public double tarea(Integer p) {
        return p / 0;
    }

}
