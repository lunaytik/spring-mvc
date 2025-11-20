package lunaytik.web.controller;

import lunaytik.web.domain.Task;
import lunaytik.web.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldDisplayTaskList()  throws Exception {
        when(taskService.findAll()).thenReturn(List.of(
                new Task(1L, "Learn Spring", false)
        ));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attributeExists("tasks"));
    }

    @Test
    void shouldShowNewTaskForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        Task newTask = new Task(null, "Write TDD tests", false);

        when(taskService.save(any(Task.class))).thenReturn(new Task(1L, "Write TDD tests", false));

        mockMvc.perform(post("/tasks")
                .param("title", "Write TDD tests"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService).save(any(Task.class));
    }

    @Test
    void shouldShowEditTaskForm() throws Exception {
        Task existingTask = new Task(1L, "Learn Spring", false);
        when(taskService.findById(1L)).thenReturn(existingTask);

        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", existingTask));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteById(1L);

        mockMvc.perform(post("/tasks/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService).deleteById(1L);
    }

    @Test
    void shouldRejectEmptyTitleOnCreate() throws Exception {
        mockMvc.perform(post("/tasks")
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().attributeHasFieldErrors("task", "title"));
    }

    @Test
    void shouldRejectEmptyTitleOnUpdate() throws Exception {
        mockMvc.perform(post("/tasks/1")
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"))
                .andExpect(model().attributeHasFieldErrors("task", "title"));
    }
}
