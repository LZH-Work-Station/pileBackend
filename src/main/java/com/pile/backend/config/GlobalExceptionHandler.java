package com.pile.backend.config;

import com.pile.backend.common.Exception.PileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * global Exception Handler
 *
 * @author lizehan
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String error(Exception e){
        e.printStackTrace();
        return "error";
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(PileException.class)
    public String error(PileException e, Model model){
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
