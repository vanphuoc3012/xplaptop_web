package com.xplaptop.admin.order;

import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
import com.xplaptop.common.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String viewOrdersFirstPage() {
        return "redirect:/orders/page/1?sortDir=desc&sortField=orderTime";
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String viewOrdersPage(@PagingAndSortingParam PagingAndSortingHelper helper,
                                 ModelMap model,
                                 @PathVariable(name = "pageNumber") Integer pageNumber,
                                 @RequestParam(name = "sortDir", required = false) String sortDir,
                                 @RequestParam(name = "sortField", required = false) String sortField,
                                 @RequestParam(name = "keyword", required = false) String keyword){
        if(sortDir == null || sortField == null) {
            return "redirect:/customers/page/"+pageNumber+"?sortField=firstName&sortDir=asc";
        }
        Page<Order> page = orderService.findOrders(pageNumber, sortDir, sortField, keyword);
        List<Order> orderList = page.getContent();
        helper.updateModelPaginationAttributes(pageNumber, page);

        model.put("orderList", orderList);
        return "order/orders";
    }
}
