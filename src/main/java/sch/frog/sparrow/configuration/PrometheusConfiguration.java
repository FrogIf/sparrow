package sch.frog.sparrow.configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prometheus相关配置
 */
@Configuration
public class PrometheusConfiguration {

    @Resource
    private PrometheusMeterRegistry meterRegistry;

    /**
     * 统计访问量
     */
    @Bean
    public Counter requestTotalCountCollector(){
        return Counter.build().name("sparrow_http_request_count")
                .labelNames("path", "method").help("http请求数").register(meterRegistry.getPrometheusRegistry());
    }

    @Bean
    public WebMvcConfigurer openTelemetryMvcConfigurer(@Qualifier("requestTotalCountCollector") final Counter counter){
        return new WebMvcConfigurer(){
            @Override
            public void addInterceptors(InterceptorRegistry registry){
                // 注册 访问量统计的counter拦截器
                registry.addInterceptor(new HandlerInterceptor(){
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        String uri = request.getRequestURI();
                        String method = request.getMethod();
                        counter.labels(uri, method).inc();
                        return true;
                    }
                });
            }
        };
    }

}
