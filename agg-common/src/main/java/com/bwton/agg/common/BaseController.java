package com.bwton.agg.common;

import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.core.web.CommonResult;
import com.bwton.exception.BusinessException;
import com.bwton.lang.Result;
import org.springframework.validation.BindingResult;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
public class BaseController {
    public Result validation(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BusinessException(AggPayExceptionEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        return CommonResult.success();
    }
}
