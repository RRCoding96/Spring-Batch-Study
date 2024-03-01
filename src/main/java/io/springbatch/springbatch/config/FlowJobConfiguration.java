package io.springbatch.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    @Bean
    public Job flowJob() {
        return new JobBuilder("flowJob", jobRepository)
            .start(step1())
            .on("COMPLETED").to(step2())
            .from(step1())
            .on("FAILED").to(step3())
            .end()
            .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("flowJobStep1", jobRepository)
            .tasklet(new Tasklet() {
                @Override
                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                    System.out.println("flowJobStep1 has executed");
//                    throw new RuntimeException("");
                    return RepeatStatus.FINISHED;
                }
            }, tx).build();

    }

    @Bean
    public Step step2() {
        return new StepBuilder("flowJobStep2", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("flowJobStep2 has executed");
                return RepeatStatus.FINISHED;
            }, tx).build();
    }

    @Bean
    public Step step3() {
        return new StepBuilder("flowJobStep3", jobRepository)
            .tasklet(new Tasklet() {
                @Override
                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                    System.out.println("flowJobStep3 has executed");
                    return RepeatStatus.FINISHED;
                }
            }, tx).build();
    }


}

