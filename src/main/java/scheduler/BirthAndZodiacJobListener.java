package scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BirthAndZodiacJobListener implements JobListener {
    private static final Logger LOG = LoggerFactory.getLogger(BirthAndZodiacJobListener.class);

    public String getName() {
        return "BirthAndZodiacJobListener";
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        final String jobName = context.getJobDetail().getKey().toString();
        LOG.info("Job to be executed: {} is starting", jobName);

    }

    public void jobExecutionVetoed(JobExecutionContext context) {
        final String jobName = context.getJobDetail().getKey().toString();
        LOG.error("Job error: {}", jobName);
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        final String jobName = context.getJobDetail().getKey().toString();
        LOG.info("Job : {} is finished!!", jobName);

    }
}
