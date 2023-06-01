package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.*;
import org.konkuk.klab.mtot.exception.DuplicatePostException;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JourneyRepository journeyRepository;
    @Autowired
    private MemberTeamRepository memberTeamRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("여정에 게시글을 성공적으로 등록한다.")
    public void uploadPost(){
        // given
        Long journeyId = registerAndReturnJourney().getId();

        // when
        postService.createPost(email, journeyId, title, article);

        // then
        List<Post> found = postRepository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo(title);
    }
    
    @Test
    @DisplayName("여정에 속하지 않는 사람이 게시글을 등록할 경우 예외를 발생한다.")
    public void validateWriterInGroup(){
        // given
        Long journeyId = registerAndReturnJourney().getId();
        String newMail = "def@mail.net";
        Member member = new Member("Park", newMail);
        memberRepository.save(member);

        assertThatThrownBy(()->postService.createPost(newMail, journeyId, title, article))
                .isInstanceOf(TeamAccessDeniedException.class);

    }

    @Test
    @DisplayName("게시글이 이미 존재할 경우 예외를 발생한다.")
    public void validateDuplicatePost(){
        // given
        Long journeyId = registerAndReturnJourney().getId();
        postService.createPost(email, journeyId, title, article);

        assertThatThrownBy(()-> postService.createPost(email, journeyId, "New Post1", "Article 1"))
                .isInstanceOf(DuplicatePostException.class);
    }

    @Test
    @DisplayName("그룹장이 아닌 다른 그룹원이 성공적으로 게시글을 등록한다.")
    public void createPostByAnotherGroupMember(){
        // given
        String newMail = "def@mail.net";
        Journey journey = registerAndReturnJourney();
        Member member = new Member("Park", newMail);
        memberRepository.save(member);
        memberTeamRepository.save(new MemberTeam(member, journey.getTeam()));

        // when
        assertThatNoException()
                .isThrownBy(()->
                        postService.createPost(newMail, journey.getId(), "New Title", "New Article"));
    }

    @Test
    @DisplayName("게시글을 성공적으로 수정한다.")
    public void editPost(){
        // given
        Long journeyId = registerAndReturnJourney().getId();
        postService.createPost(email, journeyId, title, article);

        // when
        String editTitle = "Edit Title";
        postService.editPost(email, journeyId, editTitle, "EDIT");

        assertThat(postRepository.findByJourneyId(journeyId).get().getTitle()).isEqualTo(editTitle);
    }

    @Test
    @DisplayName("그룹장이 아닌 다른 그룹원이 성공적으로 게시글을 수정한다.")
    public void editPostByAnotherGroupMember(){
        // given
        String newMail = "def@mail.net";
        Journey journey = registerAndReturnJourney();
        Member member = new Member("Park", newMail);
        memberRepository.save(member);
        memberTeamRepository.save(new MemberTeam(member, journey.getTeam()));
        postService.createPost(email, journey.getId(), "Title", "Article");

        // when
        assertThatNoException()
                .isThrownBy(()->
                        postService.editPost(newMail, journey.getId(), "New Title", "New Article"));
    }

    @Test
    @DisplayName("그룹 외의 사람이 게시글을 수정한다.")
    public void raiseWhenEditPostByAnotherPerson(){
        // given
        String newMail = "def@mail.net";
        Journey journey = registerAndReturnJourney();
        Member member = new Member("Park", newMail);
        memberRepository.save(member);
        postService.createPost(email, journey.getId(), "Title", "Article");

        // when
        assertThatThrownBy(()->
                        postService.editPost(newMail, journey.getId(), "New Title", "New Article"))
                .isInstanceOf(TeamAccessDeniedException.class);
    }

    @Test
    @DisplayName("게시글 중복 요청 시 예외를 발생한다")
    public void validateDuplicatePosts(){
        Journey journey = registerAndReturnJourney();
        postService.createPost(email, journey.getId(), title, article);

        assertThatThrownBy(()->{postService.createPost(email, journey.getId(), "More post", "More post article");})
                .isInstanceOf(DuplicatePostException.class);
    }

    private final String title = "Post no.1";
    private final String article = "Post Article Lorem Ipsum";
    private final String email = "abc@mail.com";
    private final String journeyName = "My Journey";
    
    private Journey registerAndReturnJourney(){
        Member member = new Member("Lee", email);
        Long id = memberRepository.save(member).getId();
        Team team = new Team("My Team", id);
        Long teamId = teamRepository.save(team).getId();
        MemberTeam memberTeam = new MemberTeam(member, team);
        memberTeamRepository.save(memberTeam);
        Journey journey = new Journey(team, journeyName);
        return journeyRepository.save(journey);
    }

    @AfterEach
    public void tearDown(){
        postRepository.deleteAll();
        journeyRepository.deleteAll();
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
