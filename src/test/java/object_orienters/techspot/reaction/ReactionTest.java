
package object_orienters.techspot.reaction;

import object_orienters.techspot.comment.Comment;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReactionController.class)
class ReactionTest {
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
//    @TestConfiguration
//    static class SecurityPermitAllConfig {
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                    .anyRequest().permitAll();
//            return http.build();
//        }
//    }
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpleReactionService reactionService;

    @MockBean
    private ReactionModelAssembler assembler;
    private User user = createUser();
    private Profile profile = createProfile(user);
    private Post post = createPost(profile);
    private Comment comment = createComment("Test comment", profile, post);
    private Reaction postReaction = new Reaction(profile, Reaction.ReactionType.LIKE,post);
    private Reaction commentReaction = new Reaction(profile, Reaction.ReactionType.LIKE,comment);
    public void generator(){
        post.setContentID(1L);
        comment.setContentID(2L);
        postReaction.setReactionID(4L);
        commentReaction.setReactionID(5L);
    }
    public static User createUser() {
        return new User("husam_ramoni", "husam@example.com", "securepassword123");
    }

    public static Profile createProfile(User user) {
        return new Profile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
                "url_to_profile_pic", Profile.Gender.MALE, "1985-04-12");
    }

    public static Post createPost(Profile author) {
        return new Post("This is a test post", Privacy.PUBLIC, author);
    }

    public static Comment createComment(String commentContent, Profile commentor, ReactableContent commentedOn) {
        return new Comment(commentContent, commentor, commentedOn);
    }
    @Test
    public void testGetReactionOnPost() throws Exception {
        generator();

        EntityModel<Reaction> entityModel = EntityModel.of(postReaction);

        given(reactionService.getReaction(postReaction.getReactionID())).willReturn(postReaction);
        given(assembler.toModel(postReaction)).willReturn(entityModel);

        mockMvc.perform(get("/content/{contentID}/reactions/{reactionId}", 1, 4))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetReactionOnComment() throws Exception {
        generator();

        EntityModel<Reaction> entityModel = EntityModel.of(commentReaction);

        given(reactionService.getReaction(commentReaction.getReactionID())).willReturn(commentReaction);
        given(assembler.toModel(commentReaction)).willReturn(entityModel);

        mockMvc.perform(get("/content/{contentID}/reactions/{reactionId}", 4, 5))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetReactionsOnPost() throws Exception {
        generator();
        List<EntityModel<Reaction>> reactions = List.of(EntityModel.of(postReaction));

        given(reactionService.getReactions(post.getContentID())).willReturn(List.of(postReaction));
        given(assembler.toModel(postReaction)).willReturn(EntityModel.of(postReaction));

        mockMvc.perform(get("/content/{contentID}/reactions", 1))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetReactionsOnComment() throws Exception {
        generator();
        List<EntityModel<Reaction>> reactions = List.of(EntityModel.of(commentReaction));

        given(reactionService.getReactions(comment.getContentID())).willReturn(List.of(commentReaction));
        given(assembler.toModel(commentReaction)).willReturn(EntityModel.of(commentReaction));

        mockMvc.perform(get("/content/{contentID}/reactions", 2))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    public void testCreateReactionOnPost() throws Exception {

        EntityModel<Reaction> entityModel = EntityModel.of(postReaction);

        given(reactionService.createReaction("user1", "LIKE", post.getContentID())).willReturn(postReaction);
        given(assembler.toModel(postReaction)).willReturn(entityModel);

        mockMvc.perform(post("/content/{contentID}/reactions", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reactorID\":\"user1\", \"reactionType\":\"LIKE\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testCreateReactionOnComment() throws Exception {

        EntityModel<Reaction> entityModel = EntityModel.of(commentReaction);

        given(reactionService.createReaction("user1", "LIKE", comment.getContentID())).willReturn(commentReaction);
        given(assembler.toModel(commentReaction)).willReturn(entityModel);

        mockMvc.perform(post("/content/{contentID}/reactions", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reactorID\":\"user1\", \"reactionType\":\"LIKE\"}"))
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser
    public void testDeleteReactionOfPost() throws Exception {
        generator();
        System.out.println(post.getContentID());
        System.out.println(postReaction.getReactionID());

        mockMvc.perform(delete("/content/{contentID}/reactions/{reactionId}", post.getContentID(), postReaction.getReactionID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Reaction deleted successfully"));
    }
    @Test
    @WithMockUser
    public void testDeleteReactionOfComment() throws Exception {
        generator();
        System.out.println(comment.getContentID());
        System.out.println(commentReaction.getReactionID());

        mockMvc.perform(delete("/content/{contentID}/reactions/{reactionId}", comment.getContentID(), commentReaction.getReactionID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Reaction deleted successfully"));
    }
    @Test
    @WithMockUser
    public void testUpdateReactionOnPost() throws Exception {
        generator();
        Reaction updatedReaction = reactionService.updateReaction(postReaction.getReactionID(), "HAHA");
        System.out.println(reactionService.updateReaction(postReaction.getReactionID(), "HAHA"));
        System.out.println(postReaction.getType());
        //System.out.println(postReaction);
        //System.out.println(commentReaction);

        EntityModel<Reaction> entityModel = EntityModel.of(updatedReaction);
        given(assembler.toModel(updatedReaction)).willReturn(entityModel);

        mockMvc.perform(put("/content/{contentID}/reactions/{reactionId}", post.getContentID(), postReaction.getReactionID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reactionType\":\"HAHA\"}"))
                .andExpect(status().isOk());
    }








}

