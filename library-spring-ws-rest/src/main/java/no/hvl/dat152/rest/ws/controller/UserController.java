/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(){
		
		List<User> users = userService.findAllUsers();
		
		if(users.isEmpty())
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException{
		
		User user = userService.findUser(id);
		
		return new ResponseEntity<>(user, HttpStatus.OK);	
		
	}

	@PostMapping(value = "/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		User newUser = userService.saveUser(user);

		if (newUser == null)
			return new ResponseEntity<>(newUser, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(newUser, HttpStatus.CREATED);

	}

	@PutMapping(value = "/users/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {

		User updatedUser = userService.updateUser(user, id);

		if (updatedUser == null)
			return new ResponseEntity<>(updatedUser, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(updatedUser, HttpStatus.OK);

	}


	@DeleteMapping("users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

		try {

			userService.deleteUser(id);

		} catch (UserNotFoundException e) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	@GetMapping("users/{id}/orders")
	public ResponseEntity<Set<Order>> getUserOrders(@PathVariable Long id) {

		Set<Order> orders = userService.getUserOrders(id);

		if (orders == null)
			return ResponseEntity.notFound().build();

		return new ResponseEntity<>(orders, HttpStatus.OK);

	}
	
	@GetMapping("users/{uid}/orders/{oid}")
	public ResponseEntity<Order> getUserOrder(@PathVariable Long uid, @PathVariable Long oid) {

		Order order = userService.getUserOrder(uid, oid);

		if (order == null)
			return ResponseEntity.notFound().build();

		return new ResponseEntity<>(order, HttpStatus.OK);

	}

	@DeleteMapping("users/{uid}/orders/{oid}")
	public ResponseEntity<Void> deleteUserOrder(@PathVariable Long uid, @PathVariable Long oid) {

		try {

			userService.findUser(uid);

		} catch (UserNotFoundException e) {

			return ResponseEntity.notFound().build();

		}

		if (userService.getUserOrder(uid, oid) == null)

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		userService.deleteOrderForUser(uid, oid);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
	
	@PostMapping("users/{id}/orders")
	public ResponseEntity<User> createUserOrder(@RequestBody Order order, @PathVariable Long userid) {
		
		User user = userService.createOrdersForUser(userid, order);

		if (user == null) {

			return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);

		}

		return new ResponseEntity<>(user, HttpStatus.CREATED);

	}

	
}
