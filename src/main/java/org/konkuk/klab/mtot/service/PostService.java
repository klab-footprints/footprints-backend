package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Post;
import org.konkuk.klab.mtot.dto.response.PostCreateResponse;
import org.konkuk.klab.mtot.exception.DuplicatePostException;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.exception.JourneyNotFoundException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JourneyRepository journeyRepository;

    @Transactional
    public PostCreateResponse createPost(String memberEmail, Long journeyId, String title, String article){
        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);

        journey.getTeam().getMemberTeams()
                .stream()
                .filter(memberTeam -> memberTeam.getMember().getEmail().equals(memberEmail))
                .findAny()
                .orElseThrow(TeamAccessDeniedException::new);

        postRepository.findByJourneyId(journeyId)
                .ifPresent(foundJourney -> {throw new DuplicatePostException();});

        Post post = new Post(journey, title, article);
        Long postId = postRepository.save(post).getId();
        return new PostCreateResponse(postId);
    }
}
