package hr.eestec_zg.frmscore.domain;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.CompanyType;

import java.util.List;
import java.util.function.Predicate;

public interface CompanyRepository {
    void createCompany(Company company);

    void updateCompany(Company company);

    void deleteCompany(Company company);

    Company getCompany(Long id);

    Company getCompanyByName(String name);

    List<Company> filterCompanies(String name, CompanyType type);

    List<Company> getCompanies(Predicate<Company> condition);

    default List<Company> getCompanies() {
        return getCompanies(c -> true);
    }
}
