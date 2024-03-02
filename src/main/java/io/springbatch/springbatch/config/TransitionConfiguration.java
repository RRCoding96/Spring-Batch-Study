//package io.springbatch.springbatch.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.job.builder.FlowBuilder;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.job.flow.Flow;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@RequiredArgsConstructor
//@Configuration
//public class TransitionConfiguration {
//
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager tx;
//
//    @Bean
//    public Job startNextJob() {
//        return new JobBuilder("transitionJob", jobRepository)
//            .start(step1())
//                .on("FAILED")
//                .to(step2())
//                .on("*")
//                .stop()
//            .from(step1()).on("*")
//                .to(step5())
//                .next(step6())
//                .on("COMPLETED")
//                .end()
//            .end()
//            .build();
//    }
//
//    @Bean
//    public Flow flowA() {
//        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowA");
//        flowBuilder.start(step1())
//            .next(step2())
//            .end();
//
//        return flowBuilder.build();
//    }
//
//    @Bean
//    public Flow flowB() {
//        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowB");
//        flowBuilder.start(step4())
//            .next(step5())
//            .end();
//
//        return flowBuilder.build();
//    }
//
//    @Bean
//    public Step step1() {
//        return new StepBuilder("step1", jobRepository)
//            .tasklet(new Tasklet() {
//                @Override
//                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                    System.out.println(">> step1 has executed");
//                    return RepeatStatus.FINISHED;
//                }
//            }, tx).build();
//    }
//
//    @Bean
//    public Step step2() {
//        return new StepBuilder("step2", jobRepository)
//            .tasklet((contribution, chunkContext) -> {
//                System.out.println(">> step2 has executed");
//                return RepeatStatus.FINISHED;
//            }, tx).build();
//    }
//
//    @Bean
//    public Step step3() {
//        return new StepBuilder("step3", jobRepository)
//            .tasklet(new Tasklet() {
//                @Override
//                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                    System.out.println(">> step3 has executed");
//                    return RepeatStatus.FINISHED;
//                }
//            }, tx).build();
//    }
//
//    @Bean
//    public Step step4() {
//        return new StepBuilder("step4", jobRepository)
//            .tasklet(new Tasklet() {
//                @Override
//                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                    System.out.println(">> step4 has executed");
//                    return RepeatStatus.FINISHED;
//                }
//            }, tx).build();
//    }
//
//    @Bean
//    public Step step5() {
//        return new StepBuilder("step5", jobRepository)
//            .tasklet(new Tasklet() {
//                @Override
//                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                    System.out.println(">> step5 has executed");
//                    return RepeatStatus.FINISHED;
//                }
//            }, tx).build();
//    }
//
//    @Bean
//    public Step step6() {
//        return new StepBuilder("step6", jobRepository)
//            .tasklet(new Tasklet() {
//                @Override
//                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                    System.out.println(">> step6 has executed");
//                    return RepeatStatus.FINISHED;
//                }
//            }, tx).build();
//    }
//
//}
