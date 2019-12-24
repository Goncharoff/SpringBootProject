package com.igni.SpringBootProject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.igni.SpringBootProject.domain.Comment;
import com.igni.SpringBootProject.domain.CommentType;
import com.igni.SpringBootProject.repo.CommentRepository;
import com.igni.SpringBootProject.service.CommentService;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    private CommentService commentService;

    @Before
    public void init() {
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void getAllCommentsForToday_HappyPath_ShouldReturn1Comment() {

        //given
        Comment comment = new Comment();
        comment.setComment("Test");
        comment.setType(CommentType.PLUS);
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        List<Comment> commentList = Arrays.asList(comment);
        LocalDate now = LocalDate.now();

        when(commentRepository.findByCreatedYearAndMonthAndDay(
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth()
        )).thenReturn(commentList);

        //when
        List<Comment> actualComments = commentService.getAllCommentsForToday();

        //then
        verify(commentRepository, times(1)).findByCreatedYearAndMonthAndDay(
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth()
        );

        assertThat(commentList).isEqualTo(actualComments);
    }

    @Test
    public void saveAll_HappyPath_ShouldSave2Comments() {

        //given
        Comment comment = new Comment();
        comment.setComment("Test Plus");
        comment.setType(CommentType.PLUS);
        comment.setCreatedBy("nik");
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Comment comment2 = new Comment();
        comment2.setComment("Test Star");
        comment2.setCreatedBy("nik2");
        comment2.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        List<Comment> comments = Arrays.asList(comment, comment2);
        when(commentRepository.saveAll(comments)).thenReturn(comments);

        //when
        List<Comment> saved = commentService.saveAll(comments);

        //then
        assertThat(saved).isNotEmpty();
        verify(commentRepository, times(1)).saveAll(comments);
    }
}
