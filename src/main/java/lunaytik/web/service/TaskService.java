package lunaytik.web.service;

import lunaytik.web.domain.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();
    Task save(Task task);
    Task findById(Long id);
    void deleteById(Long id);
}
