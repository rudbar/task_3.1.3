package com.kachanyan.spring.security.controllers;

import com.kachanyan.spring.security.models.Role;
import com.kachanyan.spring.security.models.User;
import com.kachanyan.spring.security.repositories.RoleRepo;
import com.kachanyan.spring.security.repositories.UserRepo;
import com.kachanyan.spring.security.services.RoleService;
import com.kachanyan.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {
    private UserService userService;
    private UserRepo userRepo;
    private RoleService roleService;
    private RoleRepo roleRepo;

    @Autowired
    public UserController(UserService userService, UserRepo userRepo, RoleService roleService, RoleRepo roleRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.roleRepo = roleRepo;
    }

    @GetMapping(path = "/")
    public String homePage() {
        return "login";
    }

    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal, Model model) {
        User user = userRepo.getUserByUsername(principal.getName());
//		Authentication a = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userRoles", user.getRoles());
        model.addAttribute("userAuthorities", user.getAuthorities());
        return "authenticated";
    }

    @GetMapping(path = "/admin/main")
    public String mainPageForAdmin(Principal principal, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/main";
    }

    @GetMapping(path = "/user/user")
    public String userPageForUserAndAdmin(Principal principal, Model model) {
        User user = userRepo.getUserByUsername(principal.getName());
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userRoles", user.getRoles());
        return "/user/user";
    }

    @GetMapping(path = "/admin/new")
    public String sendNewUserPageForAdmin(Principal principal, @ModelAttribute("user") User user) {
        return "/admin/new";
    }

    @PostMapping()
    public String createNewUserPageForAdmin(Principal principal,
                                            @ModelAttribute("user") User user,
                                            Model model,
                                            @RequestParam(required = false) String[] roles) {

        Set<Role> roleSet = new HashSet<>();

        if (roles != null) {
            for (String role : roles) {
                roleSet.add(roleService.getRoleByName(role));
            }
            user.setRoles(roleSet);
        }
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
                                         @RequestParam(required = false) String[] roles) {
        Set<Role> roleSet = new HashSet<>();

        if (roles != null) {
            for (String role : roles) {
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
