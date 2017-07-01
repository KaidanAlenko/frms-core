package hr.eestec_zg.frmsbackend;

import hr.eestec_zg.frmscore.domain.models.Company;
import hr.eestec_zg.frmscore.domain.models.CompanyType;
import hr.eestec_zg.frmscore.exceptions.CompanyNotFoundException;
import hr.eestec_zg.frmscore.services.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.DUMMY_VALUE;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_NAME_3;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_SHORT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_COMPANY_SHORT_NAME_2;
import static hr.eestec_zg.frmscore.domain.models.CompanyType.COMPUTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompanyServiceTest extends TestBase {

    @Autowired
    private CompanyService companyService;

    private Company testCompany1;
    private Company testCompany2;

    @Before
    public void setTestData() {
        testCompany1 = new Company(TEST_COMPANY_NAME_1, TEST_COMPANY_SHORT_NAME_1, COMPUTING);
        Company company2 = new Company(TEST_COMPANY_NAME_2, TEST_COMPANY_SHORT_NAME_2, COMPUTING);

        companyRepository.createCompany(testCompany1);
        companyRepository.createCompany(company2);
    }

    @Test
    public void testGettingCompanyByName() {
        Company testCompany = companyService.getCompanyByName(TEST_COMPANY_NAME_1);

        assertNotNull(testCompany);
    }

    @Test(expected = CompanyNotFoundException.class)
    public void testGettingNonExistingCompanyByName() {
        companyService.getCompanyByName("pasn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeletingCompanyWithoutSendingId() {
        companyService.deleteCompany(null);
    }

    @Test(expected = CompanyNotFoundException.class)
    public void testDeletingNonExistingCompany() {
        Company testCompany = companyService.getCompanyByName("pasn");

        companyService.deleteCompany(testCompany.getId());
    }

    @Test(expected = CompanyNotFoundException.class)
    public void testUpdatingNonExistingCompany() {
        Company testCompany = new Company("spaasdasdadn", TEST_COMPANY_SHORT_NAME_1, COMPUTING);

        companyService.updateCompany(testCompany);
    }

    @Test
    public void testUpdatingCompany() {
        Company testCompany = companyService.getCompanyByName(TEST_COMPANY_NAME_1);
        testCompany.setAddress("asfadsfas");
        companyService.updateCompany(testCompany);

        Company company = companyService.getCompanyByName(TEST_COMPANY_NAME_1);
        String address = company.getAddress();
        String shortName = company.getShortName();

        assertEquals(address, "asfadsfas");
        assertEquals(shortName, TEST_COMPANY_SHORT_NAME_1);
    }

    @Test
    public void testGettingCompaniesByType() {
        testCompany2 = new Company(
                TEST_COMPANY_NAME_3,
                TEST_COMPANY_SHORT_NAME_2,
                DUMMY_VALUE,
                DUMMY_VALUE,
                DUMMY_VALUE,
                CompanyType.AUTOMATIZATION
        );

        companyService.createCompany(testCompany2);

        List<Company> companies = companyService.getCompaniesByType(CompanyType.AUTOMATIZATION);
        testCompany1 = companyService.getCompanyById(testCompany2.getId());

        assertEquals(1, companies.size());
        assertEquals(testCompany1, testCompany2);
        assertTrue(
                "There is no testCompany1 with name " + testCompany2.getName() + " stored",
                companies.contains(testCompany2));
    }

    @Test
    public void testGetCompaniesByTypeWhenThereAreNoCompaniesOfThatType() {
        List<Company> companies = companyService.getCompaniesByType(CompanyType.AUTOMATIZATION);

        assertEquals(0, companies.size());
    }

    @Test
    public void testGettingCompanies() {
        List<Company> companies = companyService.getCompanies();

        assertEquals(2, companies.size());
    }

    @Test
    public void testFilteringCompanies() {
        List<Company> companies = companyService.filterCompanies(TEST_COMPANY_NAME_1, COMPUTING);

        assertEquals(1, companies.size());
    }

    @Test
    public void testCreationOfCompany() {
        testCompany2 = new Company(
                TEST_COMPANY_NAME_3,
                TEST_COMPANY_SHORT_NAME_2,
                DUMMY_VALUE,
                DUMMY_VALUE,
                DUMMY_VALUE,
                CompanyType.AUTOMATIZATION
        );

        companyService.createCompany(testCompany2);

        List<Company> companies = companyService.getCompaniesByType(CompanyType.AUTOMATIZATION);

        assertTrue(companies.size() == 1 && companies.contains(testCompany2));
    }

    @Test
    public void testCreatingAndDeletingCompany() {
        Long companyId = testCompany1.getId();

        companyService.deleteCompany(companyId);

        companyId++;
        companyService.deleteCompany(companyId);

        List<Company> companies = companyService.getCompaniesByType(CompanyType.COMPUTING);

        assertEquals(0, companies.size());

        testCompany2 = new Company(
                TEST_COMPANY_NAME_3,
                TEST_COMPANY_SHORT_NAME_2,
                DUMMY_VALUE,
                DUMMY_VALUE,
                DUMMY_VALUE,
                CompanyType.COMPUTING
        );

        companyService.createCompany(testCompany2);

        companies = companyService.getCompaniesByType(CompanyType.COMPUTING);

        assertTrue(companies.size() == 1 && companies.contains(testCompany2));
    }

}
