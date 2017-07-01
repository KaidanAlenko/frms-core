package hr.eestec_zg.frmscore.services;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.CompanyType;

import java.util.List;

public interface CompanyService {
    void createCompany(Company company);

    void updateCompany(Company company);

    void deleteCompany(Long companyId);

    Company getCompanyByName(String name);

    Company getCompanyById(Long id);

    List<Company> filterCompanies(String name, CompanyType type);

    List<Company> getCompanies();

    List<Company> getCompaniesByType(CompanyType companyType);
}
