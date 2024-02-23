package io.springbatch.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("Job", jobRepository)
            .start(step1)
            .next(step2)
            .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "step1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                JobInstance jobInstance = contribution.getStepExecution().getJobExecution().getJobInstance();
                System.out.println("====================================");
                System.out.println(" step1 executed ");
                System.out.println("jobInstance.getId() : " + jobInstance.getId());
                System.out.println("jobInstance.getInstanceId() : " + jobInstance.getInstanceId());
                System.out.println("jobInstance.getJobName() : " + jobInstance.getJobName());
                System.out.println("jobInstance.getJobVersion : " + jobInstance.getVersion());
                System.out.println("====================================");
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "step2", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("====================================");
                System.out.println(" step2 executed ");
                System.out.println("====================================");
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }
}
