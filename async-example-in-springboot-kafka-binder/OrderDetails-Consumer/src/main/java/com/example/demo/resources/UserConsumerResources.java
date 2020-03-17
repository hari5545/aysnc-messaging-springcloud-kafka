package com.example.demo.resources;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetails;
import com.example.demo.model.UserDetail;
import com.example.demo.service.OrderServices;

@RestController
@RequestMapping("/consumer")
public class UserConsumerResources {

	UserDetail user;

	@Autowired
	OrderServices orderServices;

	@StreamListener(Processor.INPUT)
	@Async("threadPoolexecutor")
	public void getUserFromKafka(UserDetail user) {
		System.out.println(user);
		this.user=user;
	}

	@PostMapping(path = "/saveOrder")
	@Async("threadPoolexecutor")
	public  @ResponseBody CompletableFuture<OrderDetails> saveOrder(@RequestBody Order order)  {
		String orderBy=user.getFirstName()+user.getMiddleName()+user.getLastName();
		OrderDetails orderDetails=new OrderDetails(order.getOrderType(),orderBy,order.getStartDate(),order.getEndDate(), user.getId());
		CompletableFuture<OrderDetails> orderdetails2=orderServices.saveOrderDetails(orderDetails);
		return orderdetails2;
	}
}