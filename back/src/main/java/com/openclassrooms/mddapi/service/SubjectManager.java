package com.openclassrooms.mddapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.openclassrooms.mddapi.dto.TopicDTO;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubjectDataAccess;
import com.openclassrooms.mddapi.repository.AccountDataAccess;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectManager implements ISubjectManager {

    private final SubjectDataAccess categoryRepository;
    private final AccountDataAccess userRepository;

    public SubjectManager(SubjectDataAccess categoryRepository, AccountDataAccess userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> getAllTopics() {
        return categoryRepository.findAll().stream()
                .map(this::buildTopicDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> getSubscribedTopics(Long accountId) {
        User account = userRepository.findAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getSubscribedTopics().stream()
                .map(this::buildTopicDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> getUnsubscribedTopics(Long accountId) {
        User account = userRepository.findAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Topic> allCategories = categoryRepository.findAll();
        Set<Topic> followedCategoriesSet = account.getSubscribedTopics();
        List<Topic> followedCategoriesList = new ArrayList<>(followedCategoriesSet);

        List<Topic> availableCategories = allCategories.stream()
                .filter(category -> !followedCategoriesList.contains(category))
                .collect(Collectors.toList());

        return availableCategories.stream()
                .map(this::buildTopicDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void subscribeToTopic(Long accountId, Long subjectId) {
        User account = userRepository.findAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Topic category = categoryRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!account.getSubscribedTopics().contains(category)) {
            account.getSubscribedTopics().add(category);
            userRepository.save(account);
        }
    }

    @Override
    @Transactional
    public void unsubscribeFromTopic(Long accountId, Long subjectId) {
        User account = userRepository.findAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Topic category = categoryRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        account.getSubscribedTopics().remove(category);
        userRepository.save(account);
    }

    private TopicDTO buildTopicDto(Topic category) {
        return new TopicDTO(category.getId(), category.getTitle(), category.getDescription());
    }
}