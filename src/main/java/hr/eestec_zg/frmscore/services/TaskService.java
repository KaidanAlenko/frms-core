package hr.eestec_zg.frmscore.services;


import hr.eestec_zg.frmscore.domain.dto.TaskStatisticsDto;
import hr.eestec_zg.frmscore.domain.models.SponsorshipType;
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

    List<Task> filterTasks(Integer eventId, Integer companyId, Integer userId, SponsorshipType type, TaskStatus status);

    List<Task> getTasksByEvent(Long eventId);

    List<Task> getTasksByCompany(Long companyId);

    List<Task> getTaskByStatus(TaskStatus status);

    TaskStatisticsDto getStatistics(Long userId);
}
