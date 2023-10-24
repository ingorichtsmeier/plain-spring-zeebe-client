package com.camunda.consulting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class SimpleWorker {
  
  private static final Logger LOG = LoggerFactory.getLogger(SimpleWorker.class);

  @JobWorker(name = "exampleWorker")
  public void handleExample(ActivatedJob job) {
    LOG.info("Handling job {} for process instance {}", job.getKey(), job.getProcessInstanceKey());
  }
}
