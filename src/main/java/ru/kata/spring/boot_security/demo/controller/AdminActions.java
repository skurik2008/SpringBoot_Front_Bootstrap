package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/list_users")
    public String mainPage(Model model) {
        List<MyUser> users = userService.getUsers();
        model.addAttribute("users", users);
        return "main";
    }

    @GetMapping("/new_user")
    public String newUser(@ModelAttribute("user") MyUser user, Model model) {
        model.addAttribute("flag", "new");
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "new_and_edit_user_form";
    }

    @PostMapping("user_save")
    public String saveUser(@ModelAttribute("user") MyUser user,
                           @RequestParam("roles_id") List<String> roles_id) {
        user.setRoles(roleService.getRolesByIds(roles_id));
        userService.addUser(user);
        return "redirect:/admin/list_users";
    }

    @GetMapping("edit_user")
    public String editeUser(@RequestParam long id, Model model) {
        model.addAttribute("flag", "edit");
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "new_and_edit_user_form";
    }

    @PatchMapping("update_user")
    public String updateUser(@ModelAttribute("user") MyUser user,
                             @RequestParam("roles_id") List<String> roles_id) {
        user.setRoles(roleService.getRolesByIds(roles_id));
        userService.updateUser(user);
        return "redirect:/admin/list_users";
    }

    @DeleteMapping("/delete")
    public String delUser(@RequestParam long id) {
        userService.deleteUser(id);
        return "redirect:/admin/list_users";
    }

}
