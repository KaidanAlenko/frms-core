package hr.eestec_zg.frmsbackend;

import config.TestAppConfig;
import hr.eestec_zg.frmscore.domain.CompanyRepository;
import hr.eestec_zg.frmscore.domain.EventRepository;
import hr.eestec_zg.frmscore.domain.TaskRepository;
import hr.eestec_zg.frmscore.domain.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class})
@Transactional
public abstract class TestBase {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CompanyRepository companyRepository;
    @Autowired
    protected TaskRepository taskRepository;
    @Autowired
    protected EventRepository eventRepository;
}
