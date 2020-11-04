package com.demo.asyncretry.config;

import java.util.HashMap;
import java.util.Map;
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

    @Value("${app.reintento.cantidad-max}")
    Integer cantidadMaximaIntentos;

    @Bean
    public RetryTemplate miRetryTemplate() {

        ExponentialBackOffPolicy politicaDeEsperaEntreReintentos = new ExponentialBackOffPolicy();
        politicaDeEsperaEntreReintentos.setInitialInterval(200L);
        politicaDeEsperaEntreReintentos.setMultiplier(2);

        SimpleRetryPolicy politicaDeReintento = new SimpleRetryPolicy(cantidadMaximaIntentos);
//        SimpleRetryPolicy politicaDeReintento = new SimpleRetryPolicy(4);
        
//        Se pueden especificar las excepciones que causan un reintento y cuales no
//        Map<Class<? extends Throwable>, Boolean> hashMap = new HashMap<>();
//        hashMap.put(IllegalArgumentException.class, true);
//        hashMap.put(NullPointerException.class, false);
//        SimpleRetryPolicy politicaDeReintento = new SimpleRetryPolicy(cantidadMaximaIntentos, hashMap);

        RetryListener[] listeners = {new LogListener()};

        //RETRY TEMPLATE
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(politicaDeEsperaEntreReintentos);
        retryTemplate.setRetryPolicy(politicaDeReintento);
        retryTemplate.setListeners(listeners);

        return retryTemplate;
    }

    @Slf4j
    public static class LogListener extends RetryListenerSupport {

        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
            log.warn("Reintentando consulta " + context.getRetryCount() + "...");
            log.debug("Error que produjo reintento: " + throwable);
        }

    }
}
