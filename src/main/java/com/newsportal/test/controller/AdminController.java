package com.newsportal.test.controller;

import com.newsportal.test.model.Role;
import com.newsportal.test.model.User;
import com.newsportal.test.repo.RoleRepository;
import com.newsportal.test.repo.UserRepository;
import com.newsportal.test.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {
    private UserRepository userRepository;

    private UserService userService;

    private RoleRepository roleRepository;

    public AdminController(UserRepository userRepository, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/admin/home")
    public String home() {

        return "/admin/home";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/admin/listUser")
    public String userList(@PageableDefault(7) Pageable pageable,
                           Model model) {
        Page<User> users;
        users = userService.findAll(pageable);
        model.addAttribute("userList", users);
        return "/admin/userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/admin/createUser")
    public ModelAndView createUser() {
        List<Role> roles = roleRepository.findAll();
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("/admin/createUser");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/admin/createUser")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        User userExists = userService.findUserByUserName(user.getUserName());
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            return "/admin/createUser";
        } else {
            userService.saveUser(user);
            model.addAttribute("userList", userRepository.findAll());
            return "/admin/userList";
        }
    }

    @PreAuthorize(("hasAnyAuthority('ADMIN')"))
    @GetMapping("/admin/userList/update")
    public String updateUser(@RequestParam(name = "userId") Long userId, Model model) {
        User user = userRepository.findById(userId).get();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);

        return "/admin/userUpdate";
    }

    @PreAuthorize(("hasAnyAuthority('ADMIN')"))
    @PostMapping("/admin/userList/updateSave/{id}")
    public String updateUser(@PathVariable("id") Long userId, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/userUpdate";
        } else {
            user.setId(userId);
            userService.saveUser(user);
            return "redirect:/admin/listUser";
        }
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/userList/delete")
    public String deleteUser(@RequestParam(name = "userId") Long userId) {
        userRepository.deleteById(userId);
        return "redirect:/admin/listUser";
    }

}
