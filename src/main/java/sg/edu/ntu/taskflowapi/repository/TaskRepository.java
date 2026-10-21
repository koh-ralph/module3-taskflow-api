package sg.edu.ntu.taskflowapi.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import sg.edu.ntu.taskflowapi.model.Task;

@Repository
public class TaskRepository {

    // In-memory store keyed by task id. Initialised here so it is never null.
    private final Map<Long, Task> tasks = new HashMap<>();

    // Simple counter used to generate ids.
    private Long nextId = 1L;

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    // Returns null when there is no task with this id.
    public Task findById(Long id) {
        return tasks.get(id);
    }

    // Saves a new task (assigns an id) or replaces an existing one.
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public void deleteById(Long id) {
        tasks.remove(id);
    }
}
