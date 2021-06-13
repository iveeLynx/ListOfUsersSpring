package com.yaskovskyi.task4.controller;

import com.yaskovskyi.task4.details.UserData;
import com.yaskovskyi.task4.entity.User;
import com.yaskovskyi.task4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.transaction.Transactional;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/index")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/registration")
    public String showRegistration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        if (user.getEmail() != "" && user.getName() != "" && userRepo.findByEmail(user.getEmail()) == null) {
            user.setRegistrationDate(date.toString());
            user.setLastLoginDate(date.toString());
            user.setStatus("Offline");
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepo.save(user);
            return "index";
        }
        return "registration_failure";
    }

    @Transactional
    @GetMapping("/users")
    public String listUsers(Model model) {
        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserData) {
            name = ((UserData) principal).getUsername();
            if(userRepo.findByEmail(name).getStatus().equals("Blocked")){
                return "redirect:/logout_blocked";
            }
        } else {
            name = principal. toString();
        }
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        userRepo.updateLoginDate(name, date.toString());
        userRepo.changeStatusByName(name, "Online");
        List<User> listUsers = (List<User>) userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/logout_blocked")
    public String blockedUser(){
        return "logout_blocked";
    }

    @Transactional
    @PostMapping("/action")
    public String delete(@RequestParam(value = "my-checkbox", required = false) List<String> checkid,
                         @RequestParam(value = "action") String action) {

//        String action = a.
//        System.out.println(a);
        if (checkid != null) {
            switch(action){
                case "Delete users":
                    for (String checked : checkid) {
                        int id = Integer.parseInt(checked);
                        userRepo.deleteByIds(id);
                    }
                    break;
                case "Block users":
                    for(String checked: checkid){
                        int id = Integer.parseInt(checked);
                        userRepo.changeStatusById(id, "Blocked");
                    }
                    break;
                case "Unblock users":
                    for(String checked: checkid){
                        int id = Integer.parseInt(checked);
                        userRepo.changeStatusById(id, "Offline");
                    }
                    break;
            }
            return "action";
        } else {
//            return "action";
        }

        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserData) {
            name = ((UserData)principal).getUsername();
        } else {
            name = principal.toString();
        }
        if(userRepo.findByName(name) != null){
            return "action";
        }
        return"index";
}

    public String getPrincipal(){
        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserData) {
            name = ((UserData)principal).getUsername();
        } else {
            name = principal.toString();
        }
        return name;
    }

    @Transactional
    @GetMapping("/logout_tech")
    public String logoutUpdate(User user){
        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserData) {
            name = ((UserData)principal).getUsername();
            userRepo.changeStatusByName(name, "Offline");
        }
        return "redirect:/logout";
    }

}
