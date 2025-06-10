package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.TopicDTO;

public interface ISubjectManager {
    List<TopicDTO> getAllTopics();
    List<TopicDTO> getSubscribedTopics(Long accountId);
    List<TopicDTO> getUnsubscribedTopics(Long accountId);
    void subscribeToTopic(Long accountId, Long subjectId);
    void unsubscribeFromTopic(Long accountId, Long subjectId);
}