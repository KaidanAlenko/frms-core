package hr.eestec_zg.frmsbackend;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.CompanyType;
import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.domain.models.Role;
import hr.eestec_zg.frmscore.domain.models.SponsorshipType;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;
import hr.eestec_zg.frmscore.exceptions.UserNotFoundException;
import hr.eestec_zg.frmscore.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_SHORT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_SHORT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_YEAR_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_FIRST_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_FIRST_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_FIRST_NAME_3;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_LAST_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_LAST_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_LAST_NAME_3;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_MAIL_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_MAIL_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_USER_MAIL_3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserServiceTest extends TestBase {

    @Autowired
    private UserService userService;

    private Company testCompany1;
    private Event testEvent1;
    private User testUser1;
    private User testUser2;
    private User testUser3;

    @Before
    public void setTestData() {
        testCompany1 = new Company(TEST_COMPANY_NAME_1, TEST_COMPANY_SHORT_NAME_1, CompanyType.COMPUTING);
        companyRepository.createCompany(testCompany1);

        testEvent1 = new Event(TEST_EVENT_NAME_1, TEST_EVENT_SHORT_NAME_1, TEST_EVENT_YEAR_1);
        eventRepository.createEvent(testEvent1);

        testUser1 = new User(
                TEST_USER_FIRST_NAME_1,
                TEST_USER_LAST_NAME_1,
                TEST_USER_MAIL_1,
                "pass1",
                "0001",
                Role.USER,
                null
        );
        userRepository.createUser(testUser1);

        testUser2 = new User(
                TEST_USER_FIRST_NAME_2,
                TEST_USER_LAST_NAME_2,
                TEST_USER_MAIL_2,
                "pass2",
                "0002",
                Role.COORDINATOR,
                null
        );
        userRepository.createUser(testUser2);

        testUser3 = new User(
                TEST_USER_FIRST_NAME_3,
                TEST_USER_LAST_NAME_3,
                TEST_USER_MAIL_3,
                "pass3",
                "0003",
                Role.COORDINATOR,
                null
        );
        userRepository.createUser(testUser3);
    }

    @Test
    public void testGettingAllTasksForAssignee() {
        // resources
        Task task = new Task(
                testEvent1,
                testCompany1,
                testUser1,
                SponsorshipType.FINANCIAL,
                null, null, null,
                TaskStatus.IN_PROGRESS,
                ""
        );
        taskRepository.createTask(task);
        // method
        User u = userRepository.getUserByEmail(TEST_USER_MAIL_1);
        List<Task> tasks = userService.getAssignedTasks(u.getId(), TaskStatus.IN_PROGRESS);
        // check
        assertEquals(1, tasks.size());
    }

    @Test
    public void testGettingAllTasksForNonExistingAssignee() {
        // resources
        Task task = new Task(
                testEvent1,
                testCompany1,
                null,
                SponsorshipType.FINANCIAL,
                null, null, null,
                TaskStatus.IN_PROGRESS,
                ""
        );
        taskRepository.createTask(task);
        // method
        User testUser = userRepository.getUserByEmail(TEST_USER_MAIL_1);
        List<Task> tasks = userService.getAssignedTasks(testUser.getId(), TaskStatus.IN_PROGRESS);
        // check
        assertEquals(0, tasks.size());
    }


    @Test
    public void testGettingAllUsers() {
        List<User> users = userService.getAllUsers();

        assertTrue(
                users.size() == 3 &&
                users.contains(testUser2) &&
                users.contains(testUser1) &&
                users.contains(testUser3));
    }

    @Test
    public void testGettingUsersByName() {
        List<User> usersByFullName = userService.getUsersByName(TEST_USER_FIRST_NAME_1, TEST_USER_LAST_NAME_1);
        List<User> usersByFirstName = userService.getUsersByName(TEST_USER_FIRST_NAME_1, null);
        List<User> usersByLastName = userService.getUsersByName(null, TEST_USER_LAST_NAME_1);

        assertTrue(usersByFullName.size() == 2 && usersByFullName.contains(testUser1));
        assertTrue(
                usersByFirstName.size() == 2 &&
                usersByFirstName.contains(testUser1) &&
                usersByFirstName.contains(testUser2));
        assertTrue(
                usersByLastName.size() == 2 &&
                usersByLastName.contains(testUser1));
    }

    @Test
    public void testGettingUsersByRole() {
        List<User> usersUser = userService.getUsersByRole(Role.USER);
        List<User> usersCoordinator = userService.getUsersByRole(Role.COORDINATOR);
        List<User> usersAdmin = userService.getUsersByRole(Role.ADMIN);

        assertTrue(usersUser.size() == 1 && usersUser.contains(testUser1));
        assertTrue(
                usersCoordinator.size() == 2 &&
                usersCoordinator.contains(testUser2) &&
                usersCoordinator.contains(testUser3));
        assertTrue(usersAdmin.size() == 0);
    }

    @Test
    public void testGettingUserById() {
        userRepository.createUser(testUser1);
        User testUser = userService.getUserById(testUser1.getId());

        assertTrue(testUser.getId() == testUser1.getId());
    }

    @Test
    public void testGettingUserByEmail() {
        userRepository.createUser(testUser1);
        User testUser = userService.getUserByEmail(TEST_USER_MAIL_1);

        assertTrue(testUser1.getEmail().equals(testUser.getEmail()));
    }

    @Test
    public void testGettingUserByPhoneNumber() {
        userRepository.createUser(testUser1);
        User testUser = userService.getUserByPhoneNumber("0001");

        assertTrue(testUser1.getPhoneNumber().equals(testUser.getPhoneNumber()));

    }

    @Test
    public void testCreationOfUser() {
        testUser1 = new User("Fafa", "Fufa", "em11aial1@ss", "pass1", "0001", Role.USER, null);
        userService.createUser(testUser1);

        assertEquals("em11aial1@ss", userService.getUserByEmail("em11aial1@ss").getEmail());
    }

    @Test
    public void testUpdatingUser() {
        testUser1 = userService.getUserByEmail(TEST_USER_MAIL_1);
        testUser1.setEmail("asdasdasdasd@haha");

        userService.updateUser(testUser1);

        assertEquals("asdasdasdasd@haha", userService.getUserByEmail("asdasdasdasd@haha").getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeletingNonExistingUser() {

        userService.deleteUser(-1L);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdatingNonExistingUser() {
        testUser1.setId(-1L);

        testUser1.setFirstName("asdasd");
        userService.updateUser(testUser1);
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeletingUser() {
        userService.deleteUser(userService.getUserByEmail(TEST_USER_MAIL_1).getId());
        assertEquals(null, userService.getUserByEmail("asdasdasdasd@haha").getEmail());
    }

    @Test
    public void testChangingUserPassword() {
        testUser1 = userService.getUserByEmail(TEST_USER_MAIL_1);

        userService.changePassword(testUser1.getId(), "pass1", "blabla");

        testUser2 = userService.getUserByEmail(TEST_USER_MAIL_1);

        assertEquals("blabla", testUser2.getPassword());
    }
}
