package sg.edu.ntu.taskflowapi.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import sg.edu.ntu.taskflowapi.model.Task;
import sg.edu.ntu.taskflowapi.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ChatClient chatClient;

    // Constructor injection: Spring passes in the repository and the
    // auto-configured ChatClient.Builder (Task 6).
    public TaskService(TaskRepository taskRepository, ChatClient.Builder chatClientBuilder) {
        this.taskRepository = taskRepository;
        this.chatClient = chatClientBuilder.build();
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " not found");
        }
        return task;
    }

    public Task createTask(Task task) {
        // Ignore any id sent by the client; the repository assigns one.
        task.setId(null);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        Task existing = findTaskById(id);
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setCompleted(task.isCompleted());
        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        findTaskById(id); // 404 if it does not exist
        taskRepository.deleteById(id);
    }

    public Task markTaskAsComplete(Long id) {
        Task task = findTaskById(id);
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    // Task 6: send the task list to the model and return a short summary.
    public String summarizeTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return "There are no tasks yet.";
        }

        StringBuilder taskList = new StringBuilder();
        for (Task task : tasks) {
            taskList.append("- [")
                    .append(task.isCompleted() ? "done" : "pending")
                    .append("] ")
                    .append(task.getTitle());
            if (task.getDescription() != null && !task.getDescription().isBlank()) {
                taskList.append(": ").append(task.getDescription());
            }
            taskList.append("\n");
        }

        String prompt = """
                Here is my task list:
                %s
                Write a short plain-English summary (2 to 3 sentences) of what is done \
                and what is still pending.
                """.formatted(taskList);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
