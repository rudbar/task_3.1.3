package com.kachanyan.spring.security.controllers;

import com.kachanyan.spring.security.models.Role;
import com.kachanyan.spring.security.models.User;
import com.kachanyan.spring.security.services.RoleService;
import com.kachanyan.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(path = "/user/user")
    public String userInfo(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "user/user";
    }

    @GetMapping(value = "/admin/main")
    public String listUsers(@AuthenticationPrincipal User user, @AuthenticationPrincipal Role role, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.allRoles());
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin/main";
    }

    @PostMapping("admin/create")
    public String create(@ModelAttribute User user, @RequestParam(value = "newRoles") String[] editRoles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : editRoles) {
            roleSet.add(roleService.getRoleByName(role));
        }
        user.setRoles(roleSet);
        userService.addUser(user);

        return "redirect:/admin/main";
    }

    @GetMapping(path = "/admin/{id}/edit")
    public String editUserPageForAdmin(Principal principal, Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/edit";
    }

    @PatchMapping(path = "/admin/{id}")
    public String updateUserPageForAdmin(Principal principal,
                                         @ModelAttribute("user") User user,
                                         @PathVariable("id") Long id,
                                         @RequestParam(value = "editRoles") String[] editRoles) {
        Set<Role> roleSet = new HashSet<>();

        if (editRoles != null) {
            for (String role : editRoles) {
                roleSet.add(roleService.getRoleByName(role));
            }
            user.setRoles(roleSet);
        }

        userService.editUser(user);
        return "redirect:/admin/main";
    }

    @DeleteMapping(path = "/admin/{id}")
    public String deleteUserPageForAdmin(Principal principal, @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/main";
    }
}
