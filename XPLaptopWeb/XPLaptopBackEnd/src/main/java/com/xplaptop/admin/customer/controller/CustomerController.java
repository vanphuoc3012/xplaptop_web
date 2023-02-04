package com.xplaptop.admin.customer.controller;

import com.xplaptop.admin.customer.CustomerNotFoundException;
import com.xplaptop.admin.customer.CustomerService;
import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
import com.xplaptop.admin.settting.country.CountryRepository;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/customers")
    public String listAllCustomer() {
        return "redirect:/customers/page/1?sortDir=asc&sortField=firstName";
    }

    @GetMapping("/customers/page/{pageNumber}")
    public String listPageCustomer(@PagingAndSortingParam PagingAndSortingHelper helper,
                                   ModelMap model,
                                   @PathVariable(name = "pageNumber") Integer pageNumber,
                                   @RequestParam(name = "sortDir", required = false) String sortDir,
                                   @RequestParam(name = "sortField", required = false) String sortField,
                                   @RequestParam(name = "keyword", required = false) String keyword) {
        if(sortDir == null || sortField == null) {
            return "redirect:/customers/page/"+pageNumber+"?sortField=firstName&sortDir=asc";
        }
        Page<Customer> page = customerService.getPage(pageNumber, sortDir, sortField, keyword);
        List<Customer> customerList = page.getContent();
        model.put("customerList", customerList);
        helper.updateModelPaginationAttributes(pageNumber, page);
        return "customer/customers";
    }

    @GetMapping("/customers/enable/{id}")
    public String updateEnabledCustomerStatus(@PathVariable Integer id,
                                              ModelMap model,
                                              @RequestParam(name = "path") String path,
                                              RedirectAttributesModelMap ra) {
        try {
            String m = customerService.updateEnabledStatus(id);
            ra.addFlashAttribute("message", "Customer with ID: "+id+" has been "+m);
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:"+path.replace(">", "&");
    }

    @GetMapping("/customers/detail/{id}")
    public String detailsCustomer(ModelMap model,
                                  @PathVariable(name = "id") Integer id,
                                  @RequestParam(name = "path") String path,
                                  RedirectAttributesModelMap ra) {
        try {
            Customer customer = customerService.findCustomerById(id);
            model.put("customer", customer);
            model.put("path", path);
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:"+path.replace(">", "&");
        }
        return "/customer/customer_details_modal";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id,
                                 @RequestParam(name = "path") String path,
                                 RedirectAttributes ra) {
        try {
            customerService.deleteCustomerById(id);
            ra.addFlashAttribute("message", "Customer with ID: " + id + " has been deleted!");
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:"+path.replace(">", "&");
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(ModelMap model,
                               @PathVariable(name = "id") Integer id,
                               @RequestParam(name = "path") String path,
                               RedirectAttributes ra) {
        try {
            Customer customer = customerService.findCustomerById(id);
            model.put("customer", customer);
            List<Country> listAllCountries = countryRepository.findAllByOrderByNameAsc();
            model.put("listAllCountries", listAllCountries);
            model.addAttribute("title", "Edit customer");
            model.addAttribute("path", path);
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:"+path.replace(">", "&");
        }
        return "customer/customer_form";
    }

    @PostMapping("/customers/save")
    public String updateCustomer(Customer customer,
                               RedirectAttributes ra,
                               @RequestParam(name = "path") String path) {
        customerService.updateCustomer(customer);
        ra.addFlashAttribute("message", "Customer with ID: " + customer.getId() + " has been updated");
        return "redirect:"+path.replace(">", "&");
    }
}
