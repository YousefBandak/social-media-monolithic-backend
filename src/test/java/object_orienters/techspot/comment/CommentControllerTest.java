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
    @Autowired
    private ImpleCommentService commentService;
    private User user = createUser();
    private Profile profile = createProfile(user);
    private Post post = createPost(profile);
    private  Comment comment = createComment("Test comment", profile, post);
    private Comment repliedComment = createComment("Replied comment", profile, comment);

    public static User createUser() {

        return new User("husam_ramoni", "husam@example.com", "securepassword123");
    }

    public static Profile createProfile(User user) {
        return new Profile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
                "url_to_profile_pic", Profile.Gender.MALE, "1985-04-12");
    }

    public Post createPost(Profile author) {

        return new Post("This is a test post", Privacy.PUBLIC, author);
    }

    public Comment createComment(String commentContent, Profile commentor, ReactableContent commentedOn) {

        return new Comment(commentContent, commentor, commentedOn);
    }

    public void generator() {
        post.setContentID(1L);
        comment.setContentID(2L);
        repliedComment.setContentID(3L);
    }

    @Test
    public void testGetCommentsOnPost() throws Exception {
        generator();
        System.out.println(post.getContentID());
        System.out.println(profile.getPublishedPosts());
        System.out.println(post.getComments());
        System.out.println(profile.getUsername());
//        System.out.println(user.getUsername());
//        System.out.println(profile.getUsername());
//        System.out.println(profile.getPublishedPosts());
//        System.out.println(post.getComments());
        EntityModel<Comment> entityModel = EntityModel.of(comment); // Wrap in HATEOAS entity model.
        given(commentService.getComments(post.getContentID())).willReturn(List.of(comment));
        given(assembler.toModel(comment)).willReturn(entityModel);
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments", profile.getUsername(), 1)
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
        System.out.println(comment.getCommentedOn().getContentID());
        // Verify interaction with mock objects
        verify(commentService).getComment(comment.getContentID());
        verify(assembler).toModel(comment);
    }

    @Test
    public void testGetCommentsOnComment() throws Exception {
        generator();
        System.out.println(comment.getComments());
        System.out.println(repliedComment.getComment());
        System.out.println(repliedComment.getCommentedOn());
        EntityModel<Comment> entityModel = EntityModel.of(repliedComment); // Wrap in HATEOAS entity model.
        given(commentService.getComments(comment.getContentID())).willReturn(List.of(repliedComment));
        given(assembler.toModel(repliedComment)).willReturn(entityModel);
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments", "husam_ramoni", 2))
                .andExpect(status().isOk()); // Here we expect the HTTP status to be 200 OK.

    }

    @Test
    public void testGetCommentOnComment() throws Exception {
        generator();

        EntityModel<Comment> entityModel = EntityModel.of(repliedComment,
                linkTo(methodOn(CommentController.class).getComment(repliedComment.getContentID(), comment.getContentID(), user.getUsername())).withSelfRel());

        given(commentService.getComment(repliedComment.getContentID())).willReturn(repliedComment);
        given(assembler.toModel(repliedComment)).willReturn(entityModel);

        // Act & Assert
        mockMvc.perform(get("/profiles/{username}/content/{contentID}/comments/{commentID}", "husam_ramoni", 2, 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify some fields in the response.
                .andExpect(jsonPath("$.contentID").value(3));
        // Verify interaction with mock objects
        verify(commentService).getComment(repliedComment.getContentID());
        verify(assembler).toModel(repliedComment);
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
