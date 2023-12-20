package com.example.capstone3.Service;

import com.example.capstone3.Api.ApiException;
import com.example.capstone3.DTO.TeamDTO;
import com.example.capstone3.Model.Hackathon;
import com.example.capstone3.Model.Member;
import com.example.capstone3.Model.Team;
import com.example.capstone3.Repository.HackathonRepository;
import com.example.capstone3.Repository.MemberRepository;
import com.example.capstone3.Repository.ProjectRepository;
import com.example.capstone3.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ProjectRepository projectRepository;
    private final HackathonRepository hackathonRepository;
    private final HackathonService hackathonService;
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public void addTeam(TeamDTO teamDTO) {
        Member leader = memberRepository.findMemberById(teamDTO.getMember_id());
        if (!leader.getRole().equals("Leader")||leader==null) {
            throw new ApiException("member must be a leader to creat team");
        }
        Hackathon hackathon=hackathonRepository.findHackathonById(teamDTO.getHackathon_id());
        if(hackathon==null){
            throw new ApiException("The Wanted Hackathon IS Not Found");
        }
        if(hackathon.getMax()<teamDTO.getMaxCap()){
            throw new ApiException("max capacity of team more than max capacity of hackathon");
        }
        if ( hackathon.getStartDate().isAfter(LocalDate.now())) {

            Team team=new Team(null,teamDTO.getName(),teamDTO.getDescription(),teamDTO.getMaxCap(),null,null,hackathon,null);
            teamRepository.save(team);
            hackathonService.assignTeamToHackathon(hackathon.getId(), team.getId());
            assignMemberToTeam(team.getId(), teamDTO.getMember_id());
        }
        else throw new ApiException("Hackathon's Registration Period Ended!");

//        Project project=projectRepository.findProjectById(team.getProject().getId());
//        if(project==null||project.getTeam().getId()!= team.getId()){
//            throw new ApiException("Project not found");
//        }
        //teamRepository.findTeamById(team.getId()).setMembers();
    }
    public void updateTeam(TeamDTO teamDTO,Integer team_id,Integer member_id) {
        Member leader = memberRepository.findMemberById(member_id);
        if (!leader.getRole().equals("Leader")) {
            throw new ApiException("member must be a leader to update team");
        }
        Team oldTeam = teamRepository.findTeamById(team_id);
        if (oldTeam == null || leader == null) {
            throw new ApiException("team or leader not found");
        }
//        for(Member m:team.getMembers()){
//            Member member1=memberRepository.findMemberById(m.getId());
//            if(member1==null) ////////////////////////////////////////////////////////////
//            {
//                throw new ApiException("Member not found");
//            }
        for (Member m : oldTeam.getMembers()) {
            if (m.getId() == member_id) {
                oldTeam.setName(teamDTO.getName());
                oldTeam.setDescription(teamDTO.getDescription());
                oldTeam.setMaxCap(teamDTO.getMaxCap());
            }
            teamRepository.save(oldTeam);
        }
    }
    public void deleteTeam(Integer id,Integer member_id){
        Member leader = memberRepository.findMemberById(member_id);
        if (!leader.getRole().equals("Leader")||leader==null) {
            throw new ApiException("member must be a leader to delete team");
        }
        Team team=teamRepository.findTeamById(id);
        if(team==null){
            throw new ApiException("team not found");
        }
        //check leader is leader for this team
        for(Member m: team.getMembers()){
            if(m.getId()==member_id){
                for(Member s: team.getMembers()){
                    s.setTeam(null);
                    memberRepository.save(s);
                }
                teamRepository.delete(team);
            }
        }
    } ///////////اذا انحذف التيم اللي داخل ينحذفون بعد جربييه

    public void assignMemberToTeam(Integer team_id,Integer member_id){
        Team team=teamRepository.findTeamById(team_id);
        Member member=memberRepository.findMemberById(member_id);

        if(team==null||member==null){
            throw new ApiException("team or member not found");
        }
        if(member.getIsBlacklist().equals(true)){
            throw new ApiException(member+ "this member on blacklist");
        }
        team.setMaxCap(team.getMaxCap()-1);
        member.setParticipationTimes(member.getParticipationTimes()+1);
        member.setTeam(team);

        memberRepository.save(member);
    }
    //request by leader?

}

//////////change leader
/////////project