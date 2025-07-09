package com.example.bankcards;
import com.example.bankcards.model.*;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.security.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
public class AbstractTest {

    protected final static String phoneNumber = "9319206789";
    protected final static String adminNumber = "9319206790";

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtTokenService jwtTokenService;

    @Autowired
    protected PasswordEncoder encoder;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2-alpine"))
                    .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

    }

    @BeforeEach
    void setUp(){

        userRepository.saveAll(new ArrayList<>(List.of(createUser())));
    }


    private User[] createUser(){

        Role roleUser = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        User user = User.builder()
                .firstName("Json")
                .lastName("Do")
                .patronymic("Aslo")
                .phoneNumber("9319206789")
                .password(encoder.encode("12121212121"))
                .registerTime(LocalDateTime.now())
                .roles(Collections.singletonList(roleUser))
                .build();
        roleUser.setUser(user);
        Card[] cards = cardUser(user);
        user.setCards(new ArrayList<>(List.of(cards)));

        Role roleUser2 = Role.builder()
                .roleType(RoleType.ROLE_ADMIN)
                .build();
        User user2 = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .patronymic("Johnson")
                .phoneNumber("9319206790")
                .password(encoder.encode("password123"))
                .registerTime(LocalDateTime.now())
                .roles(Collections.singletonList(roleUser2))
                .build();
        roleUser2.setUser(user2);

        Role roleUser3 = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        User user3 = User.builder()
                .firstName("Bob")
                .lastName("Brown")
                .patronymic("Lee")
                .phoneNumber("9319206791")
                .password(encoder.encode("qwerty456"))
                .registerTime(LocalDateTime.now())
                .roles(Collections.singletonList(roleUser3))
                .build();
        roleUser3.setUser(user3);

        Role roleUser4 = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        User user4 = User.builder()
                .firstName("Emma")
                .lastName("Wilson")
                .patronymic("Grace")
                .phoneNumber("9319206792")
                .password(encoder.encode("emma789"))
                .registerTime(LocalDateTime.now())
                .roles(Collections.singletonList(roleUser4))
                .build();
        roleUser4.setUser(user4);
        Card[] card1 = cardUser2(user4);
        user4.setCards(new ArrayList<>(List.of(card1)));

        Role roleUser5 = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        User user5 = User.builder()
                .firstName("David")
                .lastName("Taylor")
                .patronymic("James")
                .phoneNumber("9319206793")
                .password(encoder.encode("david101"))
                .registerTime(LocalDateTime.now())
                .roles(Collections.singletonList(roleUser5))
                .build();
        roleUser5.setUser(user5);

        Role roleUser6 = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        User user6 = User.builder()
                .firstName("Sophia")
                .lastName("Anderson")
                .patronymic("Rose")
                .phoneNumber("9319206794")
                .password(encoder.encode("sophia202"))
                .registerTime(LocalDateTime.now())
                .roles(Collections.singletonList(roleUser6))
                .build();
        roleUser6.setUser(user6);
        return new User[]{user,user2,user3,user4,user5,user6};
    }

    private Card[] cardUser(User user){
        return new Card[]{
                Card.builder()
                        .user(user)
                        .score(new BigDecimal(2000000))
                        .numberCard("1111 1111 1111 1111")
                        .paymentSystem(PaymentSystem.VISA)
                        .isActive(new Random().nextBoolean())
                        .build(),

                Card.builder()
                        .user(user)
                        .score(new BigDecimal(300000))
                        .numberCard("2222 2222 2222 2222")
                        .paymentSystem(PaymentSystem.MASTER_CARD)
                        .isActive(new Random().nextBoolean())
                        .build(),

                Card.builder()
                        .user(user)
                        .score(new BigDecimal(4000000))
                        .numberCard("3333 3333 3333 3333")
                        .paymentSystem(PaymentSystem.MIR)
                        .isActive(new Random().nextBoolean())
                        .build()
        };
    }

    private Card[] cardUser2(User user){
        return new Card[]{
                Card.builder()
                        .user(user)
                        .score(new BigDecimal(345000))
                        .numberCard("4444 4444 4444 4444")
                        .paymentSystem(PaymentSystem.VISA)
                        .isActive(new Random().nextBoolean())
                        .build(),

                Card.builder()
                        .user(user)
                        .score(new BigDecimal(450000))
                        .numberCard("5555 5555 5555 5555")
                        .paymentSystem(PaymentSystem.MASTER_CARD)
                        .isActive(new Random().nextBoolean())
                        .build(),

                Card.builder()
                        .user(user)
                        .score(new BigDecimal(343434343))
                        .numberCard("6666 6666 6666 6666")
                        .paymentSystem(PaymentSystem.MIR)
                        .isActive(new Random().nextBoolean())
                        .build()
        };
    }
}
