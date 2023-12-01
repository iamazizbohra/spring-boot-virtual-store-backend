package com.coedmaster.vstore.service.contract;

import org.jobrunr.jobs.context.JobContext;

public interface ISmsService {
	void sendMobileVerificationMessage(JobContext jobContext, String mobile, String otp);
}
