package com.ling1.springmvc.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
=======
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
>>>>>>> 6d2d017d4c75e58175271779b56721445891cb6e
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

<<<<<<< HEAD
=======
import com.ling1.springmvc.player.PlayerColor;
import com.ling1.springmvc.player.PlayerService;

>>>>>>> 6d2d017d4c75e58175271779b56721445891cb6e
@Controller
@RequestMapping("/users")
public class UserController {
    
    public static final String USERS_LISTING="Users/UsersListing";
    public static final String USER_EDIT="Users/EditUser";
    public static final String REGISTER_EDIT="Users/RegisterUser";
    public static final String WELCOME = "welcome";

    private UserService userService;
<<<<<<< HEAD

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
=======
    private PlayerService playerService;

    @Autowired
    public UserController(UserService userService, PlayerService playerService){
        this.userService=userService;
        this.playerService=playerService;     
>>>>>>> 6d2d017d4c75e58175271779b56721445891cb6e
    }

    @ModelAttribute("status")
    public Collection<UserStatusEnum> populateStatus(){
        return this.userService.findStatus();
    }

    @GetMapping
    public ModelAndView showUsersListing(){
        ModelAndView result = new ModelAndView(USERS_LISTING);
        result.addObject("users", userService.getAllUsers());
        return result;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") int id){
        ModelAndView result=new ModelAndView(USER_EDIT);
        User user=userService.getUserById(id);
        if(user!=null){
            result.addObject("user", user);
        } else {
            result=showUsersListing();
        }
        return result;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") int id, @Valid User user, BindingResult br) {
        ModelAndView result=null;
        if(br.hasErrors()){
            result=new ModelAndView(USER_EDIT);
            result.addAllObjects(br.getModel());
        } else {
            User userToUpdate=userService.getUserById(id);

            if(userToUpdate!=null){
                BeanUtils.copyProperties(user, userToUpdate,"id");
<<<<<<< HEAD
                userService.save(userToUpdate);
                result=showUsersListing();
                result.addObject("message", "User saved successfully!");
            } else {
                result=showUsersListing();
                result.addObject("message", "Lobby with id "+id+" not found!");
=======
                if ((userToUpdate.getRole().equals("admin") || userToUpdate.getRole().equals("member"))){
                userService.save(userToUpdate);
                result = new ModelAndView("redirect:/users/");
                result.addObject("message", "User saved successfully!");
                }
                else{
                    result = new ModelAndView("redirect:/users/");
                    result.addObject("message", "Not valid role");
                }
            } else {
                result = new ModelAndView("redirect:/users/");
                result.addObject("message", "User with id "+id+" not found!");
>>>>>>> 6d2d017d4c75e58175271779b56721445891cb6e
            }
        }
        return result;
    }

    @GetMapping("/create")
    public ModelAndView createUser() {
        ModelAndView result = new ModelAndView(USER_EDIT);
        User user = new User();
        result.addObject("user", user);
        return result;
    }

    @PostMapping("/create")
    public ModelAndView saveNewUser(@Valid User user, BindingResult br){
        ModelAndView result = null;
        if(br.hasErrors()){
            result=new ModelAndView(USER_EDIT);
            result.addObject(br.getModel());
        } else {
            userService.save(user);
            result=showUsersListing();
            result.addObject("message", "User saved successfully");
        }
        return result;
    }

    @GetMapping("/register")
    public ModelAndView registerUser() {
        ModelAndView result = new ModelAndView(REGISTER_EDIT);
        User user = new User();
        result.addObject("user", user);
        return result;
    }

    @PostMapping("/register")
    public ModelAndView saveNewRegisteredUser(@Valid User user, BindingResult br){
        ModelAndView result = null;
        UserStatusEnum status = userService.findStatusById(2);
<<<<<<< HEAD
        user.setRole("member");
        user.setUserStatus(status);
        if(br.hasErrors()){
            result=new ModelAndView(REGISTER_EDIT);
            result.addObject(br.getModel());
        } else {
            userService.save(user);
            result = new ModelAndView(WELCOME);
            result.addObject("message", "User registered successfully");
=======
        PlayerColor prefColor = playerService.red();
        user.setRole("member");
        user.setUserStatus(status);
        user.setPrefColor(prefColor);
        if(br.hasErrors()){
            result=new ModelAndView(REGISTER_EDIT);
            result.addObject(br.getModel());
        } else if (userService.findUsername(user.getLogin())==null){
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userService.save(user);
            result = new ModelAndView(WELCOME);
            result.addObject("message", "User registered successfully");
        } else {
            result = new ModelAndView(REGISTER_EDIT);
            result.addObject("message", "Username "+user.getLogin()+" is already taken!");
>>>>>>> 6d2d017d4c75e58175271779b56721445891cb6e
        }
        return result;
    }


}
