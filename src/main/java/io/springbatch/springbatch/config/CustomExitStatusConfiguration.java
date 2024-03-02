package io.springbatch.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class CustomExitStatusConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    @Bean
    public Job customExitStatusJob() {
        return new JobBuilder("customExitStatusJob", jobRepository)
            .start(step1())
                .on("FAILED")
                .to(step2())
                .on("PASS")
                .stop()
            .end()
            .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println(">> step1 has executed");
                contribution.setExitStatus(ExitStatus.FAILED);
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }
    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println(">> step2 has executed");
                return RepeatStatus.FINISHED;
            }, tx)
            .listener(new PassCheckingListener())
            .build();
    }

    static class PassCheckingListener extends StepExecutionListenerSupport {

        public ExitStatus afterStep(StepExecution stepExecution) {

            String exitCode = stepExecution.getExitStatus().getExitCode();
            if (!exitCode.equals(ExitStatus.FAILED.getExitCode())) {
                return new ExitStatus("PASS");
            } else {
                return null;
            }
        }
    }

}
