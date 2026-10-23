/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order saveOrder(Order order) {
		
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOrder(Long id) throws OrderNotFoundException {
		
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: "+id+" not found in the order list!"));
		
		return order;
	}
	
	public void deleteOrder(Long id) throws NoSuchElementException {

		Order orderToDelete = orderRepository.findById(id).orElseThrow();

		orderRepository.delete(orderToDelete);

	}
	
	/* 
	public List<Order> findAllOrders(Pageable page) {

		return orderRepository.findAllPageable(
				page.getPageSize(),
				(int) page.getOffset());

	}
	*/

	public List<Order> findAllOrders(Pageable page) {
		return orderRepository.findAll(page).getContent();
	}
	
	public List<Order> findByExpiryDate(LocalDate expiry, Pageable page) {
		
		return orderRepository.findOrderByExpiry(
			expiry, 
			page.getPageSize(), 
			(int) page.getOffset());

	}
	
	public Order updateOrder(Order order, Long id) throws NoSuchElementException {

		Order existingOrder = orderRepository.findById(id).orElseThrow();

		existingOrder.setIsbn(order.getIsbn());
		existingOrder.setExpiry(order.getExpiry());

		return orderRepository.save(existingOrder);

	}

}
