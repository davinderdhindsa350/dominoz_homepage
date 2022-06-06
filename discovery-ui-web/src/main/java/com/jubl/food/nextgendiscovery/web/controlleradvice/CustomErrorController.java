package com.jubl.food.nextgendiscovery.web.controlleradvice;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jubl.food.nextgendiscovery.model.RequestThreadLocal;
import com.jubl.food.nextgendiscovery.model.constants.Constants;


/**
 * Custom Error page renderer.
 *
 * @author Davinder Singh
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Logger for the controller.
     * */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

    /**
     * Default handling of error page.
     *
     * @param request
     * @return view
     */
    @RequestMapping(Constants.ERROR_URL)
    public ModelAndView handleError(final HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        ModelAndView view = new ModelAndView();

       // String headerContent = cmsContentService.getContent(uri, HEADER_HOOK_NAME);
       // view.addObject("headerContent", headerContent);

        LOGGER.info("status: {}", status);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                view.setViewName(Constants.NOT_FOUND_ERROR_VIEW_NAME);
            } else {
                if (StringUtils.isNotBlank(request.getParameter("error")) && StringUtils.isNotBlank(Constants.ERROR_DESCRIPTION)) {
                    view.addObject("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
                    view.setViewName(Constants.BAD_REQUEST_VIEW_NAME);

                } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    view.setViewName(Constants.SERVER_ERROR_VIEW_NAME);
                } else {
                    view.setViewName(Constants.DEFAULT_ERROR_VIEW_NAME);
                }
            }
        } else {
            if (StringUtils.isNotBlank(request.getParameter("error"))
                    && StringUtils.isNotBlank(request.getParameter(Constants.ERROR_DESCRIPTION))) {
                view.addObject("message", request.getParameter(Constants.ERROR_DESCRIPTION));
                view.setViewName(Constants.BAD_REQUEST_VIEW_NAME);
                return view;
            }
            view.setViewName(Constants.DEFAULT_ERROR_VIEW_NAME);
        }
        view.addObject("domain", RequestThreadLocal.getDamain());
        return view;
    }

    /**
     * @return url
     */
    public String getErrorPath() {
        return Constants.ERROR_URL;
    }

}
