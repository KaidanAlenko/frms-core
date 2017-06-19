package hr.eestec_zg.frmscore.domain;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.CompanyType;
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
public class DatabaseBackedCompanyRepository extends AbstractRepository<Long, Company> implements CompanyRepository {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SHORT_NAME = "shortName";
    private static final String TYPE = "type";

    @Override
    public void createCompany(Company company) {
        persist(company);
    }

    @Override
    public void updateCompany(Company company) {
        update(company);
    }

    @Override
    public void deleteCompany(Company company) {
        delete(company);
    }

    @Override
    public Company getCompany(Long id) {
        return getByKey(id);
    }

    @Override
    public Company getCompanyByName(String name) {
        String searchTerm = "%" + name.toLowerCase() + "%";
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Company> query = cb.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        query.where(
                cb.or(
                        cb.like(cb.lower(root.get(NAME)), searchTerm),
                        cb.like(cb.lower(root.get(SHORT_NAME)), searchTerm)));
        return getCompany(query.select(root));
    }

    @Override
    public List<Company> filterCompanies(String name, CompanyType type) {
        String searchTerm = null;
        if (name != null) {
            searchTerm = "%" + name.toLowerCase() + "%";
        }

        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Company> query = cb.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);

        query.where(
                cb.and(
                        cb.or(
                                cb.equal(cb.literal(name == null), true),
                                cb.like(cb.lower(root.get(NAME)).as(String.class), searchTerm),
                                cb.like(cb.lower(root.get(SHORT_NAME)).as(String.class), searchTerm)
                        ),
                        cb.or(
                                cb.equal(cb.literal(type == null), true),
                                cb.equal(root.get(TYPE).as(CompanyType.class), type)
                        )
                )
        );

        return getSession().createQuery(query).getResultList();
    }

    @Override
    public List<Company> getCompanies(Predicate<Company> condition) {
        CriteriaQuery<Company> query = criteriaBuilder().createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        return getCompanies(query.select(root))
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> getCompaniesWhichAreNotInIdList(List<Long> companyIds) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Company> query = cb.createQuery(Company.class);

        Root<Company> root = query.from(Company.class);

        query.where(
                cb.not(root.get(ID).in(companyIds))
        );

        return getSession().createQuery(query).getResultList();
    }

    private Company getCompany(CriteriaQuery<Company> query) {
        try {
            return getSession().createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    private List<Company> getCompanies(CriteriaQuery<Company> query) {
        return getSession().createQuery(query).getResultList();
    }
}
