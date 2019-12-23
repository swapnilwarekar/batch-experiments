/*
 * Creation : 20 Dec 2019
 */
package com.example.testbatch.domain.services;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchService {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importUserJob;

    public void launch() {
        System.out.println("test");

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        try {

            JobExecution execution = jobLauncher.run(importUserJob, jobParameters);
            System.out.println("Exit Status : " + execution.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

    }

}
