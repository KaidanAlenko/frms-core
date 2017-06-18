package hr.eestec_zg.frmscore.domain;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.domain.models.SponsorshipType;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;

import java.util.List;
import java.util.function.Predicate;

public interface TaskRepository {
    void createTask(Task task);

    void updateTask(Task task);

    void deleteTask(Task task);

    Task getTask(Long id);

    List<Task> getTasksByAssignee(User user);

    List<Task> getTasksByAssignee(User user, TaskStatus status);

    List<Task> getTasksByEvent(Event event);

    List<Task> getTasksByCompany(Company company);

    List<Task> getTaskByStatus(TaskStatus status);

    List<Task> getTasks(Predicate<Task> condition);

    List<Task> filterTasks(Integer eventId, Integer companyId, SponsorshipType type, TaskStatus status);

    Long countTasksByStatus(Long userId, TaskStatus status);

    Long countDistinctEventsOfUser(Long userId);

    default List<Task> getTasks() {
        return getTasks(t -> true);
    }
}
