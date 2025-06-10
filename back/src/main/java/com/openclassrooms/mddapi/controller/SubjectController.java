package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.service.ISubjectManager;
import com.openclassrooms.mddapi.dto.TopicDTO;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class SubjectController {
    private ISubjectManager subjectService;

    public SubjectController(ISubjectManager subjectService) {
        this.subjectService = subjectService;
    }

	@GetMapping("/all")
	public ResponseEntity<List<TopicDTO>> retrieveAllSubjects() {
		List<TopicDTO> allSubjects = subjectService.getAllTopics();
		return ResponseEntity.ok(allSubjects);
	}

	@GetMapping("/not-subscribed")
	public ResponseEntity<List<TopicDTO>> retrieveAvailableSubjects() {
		User authenticatedUser = extractAuthenticatedUser();
		List<TopicDTO> availableSubjects = subjectService.getUnsubscribedTopics(authenticatedUser.getId());
		return ResponseEntity.ok(availableSubjects);
	}

	@GetMapping("/subscribed")
	public ResponseEntity<List<TopicDTO>> retrieveFollowedSubjects() {
		User authenticatedUser = extractAuthenticatedUser();
		List<TopicDTO> followedSubjects = subjectService.getSubscribedTopics(authenticatedUser.getId());
		return ResponseEntity.ok(followedSubjects);
	}

	@PostMapping("/subscribe/{subjectId}")
	public ResponseEntity<Void> followSubject(@PathVariable Long subjectId) {
		User authenticatedUser = extractAuthenticatedUser();
		subjectService.subscribeToTopic(authenticatedUser.getId(), subjectId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/unsubscribe/{subjectId}")
	public ResponseEntity<Void> unfollowSubject(@PathVariable Long subjectId) {
		User authenticatedUser = extractAuthenticatedUser();
		subjectService.unsubscribeFromTopic(authenticatedUser.getId(), subjectId);
		return ResponseEntity.ok().build();
	}

	private User extractAuthenticatedUser() {
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		if (currentAuth == null || !(currentAuth.getPrincipal() instanceof User)) {
			throw new RuntimeException("Authentication verification failed or invalid user principal");
		}
		return (User) currentAuth.getPrincipal();
	}
}