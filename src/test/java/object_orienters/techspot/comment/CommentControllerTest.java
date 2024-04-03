package object_orienters.techspot.comment;

import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.security.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
//@WebMvcTest(value = CommentController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)

public class CommentControllerTest {
    @TestConfiguration
    static class SecurityPermitAllConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest().permitAll();
            return http.build();
        }
    }
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentModelAssembler assembler;

    @MockBean
    private ImpleCommentService commentService;
    private User user = createUser();
    private Profile profile = createProfile(user);
    private Post post = createPost(profile);
    private Comment comment = createComment("Test comment", profile, post);
    private Comment replaedComment = createComment("Replaced comment", profile, comment);

    public static User createUser() {
        return new User("husam_ramoni", "husam@example.com", "securepassword123");
    }

    public static Profile createProfile(User user) {
        // Assuming the format for dob is YYYY-MM-DD and gender is an enum that has MALE as a value.
        // You might need to adjust the date format and enum based on your actual model.
        return new Profile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
                "url_to_profile_pic", Profile.Gender.MALE, "1985-04-12");
    }

    public static Post createPost(Profile author) {
        return new Post("This is a test post", Privacy.PUBLIC, author);
    }

    public static Comment createComment(String commentContent, Profile commentor, ReactableContent commentedOn) {
        return new Comment(commentContent, commentor, commentedOn);
    }

    public void generator() {
        profile.setPublishedPosts(List.of(post));
        post.setComments(List.of(comment));
        profile.setUsername(user.getUsername());
        post.setContentID(1L);
        comment.setContentID(2L);
        replaedComment.setContentID(3L);
    }

    @Test
    public void testGetCommentsOnPost() throws Exception {
        generator();
        System.out.println(user.getUsername());
        System.out.println(profile.getUsername());
        System.out.println(profile.getPublishedPosts());
        System.out.println(post.getComments());
        EntityModel<Comment> entityModel = EntityModel.of(comment); // Wrap in HATEOAS entity model.
        given(commentService.getComments(post.getContentID())).willReturn(List.of(comment));
        given(assembler.toModel(comment)).willReturn(entityModel);
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments", "husam_ramoni", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Here we expect the HTTP status to be 200 OK.

    }

    @Test
    public void testGetCommentOnPost() throws Exception {
        generator();

        EntityModel<Comment> entityModel = EntityModel.of(comment,
                linkTo(methodOn(CommentController.class).getComment(comment.getContentID(), post.getContentID(), user.getUsername())).withSelfRel());

        given(commentService.getComment(comment.getContentID())).willReturn(comment);
        given(assembler.toModel(comment)).willReturn(entityModel);

        // Act & Assert
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments/{commentID}", "husam_ramoni", 1, 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify some fields in the response.
                .andExpect(jsonPath("$.contentID").value(2));
        // Verify interaction with mock objects
        verify(commentService).getComment(comment.getContentID());
        verify(assembler).toModel(comment);
    }

    @Test
    public void testGetCommentsOnComment() throws Exception {
        generator();
        System.out.println(comment.getComments());
        System.out.println(replaedComment.getComment());
        System.out.println(replaedComment.getCommentedOn());
        EntityModel<Comment> entityModel = EntityModel.of(replaedComment); // Wrap in HATEOAS entity model.
        given(commentService.getComments(comment.getContentID())).willReturn(List.of(replaedComment));
        given(assembler.toModel(replaedComment)).willReturn(entityModel);
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments", "husam_ramoni", 2))
                .andExpect(status().isOk()); // Here we expect the HTTP status to be 200 OK.

    }

    @Test
    public void testGetCommentOnComment() throws Exception {
        generator();

        EntityModel<Comment> entityModel = EntityModel.of(replaedComment,
                linkTo(methodOn(CommentController.class).getComment(replaedComment.getContentID(), comment.getContentID(), user.getUsername())).withSelfRel());

        given(commentService.getComment(replaedComment.getContentID())).willReturn(replaedComment);
        given(assembler.toModel(replaedComment)).willReturn(entityModel);

        // Act & Assert
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments/{commentID}", "husam_ramoni", 2, 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify some fields in the response.
                .andExpect(jsonPath("$.contentID").value(3));
        // Verify interaction with mock objects
        verify(commentService).getComment(replaedComment.getContentID());
        verify(assembler).toModel(replaedComment);
    }

    @Test
    @WithMockUser(username = "husam_ramoni")
    public void testAddComment() throws Exception {
        generator();
        EntityModel<Comment> entityModel = EntityModel.of(comment);
        given(commentService.addComment(post.getContentID(), comment.getComment(), user.getUsername())).willReturn(comment);
        given(assembler.toModel(comment)).willReturn(entityModel);

        mockMvc.perform(post("/profiles/{username}/content/{contentID}/comments", user.getUsername(), post.getContentID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Test comment\",\"commentor\":\"husam_ramoni\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "husam_ramoni")
    public void testDeleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(post.getContentID(), comment.getContentID());

        // Act & Assert
        mockMvc.perform(delete("/profiles/{username}/content/{contentID}/comments/{commentID}", "husam_ramoni", 1, 2))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(username = "husam_ramoni")
    public void testUpdateComment() throws Exception {
        // Arrange
        generator();
        String updatedText = "Updated comment text";
        Map<String, String> updateRequest = Map.of("comment", updatedText);
        //comment.setComment(updatedText);
        System.out.println(commentService.updateComment(post.getContentID(), comment.getContentID(), updatedText));
        EntityModel<Comment> entityModel = EntityModel.of(comment);

        given(commentService.updateComment(post.getContentID(), comment.getContentID(), updatedText)).willReturn(comment);
        given(assembler.toModel(comment)).willReturn(entityModel);
        // Act & Assert
        mockMvc.perform(put("/profiles/{username}/content/{contentID}/comments/{commentID}", "husam_ramoni", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"" + updatedText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentID").value(2))
                .andExpect(jsonPath("$.comment").value("Updated comment text"));

        // Verify service interaction
        verify(commentService).updateComment(1L, 2L, updatedText);
        verify(assembler).toModel(comment);
    }


}
