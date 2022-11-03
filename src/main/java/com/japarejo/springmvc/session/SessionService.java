package com.japarejo.springmvc.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.japarejo.springmvc.board.BoardService;
import com.japarejo.springmvc.lobby.LobbyService;

@Service
public class SessionService {
	
	@Autowired
	private BoardService boardService;
	
	private SessionRepository sessionRepo;
		
	private SessionTypeService sessionTypeService;
	
	private LobbyService roomService;


	

}
