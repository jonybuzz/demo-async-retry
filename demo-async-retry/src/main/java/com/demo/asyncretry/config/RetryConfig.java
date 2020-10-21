package com.demo.asyncretry.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryTemplate consultaEstadoCredinRetryTemplate(
            @Value("${app.reintento.cantidad-max}") Integer cantidadMaximaIntentos) {

        ExponentialBackOffPolicy politicaDeEspera = new ExponentialBackOffPolicy();
        politicaDeEspera.setInitialInterval(200L);

        SimpleRetryPolicy politicaDeReintento = new SimpleRetryPolicy(cantidadMaximaIntentos);
//        Se pueden especificar las excepciones que causar un reintento
//        SimpleRetryPolicy politicaDeReintento = new SimpleRetryPolicy(cantidadMaximaIntentos, hashMap);

        RetryListener[] listeners = {new LogListener()};

        //RETRY TEMPLATE
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(politicaDeEspera);
        retryTemplate.setRetryPolicy(politicaDeReintento);
        retryTemplate.setListeners(listeners);

        return retryTemplate;
    }

    @Slf4j
    public static class LogListener extends RetryListenerSupport {

        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
            log.warn("Reintentando consulta...");
            log.debug("Error que produjo reintento:", throwable);
        }

    }
}