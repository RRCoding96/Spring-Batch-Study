package io.springbatch.springbatch.config;

import io.springbatch.springbatch.tasklet.ExecutionContextTasklet1;
import io.springbatch.springbatch.tasklet.ExecutionContextTasklet2;
import io.springbatch.springbatch.tasklet.ExecutionContextTasklet3;
import io.springbatch.springbatch.tasklet.ExecutionContextTasklet4;
import io.springbatch.springbatch.validator.CustomJobParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ValidatorConfiguration {

    private final ExecutionContextTasklet1 executionContextTasklet1;
    private final ExecutionContextTasklet2 executionContextTasklet2;
    private final ExecutionContextTasklet3 executionContextTasklet3;
    private final ExecutionContextTasklet4 executionContextTasklet4;

    @Bean
    public Job job(
        JobRepository jobRepository,
        Step step1,
        Step step2,
        Step step3,
        Step step4
    ) {
        return new JobBuilder("Job", jobRepository)
//            .validator(new DefaultJobParametersValidator(new String[]{"name"}, new String[]{"year"}))
            .start(step1)
            .next(step2)
            .next(step3)
            .next(step4)
            .validator(new CustomJobParametersValidator())
            .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "step1", jobRepository)
            .tasklet(executionContextTasklet1, tx)
            .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "step2", jobRepository)
            .tasklet(executionContextTasklet2, tx)
            .build();
    }

    @Bean
    public Flow flow(Step step3, Step step4) {
        return new FlowBuilder<Flow>("flow")
            .start(step3)
            .next(step4)
            .build();
    }

//    @Bean
//    public Flow flow() {
//        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
//        flowBuilder.start(step3())
//            .next(step4())
//            .end();
//        return flowBuilder.build();
//    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "step3", jobRepository)
            .tasklet(executionContextTasklet3, tx)
            .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "step4", jobRepository)
            .tasklet(executionContextTasklet4, tx)
            .build();
    }
}
