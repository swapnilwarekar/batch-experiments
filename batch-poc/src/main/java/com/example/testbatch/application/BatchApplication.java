package com.example.testbatch.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.AbstractApplicationContext;

import com.example.testbatch.domain.services.BatchService;

@ComponentScan(basePackages = "com.example.testbatch")

public class BatchApplication {

    private BatchService service;

    @Autowired
    public BatchApplication(BatchService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(BatchApplication.class);
        BatchApplication app = context.getBean(BatchApplication.class);
        app.executeJob();
        context.close();

    }

    private void executeJob() {
        service.launch();
    }

}
