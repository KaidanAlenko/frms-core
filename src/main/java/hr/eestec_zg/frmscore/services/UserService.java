package hr.eestec_zg.frmscore.services;

import hr.eestec_zg.frmscore.domain.models.Role;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;

import java.util.List;

public interface UserService {
    List<Task> getAssignedTasks(Long userId, TaskStatus status);

    List<User> getAllUsers();
    List<User> getUsersByRole(Role role);
    List<User> getUsersByName(String firstName, String lastName);

    User getUserById(Long id);
    User getUserByPhoneNumber(String phoneNumber);
    User getUserByEmail(String email);
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(Long userId);
    void changePassword(Long userId, String oldPassword, String newPassword);

}
