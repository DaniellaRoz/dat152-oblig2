/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/orders")
	public ResponseEntity<List<Order>> getAllBorrowOrders(
		@RequestParam(required = false) String expiry, 
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders;

		if (expiry != null)
			orders = orderService.findByExpiryDate(LocalDate.parse(expiry), pageable);
		else
			orders = orderService.findAllOrders();

		if (orders.isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(orders, HttpStatus.OK);

	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> getBorrowOrder(@PathVariable Long id) {

		try {

			Order order = orderService.findOrder(id);

			return new ResponseEntity<>(order, HttpStatus.OK);

		} catch (OrderNotFoundException e) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}

	}
	
	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {

		try {

			Order updatedOrder = orderService.updateOrder(order, id);

			return new ResponseEntity<>(updatedOrder, HttpStatus.OK);

		} catch (NoSuchElementException e) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}

	}
	
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Void> deleteBookOrder(@PathVariable Long id) {

		try {

			orderService.deleteOrder(id);

		} catch (NoSuchElementException e) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
	
}
