//<<<<<<< HEAD
////  package object_orienters.techspot.post;
//
////  import com.fasterxml.jackson.databind.ObjectMapper;
////  import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
////  import object_orienters.techspot.comment.Comment;
////  import object_orienters.techspot.content.Content;
////  import object_orienters.techspot.content.ReactableContent;
////  import object_orienters.techspot.model.Privacy;
////  import object_orienters.techspot.postTypes.DataType;
////  import object_orienters.techspot.profile.ImpleProfileService;
////  import object_orienters.techspot.profile.Profile;
////  import object_orienters.techspot.exceptions.UserNotFoundException;
////  import object_orienters.techspot.security.model.User;
////  import org.junit.jupiter.api.BeforeEach;
////  import org.junit.jupiter.api.Test;
////  import org.springframework.beans.factory.annotation.Autowired;
////  import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
////  import org.springframework.boot.test.mock.mockito.MockBean;
////  import org.springframework.core.io.ClassPathResource;
////  import org.springframework.hateoas.EntityModel;
////  import org.springframework.http.MediaType;
////  import org.springframework.mock.web.MockMultipartFile;
////  import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
////  import org.springframework.security.test.context.support.WithMockUser;
////  import org.springframework.test.web.servlet.MockMvc;
////  import org.springframework.test.web.servlet.setup.MockMvcBuilders;
////  import org.springframework.web.context.WebApplicationContext;
//
////  import java.nio.file.Files;
////  import java.util.ArrayList;
//
////  import static org.mockito.ArgumentMatchers.any;
////  import static org.mockito.BDDMockito.given;
////  import static org.mockito.Mockito.*;
////  import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
////  import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
////  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
////  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
////  @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
////  @WebMvcTest(PostController.class)
////  class PostControllerTest {
//
////      @Autowired
////      private MockMvc mockMvc;
////      @MockBean
////      private PostModelAssembler assembler;
////      @MockBean
////      private SharedPostModelAssembler sharedPostModelAssembler;
////      @MockBean
////      private ImplePostService postService;
////      @MockBean
////      private ImplSharedPostService sharedPostService;
////      @MockBean
////      private ImpleProfileService profileService;
//
//
////      private User user;
////      private User user2;
//
////      private Profile profile;
////      private Profile profile2;
//
////      private Post post;
////      private Post privatePost;
//
////      private Comment comment;
////      private Comment repliedComment;
//
//
////      public static User createUser(String username, String email, String password) {
//
////          return new User(username, email, password);
////      }
//
////      public static Profile createProfile(User user, String name , String profession, String email, DataType profilePic, Profile.Gender gender, String dob) {
////          return new Profile(user, name, profession, email, profilePic, gender, dob);
////      }
//
////      public Post createTextPost(String textData, Privacy privacy, Profile author) {
//
////          return new Post(textData, privacy, author);
////      }
////      public Post createMediaPost(DataType mediaData, Privacy privacy, Profile author) {
//
////          return new Post(mediaData, privacy, author);
////      }
////      public Comment createComment(Profile commentor, ReactableContent commentedOn, String commentContent) {
//
////          return new Comment(commentor, commentedOn, commentContent);
////      }
////      public void generator() {
////          post.setContentID(1L);
////          comment.setContentID(2L);
////          repliedComment.setContentID(3L);
////          privatePost.setContentID(4L);
////      }
//
////      @BeforeEach
////      void setup(WebApplicationContext wac) {
////          this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////          user = createUser("husam_ramoni", "husam@example.com", "securepassword123");
////          user2 = createUser("angela", "angela@example.com", "securepassword123");
////          profile = createProfile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
////                  null, Profile.Gender.MALE, "1985-04-12");
////          profile2 = createProfile(user, "Angela Salem", "Software Engineer", "angela@example.com",
////                  null, Profile.Gender.FEMALE, "1985-04-12");
////          post = createTextPost("Post husam", Privacy.PUBLIC, profile);
////          privatePost = createTextPost("private Post husam", Privacy.PRIVATE, profile);
////          comment = createComment(profile, post, "Test comment");
////          repliedComment = createComment(profile, comment, "Replied comment");
////          generator();
////      }
//
//
////      @Test
////      public void getPosts_UserNotFound() throws Exception {
////          String username = "rawan";
////          //when(postService.getPosts(username)).thenThrow(new UserNotFoundException("rawan"));
////          //System.out.println(postService.getPosts(username));  --> will throw UserNotFoundException in the console
////          given(postService.getPosts(username)).willThrow(new UserNotFoundException("rawan"));
//
//
//
////          mockMvc.perform(get("/profiles/{username}/posts", username))
////                  .andExpect(status().isNotFound())
////                  .andExpect(jsonPath("$.title").value("User Not Found"))
////                  .andExpect(jsonPath("$.detail").value("User With Username: rawan Could Not Be Found."));
//
////          verify(postService).getPosts(username);
//
////      }
//
////      @Test
////      @WithMockUser(username = "husam_ramoni")
////      public void testAddMediaPost() throws Exception {
////          ClassPathResource imageResource = new ClassPathResource("p1.png");
////          System.out.println(imageResource.getFile().getAbsolutePath());
////          if (!imageResource.exists()) {
////              throw new AssertionError("Test file not found");
////          }
////          byte[] content = Files.readAllBytes(imageResource.getFile().toPath());
//
////          MockMultipartFile mockFile = new MockMultipartFile(
////                  "file",
////                  "p1.png", // Filename
////                  "image/png", // Content type
////                  content // Correct file content
////          );
//
//
////          EntityModel<Content> entityModel = EntityModel.of(post);
////          given(postService.addTimelinePosts("husam_ramoni",mockFile,"Post husam",Privacy.PUBLIC, new ArrayList<>())).willReturn(post);
////          given(assembler.toModel(any(Post.class))).willReturn(entityModel);
//
////          // Act & Assert
////          mockMvc.perform(multipart("/profiles/{username}/posts", "husam_ramoni")
////                          .file(mockFile)
////                          .param("text", "Post husam")
////                          .param("privacy", post.getPrivacy().toString())
////                          .contentType(MediaType.MULTIPART_FORM_DATA))
////                  .andDo(print()) // This will print the response to the console
////                  .andExpect(status().isCreated());
////          System.out.println(mockFile.isEmpty());
////          System.out.println(mockFile.getContentType());
////          System.out.println(mockFile.getName());
////      }
//
////      @Test
////      public void testGetPost_Success() throws Exception {
//
////          EntityModel<Content> entityModel = EntityModel.of(post);
////          given(profileService.getUserByUsername(profile.getUsername())).willReturn(profile);
////          given(postService.getPost(post.getContentID())).willReturn(post);
////          given(assembler.toModel(post)).willReturn(entityModel);
//
////          // Act & Assert
////          mockMvc.perform(get("/profiles/{username}/posts/{postId}", "husam_ramoni", 1L))
////                  .andExpect(status().isOk())
////                  .andExpect(jsonPath("$.textData").value("Post husam"));
////      }
//
////      @Test
////      public void testGetPost_PostNotFound() throws Exception {
//
////          given(profileService.getUserByUsername(profile.getUsername())).willReturn(profile);
////          given(postService.getPost(123L)).willThrow(new PostNotFoundException(123L));
//=======
// package object_orienters.techspot.post;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import object_orienters.techspot.comment.Comment;
// import object_orienters.techspot.content.Content;
// import object_orienters.techspot.content.ReactableContent;
// import object_orienters.techspot.model.Privacy;
// import object_orienters.techspot.postTypes.DataType;
// import object_orienters.techspot.profile.ImpleProfileService;
// import object_orienters.techspot.profile.Profile;
// import object_orienters.techspot.exceptions.UserNotFoundException;
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
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.context.WebApplicationContext;
//
// import java.nio.file.Files;
// import java.util.ArrayList;
//
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// @WebMvcTest(PostController.class)
// class PostControllerTest {
//
//     @Autowired
//     private MockMvc mockMvc;
//     @MockBean
//     private PostModelAssembler assembler;
//     @MockBean
//     private SharedPostModelAssembler sharedPostModelAssembler;
//     @MockBean
//     private ImplePostService postService;
//     @MockBean
//     private ImplSharedPostService sharedPostService;
//     @MockBean
//     private ImpleProfileService profileService;
//
//
//     private User user;
//     private User user2;
//
//     private Profile profile;
//     private Profile profile2;
//
//     private Post post;
//     private Post privatePost;
//
//     private Comment comment;
//     private Comment repliedComment;
//
//
//     public static User createUser(String username, String email, String password) {
//
//         return new User(username, email, password);
//     }
//
//     public static Profile createProfile(User user, String name , String profession, String email, DataType profilePic, Profile.Gender gender, String dob) {
//         return new Profile(user, name, profession, email, profilePic, gender, dob);
//     }
//
//     public Post createTextPost(String textData, Privacy privacy, Profile author) {
//
//         return new Post(textData, privacy, author);
//     }
//     public Post createMediaPost(DataType mediaData, Privacy privacy, Profile author) {
//
//         return new Post(mediaData, privacy, author);
//     }
//     public Comment createComment(Profile commentor, ReactableContent commentedOn, String commentContent) {
//
//         return new Comment(commentor, commentedOn, commentContent);
//     }
//     public void generator() {
//         post.setContentID(1L);
//         comment.setContentID(2L);
//         repliedComment.setContentID(3L);
//         privatePost.setContentID(4L);
//     }
//
//     @BeforeEach
//     void setup(WebApplicationContext wac) {
//         this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//         user = createUser("husam_ramoni", "husam@example.com", "securepassword123");
//         user2 = createUser("angela", "angela@example.com", "securepassword123");
//         profile = createProfile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
//                 null, Profile.Gender.MALE, "1985-04-12");
//         profile2 = createProfile(user, "Angela Salem", "Software Engineer", "angela@example.com",
//                 null, Profile.Gender.FEMALE, "1985-04-12");
//         post = createTextPost("Post husam", Privacy.PUBLIC, profile);
//         privatePost = createTextPost("private Post husam", Privacy.PRIVATE, profile);
//         comment = createComment(profile, post, "Test comment");
//         repliedComment = createComment(profile, comment, "Replied comment");
//         generator();
//     }
//
//
//     @Test
//     public void getPosts_UserNotFound() throws Exception {
//         String username = "rawan";
//         //when(postService.getPosts(username)).thenThrow(new UserNotFoundException("rawan"));
//         //System.out.println(postService.getPosts(username));  --> will throw UserNotFoundException in the console
//         given(postService.getPosts(username)).willThrow(new UserNotFoundException("rawan"));
//
//
//
//         mockMvc.perform(get("/profiles/{username}/posts", username))
//                 .andExpect(status().isNotFound())
//                 .andExpect(jsonPath("$.title").value("User Not Found"))
//                 .andExpect(jsonPath("$.detail").value("User With Username: rawan Could Not Be Found."));
//
//         verify(postService).getPosts(username);
//
//     }
//
//     @Test
//     @WithMockUser(username = "husam_ramoni")
//     public void testAddMediaPost() throws Exception {
//         ClassPathResource imageResource = new ClassPathResource("p1.png");
//         System.out.println(imageResource.getFile().getAbsolutePath());
//         if (!imageResource.exists()) {
//             throw new AssertionError("Test file not found");
//         }
//         byte[] content = Files.readAllBytes(imageResource.getFile().toPath());
//
//         MockMultipartFile mockFile = new MockMultipartFile(
//                 "file",
//                 "p1.png", // Filename
//                 "image/png", // Content type
//                 content // Correct file content
//         );
//
//
//         EntityModel<Content> entityModel = EntityModel.of(post);
//         //given(postService.addTimelinePosts("husam_ramoni",mockFile,"Post husam",Privacy.PUBLIC, new ArrayList<>())).willReturn(post);
//         given(assembler.toModel(any(Post.class))).willReturn(entityModel);
//
//         // Act & Assert
//         mockMvc.perform(multipart("/profiles/{username}/posts", "husam_ramoni")
//                         .file(mockFile)
//                         .param("text", "Post husam")
//                         .param("privacy", post.getPrivacy().toString())
//                         .contentType(MediaType.MULTIPART_FORM_DATA))
//                 .andDo(print()) // This will print the response to the console
//                 .andExpect(status().isCreated());
//         System.out.println(mockFile.isEmpty());
//         System.out.println(mockFile.getContentType());
//         System.out.println(mockFile.getName());
//     }
//
//     @Test
//     public void testGetPost_Success() throws Exception {
//
//         EntityModel<Content> entityModel = EntityModel.of(post);
//         given(profileService.getUserByUsername(profile.getUsername())).willReturn(profile);
//         given(postService.getPost(post.getContentID())).willReturn(post);
//         given(assembler.toModel(post)).willReturn(entityModel);
//
//         // Act & Assert
//         mockMvc.perform(get("/profiles/{username}/posts/{postId}", "husam_ramoni", 1L))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.textData").value("Post husam"));
//     }
//
//     @Test
//     public void testGetPost_PostNotFound() throws Exception {
//
//         given(profileService.getUserByUsername(profile.getUsername())).willReturn(profile);
//         given(postService.getPost(123L)).willThrow(new PostNotFoundException(123L));
//>>>>>>> e017b60fc2e29922ab416dc65ac7f413d28ffef0
//
//
////          // Act & Assert
////          mockMvc.perform(get("/profiles/{username}/posts/{postId}", "husam_ramoni", 123L))
////                  .andExpect(status().isNotFound())
////                  .andExpect(jsonPath("$.title").value("Post Not Found"))
////                  .andExpect(jsonPath("$.detail").value("Post With ID:123 Could Not Be Found."));
//
////          // Verify service interaction
////          verify(postService).getPost(123L);
////      }
//
////      @Test
////      public void testGetPost_ContentPrivate() throws Exception {
//
////          given(profileService.getUserByUsername(profile.getUsername())).willReturn(profile);
////          given(postService.getPost(privatePost.getContentID())).willThrow(new ContentIsPrivateException());
//
////          // Act & Assert
////          mockMvc.perform(get("/profiles/{username}/posts/{postId}", "husam_ramoni", 4L))
////                  .andExpect(status().isForbidden())
////                  .andExpect(jsonPath("$.title").value("Action Not Allowed"));
//
////          // Verify service interaction
////          verify(postService).getPost(privatePost.getContentID());
////          verify(postService).getPost(4L);
////      }
//
////      @Test
////      @WithMockUser(username = "husam_ramoni")
////      public void testDeletePost_Success() throws Exception {
////          // Arrange
////          String username = "husam_ramoni";
////          long postId = post.getContentID();
//
////          doNothing().when(postService).deletePost(username, postId);
//
////          // Act & Assert
////          mockMvc.perform(delete("/profiles/{username}/posts/{postId}", username, postId))
////                  .andExpect(status().isNoContent());
//
////          // Verify service interaction
////          verify(postService).deletePost(username, postId);
////      }
//
////      @Test
////      @WithMockUser(username = "husam_ramoni")
////      public void testDeletePostNotFound() throws Exception {
////          // Arrange
////          String username = "husam_ramoni";
////          long postId = 123456L;
//
//
////          doThrow(new PostNotFoundException(postId)).when(postService).deletePost(username, postId);
//
////          // Act & Assert
////          mockMvc.perform(delete("/profiles/{username}/posts/{postId}", username, postId))
////                  .andExpect(status().isNotFound())
////                  .andExpect(jsonPath("$.title").value("Not Found"))
////                  .andExpect(jsonPath("$.detail").value("Post With ID:123456 Could Not Be Found."));
//
////          // Verify service interaction
////          verify(postService).deletePost(username, postId);
////      }
//
////      @Test
////      @WithMockUser(username = "husam_ramoni")
////      public void testEditPost_Success() throws Exception {
////          // Arrange
////          ObjectMapper objectMapper = new ObjectMapper();
////          objectMapper.registerModule(new JavaTimeModule()); // Register the Java Time module to handle Java 8 Date/Time
//
////          String username = "husam_ramoni";
////          long postId = post.getContentID();
////          Post editedPost = createTextPost("Edited Post", Privacy.PRIVATE, profile);
////          editedPost.setContentID(postId);
////          editedPost.setMediaData(null);
//
//
////          given(postService.editTimelinePost(username, postId, null,"Edited Post",Privacy.PRIVATE)).willReturn(editedPost);
//
////          String postJson = objectMapper.writeValueAsString(editedPost); // Serialize editedPost to JSON
//
////          // Act & Assert
////          mockMvc.perform(put("/profiles/{username}/posts/{postId}", username, postId)
////                          .param("text", "Edited Post")
////                          .param("privacy", Privacy.PRIVATE.name()) // Assuming Privacy is an enum with a name() method
////                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE) // This is key, as you're simulating form data
////                          .accept(MediaType.APPLICATION_JSON))
////                  .andDo(print()) // This will print the response which is useful for debugging
////                  .andExpect(status().isOk());
//
////      }
//
//
//
//
//
//
//
//
//
//
//
//
//
//<<<<<<< HEAD
////  }
//=======
// }
//>>>>>>> e017b60fc2e29922ab416dc65ac7f413d28ffef0
