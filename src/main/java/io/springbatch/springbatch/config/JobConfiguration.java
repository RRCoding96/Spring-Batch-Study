package io.springbatch.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.Map;

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
                // contribution로 참조
                JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                String name = jobParameters.getString("name");
                long seq = jobParameters.getLong("seq");
                Date date = jobParameters.getDate("date");
                System.out.println("====================================");
                System.out.println(" step1 executed ");
                System.out.println("name:" + name);
                System.out.println("seq: " + seq);
                System.out.println("date: " + date);
                System.out.println("====================================");

                // chunkContext로 참조
                Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
                String name2 = (String)jobParameters2.get("name");
                long seq2 = (long)jobParameters2.get("seq");

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
