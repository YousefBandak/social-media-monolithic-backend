// package object_orienters.techspot.comment;

// import object_orienters.techspot.content.ReactableContent;
// import object_orienters.techspot.model.Privacy;
// import object_orienters.techspot.post.ImplePostService;
// import object_orienters.techspot.post.Post;
// import object_orienters.techspot.postTypes.DataType;
// import object_orienters.techspot.profile.ImpleProfileService;
// import object_orienters.techspot.profile.Profile;
// import object_orienters.techspot.security.model.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.hateoas.EntityModel;
// import org.springframework.http.MediaType;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.context.WebApplicationContext;

// import java.nio.file.Files;
// import java.util.List;
// import java.util.Map;

// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.verify;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// //@EnableMethodSecurity
// @WebMvcTest(CommentController.class)
// class CommentControllerTest {
//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private CommentModelAssembler assembler;

//     @MockBean
//     private ImpleCommentService commentService;

//     @MockBean
//     private ImplePostService postService;

//     @MockBean
//     private ImpleProfileService profileService;


//     private User user;
//     private User user2;
//     private Profile profile;
//     private Profile profile2;
//     private Post post;
//     private Comment comment;
//     private Comment repliedComment;

//     public static User createUser(String username, String email, String password) {

//         return new User(username, email, password);
//     }

//     public static Profile createProfile(User user, String name, String profession, String email, DataType profilePic, Profile.Gender gender, String dob) {
//         return new Profile(user, name, profession, email, profilePic, gender, dob);
//     }

//     @BeforeEach
//     void setup(WebApplicationContext wac) {

//         this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//         user = createUser("husam_ramoni", "husam@example.com", "securepassword123");
//         user2 = createUser("rawan", "rawan@example.com", "securepassword123");
//         profile = createProfile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
//                 null, Profile.Gender.MALE, "1985-04-12");
//         profile2 = createProfile(user, "Rawan Gedeoon", "Software Engineer", "rawan@example.com",
//                 null, Profile.Gender.FEMALE, "1985-04-12");
//         post = createPost(profile);
//         comment = createComment(profile, post, "Test comment");
//         repliedComment = createComment(profile, comment, "Replied comment");
//         generator();
//     }

//     public Post createPost(Profile author) {

//         return new Post("This is a test post", Privacy.PUBLIC, author);
//     }

//     public Comment createComment(Profile commentor, ReactableContent commentedOn, String commentContent) {

//         return new Comment(commentor, commentedOn, commentContent);
//     }

//     public void generator() {
//         post.setContentID(1L);
//         comment.setContentID(2L);
//         repliedComment.setContentID(3L);
//     }


//     @Test
//     public void testGetCommentOnPost() throws Exception {

//         EntityModel<Comment> entityModel = EntityModel.of(comment,
//                 linkTo(methodOn(CommentController.class).getComment(comment.getContentID(), post.getContentID())).withSelfRel());

//         given(commentService.getComment(comment.getContentID())).willReturn(comment);
//         given(assembler.toModel(comment)).willReturn(entityModel);

//         // Act & Assert
//         mockMvc.perform(get("/content/{contentID}/comments/{commentID}",  1, 2)
//                         .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 // Verify some fields in the response.
//                 .andExpect(jsonPath("$.contentID").value(2))
//         ;
//         System.out.println(comment.getCommentedOn().getContentID());
//         // Verify interaction with mock objects
//         verify(commentService).getComment(comment.getContentID());
//         verify(assembler).toModel(comment);
//     }


//     @Test
//     public void testGetCommentOnComment() throws Exception {

//         EntityModel<Comment> entityModel = EntityModel.of(repliedComment,
//                 linkTo(methodOn(CommentController.class).getComment(repliedComment.getContentID(), comment.getContentID())).withSelfRel());

//         given(commentService.getComment(repliedComment.getContentID())).willReturn(repliedComment);
//         given(assembler.toModel(repliedComment)).willReturn(entityModel);

//         // Act & Assert
//         mockMvc.perform(get("/content/{contentID}/comments/{commentID}",  2, 3)
//                         .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 // Verify some fields in the response.
//                 .andExpect(jsonPath("$.contentID").value(3));
//         // Verify interaction with mock objects
//         verify(commentService).getComment(repliedComment.getContentID());
//         verify(assembler).toModel(repliedComment);
//     }

//     @Test
//     @WithMockUser(username = "husam_ramoni")
//     public void testAddTextCommentOnPost() throws Exception {

//         String expectedText = "Test comment";
//         comment.setTextData(expectedText);
//         given(commentService.addComment(1L, "husam_ramoni", null, expectedText)).willReturn(comment);


//         mockMvc.perform(MockMvcRequestBuilders.multipart("/content/{contentID}/comments", 1L)
//                                 .param("text", expectedText)
//                                 .param("commenter", "husam_ramoni")
//                 )
//                 .andExpect(status().isCreated());

//         // Verify the interaction with the service
//         verify(commentService).addComment(1L, "husam_ramoni", null, expectedText);
//     }




//     @Test
//     @WithMockUser(username = "husam_ramoni")
//     public void testDeleteComment() throws Exception {
//         // Arrange
//         String username = profile.getUsername();
//         long commentId = comment.getContentID();
//         long postID = post.getContentID();

//         doNothing().when(commentService).deleteComment(post.getContentID(), comment.getContentID());

//         // Act & Assert
//         mockMvc.perform(delete("/content/{contentID}/comments/{commentID}",postID, commentId))
//                 .andExpect(status().isNoContent());

//         // Verify service interaction
//         verify(commentService).deleteComment(post.getContentID(), comment.getContentID());
//     }


//     @Test
//     @WithMockUser(username = "husam_ramoni")
//     public void testUpdateComment() throws Exception {
//         // Arrange
//         String updatedText = "Updated comment text";
//         Map<String, String> updateRequest = Map.of("comment", updatedText);
//         //comment.setComment(updatedText);
//         EntityModel<Comment> entityModel = EntityModel.of(comment);
//         given(commentService.updateComment(post.getContentID(), comment.getContentID(), null, updatedText)).willReturn(comment);
//         given(assembler.toModel(comment)).willReturn(entityModel);
//         // Act & Assert
//         mockMvc.perform(put("/content/{contentID}/comments/{commentID}", 1, 2)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .param("text", updatedText)) // Update this line
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.contentID").value(2));

//     }

//     @Test
//     @WithMockUser(username = "husam_ramoni")
//     public void testAddMediaCommentOnPost() throws Exception {
//         ClassPathResource imageResource = new ClassPathResource("p1.png");
//         System.out.println(imageResource.getFile().getAbsolutePath());
//         if (!imageResource.exists()) {
//             throw new AssertionError("Test file not found");
//         }
//         byte[] content = Files.readAllBytes(imageResource.getFile().toPath());

//         MockMultipartFile mockFile = new MockMultipartFile(
//                 "file",
//                 "p1.png", // Filename
//                 "image/png", // Content type
//                 content // Correct file content
//         );

//         // Configure mock service to return a predefined comment object
//         given(commentService.addComment(1L, "husam_ramoni", mockFile, "Test comment")).willReturn(comment);

//         mockMvc.perform(MockMvcRequestBuilders.multipart("/content/{contentID}/comments", post.getContentID())
//                         .file(mockFile)
//                         .param("text", "Test comment")
//                         .param("contentID", "1")
//                         .param("commenter", "husam_ramoni")
//                         .contentType(MediaType.MULTIPART_FORM_DATA))
//                 .andExpect(status().isCreated());

//     }

// }
