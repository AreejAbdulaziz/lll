package com.example.capstone3.Service;

import com.example.capstone3.Api.ApiException;
import com.example.capstone3.Model.Hackathon;
import com.example.capstone3.Model.Team;
import com.example.capstone3.Repository.HackathonRepository;
import com.example.capstone3.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    public List<Hackathon> getAllHackathon(){
        return hackathonRepository.findAll();
    }

    public void addHackathon(Hackathon hackathon){
        hackathonRepository.save(hackathon);
    }

    public void updateHackathon(Integer id,Hackathon hackathon){
        Hackathon oldHackathon=hackathonRepository.findHackathonById(id);
        if(oldHackathon==null){
            throw new ApiException("Hackathon not found");
        }
        oldHackathon.setName(hackathon.getName());
        oldHackathon.setName(hackathon.getName());
        oldHackathon.setMax(hackathon.getMax());
        oldHackathon.setPrize(hackathon.getPrize());
        oldHackathon.setMinAge(hackathon.getMinAge());
        oldHackathon.setMaxAge(hackathon.getMaxAge());
        oldHackathon.setStartDate(hackathon.getStartDate());
        oldHackathon.setEndDate(hackathon.getEndDate());
        oldHackathon.setCompanyName(hackathon.getCompanyName());
        oldHackathon.setCity(hackathon.getCity());
        oldHackathon.setIsOnline(hackathon.getIsOnline());
        oldHackathon.setWinningTeamId(hackathon.getWinningTeamId());
        oldHackathon.setIdTeams(hackathon.getIdTeams());

        hackathonRepository.save(oldHackathon);
    }

    public void deleteHackathon(Integer id){
        Hackathon hackathon=hackathonRepository.findHackathonById(id);
        if(hackathon==null){
            throw new ApiException("Hackathon not found");
        }
        hackathonRepository.delete(hackathon);
    }
    public void assignTeamToHackathon(Integer hackathonId,Integer teamId){
        Hackathon hackathon=hackathonRepository.findHackathonById(hackathonId);
        Team team=teamRepository.findTeamById(teamId);
        if(hackathon==null||team==null){
            throw new ApiException("Hackathon or team not found");
        }
        if(hackathon.getMax()<team.getMaxCap()){
            throw new ApiException(" Maximum Number Of Members Is : "+hackathon.getMax());
        }
        team.setHackathon(hackathon);
        List<Integer>t=hackathon.getIdTeams();
        t.add(teamId);
        hackathon.setIdTeams(t);

        Set<Team>s=hackathon.getTeams();
        s.add(team);
        hackathon.setTeams(s);

        hackathonRepository.save(hackathon);
        teamRepository.save(team);
    }

    public Integer getLowestPrize() {
        return hackathonRepository.findLowestPrizeHackathon();
    }

    //--- endpoint 2  اند بوينت تعرض الجائزه الاعلى ---
    public Integer getHighestPrize() {
        return hackathonRepository.findHighestPrizeHackathon();
    }


    //--- endpoint 3  اند بوينت تعرض الهاكثونات تبدا قبل تاريخ معين ---
    public List<Hackathon> getHackathonsStartingBeforeDate(LocalDate targetDate) {
        if (targetDate == null) {
            throw new ApiException("Invalid targetDate");
        }
        return hackathonRepository.findHackathonsStartingBeforeDate(targetDate);
    }

    //--- endpoint 4  اند بوينت تعرض الهاكثونات تبدا بعد تاريخ معين ---

    public List<Hackathon> getHackathonsStartingAfterDate(LocalDate targetDate) {
        if (targetDate == null) {
            throw new ApiException("Invalid targetDate");
        }
        return hackathonRepository.findHackathonsStartingAfterDate(targetDate);
    }


    //--- endpoint 5  اند بوينت تعرض الهاكثونات تبدا بين تاريخين معينه ---
    public List<Hackathon> findHackathonsByStartDateBetweenAndEndDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new ApiException("Invalid startDate or endDate");
        }
        return hackathonRepository.findHackathonsByStartDateBetweenAndEndDate(startDate, endDate);
    }


    //--- endpoint 6 اند بوينت تعرض الهاكثونات للاطفال ---
    public List<Hackathon> findHackathonChildren() {
        return hackathonRepository.findHackathonChildren();
    }

    //--- endpoint 7  اند بوينت تعرض الهاكثونات للمراهقين ---
    public List<Hackathon> findHackathonsForTeens() {
        return hackathonRepository.findHackathonsForTeens();
    }


    //--- endpoint 8  اند بوينت تعرض الهاكثونات للبالغين  ---
    public List<Hackathon> findHackathonsForAdults() {
        return hackathonRepository.findHackathonsForAdults();
    }


    //--- endpoint 9  اند بوينت تعرض الهاكثونات لمجال معين  ---
    public List<Hackathon> findHackathonByField(String field) {
        if (field == null || field.isEmpty()) {
            throw new ApiException("Invalid field");
        }
        return hackathonRepository.findHackathonByField(field);
    }

    //--- endpoint 10  اند بوينت تعرض هاكثونات لجهه معينه  ---
    public List<Hackathon> findHackathonByCompanyName(String companyName) {
        if (companyName == null || companyName.isEmpty()) {
            throw new ApiException("Invalid companyName");
        }
        return hackathonRepository.findHackathonByCompanyName(companyName);
    }


    //--- endpoint 11  اند بوينت تعرض هاكثونات اونلاين  ---
    public List<Hackathon> findOnlineHackathons() {
        return hackathonRepository.findHackathonsByIsOnlineTrue();
    }

    //--- endpoint 12  اند بوينت تعرض هاكثونات مدينه معينه  ---

    public List<Hackathon> findHackathonsByCity(String city) {
        if (city == null || city.isEmpty()) {
            throw new ApiException("Invalid city");
        }
        return hackathonRepository.findHackathonsByCity(city);
    }


    //--- endpoint 13  اند بوينت ابحث عن الهاكثون باسمه  ---
    public Hackathon findHackathonByName(String name) {
        Hackathon hackathon = hackathonRepository.findHackathonByName(name);
        if (hackathon == null) {
            throw new ApiException("Hackathon with name " + name + " not found");
        }
        return hackathon;
    }

}
