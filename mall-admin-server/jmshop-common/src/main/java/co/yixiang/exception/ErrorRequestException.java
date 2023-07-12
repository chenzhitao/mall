package co.yixiang.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author hupeng
 * @date 2019-11-11
 * 统一异常处理
 */
@Getter
public class ErrorRequestException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public ErrorRequestException(String msg){
        super(msg);
    }

    public ErrorRequestException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
