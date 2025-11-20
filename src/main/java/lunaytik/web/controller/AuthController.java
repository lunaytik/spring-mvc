package lunaytik.web.controller;

import lombok.RequiredArgsConstructor;
import lunaytik.web.domain.User;
import lunaytik.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user, Model model) {

        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "User already exists");
            return "auth/register";
        }

        userService.register(user.getUsername(), user.getPassword());
        return "redirect:/login?registered";
    }
}
