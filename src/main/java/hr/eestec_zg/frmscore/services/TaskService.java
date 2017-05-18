package hr.eestec_zg.frmscore.services;


import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.dto.TaskDto;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDto task);

    void updateTask(Long id, TaskDto task);

    void deleteTask(Task task);

    void assignToUser(Long userId, Task task);

    Task getTask(Long id);

    List<Task> getTasksByAssignee(Long userId);

    List<Task> getTasksByEvent(Long eventId);

    List<Task> getTasksByCompany(Long companyId);

    List<Task> getTaskByStatus(TaskStatus status);
}
