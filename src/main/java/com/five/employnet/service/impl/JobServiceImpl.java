package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Job;
import com.five.employnet.entity.JobMessage;
import com.five.employnet.mapper.JobMapper;
import com.five.employnet.service.JobMessageService;
import com.five.employnet.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    final JobMessageService jobMessageService;

    public JobServiceImpl(JobMessageService jobMessageService) {
        this.jobMessageService = jobMessageService;
    }

    @Override
    @Transactional
    public void saveJob(Job job) {
        this.save(job);
        JobMessage jobMessage = job.getMessageDetail();
        jobMessage.setJobId(job.getJobId());
        jobMessageService.save(jobMessage);
    }
}