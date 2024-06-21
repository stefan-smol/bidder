package com.bidder.userservice.controller;

import com.bidder.userservice.dto.UserDTO;
import com.bidder.userservice.model.User;
import com.bidder.userservice.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
  
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @PutMapping("/update/{username}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String username, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(username, userDTO); 
        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}
