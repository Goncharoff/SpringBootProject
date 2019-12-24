package com.igni.SpringBootProject;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.igni.SpringBootProject.domain.User;
import com.igni.SpringBootProject.repo.UserRepository;
import com.igni.SpringBootProject.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void init() {
        this.userService = new UserService(userRepository);
    }

    @Test
    public void getAllCommentsForToday_HappyPath_ShoulsReturn1Comment() {
        //given
        User user = new User();
        user.setUsername("nik");
        user.setPassword("password");
        user.setRole("USER");

        when(userRepository.findByUsername("nik")).thenReturn(user);

        //when
        UserDetails actual = userService.loadUserByUsername("nik");

        //then
        verify(userRepository, times(1)).findByUsername("nik");
    }
}
