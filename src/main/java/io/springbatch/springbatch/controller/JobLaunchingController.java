package io.springbatch.springbatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class JobLaunchingController {

    private final Job job;
    private final JobLauncher jobLauncher;

    @PostMapping(value = "/batch")
    public String launch(@RequestBody Member member) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("id", member.getId())
            .addDate("date", new Date())
            .toJobParameters();

		TaskExecutorJobLauncher taskExecutorJobLauncher = (TaskExecutorJobLauncher) jobLauncher;

        taskExecutorJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor()); // 비동기
        taskExecutorJobLauncher.run(job, jobParameters);

        System.out.println("Job is completed");

        return "batch completed";
    }
}
