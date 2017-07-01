package hr.eestec_zg.frmsbackend;

import hr.eestec_zg.frmscore.domain.dto.TaskStatisticsDto;
import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.CompanyType;
import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.domain.models.Role;
import hr.eestec_zg.frmscore.domain.models.SponsorshipType;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;
import hr.eestec_zg.frmscore.domain.models.dto.TaskDto;
import hr.eestec_zg.frmscore.exceptions.TaskNotFoundException;
import hr.eestec_zg.frmscore.exceptions.UserNotFoundException;
import hr.eestec_zg.frmscore.services.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.DUMMY_VALUE;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_SHORT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_SHORT_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_SHORT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_YEAR_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_FIRST_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_FIRST_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_LAST_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_LAST_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_MAIL_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_MAIL_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TaskServiceTest extends TestBase {

    @Autowired
    private TaskService taskService;

    private Task testTask1;
    private Event testEvent;
    private User testUser1, testUser2;
    private Company company;

    @Before
    public void setTestData() {
        testEvent = new Event(TEST_EVENT_SHORT_NAME_1, TEST_EVENT_SHORT_NAME_1, TEST_EVENT_YEAR_1);
        eventRepository.createEvent(testEvent);

        company = new Company(TEST_COMPANY_NAME_1, TEST_COMPANY_SHORT_NAME_1, CompanyType.COMPUTING);
        companyRepository.createCompany(company);

        testUser1 = new User(
                TEST_USER_FIRST_NAME_1,
                TEST_USER_LAST_NAME_1,
                TEST_USER_MAIL_1,
                DUMMY_VALUE,
                DUMMY_VALUE,
                Role.USER,
                null
        );
        userRepository.createUser(testUser1);

        testTask1 = new Task(
                testEvent, company, testUser1, SponsorshipType.MATERIAL, null, null, null, TaskStatus.IN_PROGRESS, "");
        taskRepository.createTask(testTask1);
    }

    @Test
    public void testCreationOfTask() {
        testUser2 = new User(
                TEST_USER_FIRST_NAME_2,
                TEST_USER_LAST_NAME_2,
                TEST_USER_MAIL_2,
                DUMMY_VALUE,
                DUMMY_VALUE,
                Role.USER,
                null
        );
        userRepository.createUser(testUser2);

        TaskDto taskDto = new TaskDto(
                testEvent.getId(),
                company.getId(),
                testUser2.getId(),
                SponsorshipType.FINANCIAL,
                null, null, null,
                TaskStatus.IN_PROGRESS,
                ""
        );
        Task newTask = taskService.createTask(taskDto);

        List<Task> tasks = taskService.getTasksByCompany(company.getId());

        assertTrue(tasks.size() == 2 && tasks.contains(newTask));
    }

    @Test
    public void testCreationAndDeletionOfTask() {
        testUser2 = new User(
                TEST_USER_FIRST_NAME_2,
                TEST_USER_LAST_NAME_2,
                TEST_USER_MAIL_2,
                DUMMY_VALUE,
                DUMMY_VALUE,
                Role.USER,
                null
        );
        userRepository.createUser(testUser2);

        TaskDto task = new TaskDto(
                testEvent.getId(),
                company.getId(),
                testUser2.getId(),
                SponsorshipType.MATERIAL,
                null, null, null,
                TaskStatus.IN_PROGRESS,
                ""
        );
        Task newTask = taskService.createTask(task);

        taskService.deleteTask(testTask1);

        List<Task> tasks = taskService.getTasksByCompany(company.getId());

        assertTrue(tasks.size() == 1 && tasks.contains(newTask));
    }

    @Test
    public void testUpdatingTask() {
        testUser2 = new User(
                TEST_USER_FIRST_NAME_2,
                TEST_USER_LAST_NAME_2,
                TEST_USER_MAIL_2,
                DUMMY_VALUE,
                DUMMY_VALUE,
                Role.USER,
                null
        );
        userRepository.createUser(testUser2);

        Task task = taskService.getTask(testTask1.getId());
        task.setAssignee(testUser2);

        TaskDto taskDto = new TaskDto(
                task.getEvent().getId(),
                task.getCompany().getId(),
                task.getAssignee().getId(),
                task.getType(),
                task.getCallTime(),
                task.getMailTime(),
                task.getFollowUpTime(),
                task.getStatus(),
                task.getNotes());

        taskService.updateTask(task.getId(), taskDto);

        List<Task> tasks = taskService.getTasksByCompany(company.getId());
        assertTrue(tasks.size() == 1 && tasks.contains(task));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationOfTaskWithoutTaskData() {
        taskService.createTask(null);
    }

    @Test(expected = TaskNotFoundException.class)
    public void testGettingNonExistingTask() {

        taskService.getTask(-1L);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAssigningNonExistingUserToTask() {
        taskService.assignToUser(-1L, testTask1);
    }

    @Test
    public void testAssignToUser() {
        Task testTask2 = new Task(
                testEvent, company, null, SponsorshipType.MATERIAL, null, null, null, TaskStatus.IN_PROGRESS, "");

        taskService.assignToUser(testUser1.getId(), testTask2);

        assertNotNull(testTask2.getAssignee());
    }

    @Test
    public void testGettingTasksByCompany() {
        List<Task> tasks = taskService.getTasksByCompany(company.getId());

        assertEquals(1, tasks.size());
    }

    @Test
    public void testGettingTasksByEvent() {
        List<Task> tasks = taskService.getTasksByEvent(testEvent.getId());

        assertEquals(1, tasks.size());
    }

    @Test
    public void testCreatingTask() {
        List<Company> companies = taskService.getCompaniesForWhichThereAreNoTasksForEvent(testEvent.getId());

        int size = companies.size();

        Company c = new Company(TEST_COMPANY_NAME_2, TEST_COMPANY_SHORT_NAME_2, CompanyType.COMPUTING);
        companyRepository.createCompany(c);

        companies = taskService.getCompaniesForWhichThereAreNoTasksForEvent(testEvent.getId());
        assertEquals(size + 1, companies.size());
    }

    @Test
    public void testGettingStatisticsForTask() {
        TaskStatisticsDto taskStatisticsDto = taskService.getStatistics(testTask1.getAssignee().getId());

        assertEquals(0, (long) taskStatisticsDto.getSuccessful());
        assertEquals(0, (long) taskStatisticsDto.getUnsuccessful());
        assertEquals(1, (long) taskStatisticsDto.getNumberOfEvents());
    }
}