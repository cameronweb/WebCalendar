package ru.work.webcalendar.Controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.WebEntityManager;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.utils.Pair;

import javax.servlet.http.HttpServletRequest;

@RestController @RequestMapping("/users")
public class UsersController {
    @Autowired
    WebEntityManager webEntityManager;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    UsersDAO usersDAO;
    @GetMapping("")
    public List<User> users(@RequestParam(value="name", defaultValue="Cameron") String name) throws ParseException {
        return usersDAO.getUsers();
    }
    @GetMapping(value = "{id}")
    public ResponseEntity<User> user(@PathVariable String id)
    {
        ResponseEntity<User> responseEntity;
        User user = webEntityManager.getById(User.class,id);
        if (user!=null)
        {
            responseEntity=ResponseEntity.status(HttpStatus.OK).body(user);
            return responseEntity;
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(webEntityManager.getNewUser());
        }
    }
    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id)
    {       int result= webEntityManager.deleteEntity(id,User.class);
            if (result==HttpStatusCode.OK) {
                return ResponseEntity.status(HttpStatus.OK).body("Deleted");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NotFound");
    }
    @PutMapping(value = "{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,@RequestBody User user){
        int result= webEntityManager.updateEntity(user,id);
        if (result==HttpStatusCode.OK)
        {
          return ResponseEntity.status(HttpStatus.OK).body("Updated");
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NotFound");
    }

    @PostMapping("")
    public ResponseEntity<String> insertUser(@RequestBody User user, HttpServletRequest request) {
        Pair<String,Boolean> result= webEntityManager.insertEntity(user);
        if(!result.right()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NotFound");
        }else return ResponseEntity.status(HttpStatus.OK).body(result.left());
    }


}
