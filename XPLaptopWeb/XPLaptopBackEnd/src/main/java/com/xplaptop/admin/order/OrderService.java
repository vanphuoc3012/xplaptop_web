package com.xplaptop.admin.order;

import com.xplaptop.common.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final int ORDERS_PER_PAGE = 10;
    @Autowired
    private OrderRepository orderRepo;

    public Page<Order> findOrders(Integer pageNumber, String sortDir, String sortField, String keyword) {
        Sort sort = Sort.by(sortField);
        if(sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, ORDERS_PER_PAGE, sort);

        if(keyword == null) return orderRepo.findAll(pageable);

        return orderRepo.findAll(keyword, pageable);
    }
}
