package com.igni.SpringBootProject;

import com.igni.SpringBootProject.controller.CommentController;
import com.igni.SpringBootProject.domain.Comment;
import com.igni.SpringBootProject.domain.CommentType;
import com.igni.SpringBootProject.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void saveComments_HappyPath_ShouldReturnStatus302() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/comment").with(csrf())
                        .with(user("nik")
                                .roles("USER"))
                        .param("plusComment", "Test Plus")
        );

        //then
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(commentService, times(1)).saveAll(anyList());
        verifyNoMoreInteractions(commentService);

    }

    @Test
    public void getComments_HeppyPath_ShouldReturnStatus200() throws Exception {
        //given
        Comment comment = new Comment();
        comment.setComment("Test Plus");
        comment.setType(CommentType.PLUS);
        comment.setCreatedBy("nik");
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Comment comment2 = new Comment();
        comment2.setComment("Test Plus");
        comment2.setType(CommentType.STAR);
        comment2.setCreatedBy("nik2");
        comment2.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = Arrays.asList(comment, comment2);
        when(commentService.getAllCommentsForToday()).thenReturn(comments);

        //when
        ResultActions resultActions = mockMvc.perform(get("/").with(user("nik").roles("USER")));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("comment"))
                .andExpect(model().attribute("plusComments", hasSize(1)))
                .andExpect(model().attribute("plusComments", hasItem(
                        allOf(
                                hasProperty("createdBy", is("nik")),
                                hasProperty("comment", is("Test Plus"))
                        )
                )))
                .andExpect(model().attribute("starComments", hasSize(1)))
                .andExpect(model().attribute("starComments", hasItem(
                        allOf(
                                hasProperty("createdBy", is("nik2")),
                                hasProperty("comment", is("Test Star"))
                        )
                )));

        verify(commentService, times(1)).getAllCommentsForToday();
        verifyNoMoreInteractions(commentService);
    }
}
