package sg.edu.ntu.taskflowapi.controller;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sg.edu.ntu.taskflowapi.service.TaskService;

// Only active under the "demo" profile (the public showcase). Caps the shared
// in-memory board so visitors cannot fill it up without limit.
@Component
@Profile("demo")
public class DemoLimitFilter extends OncePerRequestFilter {

    static final int MAX_TASKS = 30;

    private final TaskService taskService;

    public DemoLimitFilter(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        boolean creating = "POST".equals(request.getMethod())
                && "/api/tasks".equals(request.getRequestURI());
        if (creating && taskService.findAllTasks().size() >= MAX_TASKS) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"The demo board is full (" + MAX_TASKS + " tasks). It resets every hour.\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
