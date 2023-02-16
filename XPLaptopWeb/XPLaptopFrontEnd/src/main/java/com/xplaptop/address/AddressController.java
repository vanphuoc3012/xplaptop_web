package com.xplaptop.address;

import com.xplaptop.Utitlity;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.exception.AddressNotFoundException;
import com.xplaptop.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/address_book")
    public String viewAddressBook(ModelMap model,
                                  HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> addressList = addressService.getAllCustomerAddress(customer.getId());
        model.put("addressList", addressList);
        return "address/address_book";
    }

    @GetMapping("/address_book/new")
    public String viewAddressForm(ModelMap model) {
        List<Country> countryList = addressService.listAllCountry();
        model.put("countryList", countryList);
        model.put("address", new Address());
        model.put("pageTitle", "Create new address");
        return "address/address_form";
    }

    @PostMapping("/address_book/save")
    public String saveNewAddress(Address address,
                                 RedirectAttributes ra,
                                 HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        address.setCustomer(customer);
        addressService.save(address);
        ra.addFlashAttribute("message", "New address has been added");
        return "redirect:/address_book";
    }

    @GetMapping("/address_book/edit/{addressId}")
    public String editAddress(@PathVariable(name = "addressId") Integer addressId,
                              HttpServletRequest request,
                              ModelMap model,
                              RedirectAttributes ra) {
        Customer customer = getAuthenticatedCustomer(request);
        try {
            Address address = addressService.getAddressByIdAndCustomer(addressId, customer);
            model.put("address", address);

            List<Country> countryList = addressService.listAllCountry();
            model.put("countryList", countryList);

            List<State> stateList = addressService.listAllStateFromCountry(address.getCountry().getId());
            model.put("stateList", stateList);
            return "address/address_form";
        } catch (AddressNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/address_book";
        }
    }

    @GetMapping("/address_book/delete/{addressId}")
    public String deleteAddress(@PathVariable(name = "addressId") Integer addressId,
                              HttpServletRequest request,
                              RedirectAttributes ra) {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.deleteAddressByIdAndCustomer(addressId, customer);
        ra.addFlashAttribute("message", "Address has been deleted");
        return "redirect:/address_book";
    }

    @GetMapping("/address_book/setdefault/{addressId}")
    public String setAddressDefault(@PathVariable(name = "addressId") Integer addressId,
                                    HttpServletRequest request,
                                    RedirectAttributes ra) {
        Customer customer = getAuthenticatedCustomer(request);

        try {
            addressService.changeDefaultAddress(addressId, customer);
        } catch (AddressNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/address_book";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utitlity.getEmailOfAuthenticatedCustomer(request);
        return customerService.findCustomerByEmail(email);
    }
}
