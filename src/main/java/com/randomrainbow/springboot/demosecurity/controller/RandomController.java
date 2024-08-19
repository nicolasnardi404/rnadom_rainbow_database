package com.randomrainbow.springboot.demosecurity.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.randomrainbow.springboot.demosecurity.dto.DataUserProfile;
import com.randomrainbow.springboot.demosecurity.dto.UserProfileView;
import com.randomrainbow.springboot.demosecurity.dto.VideoRandomResponse;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.VideoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/api/randomvideo")
public class RandomController {
    private VideoService videoService;
    private UserRepository userRepository;

    @GetMapping("/{maxDuration}")
    public ResponseEntity<?> getRandomVideo(@PathVariable int maxDuration) {
        Video randomVideo = videoService.getRandomApprovedVideoByDuration(maxDuration);
        if (randomVideo == null) {
            return ResponseEntity.status(404).body("No approved videos found within the specified duration.");
        }
        
        VideoRandomResponse videoResponse = new VideoRandomResponse(randomVideo.getVideoLink(), randomVideo.getVideoDescription(), randomVideo.getUser().getUsername(), randomVideo.getTitle(), randomVideo.getEndpoint());
        return ResponseEntity.ok(videoResponse);

    }

    @GetMapping("/video/{token}")
    public ResponseEntity<?> getVideoByToken(@PathVariable String token) {  
        Video video = videoService.getVideoByToken(token);
        if (video == null) {
            return ResponseEntity.status(404).body("No video found with the specified token.");
        }

        VideoRandomResponse videoResponse = new VideoRandomResponse(video.getVideoLink(), video.getVideoDescription(), video.getUser().getUsername(), video.getTitle(), video.getEndpoint());
        return ResponseEntity.ok(videoResponse);
    }

    @GetMapping("/videosbyartist/{username}")
    public ResponseEntity<?> getVideoByArtist(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            DataUserProfile dataUserProfile = new DataUserProfile(user.getArtistDescription(), user.getSocialMedia());
            List<Video> allVideosApprovedByArtist = videoService.getAllVideosApprovedByArtist(user.getId());

            UserProfileView userProfileView = new UserProfileView(user.getUsername(), allVideosApprovedByArtist, dataUserProfile);

            return ResponseEntity.ok(userProfileView);
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}
