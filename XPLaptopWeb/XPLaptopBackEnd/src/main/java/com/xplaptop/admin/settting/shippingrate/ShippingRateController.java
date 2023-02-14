package com.xplaptop.admin.settting.shippingrate;

import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
import com.xplaptop.admin.settting.country.StateRepository;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;
import com.xplaptop.common.entity.setting.ShippingRate;
import com.xplaptop.common.exception.ShippingRateAlreadyExistsException;
import com.xplaptop.common.exception.ShippingRateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {

    @Autowired
    private ShippingRateService shippingRateService;

    @Autowired
    private StateRepository stateRepo;

    @GetMapping("/shipping_rates")
    public String showFirstPageRates() {

        return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
    }

    @GetMapping("/shipping_rates/page/{pageNumber}")
    public String showShippingRateByPage(@PagingAndSortingParam PagingAndSortingHelper helper,
                                         ModelMap model,
                                         @PathVariable(name = "pageNumber") Integer pageNumber,
                                         @RequestParam(name = "sortDir", required = false) String sortDir,
                                         @RequestParam(name = "sortField", required = false) String sortField,
                                         @RequestParam(name = "keyword", required = false) String keyword) {
        if(sortDir == null || sortField == null) {
            return "redirect:/shipping_rates/page/"+pageNumber+"?sortField=country&sortDir=asc";
        }
        Page<ShippingRate> page = shippingRateService.getByPage(pageNumber, sortDir, sortField, keyword);
        helper.updateModelPaginationAttributes(pageNumber, page);
        List<ShippingRate> shippingRates = page.getContent();
        model.put("shippingRates", shippingRates);
        return "shipping/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String viewFormShippingRate(ModelMap model) {
        ShippingRate shippingRate = new ShippingRate();
        List<Country> countryList = shippingRateService.getAllCountry();
        model.put("countryList", countryList);
        model.put("shippingRate", shippingRate);
        model.put("title", "Create new Shipping Rate");
        return "shipping/shipping_rates_form";
    }

    @PostMapping("/shipping_rates/save")
    public String saveShippingRate(ShippingRate shippingRate,
                                   RedirectAttributes ra,
                                   @RequestParam(name = "path") String path) {
        try {
            String message = shippingRateService.save(shippingRate);
            ra.addFlashAttribute("message", message);
            return "redirect:" + path.replace(">", "&");
        } catch (ShippingRateAlreadyExistsException | ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:" + path.replace(">", "&");
        }
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String viewEditFormShippingRate(@PathVariable(name = "id") Integer id,
                                           ModelMap model,
                                           @RequestParam(name = "path") String path,
                                           RedirectAttributes ra) {
        ShippingRate shippingRate;
        try {
            shippingRate = shippingRateService.findShippingRateById(id);
            List<Country> countryList = shippingRateService.getAllCountry();
            List<State> stateList = stateRepo.findByCountryOrderByNameAsc(shippingRate.getCountry());
            model.put("stateList", stateList);
            model.put("countryList", countryList);
            model.put("path", path);
            model.put("shippingRate", shippingRate);
            return "shipping/shipping_rates_form";
        } catch (ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:" + path.replace(">", "&");
        }
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteShippingRate(@PathVariable(name = "id") Integer id,
                                     @RequestParam(name = "path") String path,
                                     RedirectAttributes ra) {
        try {
            shippingRateService.deleteShippingRate(id);
            ra.addFlashAttribute("message", "The shipping rate has been deleted successfully");
        } catch (ShippingRateNotFoundException e) {
           ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:" + path.replace(">", "&");
    }

    @GetMapping("/shipping_rates/cod_supported/{id}")
    public String updateCODSupported(@PathVariable(name = "id") Integer id,
                                     @RequestParam(name = "path") String path,
                                     RedirectAttributes ra) {
        try {
            String message = shippingRateService.updateCODSupported(id);
            ra.addFlashAttribute("message", message);
        } catch (ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:" + path.replace(">", "&");
    }
}
