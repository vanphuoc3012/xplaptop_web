package com.xplaptop.admin.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class PagingAndSortingResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model, NativeWebRequest request, WebDataBinderFactory binderFactory) {
        // keyword sortDir sortField
        String keyword = request.getParameter("keyword");
        String sortDir = request.getParameter("sortDir");
        String sortField = request.getParameter("sortField");

        model.addAttribute("keyword", keyword);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);

        //pagination param
        String pageNumber = request.getParameter("pageNumber");
        String totalPage = request.getParameter("totalPage");
        String totalElement = request.getParameter("totalElement");
        String startElement = request.getParameter("startElement");
        String endElement = request.getParameter("endElement");

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("startElement", startElement);
        model.addAttribute("endElement", endElement);

        HttpServletRequest servletRequest = (HttpServletRequest) request.getNativeRequest();

        String queryString  = servletRequest.getQueryString();
        String path = servletRequest.getServletPath();
        if(servletRequest.getQueryString() != null) {
            path += "?" + queryString.replace("&", ">");
        }
        model.addAttribute("path", path);
        return new PagingAndSortingHelper(model);
    }
}
