/**
 * 
 */
package no.hvl.dat152.rest.ws.service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired 
	private OrderRepository orderRepository;

	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	public User saveUser(User user) {
		
		try {

			return userRepository.save(user);

		} catch (Exception e) {

			return null;

		}

	}
	
	public void deleteUser(Long id) throws UserNotFoundException {

		User user = findUser(id);

		userRepository.delete(user);

	}
	
	public User updateUser(User user, Long id) {

		try {

			User existingUser = findUser(id);
			existingUser.setFirstname(user.getFirstname());
			existingUser.setLastname(user.getLastname());

			return userRepository.save(existingUser);

		} catch (UserNotFoundException e) {

			return null;

		}

	}

	
	public Set<Order> getUserOrders(Long userid) {
	
		try {

			User user = findUser(userid);

			Set<Order> orders = user.getOrders();

			if (orders.isEmpty())
				return null;

			return orders;

		} catch (UserNotFoundException e) {

			return null;

		}

	}
	
	public Order getUserOrder(Long userid, Long oid) {

		try {
			
			User user = findUser(userid);

			return user.getOrders().stream()
				.filter(o -> o.getId().equals(oid))
				.findFirst()
				.orElseThrow();

		} catch (NoSuchElementException | UserNotFoundException e) {
			
			return null;

		}

	}
	
	public void deleteOrderForUser(Long userid, Long oid) {
		
		try {

			//findUser(userid); // Just to check if user exists at all
			
			User user = findUser(userid);
			
			Order order = getUserOrder(userid, oid);
			
			if (order == null) {
				return;
			}
			
			user.removeOrder(order);
			userRepository.save(user);
			
			orderRepository.deleteById(oid);

		} catch (UserNotFoundException e) {

			return;

		}

		/*Order order = getUserOrder(userid, oid);

		if (order == null)
			return;

		orderRepository.deleteById(order.getId());*/

	}
	
	public User createOrdersForUser(Long userid, Order order) {
		
		try {
			
			User user = findUser(userid);

			user.addOrder(order);

			return userRepository.save(user);

		} catch (UserNotFoundException e) {

			return null; 

		}

	}
}
