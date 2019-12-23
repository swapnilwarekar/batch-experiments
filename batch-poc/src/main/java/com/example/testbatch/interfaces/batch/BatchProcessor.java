/*
 * Creation : 17 Dec 2019
 */
package com.example.testbatch.interfaces.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

@EnableBatchProcessing
public class BatchProcessor {

    private static final Logger log = LoggerFactory.getLogger(BatchProcessor.class);

}
