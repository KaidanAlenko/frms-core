package hr.eestec_zg.frmscore.domain;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.domain.models.SponsorshipType;
import hr.eestec_zg.frmscore.domain.models.Task;
import hr.eestec_zg.frmscore.domain.models.TaskStatus;
import hr.eestec_zg.frmscore.domain.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@Transactional
public class DatabaseBackedTaskRepository extends AbstractRepository<Long, Task> implements TaskRepository {

    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String ASSIGNEE = "assignee";
    private static final String EVENT = "event";
    private static final String COMPANY = "company";
    private static final String TYPE = "type";

    @Override
    public void createTask(Task task) {
        persist(task);
    }

    @Override
    public void updateTask(Task task) {
        update(task);
    }

    @Override
    public void deleteTask(Task task) {
        delete(task);
    }

    @Override
    public Task getTask(Long id) {
        return getByKey(id);
    }

    @Override
    public List<Task> getTasksByAssignee(User user) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        query.where(cb.equal(root.get(ASSIGNEE).as(User.class), user));
        return getTasks(query.select(root));
    }

    @Override
    public List<Task> getTasksByAssignee(User user, TaskStatus status) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        query.where(cb.and(
                cb.equal(root.get(ASSIGNEE).as(User.class), user),
                cb.equal(root.get(STATUS).as(TaskStatus.class), status)
        ));
        return getTasks(query.select(root));
    }

    @Override
    public List<Task> getTasksByEvent(Event event) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        query.where(cb.equal(root.get(EVENT).as(Event.class), event));
        return getTasks(query.select(root));
    }

    @Override
    public List<Long> getCompanyIdsByEventId(Long eventId) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<Task> root = query.from(Task.class);

        query.where(cb.equal(root.get(EVENT).get(ID).as(Long.class), eventId));

        query.select(cb.construct(Long.class, root.get(COMPANY).get(ID)));
        query.distinct(true);

        return getSession().createQuery(query).getResultList();
    }

    @Override
    public List<Task> getTasksByCompany(Company company) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        query.where(cb.equal(root.get(COMPANY).as(Company.class), company));
        return getTasks(query.select(root));
    }

    @Override
    public List<Task> getTaskByStatus(TaskStatus status) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        query.where(cb.equal(root.get(STATUS).as(TaskStatus.class), status));
        return getTasks(query.select(root));
    }

    @Override
    public List<Task> getTasks(Predicate<Task> condition) {
        CriteriaQuery<Task> query = criteriaBuilder().createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        return getTasks(query.select(root))
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> filterTasks(
            Integer eventId, Integer companyId, Integer userId, SponsorshipType type, TaskStatus status) {

        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);

        Root<Task> root = query.from(Task.class);

        query.where(
                cb.and(
                        cb.or(
                                cb.equal(cb.literal(eventId == null), true),
                                cb.equal(root.get(EVENT).get(ID).as(Integer.class), eventId)
                        ),
                        cb.or(
                                cb.equal(cb.literal(companyId == null), true),
                                cb.equal(root.get(COMPANY).get(ID).as(Integer.class), companyId)
                        ),
                        cb.or(
                                cb.equal(cb.literal(userId == null), true),
                                cb.equal(root.get(ASSIGNEE).get(ID).as(Integer.class), userId)
                        ),
                        cb.or(
                                cb.equal(cb.literal(type == null), true),
                                cb.equal(root.get(TYPE).as(SponsorshipType.class), type)
                        ),
                        cb.or(
                                cb.equal(cb.literal(status == null), true),
                                cb.equal(root.get(STATUS).as(TaskStatus.class), status)
                        )
                )
        );

        return getSession().createQuery(query).getResultList();
    }

    @Override
    public Long countTasksByStatus(Long userId, TaskStatus status) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<Task> root = query.from(Task.class);
        query.select(cb.count(root));
        query.where(
                cb.and(
                        cb.equal(root.get(STATUS).as(TaskStatus.class), status),
                        cb.equal(root.get(ASSIGNEE).get(ID).as(Long.class), userId)
                )
        );

        return getSession().createQuery(query).getSingleResult();
    }

    @Override
    public Long countDistinctEventsOfUser(Long userId) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<Task> root = query.from(Task.class);
        query.select(cb.countDistinct(root));

        query.where(cb.equal(root.get(ASSIGNEE).get(ID).as(Long.class), userId));

        return getSession().createQuery(query).getSingleResult();
    }

    private Task getTask(CriteriaQuery<Task> query) {
        try {
            return getSession().createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    private List<Task> getTasks(CriteriaQuery<Task> query) {
        return getSession().createQuery(query).getResultList();
    }

}
