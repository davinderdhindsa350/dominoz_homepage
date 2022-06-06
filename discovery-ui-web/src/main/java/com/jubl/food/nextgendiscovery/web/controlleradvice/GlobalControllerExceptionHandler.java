package com.jubl.food.nextgendiscovery.web.controlleradvice;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubl.food.nextgendiscovery.model.ErrorResponse;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.exception.ApiException;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;
import com.jubl.food.nextgendiscovery.model.exception.NotFoundException;
import com.jubl.food.nextgendiscovery.model.exception.UnauthorizedException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.service.exception.ServiceException;

/**
 * Global controller level exception handler.
 * @author Davinder Singh
 * @version 1.0.0
 */
//@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Private static class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Resolve properties from spring *.yml files.
     */
    @Autowired
    private Discovery discovery;

    /**
     * Handle any other exceptional cases.
     *
     * @param th the cause
     * @return the error response
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleThrowable(final Exception th) {
        LOGGER.error("unknown server exception", th);
        String message = StringUtils.isEmpty(th.getMessage()) ? discovery.getGenericErrorMessage() : th.getMessage();
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.SERVER_ERROR);
        response.setErrorMessage(message);
        response.setAppName(discovery.getAppName());
        response.setAppVersion(discovery.getAppVersion());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle {@link BadRequestException}.
     *
     * @param bre the cause
     * @return the error response
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException bre) {
        String message = StringUtils.isEmpty(bre.getMessage()) ? discovery.getGenericErrorMessage() : bre.getMessage();
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage(message);
        response.setErrorCode(bre.getCode());
        response.setAppName(discovery.getAppName());
        response.setAppVersion(discovery.getAppVersion());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle {@link ApiException}.
     *
     * @param te the cause
     * @return the error response
     */
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleThorException(final ApiException te) {
        if (te.getCause() instanceof BadRequestException) {
            return handleBadRequestException((BadRequestException) te.getCause());
        } else if (te.getCause() instanceof NotFoundException) {
            return handleNotFoundException((NotFoundException) te.getCause());
        } else if (te.getCause() instanceof UnauthorizedException) {
            return handleUnauthorizedException((UnauthorizedException) te.getCause());
        }
        LOGGER.error("unknown server thor exception", te);
        String message = StringUtils.isEmpty(te.getMessage()) ? discovery.getGenericErrorMessage() : te.getMessage();
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(te.getCode());
        response.setErrorMessage(message);
        response.setAppName(discovery.getAppName());
        response.setAppVersion(discovery.getAppVersion());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle {@link NotFoundException}.
     *
     * @param nfe the cause
     * @return the error response
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException nfe) {
        String message = StringUtils.isEmpty(nfe.getMessage()) ? discovery.getGenericErrorMessage() : nfe.getMessage();
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(nfe.getCode());
        response.setErrorMessage(message);
        response.setAppName(discovery.getAppName());
        response.setAppVersion(discovery.getAppVersion());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link ServiceException}.
     *
     * @param se the cause
     * @return the error response
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleServiceException(final ServiceException se) {
        if (se.getCause() instanceof BadRequestException) {
            return handleBadRequestException((BadRequestException) se.getCause());
        } else if (se.getCause() instanceof NotFoundException) {
            return handleNotFoundException((NotFoundException) se.getCause());
        } else if (se.getCause() instanceof UnauthorizedException) {
            return handleUnauthorizedException((UnauthorizedException) se.getCause());
        }

        LOGGER.error("unknown server service exception", se);
        String message = StringUtils.isEmpty(se.getMessage()) ? discovery.getGenericErrorMessage() : se.getMessage();
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage(message);
        response.setErrorCode(se.getCode());
        response.setAppName(discovery.getAppName());
        response.setAppVersion(discovery.getAppVersion());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle {@link UnauthorizedException}.
     *
     * @param ue the cause
     * @return the error response
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final UnauthorizedException ue) {
        String message = StringUtils.isEmpty(ue.getMessage()) ? discovery.getGenericErrorMessage() : ue.getMessage();
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ue.getCode());
        response.setErrorMessage(message);
        response.setAppName(discovery.getAppName());
        response.setAppVersion(discovery.getAppVersion());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
