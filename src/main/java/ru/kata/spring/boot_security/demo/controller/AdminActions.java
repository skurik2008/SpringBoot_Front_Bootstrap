package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminActions {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminActions(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(@ModelAttribute("user_model") MyUser user, Model model, Principal principal) {
        model.addAttribute("user_current", userService.getUserByName(principal.getName()));
        List<MyUser> users = userService.getUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/user_save")
    public String saveUser(@ModelAttribute("user") MyUser user,
                           @RequestParam("roles_id") List<String> roles_id) {
        user.setRoles(roleService.getRolesByIds(roles_id));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/update_user")
    public String updateUser(@ModelAttribute("user") MyUser user,
                             @RequestParam("role") List<String> roles_id,
                             @RequestParam("id_current_user") Long id_current_user,
                             Principal principal) {
        user.setRoles(roleService.getRolesByIds(roles_id));
        userService.updateUser(user, id_current_user);
        if (roleService.getRolesByIds(roles_id).stream().allMatch(role -> role.getName().contains("USER")) && principal.getName().contains(user.getEmail())) {
            return "redirect:/user";
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/delete")
    public String delUser(@RequestParam long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
