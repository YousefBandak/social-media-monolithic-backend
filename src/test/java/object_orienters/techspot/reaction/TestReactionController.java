//package object_orienters.techspot.reaction;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import object_orienters.techspot.comment.Comment;
//import object_orienters.techspot.content.ReactableContent;
//import object_orienters.techspot.model.Privacy;
//import object_orienters.techspot.post.Post;
//import object_orienters.techspot.postTypes.DataType;
//import object_orienters.techspot.profile.Profile;
//import object_orienters.techspot.security.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
////@EnableMethodSecurity
//@WebMvcTest(ReactionController.class)
//
//class TestReactionController {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ImpleReactionService reactionService;
//
//    @MockBean
//    private ReactionModelAssembler assembler;
//    private User user;
//    private Profile profile;
//    private Post post;
//    private Comment comment;
//    private Reaction postReaction;
//    private Reaction commentReaction;
//
//    public static User createUser(String username, String email, String password) {
//
//        return new User(username, email, password);
//    }
//
//    public static Profile createProfile(User user, String name, String profession, String email, DataType profilePic, Profile.Gender gender, String dob) {
//        return new Profile(user, name, profession, email, profilePic, gender, dob);
//    }
//
//    public static Comment createComment(String commentContent, Profile commentor, ReactableContent commentedOn) {
//        return new Comment(null, commentor, commentedOn, commentContent);
//    }
//
//    public Post createPost(String textData, Privacy privacy, Profile author) {
//
//        return new Post(textData, privacy, author);
//    }
//
//    @BeforeEach
//    void setup(WebApplicationContext wac) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        user = createUser("husam_ramoni", "husam@example.com", "securepassword123");
//        profile = createProfile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
//                null, Profile.Gender.MALE, "1985-04-12");
//        post = createPost("This is a test post", Privacy.PUBLIC, profile);
//        comment = createComment("Test comment", profile, post);
//        postReaction = new Reaction(profile, Reaction.ReactionType.LIKE, post);
//        commentReaction = new Reaction(profile, Reaction.ReactionType.LIKE, comment);
//        generator();
//    }
//
//    public void generator() {
//        post.setContentID(1L);
//        comment.setContentID(2L);
//        postReaction.setReactionID(4L);
//        commentReaction.setReactionID(5L);
//    }
//
//    @Test
//    public void testGetReactionOnPost() throws Exception {
//
//        EntityModel<Reaction> entityModel = EntityModel.of(postReaction);
//
//        given(reactionService.getReaction(postReaction.getReactionID())).willReturn(postReaction);
//        given(assembler.toModel(postReaction)).willReturn(entityModel);
//
//        mockMvc.perform(get("/content/{contentID}/reactions/{reactionId}", 1, 4))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testGetReactionOnComment() throws Exception {
//
//        EntityModel<Reaction> entityModel = EntityModel.of(commentReaction);
//
//        given(reactionService.getReaction(commentReaction.getReactionID())).willReturn(commentReaction);
//        given(assembler.toModel(commentReaction)).willReturn(entityModel);
//
//        mockMvc.perform(get("/content/{contentID}/reactions/{reactionId}", 4, 5))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testGetReactionsOnPost() throws Exception {
//        List<EntityModel<Reaction>> reactions = List.of(EntityModel.of(postReaction));
//
//        given(reactionService.getReactions(post.getContentID())).willReturn(List.of(postReaction));
//        given(assembler.toModel(postReaction)).willReturn(EntityModel.of(postReaction));
//
//        mockMvc.perform(get("/content/{contentID}/reactions", 1))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testGetReactionsOnComment() throws Exception {
//        List<EntityModel<Reaction>> reactions = List.of(EntityModel.of(commentReaction));
//
//        given(reactionService.getReactions(comment.getContentID())).willReturn(List.of(commentReaction));
//        given(assembler.toModel(commentReaction)).willReturn(EntityModel.of(commentReaction));
//
//        mockMvc.perform(get("/content/{contentID}/reactions", 2))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testCreateReactionOnPost() throws Exception {
//
//        EntityModel<Reaction> entityModel = EntityModel.of(postReaction);
//
//        given(reactionService.createReaction("user1", "LIKE", post.getContentID())).willReturn(postReaction);
//        given(assembler.toModel(postReaction)).willReturn(entityModel);
//
//        mockMvc.perform(post("/content/{contentID}/reactions", 2)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"reactorID\":\"user1\", \"reactionType\":\"LIKE\"}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testCreateReactionOnComment() throws Exception {
//
//        EntityModel<Reaction> entityModel = EntityModel.of(commentReaction);
//
//        given(reactionService.createReaction("user1", "LIKE", comment.getContentID())).willReturn(commentReaction);
//        given(assembler.toModel(commentReaction)).willReturn(entityModel);
//
//        mockMvc.perform(post("/content/{contentID}/reactions", 2)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"reactorID\":\"user1\", \"reactionType\":\"LIKE\"}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testDeleteReactionOfPost() throws Exception {
//        System.out.println(post.getContentID());
//        System.out.println(postReaction.getReactionID());
//        doNothing().when(reactionService).deleteReaction(postReaction.getReactionID());
//
//        mockMvc.perform(delete("/content/{contentID}/reactions/{reactionId}", post.getContentID(), postReaction.getReactionID()))
//                .andExpect(status().isNoContent())
//                .andExpect(content().string(""));
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testDeleteReactionOfComment() throws Exception {
//        System.out.println(comment.getContentID());
//        System.out.println(commentReaction.getReactionID());
//
//        mockMvc.perform(delete("/content/{contentID}/reactions/{reactionId}", comment.getContentID(), commentReaction.getReactionID()))
//                .andExpect(status().isNoContent())
//                .andExpect(content().string(""));
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testUpdateReaction() throws Exception {
//        // Arrange
//        Long reactionId = postReaction.getReactionID();
//        Reaction.ReactionType newReactionType = Reaction.ReactionType.HAHA;
//        Reaction updatedReaction = postReaction;
//        updatedReaction.setReactionID(reactionId);
//        updatedReaction.setType(newReactionType);
//
//        given(reactionService.updateReaction(reactionId,updatedReaction.toString())).willReturn(updatedReaction);
//
//        EntityModel<Reaction> reactionModel = EntityModel.of(updatedReaction);
//        given(assembler.toModel(any(Reaction.class))).willReturn(reactionModel);
//
//        Map<String, String> reactionUpdate = new HashMap<>();
//        reactionUpdate.put("reactionType", newReactionType.toString());
//        String reactionUpdateJson = new ObjectMapper().writeValueAsString(reactionUpdate);
//
//        // Act & Assert
//        mockMvc.perform(put("/content/{contentID}/reactions/{reactionId}", post.getContentID(), reactionId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(reactionUpdateJson))
//                .andExpect(status().isOk())
//                //.andExpect(jsonPath("$.type").value(newReactionType))
//        ;
//    }
//}
//
//
