package com.pat.exception;

import com.pat.utils.ResJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/27
 * @Modify
 * @since
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    // MaxUploadSizeExceededException

    /**
     * 上传文件超过500k，捕获异常 {@link MaxUploadSizeExceededException}
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResJSONResult handlerMaxUploadSize(MaxUploadSizeExceededException ex) {
        return ResJSONResult.errorMsg("文件上传大小不能超过500k，请压缩图片或降低图片质量再上传！");
    }
}
