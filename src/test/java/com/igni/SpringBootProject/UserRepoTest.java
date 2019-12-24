package com.igni.SpringBootProject;

import static org.assertj.core.api.Assertions.assertThat;

import com.igni.SpringBootProject.domain.User;
import com.igni.SpringBootProject.repo.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepoTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_HappyPath_ShouldReturn1User() throws Exception {
        //given
        User user = new User();
        user.setUsername("nik");
        user.setPassword("password");
        user.setRole("USER");
        testEntityManager.persist(user);
        testEntityManager.flush();

        //when
        User actual = userRepository.findByUsername("nik");

        //then
        assertThat(actual).isEqualTo(user);

    }

    @Test
    public void save_HappyPatyh_ShouldSave1User() throws Exception {
        //given
        User user = new User();
        user.setUsername("nik");
        user.setPassword("password");
        user.setRole("USER");

        //when
        User actual = userRepository.save(user);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
    }
}
