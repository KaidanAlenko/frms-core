package hr.eestec_zg.frmscore.services;

import hr.eestec_zg.frmscore.domain.CompanyRepository;
import hr.eestec_zg.frmscore.domain.EventRepository;
import hr.eestec_zg.frmscore.domain.TaskRepository;
import hr.eestec_zg.frmscore.domain.UserRepository;
import hr.eestec_zg.frmscore.domain.dto.TaskStatisticsDto;
import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.domain.models.SponsorshipType;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;
import hr.eestec_zg.frmscore.domain.models.dto.TaskDto;
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

    private static final String TASK_NOT_DEFINED_MESSAGE = "Task not defined";

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
    public Task createTask(TaskDto task) {
        if (task == null) {
            throw new IllegalArgumentException(TASK_NOT_DEFINED_MESSAGE);
        }
        User user = userRepository.getUser(task.getUserId());
        Company company = companyRepository.getCompany(task.getCompanyId());
        if (company == null) {
            throw new IllegalArgumentException("Company does not exist");
        }
        Event event = eventRepository.getEvent(task.getEventId());
        if (event == null) {
            throw new IllegalArgumentException("Event does not exist");
        }

        Task taskT = new Task(event, company, user, task.getType(), task.getCallTime(), task.getMailTime(), task.getFollowUpTime(), task.getStatus(), task.getNotes());
        taskRepository.createTask(taskT);
        return taskT;
    }

    @Override
    public void updateTask(Long id, TaskDto task) {
        if (task == null) {
            throw new IllegalArgumentException(TASK_NOT_DEFINED_MESSAGE);
        }

        Task oldTask = this.taskRepository.getTask(id);
        if (oldTask == null) {
            throw new TaskNotFoundException();
        }
        User user = null;
        Long assigneeId = task.getUserId();
        if (assigneeId != null) {
            user = this.userRepository.getUser(assigneeId);
        }
        Company company = this.companyRepository.getCompany(task.getCompanyId());
        if (company == null) {
            throw new CompanyNotFoundException();
        }
        Event event = this.eventRepository.getEvent(task.getEventId());
        if (event == null) {
            throw new EventNotFoundException();
        }

        oldTask.setEvent(event);
        oldTask.setCompany(company);
        oldTask.setAssignee(user);
        oldTask.setType(task.getType());
        oldTask.setCallTime(task.getCallTime());
        oldTask.setMailTime(task.getMailTime());
        oldTask.setFollowUpTime(task.getFollowUpTime());
        oldTask.setStatus(task.getStatus());
        oldTask.setNotes(task.getNotes());

        taskRepository.updateTask(oldTask);

    }

    @Override
    public void deleteTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException(TASK_NOT_DEFINED_MESSAGE);
        }
        taskRepository.deleteTask(task);
    }

    @Override
    public void assignToUser(Long userId, Task task) {
        if (userId == null) {
            throw new IllegalArgumentException("User id not defined");
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
            throw new IllegalArgumentException("User id not defined");
        }
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return taskRepository.getTasksByAssignee(user);
    }

    @Override
    public List<Task> filterTasks(
            Integer eventId, Integer companyId, Integer userId, SponsorshipType type, TaskStatus status) {

        return this.taskRepository.filterTasks(eventId, companyId, userId, type, status);
    }

    @Override
    public List<Task> getTasksByEvent(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event id not defined");
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
            throw new IllegalArgumentException("Company id not defined");
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

    @Override
    public List<Company> getCompaniesForWhichThereAreNoTasksForEvent(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event id not defined");
        }

        List<Long> companyIds = taskRepository.getCompanyIdsByEventId(eventId);

        if (!companyIds.isEmpty()) {
            return companyRepository.getCompaniesWhichAreNotInIdList(companyIds);
        } else {
            return companyRepository.getCompanies();
        }
    }

    @Override
    public TaskStatisticsDto getStatistics(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id not defined");
        }
        Long successfulTasks = this.taskRepository.countTasksByStatus(userId, TaskStatus.ACCEPTED);
        Long unsuccessfulTasks = this.taskRepository.countTasksByStatus(userId, TaskStatus.DECLINED);
        Long numberOfEvents = this.taskRepository.countDistinctEventsOfUser(userId);

        return new TaskStatisticsDto(successfulTasks, unsuccessfulTasks, numberOfEvents);
    }
}