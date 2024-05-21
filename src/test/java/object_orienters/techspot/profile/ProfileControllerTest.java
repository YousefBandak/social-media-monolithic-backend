//package object_orienters.techspot.profile;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import object_orienters.techspot.exceptions.UserCannotFollowSelfException;
//import object_orienters.techspot.postTypes.DataType;
//import object_orienters.techspot.security.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(ProfileController.class)
//@EnableMethodSecurity
//class ProfileControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProfileModelAssembler assembler;
//
//    @MockBean
//    private ProfileService profileService;
//
//    @InjectMocks
//    private ProfileController profileController;
//    private ObjectMapper objectMapper;
//
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    private User user;
//    private Profile profile;
//    private Profile profile2;
//
//    public static User createUser(String username, String email, String password) {
//
//        return new User(username, email, password);
//    }
//
//    public static Profile createProfile(User user, String name , String profession, String email, DataType profilePic, Profile.Gender gender, String dob) {
//        return new Profile(user, name, profession, email, profilePic, gender, dob);
//    }
//
//    @BeforeEach
//    public void setup(WebApplicationContext context) {
//        objectMapper = new ObjectMapper();
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        user = createUser("husam_ramoni", "husam@example.com", "securepassword123");
//        profile = createProfile(user, "Husam Ramoni", "Software Engineer", "husam@example.com",
//                null, Profile.Gender.MALE, "1985-04-12");
//        profile2 = createProfile(user, "yousef_albadndak", "Software Engineer", "yousef@example.com",
//                null, Profile.Gender.MALE, "1985-04-12");
//    }
//
//    @Test
//    public void testGetProfile() throws Exception {
//        EntityModel<Profile> mockEntityModel = EntityModel.of(profile);
//        given(profileService.getUserByUsername(profile.getUsername())).willReturn(profile);
//        given(assembler.toModel(profile)).willReturn(mockEntityModel);
//
//        mockMvc.perform(get("/profiles/{username}", profile.getUsername())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    @WithMockUser(username = "yousef_albadndak")
//    public void addNewFollowerToProfile() throws Exception {
//
//        ObjectNode followerUserName = objectMapper.createObjectNode();
//        followerUserName.put("username", "yousef_albadndak");
//
//        EntityModel<Profile> profileEntityModel = EntityModel.of(profile);
//
//        given(profileService.addNewFollower(profile.getUsername(), "yousef_albadndak")).willReturn(profile);
//        given(assembler.toModel(profile)).willReturn(profileEntityModel);
//
//        // Execute & Assert
//        mockMvc.perform(post("/profiles/{username}/followers", profile.getUsername())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(followerUserName.toString()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith("application/hal+json"));
//    }
//
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testDeleteFollower_Success() throws Exception {
//        ObjectNode followerUserName = objectMapper.createObjectNode();
//        followerUserName.put("username", "yousef_albadndak");
//
//        EntityModel<Profile> profileEntityModel = EntityModel.of(profile);
//
//        given(profileService.addNewFollower(profile.getUsername(), "yousef_albadndak")).willReturn(profile);
//        given(assembler.toModel(profile)).willReturn(profileEntityModel);
//
//        doNothing().when(profileService).deleteFollower(profile.getUsername(), "yousef_albadndak");
//
//        mockMvc.perform(delete("/profiles/{username}/followers", "husam_ramoni")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"deletedUser\": \"yousef_albadndak\"}"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testDeleteFollower_UserNotFound() throws Exception {
//        // Assume that the follower is not found, and UserNotFoundException is thrown
//        doNothing().when(profileService).deleteFollower(profile.getUsername(), "yousef_albadndak");
//
//        mockMvc.perform(delete("/{username}/followers", "husam_ramoni")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("\"yousef_albadndak\""))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithMockUser(username = "husam_ramoni")
//    public void testProfileFollowHimself() throws Exception {
//        // Simulate the exception thrown when a user tries to follow themselves
//        given(profileService.addNewFollower("husam_ramoni", "husam_ramoni"))
//                .willThrow(new UserCannotFollowSelfException("husam_ramoni"));
//
//        // Attempt to follow oneself
//        mockMvc.perform(post("/profiles/{username}/followers", "husam_ramoni")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"husam_ramoni\"}"))
//                .andExpect(status().isBadRequest());
//    }
//}
