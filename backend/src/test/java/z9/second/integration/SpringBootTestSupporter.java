package z9.second.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import z9.second.domain.authentication.service.AuthenticationService;
import z9.second.global.redis.RedisRepository;
import z9.second.domain.favorite.repository.FavoriteRepository;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.classes.repository.ClassUserRepository;
import z9.second.domain.schedules.service.SchedulesService;
import z9.second.model.schedules.SchedulesRepository;
import z9.second.model.user.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class SpringBootTestSupporter {

    /**
     * mock Mvc
     */
    @Autowired
    public MockMvc mockMvc;

    /**
     * repository
     */
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected FavoriteRepository favoriteRepository;


    /**
     * service
     */
    @Autowired
    protected AuthenticationService authenticationService;

    /**
     * Common
     */
    @Autowired
    protected EntityManager em;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ClassRepository classRepository;

    @Autowired
    protected ClassUserRepository classUserRepository;

    @Autowired
    protected SchedulesRepository schedulesRepository;

    @Autowired
    protected SchedulesService schedulesService;

    /**
     * Mocking
     */
    @MockitoBean
    private RedisRepository redisRepository;
}
