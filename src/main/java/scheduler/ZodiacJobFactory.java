package scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import services.UserTagService;

public class ZodiacJobFactory implements JobFactory {

    private UserTagService userTagService;

    ZodiacJobFactory(UserTagService usersTagService) {
        this.userTagService = usersTagService;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        Job job = addNewJob(bundle, scheduler);
        AddZodiacTagJob zodiacTagJob = null;
        if (job instanceof AddZodiacTagJob) {
            zodiacTagJob = (AddZodiacTagJob) job;
            zodiacTagJob.setUserTagService(userTagService);

        }
        return zodiacTagJob;
    }

    private Job addNewJob(TriggerFiredBundle bundle, Scheduler Scheduler) throws SchedulerException {
        JobDetail jobDetail = bundle.getJobDetail();
        Class<? extends Job> jobClass = jobDetail.getJobClass();
        try {
            return jobClass.newInstance();
        } catch (Exception e) {
            throw new SchedulerException(
                    "Problem instantiating class '"
                            + jobDetail.getJobClass().getName() + "'", e);
        }
    }
}
