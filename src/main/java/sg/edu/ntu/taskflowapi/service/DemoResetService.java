package sg.edu.ntu.taskflowapi.service;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import sg.edu.ntu.taskflowapi.model.Task;

// Only active under the "demo" profile (the public showcase). Wipes whatever
// visitors left on the shared board and seeds a few sample tasks, at start-up
// and then once an hour.
@Service
@Profile("demo")
@EnableScheduling
public class DemoResetService {

    private final TaskService taskService;

    public DemoResetService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void reset() {
        for (Task task : taskService.findAllTasks()) {
            taskService.deleteTask(task.getId());
        }
        taskService.createTask(new Task(null, "Send the revised quotation to the client",
                "Version 3, with the new delivery dates", true));
        taskService.createTask(new Task(null, "Renew the office internet plan",
                "Contract ends next month, compare two providers", false));
        taskService.createTask(new Task(null, "Book a kopi meeting at Lau Pa Sat",
                "Friday 12.30pm, bring the laptop", false));
    }
}
