package ru.work.webcalendar.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.Service.WebEntityManager;

import java.util.List;

@Controller
@RequestMapping("/workers")
public class WebUserController {
    @Autowired
    WebEntityManager webEntityManager;
    @GetMapping()
    public String getUsers(Model model){
       List<User> arrayList= webEntityManager.getAllEntities(User.class);
        model.addAttribute("users",arrayList);
        return "wrks";
    }
}
