package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import services.UserTagService;

public class TriggerJob {

    public TriggerJob(UserTagService userTagService, int zodiacTime) throws Exception {
        final JobKey jobKeyZodiac = new JobKey("addZodiacKey", "group1");
        final JobDetail zodiacBuilder = JobBuilder.newJob(AddZodiacTagJob.class).withIdentity(jobKeyZodiac).build();
        final Trigger triggerZodiacBirth = TriggerBuilder.newTrigger().withIdentity("addZodiacKey2", "group1")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(zodiacTime)).build();
        final Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.getListenerManager().addJobListener(new BirthAndZodiacJobListener(), KeyMatcher.keyEquals(jobKeyZodiac));
        ZodiacJobFactory zodiacJobFactory = new ZodiacJobFactory(userTagService);
        scheduler.setJobFactory(zodiacJobFactory);
        scheduler.start();
        scheduler.scheduleJob(zodiacBuilder, triggerZodiacBirth);
    }
}