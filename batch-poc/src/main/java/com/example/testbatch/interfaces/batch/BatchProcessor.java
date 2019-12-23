/*
 * Creation : 17 Dec 2019
 */
package com.example.testbatch.interfaces.batch;

import java.util.Random;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.testbatch.domain.model.Person;
import com.example.testbatch.domain.repository.PersonRepository;

@EnableBatchProcessing
@Component
public class BatchProcessor {

	
    private static final Logger log = LoggerFactory.getLogger(BatchProcessor.class);
    
    @Autowired
    JpaTransactionManager transactionManagerJPA;
    
    @Bean
    public JobRepository jobRepository() throws Exception {
        ResourcelessTransactionManager transactionManager = new ResourcelessTransactionManager();
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
        mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        return mapJobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }

    @Bean
    public Job importUserJob(JobExecutionListener listener, JobBuilderFactory jobBuilderFactory, EntityManagerFactory entityManagerFactory,
            StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
                .flow(step1(entityManagerFactory, stepBuilderFactory)).end().build();
    }

    @Bean
    public Step step1(EntityManagerFactory entityManagerFactory, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step1").transactionManager(transactionManagerJPA).<Person, Person>chunk(10).reader(reader()).processor(processor()).writer(writer(entityManagerFactory))
                .build();
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return (item) -> {
            Random random = new Random();
            item.setId(random.nextInt(10000));
            item.concatenateName();
            return item;
        };
    }

    @Bean
    public FlatFileItemReader<Person> reader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLinesToSkip(1);

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] { "firstName", "lastName" });

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);

        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(tokenizer);
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public JpaItemWriter<Person> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Person> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public JobExecutionListener listener(PersonRepository personRepository) {
        return new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {
                /**
                 * As of now empty but can add some before job conditions
                 */
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("!!! JOB FINISHED! Time to verify the results");
                    personRepository.findAll().forEach(person -> log.info("Found <" + person + "> in the database."));
                }
            }
        };
    }
}
