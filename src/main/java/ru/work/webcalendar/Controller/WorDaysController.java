package ru.work.webcalendar.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.WebEntityManager;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.utils.Pair;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workdays")
public class WorDaysController {
    @Autowired
    WebEntityManager webEntityManager;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    WorkDayDAO workDayDAO;
    @GetMapping("")
    public ResponseEntity<List<WorkDay>> getWorkDays(){

        List<WorkDay> workDayArrayList=workDayDAO.getWorkDays();
        if(workDayArrayList.size()>0)
        {
            return ResponseEntity.status(HttpStatus.OK).body(workDayArrayList);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(workDayArrayList);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<WorkDay> getWorkDay(@PathVariable String id){
        WorkDay workDay = webEntityManager.getById(WorkDay.class,id);
        if(workDay!=null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(workDay);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(webEntityManager.getNewWorkDay());
    }
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<WorkDay>> getByUserID(@PathVariable String id){
        List<WorkDay> workDayArrayList=workDayDAO.getByUser(id,false);
        if(workDayArrayList.size()>0){
        return ResponseEntity.status(HttpStatus.OK).body(workDayArrayList);}
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(workDayArrayList);
    }




    @GetMapping(value = "/future")
    public ResponseEntity<List<WorkDay>> getFutureDays(){
        List<WorkDay> futureWorkDays=workDayDAO.getFutureWorkDays();
        if(futureWorkDays.size()>0){
            return ResponseEntity.status(HttpStatus.OK).body(futureWorkDays);}
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(futureWorkDays);
    }
    @GetMapping("/user")
    public ResponseEntity<List<WorkDay>> getByUserIDAndDate(@RequestParam(value = DataModel.WORKDAY_USERID_FIELD,defaultValue = "") String userid,
                                                                 @RequestParam(value = DataModel.WORKDAY_DATE_FIELD,defaultValue = "") String workdate){
        if(!userid.isEmpty() & !workdate.isEmpty()) {
            List<WorkDay> workDayArrayList = workDayDAO.getUserNextWorkDay(userid, Long.parseLong(workdate));
            if (workDayArrayList.size()>0){
                return ResponseEntity.status(HttpStatus.OK).body(workDayArrayList);
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(workDayArrayList);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @PostMapping("")
    public ResponseEntity<String> insertWorkDay(@RequestBody WorkDay workDay){
        Pair<String,Boolean> result= webEntityManager.insertEntity(workDay);
        if(result.right()){
            return ResponseEntity.status(HttpStatus.OK).body(result.left());
        }else return ResponseEntity.status(HttpStatus.CONFLICT).body(result.left());
    }
    @PutMapping("{id}")
    public ResponseEntity<String> updateWorDate(@RequestBody WorkDay workDay, @PathVariable String id){
        int result= webEntityManager.updateEntity(workDay,id);
        if (result== HttpStatusCode.OK) {
            return ResponseEntity.status(HttpStatus.OK).body("Update");
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.valueOf(result));
    }
    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteWorkday(@PathVariable String id){
        int result= webEntityManager.deleteEntity(id, WorkDay.class);
        if (result==HttpStatusCode.OK) {
            return ResponseEntity.status(HttpStatus.OK).body("Deleted");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NotFound");
    }
}
