package io.springbatch.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class JobExecutionDeciderConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    @Bean
    public Job customExitStatusJob() {
        return new JobBuilder("customExitStatusJob", jobRepository)
            .start(startStep())
            .next(decider())
            .from(decider()).on("ODD").to(oddStep())
            .from(decider()).on("EVEN").to(evenStep())
            .end()
            .build();
    }

    @Bean
    public Step startStep() {
        return new StepBuilder("startStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("This is the start tasklet");
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }

    @Bean
    public Step evenStep() {
        return new StepBuilder("evenStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println(">>EvenStep has executed");
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }

    @Bean
    public Step oddStep() {
        return new StepBuilder("oddStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println(">>OddStep has executed");
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }


    @Bean
    public JobExecutionDecider decider() {
        return new CustomDecider();
    }

    public static class CustomDecider implements JobExecutionDecider {

        private int count = 0;

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;

            if(count % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            }
            else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}
