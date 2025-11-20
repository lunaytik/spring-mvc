package lunaytik.web.controller;

import jakarta.validation.Valid;
import lunaytik.web.domain.Task;
import lunaytik.web.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task(null, "", false));
        return "tasks/new";
    }

    @PostMapping
    public String createTask(@Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String editTask(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long id, @Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        task.setId(id);
        taskService.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

}
