package com.camunda.consulting;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientConfiguration;
import io.camunda.zeebe.client.api.JsonMapper;
import io.camunda.zeebe.client.api.worker.BackoffSupplier;
import io.camunda.zeebe.client.impl.ZeebeClientImpl;
import io.camunda.zeebe.client.impl.ZeebeObjectMapper;
import io.camunda.zeebe.client.impl.util.ExecutorResource;
import io.camunda.zeebe.client.impl.worker.ExponentialBackoffBuilderImpl;
import io.camunda.zeebe.gateway.protocol.GatewayGrpc;
import io.camunda.zeebe.spring.client.CamundaAutoConfiguration;
import io.camunda.zeebe.spring.client.annotation.processor.AnnotationProcessorConfiguration;
import io.camunda.zeebe.spring.client.configuration.MetricsDefaultConfiguration;
import io.camunda.zeebe.spring.client.configuration.OperateClientProdAutoConfiguration;
import io.camunda.zeebe.spring.client.configuration.ZeebeActuatorConfiguration;
import io.camunda.zeebe.spring.client.configuration.ZeebeClientAllAutoConfiguration;
import io.camunda.zeebe.spring.client.configuration.ZeebeClientProdAutoConfiguration;
import io.camunda.zeebe.spring.client.jobhandling.CommandExceptionHandlingStrategy;
import io.camunda.zeebe.spring.client.jobhandling.DefaultCommandExceptionHandlingStrategy;
import io.camunda.zeebe.spring.client.jobhandling.JobWorkerManager;
import io.camunda.zeebe.spring.client.jobhandling.ZeebeClientExecutorService;
import io.camunda.zeebe.spring.client.metrics.DefaultNoopMetricsRecorder;
import io.camunda.zeebe.spring.client.metrics.MetricsRecorder;
import io.camunda.zeebe.spring.client.properties.ZeebeClientConfigurationProperties;
import io.camunda.zeebe.spring.client.properties.ZeebeClientConfigurationProperties.Broker;
import io.camunda.zeebe.spring.client.properties.ZeebeClientConfigurationProperties.Security;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;

@Configuration
@Import({ AnnotationProcessorConfiguration.class, /*CamundaAutoConfiguration.class, */ ZeebeClientProdAutoConfiguration.class,
  ZeebeClientAllAutoConfiguration.class
 })
@ComponentScans({@ComponentScan("com.camunda.consulting"), @ComponentScan("io.camunda.zeebe.spring.client")})
public class SimpleSpringZeebeApplicationContext {
  
  @Bean
  public ZeebeClientConfigurationProperties zeebeClientConfigurationProperties(Environment environment) {
    LOG.info("Environment: {}", environment);
    ZeebeClientConfigurationProperties configurationProperties = new ZeebeClientConfigurationProperties(environment);
    Broker broker = new Broker();
    broker.setGatewayAddress("localhost:26500");
    configurationProperties.setBroker(broker );
    
    Security security = new Security();
    security.setPlaintext(true);
    configurationProperties.setSecurity(security);
    return configurationProperties;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
  }
//
//  @Bean
//  public ZeebeClientExecutorService zeebeClientExecutorService() {
//    return ZeebeClientExecutorService.createDefault();
//  }
//
//  @Bean
//  public CommandExceptionHandlingStrategy commandExceptionHandlingStrategy(ZeebeClientExecutorService scheduledExecutorService) {
//    return new DefaultCommandExceptionHandlingStrategy(backoffSupplier(), scheduledExecutorService.get());
//  }
//  
//  @Bean(name = "zeebeJsonMapper")
//  public JsonMapper jsonMapper(ObjectMapper objectMapper) {
//    return new ZeebeObjectMapper(objectMapper);
//  }
//  
  @Bean
  public MetricsRecorder metricsRecorder() {
    return new DefaultNoopMetricsRecorder();
  }
//
//  @Bean
//  public JobWorkerManager jobWorkerManager(final CommandExceptionHandlingStrategy commandExceptionHandlingStrategy,
//                                           final JsonMapper jsonMapper,
//                                           final MetricsRecorder metricsRecorder) {
//    return new JobWorkerManager(commandExceptionHandlingStrategy, jsonMapper, metricsRecorder);
//  }
//
//  @Bean
//  public BackoffSupplier backoffSupplier() {
//    return new ExponentialBackoffBuilderImpl()
//      .maxDelay(1000L)
//      .minDelay(50L)
//      .backoffFactor(1.5)
//      .jitterFactor(0.2)
//      .build();
//  }
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//  
//  @Bean
//  public ZeebeClientConfiguration zeebeClientConfiguration() {
//    return new ZeebeClientConfiguration() {
//      
//      @Override
//      public boolean ownsJobWorkerExecutor() {
//        // TODO Auto-generated method stub
//        return false;
//      }
//      
//      @Override
//      public ScheduledExecutorService jobWorkerExecutor() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public boolean isPlaintextConnectionEnabled() {
//        // TODO Auto-generated method stub
//        return true;
//      }
//      
//      @Override
//      public String getOverrideAuthority() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public int getNumJobWorkerExecutionThreads() {
//        // TODO Auto-generated method stub
//        return 0;
//      }
//      
//      @Override
//      public int getMaxMessageSize() {
//        // TODO Auto-generated method stub
//        return 0;
//      }
//      
//      @Override
//      public Duration getKeepAlive() {
//        return Duration.ofSeconds(45);
//      }
//      
//      @Override
//      public JsonMapper getJsonMapper() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public List<ClientInterceptor> getInterceptors() {
//        return new ArrayList<>();
//      }
//      
//      @Override
//      public String getGatewayAddress() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public String getDefaultTenantId() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public Duration getDefaultRequestTimeout() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public Duration getDefaultMessageTimeToLive() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public List<String> getDefaultJobWorkerTenantIds() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public boolean getDefaultJobWorkerStreamEnabled() {
//        // TODO Auto-generated method stub
//        return false;
//      }
//      
//      @Override
//      public String getDefaultJobWorkerName() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public int getDefaultJobWorkerMaxJobsActive() {
//        // TODO Auto-generated method stub
//        return 0;
//      }
//      
//      @Override
//      public Duration getDefaultJobTimeout() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public Duration getDefaultJobPollInterval() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public CredentialsProvider getCredentialsProvider() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//      
//      @Override
//      public String getCaCertificatePath() {
//        // TODO Auto-generated method stub
//        return null;
//      }
//    };
//  }
//
//  @Bean(destroyMethod = "close")
//  public ZeebeClient zeebeClient(ZeebeClientExecutorService zeebeClientExecutorService, ZeebeClientConfiguration configurationProperties) { // (ZeebeClientBuilder builder) {
//    //LOG.info("Creating ZeebeClient using ZeebeClientBuilder [" + builder + "]");
//    //return builder.build();
//
//    LOG.info("Creating ZeebeClient using ZeebeClientConfiguration [" + configurationProperties + "]");
//    if (zeebeClientExecutorService!=null) {
//      ManagedChannel managedChannel = ZeebeClientImpl.buildChannel(configurationProperties);
//      GatewayGrpc.GatewayStub gatewayStub = ZeebeClientImpl.buildGatewayStub(managedChannel, configurationProperties);
//      ExecutorResource executorResource = new ExecutorResource(zeebeClientExecutorService.get(), configurationProperties.ownsJobWorkerExecutor());
//      return new ZeebeClientImpl(configurationProperties, managedChannel, gatewayStub, executorResource);
//    } else {
//      return new ZeebeClientImpl(configurationProperties);
//    }
//  }
  
//  @Bean("propertyBasedZeebeWorkerValueCustomizer")
//  public ZeebeWorkerValueCustomizer propertyBasedZeebeWorkerValueCustomizer() {
//    return new PropertyBasedZeebeWorkerValueCustomizer(this.configurationProperties);
//  }


}
