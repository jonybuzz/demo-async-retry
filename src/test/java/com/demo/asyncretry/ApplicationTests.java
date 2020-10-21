package com.demo.asyncretry;

import com.demo.asyncretry.service.NegocioService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ApplicationTests {

    @Autowired
    NegocioService negocioService;

    @Test
    public void testAsync() throws InterruptedException, ExecutionException {
        log.info("Invocando negocio desde hilo: " + Thread.currentThread().getName());
        
        negocioService.terminaloCuandoQuieras("Cobrar");
        
        log.info("Termina el test.");
    }

    @Test
    public void testAsyncConRetorno() throws InterruptedException, ExecutionException {
        log.info("Invocando negocio desde hilo: " + Thread.currentThread().getName());
        
        Future<String> saludoFuturo = negocioService.saludameQueTeEspero("Jona");

        while (true) {
            if (saludoFuturo.isDone()) {
                log.info("Saludo recibido: " + saludoFuturo.get());
                break;
            }
            log.info("Sigo esperando saludo...");
            Thread.sleep(500);
        }
    }

}
