package com.pile.backend.common.Exception;


import com.pile.backend.common.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义全局异常类
 *
 * @author Li Zehan
 */
@Data
@ApiModel(value = "personal error")
public class PileException extends RuntimeException {

    @ApiModelProperty(value = "error code")
    private Integer code;

    public PileException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public PileException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "Pile Exception{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
