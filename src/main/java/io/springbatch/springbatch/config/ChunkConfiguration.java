package io.springbatch.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ChunkConfiguration {

    @Bean
    public Job chunkJob(
        JobRepository jobRepository,
        Step chunkStep1,
        Step chunkStep2
    ) {
        return new JobBuilder("chunkJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(chunkStep1)
            .next(chunkStep2)
            .build();
    }

    @Bean
    public Step chunkStep1(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder("chunkStep1", jobRepository)
            .<String, String>chunk(2, tx)
            .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3","item4", "item5", "item6")))
            .processor(new ItemProcessor<String, String>() {
                @Override
                public String process(String item) throws Exception {
                    Thread.sleep(300);
                    System.out.println(item);
                    return "my_" + item;
                }
            })
            .writer(new ItemWriter<String>() {
                @Override
                public void write(Chunk<? extends String> chunk) throws Exception {
                    Thread.sleep(1000);
                    System.out.println(chunk.getItems());
                }
            })
            .build();
    }

    @Bean
    public Step chunkStep2(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder("chunkStep2", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("chunkStep2 has executed");
                return RepeatStatus.FINISHED;
            }, tx)
            .build();
    }
}