package com.project.cravehub.controller.admin;

import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagement {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/listUsers")
    public String listUsers(Model model)
    {
        List<User> users = userRepository.findAll();
        model.addAttribute("listUsers",users);
        return "user-management";
    }

    @GetMapping ("blockUser/{id}")
    public String blockUser(@PathVariable Integer id)
    {
        userService.blockUser(id);
        return "redirect:/admin/listUsers";

    }

    @GetMapping("unblockUser/{id}")
    public String unblockUser(@PathVariable Integer id)
    {
        userService.unblockUser(id);

        return "redirect:/admin/listUsers";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("searchTerm") String searchTerm, Model model) {
        List<User> users = userService.getUsersByPartialEmailOrName(searchTerm);
        model.addAttribute("listUsers", users);
        return "user-management";
    }

}
