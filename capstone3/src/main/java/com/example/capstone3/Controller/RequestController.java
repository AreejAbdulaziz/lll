package com.example.capstone3.Controller;

import com.example.capstone3.DTO.RequestDTO;
import com.example.capstone3.Model.Request;
import com.example.capstone3.Service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    @GetMapping("/get")
    public ResponseEntity getAllRequests(){
        return ResponseEntity.status(200).body(requestService.getAllRequests());
    }
    //////////ممكن بعدين اقترح له تيم ثاني
    @PostMapping("/add")
    public ResponseEntity addRequest(@RequestBody@Valid RequestDTO requestDTO){
        requestService.addRequest(requestDTO);
        return ResponseEntity.status(200).body("Request Added");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRequest(@PathVariable Integer id){
        requestService.deleteRequest(id);
        return ResponseEntity.status(200).body("Request Deleted");
    }
    //1
    @GetMapping("/get/requests/{leader_id}/{team_id}")
    public ResponseEntity viewTeamRequestsForOneTeam(@PathVariable Integer leader_id,@PathVariable Integer team_id){
        return ResponseEntity.status(200).body(requestService.viewTeamRequestsForOneTeam(leader_id, team_id));
    }
    //2
    @GetMapping("/getIngo/{leader_id}/{request_id}")
    public ResponseEntity viewMemberWantToJoin(@PathVariable Integer leader_id,@PathVariable Integer request_id){
        return ResponseEntity.status(200).body(requestService.viewMemberWantToJoin(leader_id, request_id));
    }
    //3
    @PutMapping("/accept/{leader_id}/{request_id}")
    public ResponseEntity acceptRequest(@PathVariable Integer leader_id,@PathVariable Integer request_id){
        requestService.acceptRequest(leader_id, request_id);
        return ResponseEntity.status(200).body("Request Accepted!");
    }
    //4
    @PutMapping("/reject/{leader_id}/{request_id}")
    public ResponseEntity rejectRequest(@PathVariable Integer leader_id,@PathVariable Integer request_id){
        requestService.rejectRequest(leader_id, request_id);
        return ResponseEntity.status(200).body("Request Rejected!");
    }
}
