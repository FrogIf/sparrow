package sch.frog.sparrow.configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sch.frog.sparrow.prometheus.FrogMonitor;
import sch.frog.sparrow.prometheus.PrometheusConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prometheus相关配置
 *
 * 这里面由于使用的不是io.micrometer.core.instrument.Counter之类的, 所以要手动指定application, 公共配置不生效
 */
@Configuration
public class PrometheusConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(PrometheusConfiguration.class);

    @Autowired
    private PrometheusMeterRegistry meterRegistry;

    @Autowired
    private FrogMonitor frogMonitor;

    @Value("${spring.application.name}") private String applicationName;

    /**
     * 统计访问量
     */
    @Bean
    public Counter requestTotalCountCollector(){
        return Counter.build().name("sparrow_http_request_count")
                .labelNames("path", "method", PrometheusConstants.APPLICATION_LABEL_KEY).help("http请求数").register(meterRegistry.getPrometheusRegistry());
    }

    @Bean
    public Histogram responseTimeCollector(){
        return Histogram.build().name("sparrow_response_duration")
                .labelNames("method", PrometheusConstants.APPLICATION_LABEL_KEY).help("响应时间").exponentialBuckets(1, 5, 6).register(meterRegistry.getPrometheusRegistry());
    }

    @Bean
    public Summary responseTimeSummaryCollector(){
        return Summary.build().name("sparrow_response_duration_quantiles")
                .labelNames("method", PrometheusConstants.APPLICATION_LABEL_KEY).help("响应时间分位")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .quantile(0.99, 0.001)
                .register(meterRegistry.getPrometheusRegistry());
    }

    @Bean
    public WebMvcConfigurer openTelemetryMvcConfigurer(@Qualifier("requestTotalCountCollector") final Counter counter,
                                                       @Qualifier("responseTimeCollector") final Histogram responseTimeCollector,
                                                       @Qualifier("responseTimeSummaryCollector") final Summary responseTimeSummaryCollector){
        return new WebMvcConfigurer(){
            @Override
            public void addInterceptors(InterceptorRegistry registry){
                registry.addInterceptor(new PrometheusMonitorInterceptor(counter,
                        frogMonitor, responseTimeCollector, responseTimeSummaryCollector, applicationName));
            }
        };
    }

    private static class PrometheusMonitorInterceptor implements HandlerInterceptor {

        private final ThreadLocal<Long> requestStart = new ThreadLocal<>();

        private final Counter accessCounter;

        private final FrogMonitor frogMonitorMBean;

        private final Histogram responseHistogram;

        private final Summary responseTimeSummaryCollector;

        private final String applicationName;

        public PrometheusMonitorInterceptor(Counter accessCounter, FrogMonitor frogMonitorMBean, Histogram responseHistogram, Summary responseTimeSummaryCollector, String applicationName) {
            this.accessCounter = accessCounter;
            this.frogMonitorMBean = frogMonitorMBean;
            this.responseHistogram = responseHistogram;
            this.responseTimeSummaryCollector = responseTimeSummaryCollector;
            this.applicationName = applicationName;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String uri = request.getRequestURI();
            String method = request.getMethod();
            accessCounter.labels(uri, method, applicationName).inc();
            frogMonitorMBean.accessIncrement();
            requestStart.set(System.currentTimeMillis());
            return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            try{
                String uri = request.getRequestURI();
                Long start = requestStart.get();
                if(start != null){
                    long duration = System.currentTimeMillis() - start;
                    logger.info("response duration : {}", duration);
                    this.responseHistogram.labels(uri, applicationName).observe(duration);
                    this.responseTimeSummaryCollector.labels(uri, applicationName).observe(duration);
                }
            }finally {
                requestStart.remove();
            }
        }
    }

}
