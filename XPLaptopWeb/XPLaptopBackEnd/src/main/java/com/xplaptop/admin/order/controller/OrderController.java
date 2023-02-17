package com.xplaptop.admin.order.controller;

import com.xplaptop.admin.order.OrderService;
import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
import com.xplaptop.common.entity.order.Order;
import com.xplaptop.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

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

    @GetMapping("/orders/delete/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId,
                              @RequestParam(name = "path") String path,
                              RedirectAttributes ra) {
        try {
            orderService.deleteOrderById(orderId);
            ra.addFlashAttribute("message", "Order (ID: "+orderId+") has been deleted.");
        } catch (OrderNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:"+path.replace(">", "&");
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetail(ModelMap model,
                                  @PathVariable(name = "id") Integer id,
                                  @RequestParam(name = "path") String path,
                                  RedirectAttributesModelMap ra) {
        try {
            Order order = orderService.findOrderById(id);
            model.put("order", order);
            model.put("path", path);
        } catch (OrderNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:"+path.replace(">", "&");
        }
        return "order/order_details_modal";
    }
}
