package scheduler;

import dto.UserDTO;
import dto.UserTagDTO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.UserTagService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AddZodiacTagJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(AddZodiacTagJob.class);
    private UserTagService userTagService;
    private String actualOffsetKey = "actualOffset";
    private String actualLimitKey = "actualLimit";

    void setUserTagService(UserTagService userTagService) {
        this.userTagService = userTagService;
    }

    public void execute(JobExecutionContext arg0) {
        SchedulerContext schedulerContext = setUpSchedulerContext(arg0);
        LOG.info("Add Zodiac job executing");
        Long limit = schedulerContext.getLong(actualLimitKey);
        Long offset = schedulerContext.getLong(actualOffsetKey);
        Long max = userTagService.getUsersWithoutTag(limit, offset).getMax();
        do {
            List<UserDTO> userDTOList = userTagService
                    .getUsersWithoutTag(limit, offset).getResponse();
            userDTOList
                    .forEach(this::addNewZodiacTag);
            schedulerContext.put(actualOffsetKey, +offset);
            offset += limit;
        } while (offset < max);
    }

    private SchedulerContext setUpSchedulerContext(JobExecutionContext arg0) {
        SchedulerContext schedulerContext = null;
        try {
            schedulerContext = arg0.getScheduler().getContext();
            schedulerContext.put(actualOffsetKey, 0L);
            schedulerContext.put(actualLimitKey, 1000L);
        } catch (SchedulerException e) {
            LOG.error("Unable to get context with message: {}", e.getMessage());
        }
        return schedulerContext;
    }

    private void addNewZodiacTag(UserDTO userDto) {
        String birthDate = userDto.getBirthDate();
        if (birthDate != null && !birthDate.isEmpty()) {
            UserTagDTO zodiacTag = new UserTagDTO(UUID.randomUUID().toString(), userDto.getId(), getZodiacSign(userDto));
            try {
                userTagService.add(zodiacTag);
            } catch (Throwable e) {
                LOG.error("Unable to add zodiac tag");
            }
        }
    }

    private String getZodiacSign(UserDTO userDto) {
        int day = LocalDateTime.parse(userDto.getBirthDate()).getDayOfMonth();
        int month = LocalDateTime.parse(userDto.getBirthDate()).getMonth().getValue();
        if ((month == 12 && day >= 22 && day <= 31) || (month == 1 && day >= 1 && day <= 19))
            return ("Capricorn");
        else if ((month == 1 && day >= 20 && day <= 31) || (month == 2 && day >= 1 && day <= 17))
            return ("Aquarius");
        else if ((month == 2 && day >= 18 && day <= 29) || (month == 3 && day >= 1 && day <= 19))
            return ("Pisces");
        else if ((month == 3 && day >= 20 && day <= 31) || (month == 4 && day >= 1 && day <= 19))
            return ("Aries");
        else if ((month == 4 && day >= 20 && day <= 30) || (month == 5 && day >= 1 && day <= 20))
            return ("Taurus");
        else if ((month == 5 && day >= 21 && day <= 31) || (month == 6 && day >= 1 && day <= 20))
            return ("Gemini");
        else if ((month == 6 && day >= 21 && day <= 30) || (month == 7 && day >= 1 && day <= 22))
            return ("Cancer");
        else if ((month == 7 && day >= 23 && day <= 31) || (month == 8 && day >= 1 && day <= 22))
            return ("Leo");
        else if ((month == 8 && day >= 23 && day <= 31) || (month == 9 && day >= 1 && day <= 22))
            return ("Virgo");
        else if ((month == 9 && day >= 23 && day <= 30) || (month == 10 && day >= 1 && day <= 22))
            return ("Libra");
        else if ((month == 10 && day >= 23 && day <= 31) || (month == 11 && day >= 1 && day <= 21))
            return ("Scorpio");
        else if ((month == 11 && day >= 22 && day <= 30) || (month == 12 && day >= 1 && day <= 21))
            return ("Sagittarius");
        else
            return ("Illegal date");
    }
}
