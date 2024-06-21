package com.bidder.userservice.service;

import com.bidder.userservice.model.User;
import com.bidder.userservice.dto.UserDTO;
import com.bidder.userservice.model.ShippingAddress;
import com.bidder.userservice.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.bidder.userservice.repository.ShippingAddressRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShippingAddressRepository shippingAddressRepository;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}

    public UserDTO getUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return convertToDTO(user);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser(String username, UserDTO userDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setShippingAddress(userDTO.getShippingAddress());

        User updatedUser = userRepository.save(user);
        ShippingAddress updatedShippingAddress = shippingAddressRepository.save(user.getShippingAddress());
        return convertToDTO(updatedUser);
    }

	@Transactional
	public void deleteUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		shippingAddressRepository.delete(user.getShippingAddress());
		userRepository.delete(user);
		
	}
	
	public User convertToEntity(UserDTO userDTO) {
	    User user = new User();
	    user.setUsername(userDTO.getUsername());
	    user.setEmail(userDTO.getEmail());
	    user.setFirstName(userDTO.getFirstName());
	    user.setLastName(userDTO.getLastName());
	    user.setRole(userDTO.getRole());
	    user.setSessions(userDTO.getSessions());
	    user.setShippingAddress(userDTO.getShippingAddress());

	    return user;
	}

	public UserDTO convertToDTO(User user) {
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUsername(user.getUsername());
	    userDTO.setEmail(user.getEmail());
	    userDTO.setFirstName(user.getFirstName());
	    userDTO.setLastName(user.getLastName());
	    userDTO.setRole(user.getRole());
	    userDTO.setSessions(user.getSessions());
	    userDTO.setShippingAddress(user.getShippingAddress()); 

	    return userDTO;
	}
}
