package com.example.capstone3.Service;

import com.example.capstone3.Api.ApiException;
import com.example.capstone3.DTO.RequestDTO;
import com.example.capstone3.Model.Member;
import com.example.capstone3.Model.Request;
import com.example.capstone3.Model.Team;
import com.example.capstone3.Repository.MemberRepository;
import com.example.capstone3.Repository.RequestRepository;
import com.example.capstone3.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;
    public List<Request> getAllRequests(){
        return requestRepository.findAll();
    }
    public void addRequest(RequestDTO requestDTO){
        Member member=memberRepository.findMemberById(requestDTO.getMember_id());
        Team team=teamRepository.findTeamById(requestDTO.getTeam_id());
        Request request1=requestRepository.findRequestByMemberAndTeamId(member.getId(), team.getId()); //عشان مايرسل ركوست مرتين لنفس التيم
        if(member==null||team==null){
            throw new ApiException("member or team is null, request cannot be created");
        }
        if(member.getIsBlacklist()){
            throw new ApiException("sorry you are blocked");
        }
        if (member.getRole().equals("Leader")){
            throw new ApiException("you must a be member to join not leader, change your role!");
        }
        if(member.getTeam()!=null){
            throw new ApiException("You Are Already In Team!");
        }
        if(request1!=null){
                throw new ApiException("You Already Sent Request For This Team!");
        }
        if(team.getMaxCap()==0){
            throw new ApiException("you cant join to this team,capacity full");
        }
        Request request=new Request(null,"Pending",member,team);
        requestRepository.save(request);
    }
    public void deleteRequest(Integer id){
        Request request=requestRepository.findRequestById(id);
        if(request==null){
            throw new ApiException("Request Not Found");
        }
        requestRepository.delete(request);
    }
    //1
    public List<Request> viewTeamRequestsForOneTeam(Integer leader_id,Integer team_id){
        Member leader=memberRepository.findMemberById(leader_id);
        Team team=teamRepository.findTeamById(team_id);
        if(leader==null&&team==null){
            throw new ApiException("leader or team not found");
        }
        return team.getRequest();
    }
    //memid
    //2//ادمجي واحد و اثنين و تاكدي ان الركوست لهذا الفريق
    public ArrayList<String> viewMemberWantToJoin(Integer leader_id,Integer request_id){
        Member leader=memberRepository.findMemberById(leader_id);
        Request request=requestRepository.findRequestById(request_id);
        if(leader==null||request==null){
            throw new ApiException("leader of request no found");
        }
        ArrayList<String> memInfo=new ArrayList<>();
        memInfo.add(request.getMember().getName());
        memInfo.add(request.getMember().getEmail());
        memInfo.add(request.getMember().getAge().toString());
        memInfo.add(request.getMember().getField());
        memInfo.add(request.getMember().getSkills().toString());
        memInfo.add(request.getMember().getExperience().toString());
        memInfo.add(request.getMember().getNationality());
        memInfo.add(request.getMember().getParticipationTimes().toString());
        memInfo.add(request.getMember().getWinningTimes().toString());
        memInfo.add(request.getMember().getReviews().toString());

        return memInfo;
    }

    //3 //تاكدي ان هذا الركوست لهذا الليدر
    public void acceptRequest(Integer leader_id,Integer request_id){
        Member leader=memberRepository.findMemberById(leader_id);
        Request request=requestRepository.findRequestById(request_id);
        if(leader==null||request==null){
            throw new ApiException("leader of request no found");
        }
        teamService.assignMemberToTeam(request.getTeam().getId(),request.getMember().getId());
        request.setStatus("Accepted");
        requestRepository.save(request);
    }

    //4
    public void rejectRequest(Integer leader_id,Integer request_id){
        Member leader=memberRepository.findMemberById(leader_id);
        Request request=requestRepository.findRequestById(request_id);
        if(leader==null||request==null){
            throw new ApiException("leader of request no found");
        }
        request.setStatus("Rejected");
        requestRepository.save(request);
    }
}
