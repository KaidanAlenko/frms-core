package hr.eestec_zg.frmscore.services;

import hr.eestec_zg.frmscore.domain.CompanyRepository;
import hr.eestec_zg.frmscore.domain.EventRepository;
import hr.eestec_zg.frmscore.domain.TaskRepository;
import hr.eestec_zg.frmscore.domain.UserRepository;
import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;
import hr.eestec_zg.frmscore.exceptions.CompanyNotFoundException;
import hr.eestec_zg.frmscore.exceptions.EventNotFoundException;
import hr.eestec_zg.frmscore.exceptions.TaskNotFoundException;
import hr.eestec_zg.frmscore.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final EventRepository eventRepository;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository,
                           CompanyRepository companyRepository, EventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task not defined");
        }
        taskRepository.createTask(task);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task not defined");
        }

        taskRepository.updateTask(task);

    }

    @Override
    public void deleteTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task not defined");
        }
        taskRepository.deleteTask(task);
    }

    @Override
    public void assignToUser(Long userId, Task task) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId not defined");
        }
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        task.setAssignee(user);
        //
    }

    @Override
    public Task getTask(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id not defined");
        }
        Task task = taskRepository.getTask(id);
        if (task == null) {
            throw new TaskNotFoundException();
        }
        return task;
    }

    @Override
    public List<Task> getTasksByAssignee(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId not defined");
        }
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return taskRepository.getTasksByAssignee(user);
    }

    @Override
    public List<Task> getTasksByEvent(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("EventId not defined");
        }
        Event event = eventRepository.getEvent(eventId);
        if (event == null) {
            throw new EventNotFoundException();
        }
        return taskRepository.getTasksByEvent(event);
    }

    @Override
    public List<Task> getTasksByCompany(Long companyId) {
        if (companyId == null) {
            throw new IllegalArgumentException("CompanyId not defined");
        }
        Company company = companyRepository.getCompany(companyId);
        if (company == null) {
            throw new CompanyNotFoundException();
        }
        return taskRepository.getTasksByCompany(company);
    }

    @Override
    public List<Task> getTaskByStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status not defined");
        }
        return taskRepository.getTaskByStatus(status);
    }
}
