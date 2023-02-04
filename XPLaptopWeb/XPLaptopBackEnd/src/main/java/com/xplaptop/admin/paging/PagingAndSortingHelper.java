package com.xplaptop.admin.paging;

import org.springframework.data.domain.Page;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingHelper {
    private ModelAndViewContainer model;

    public PagingAndSortingHelper(ModelAndViewContainer model) {
        this.model = model;
    }

    public void updateModelPaginationAttributes(int pageNumber, Page<?> page) {
        int totalPage = page.getTotalPages();
        int categoryPerPage = page.getNumberOfElements();
        long totalElement = page.getTotalElements();
        int startElement = (pageNumber - 1) * categoryPerPage + 1;
        int endElement = pageNumber * categoryPerPage;
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("startElement", startElement);
        model.addAttribute("endElement", endElement);
    }
}
