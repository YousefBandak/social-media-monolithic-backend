package object_orienters.techspot;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import object_orienters.techspot.comment.CommentRepository;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.reaction.ReactionRepository;


public class LocalDatabase {
    @Configuration
    class LoadDatabase {

        @Bean
        CommandLineRunner initDatabase(ProfileRepository repository, ReactionRepository reactionRepository, PostRepository postRepository,
                                       CommentRepository commentRepository) {

            return args -> {


            };
        }
    }
}
