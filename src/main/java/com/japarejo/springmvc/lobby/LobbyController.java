package com.japarejo.springmvc.lobby;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.japarejo.springmvc.match.MatchService;
import com.japarejo.springmvc.user.User;
import com.japarejo.springmvc.user.UserService;

@Controller
@RequestMapping("/lobbies")
public class LobbyController {

    public static final String LOBBIES_LISTING="LobbiesListing";
    public static final String LOBBY_EDIT="EditLobby";
    public static final String OCA_LISTING="OcaListing";
    public static final String PARCHIS_LISTING="ParchisListing";
    public static final String LOBBY_INSIDE="InsideLobby";


    //MATCHES DATA
    public static final String MATCHES_LISTING = "MatchesListing";
    public static final String MATCH_EDIT = "EditMatch";


    @Autowired
    LobbyService lobbyService;
    @Autowired
    MatchService matchService;
    @Autowired
    UserService userService;



	@ModelAttribute("games")
	public Collection<GameEnum> populateGameTypes() {
		return this.lobbyService.findGameTypes();
	}

     @GetMapping
     public ModelAndView showLobbiesListing() {
         ModelAndView result=new ModelAndView(LOBBIES_LISTING);
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User loggedUser = userService.findUsername(authentication.getName());
         for (Lobby lobby: lobbyService.getAllLobbies()){
            if(lobby.getPlayers()!=null){
            if (lobby.getPlayers().contains(loggedUser)){
                Collection<User> newPlayers = lobby.getPlayers();
                newPlayers.remove(loggedUser);
                lobby.setPlayers(newPlayers);
                lobbyService.save(lobby);
            }
        }
         }
         result.addObject("lobbies",lobbyService.getAllLobbies());
         return result;
     }
     @GetMapping("/oca")
     public ModelAndView showOcaListing() {
         ModelAndView result=new ModelAndView(OCA_LISTING);
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User loggedUser = userService.findUsername(authentication.getName());
         for (Lobby lobby: lobbyService.getAllLobbies()){
            if (lobby.getPlayers().contains(loggedUser)){
                Collection<User> newPlayers = lobby.getPlayers();
                newPlayers.remove(loggedUser);
                lobby.setPlayers(newPlayers);
                lobbyService.save(lobby);
            }
         }
         result.addObject("lobbiesOca",lobbyService.getAllOca());
         return result;
     }
     @GetMapping("/parchis")
     public ModelAndView showParchisListing() {
         ModelAndView result=new ModelAndView(PARCHIS_LISTING);
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User loggedUser = userService.findUsername(authentication.getName());
         for (Lobby lobby: lobbyService.getAllLobbies()){
            if (lobby.getPlayers().contains(loggedUser)){
                Collection<User> newPlayers = lobby.getPlayers();
                newPlayers.remove(loggedUser);
                lobby.setPlayers(newPlayers);
                lobbyService.save(lobby);
            }
         }
         result.addObject("lobbiesParchis",lobbyService.getAllParchis());
         return result;
     }

     @GetMapping("/delete/{id}")
     public ModelAndView deleteLobby(@PathVariable("id") int id) {
         lobbyService.deleteLobby(id);
         ModelAndView result=showLobbiesListing();
         result.addObject("message", "Lobby removed successfully");
         return result;
     }
     
     
     @GetMapping("/edit/{id}")
     public ModelAndView editLobby(@PathVariable("id") int id) {
         ModelAndView result=new ModelAndView(LOBBY_EDIT);
         Lobby lobby=lobbyService.getLobbyById(id);
         if(lobby!=null)
             result.addObject("lobby", lobby);
         else{
             result=showLobbiesListing();
            }                      
         return result;
     }
     
     @PostMapping("/edit/{id}")
     public ModelAndView editLobby(@PathVariable("id") int id, @Valid Lobby lobby,BindingResult br) {        
         ModelAndView result=null;
         if(br.hasErrors()) {
             result=new ModelAndView(LOBBY_EDIT);
             result.addAllObjects(br.getModel());         
         }else {
             Lobby lobbyToUpdate=lobbyService.getLobbyById(id);
             
             if(lobbyToUpdate!=null) {
                 lobbyToUpdate.setGame(lobby.getGame());                
                 lobbyService.save(lobbyToUpdate);
                 result=showLobbiesListing();
                 result.addObject("message", "Lobby saved succesfully!");
             }else {
                 result=showLobbiesListing();             
                 result.addObject("message", "Lobby with id "+id+" not found!");
             }
         }                                                
         return result;
     }
     
     @GetMapping("/create")
     public ModelAndView createLobby() {
         ModelAndView result=new ModelAndView(LOBBY_EDIT);
         Lobby lobby=new Lobby();         
         result.addObject("lobby", lobby);                                  
         return result;
     }
     
     
     @PostMapping("/create")
     public ModelAndView saveNewLobby(@Valid Lobby lobby,BindingResult br) {        
         ModelAndView result=null;
         if(br.hasErrors()) {
             result=new ModelAndView(LOBBY_EDIT);
             result.addAllObjects(br.getModel());         
         }else {                          
             lobbyService.save(lobby);
             result=showLobbiesListing();
             result.addObject("message", "Lobby saved succesfully!");             
         }                                                
         return result;
     }

     @GetMapping("/{id}")
     public ModelAndView insideLobby(@PathVariable("id") int id) {
        ModelAndView result=new ModelAndView(LOBBY_INSIDE);
        Lobby lobby=lobbyService.getLobbyById(id);
        Collection<User> players= lobbyService.findPlayersLobby(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userService.findUsername(authentication.getName());
        if(lobby!=null && players!=null){
            result.addObject("lobby", lobby);
            result.addObject("players", players);
            if(players.size()>=4){
                if(lobby.getGame().getName().contains("Oca")){
                    result=showOcaListing();
                }
                else{
                    result=showParchisListing();
                }
                result.addObject("message", "Lobby is full!");     
           }
            if(players.size()==0) {
                lobby.setHost(loggedUser);
                players.add(loggedUser);
                lobby.setPlayers(players);
                lobbyService.save(lobby);
            }else if(players.size()<=3){
                players.add(loggedUser);
                lobby.setPlayers(players);
                lobbyService.save(lobby);
            }
    }
        else{
        result=showLobbiesListing();
       } 
         return result;
     }

     // MATCHES

     @GetMapping("/{id}/matches")
    public ModelAndView showMatchesByLobbyId(@PathVariable("id") Integer id){
        ModelAndView result= new ModelAndView(MATCHES_LISTING);
        result.addObject("matches", matchService.findMatchesByLobbyId(id));
        return result;
    }

    /*@GetMapping("/create")
     public ModelAndView createMatch() {
         ModelAndView result=new ModelAndView(MATCH_EDIT);        
         result.addObject("match",new Match());   
         return result;
     }
     
     
     @PostMapping("/create")
     public ModelAndView saveNewMatch(@Valid Match match,BindingResult br) {        
         ModelAndView result=null;
         if(br.hasErrors()) {
             result=new ModelAndView(MATCH_EDIT);
             result.addAllObjects(br.getModel());         
         }else {                          
             matchService.save(match);
             result=showMatchesListing();
             result.addObject("message", "Match saved succesfully!");             
         }                                                
         return result;
     }*/
     

     
}
