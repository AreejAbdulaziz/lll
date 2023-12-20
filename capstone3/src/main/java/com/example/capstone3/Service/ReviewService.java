package com.example.capstone3.Service;

import com.example.capstone3.Api.ApiException;
import com.example.capstone3.DTO.ReviewDTO;
import com.example.capstone3.Model.Member;
import com.example.capstone3.Model.Review;
import com.example.capstone3.Repository.MemberRepository;
import com.example.capstone3.Repository.ReviewRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
    public void addReview(ReviewDTO reviewDTO){
        Member member=memberRepository.findMemberById(reviewDTO.getMember_id());
        Member member2=memberRepository.findMemberByEmail(reviewDTO.getMember2_email().toString());

        if(member==null||member2==null){
            throw new ApiException("member or email not found!");
        }
        if (member.getTeam().getId()!=member2.getTeam().getId()){
            throw new ApiException("this member not with your team!");
        }
        Review review=new Review(null,reviewDTO.getRating(),reviewDTO.getComment(), member2.getEmail(), member);
        if(review.getRating()<=0.5){
            member2.setStrike(member.getStrike()+1);
            if(member2.getStrike()==5){
                member2.setIsBlacklist(true);
            }
            memberRepository.save(member2);
        }
        reviewRepository.save(review);

    }
    public void updateReview(ReviewDTO reviewDTO,Integer review_id){
        Review oldReview=reviewRepository.findReviewById(review_id);
        Member member=memberRepository.findMemberByEmail(reviewDTO.getMember2_email().toString());
        if(oldReview==null){
            throw new ApiException("review not found!");
        }
        if(oldReview.getRating()<=0.5&&reviewDTO.getRating()>0.5){
            member.setStrike(member.getStrike()-1);
        }
        else if(oldReview.getRating()>0.5&&reviewDTO.getRating()<=0.5){
            member.setStrike(member.getStrike()+1);
            //check
        }
        oldReview.setComment(reviewDTO.getComment());
        oldReview.setRating(reviewDTO.getRating());
        reviewRepository.save(oldReview);
    }
    public void deleteReview(Integer review_id){
        Review review=reviewRepository.findReviewById(review_id);
        Member member=memberRepository.findMemberByEmail(review.getMember2_email());
        if(review==null){
            throw new ApiException("review not found!");
        }
        if(review.getRating()<=0.5){
            member.setStrike(member.getStrike()-1);
            memberRepository.save(member);
        }
        reviewRepository.delete(review);
    }
    ////////////delete hackathon and all teams/////upd ha
}
