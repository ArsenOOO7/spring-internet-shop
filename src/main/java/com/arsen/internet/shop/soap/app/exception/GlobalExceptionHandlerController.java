package com.arsen.internet.shop.soap.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;


/**
 * Global Exception Handler
 * @author Arsen Sydoryk
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {


    /**
     * Called when entity is null
     * @param request HttpRequest
     * @param exception NullEntityReferenceException
     * @return ModelAndView
     */
    @ExceptionHandler(NullEntityReferenceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView nullEntityReferenceExceptionHandler(HttpServletRequest request, NullEntityReferenceException exception) {
        return getModelAndView(request, HttpStatus.BAD_REQUEST, exception);
    }


    /**
     * Called when entity is undefined
     * @param request HttpRequest
     * @param exception EntityNotFoundException
     * @return ModelAndView
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ModelAndView entityNotFoundExceptionHandler(HttpServletRequest request, EntityNotFoundException exception) {
        return getModelAndView(request, HttpStatus.NOT_FOUND, exception);
    }


    /**
     * Called when user does not have access to some page
     * @param request HttpRequest
     * @param exception EntityNotFoundException
     * @return ModelAndView
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView notFound(HttpServletRequest request, AccessDeniedException exception){
        return getModelAndView(request, HttpStatus.FORBIDDEN, exception);
    }



    /**
     * Internal Server Error
     * @param request HttpRequest
     * @param exception Exception
     * @return ModelAndView
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView internalServerErrorHandler(HttpServletRequest request, Exception exception) {
        return getModelAndView(request, HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }


    /**
     *
     * @param request HttpRequest
     * @param httpStatus ErrorStatus
     * @param exception Exception
     * @return ModelAndView
     */
    private ModelAndView getModelAndView(HttpServletRequest request, HttpStatus httpStatus, Exception exception) {
        log.error("Exception raised = {} :: URL = {}", exception.getMessage(), request.getRequestURL());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", httpStatus.value() + " / " + httpStatus.getReasonPhrase());
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }

}
