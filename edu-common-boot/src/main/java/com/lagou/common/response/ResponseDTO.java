package com.lagou.common.response;

import lombok.Data;

/**
 * @ClassName ResponseDTO
 * @Description 封装返回结果对象
 * @Author zhouqian
 * @Date 2022/4/3 19:34
 * @Version 1.0
 */
@Data
public class ResponseDTO<T> {

    /**
     * 1:成功
     * 1002：服务器内容错误
     */
    private int state;
    private String message;
    private T content;

    public ResponseDTO() {

    }

    public ResponseDTO(int state, String message, T content) {
        this.state = state;
        this.message = message;
        this.content = content;
    }

    public static <T> ResponseDTO<T> response(int state, String message) {
        return response(state, message, null);
    }

    public static <T> ResponseDTO<T> response(int state, String message, T content) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setState(state);
        responseDTO.setContent(content);
        responseDTO.setMessage(message);
        return responseDTO;
    }


    public static <T> ResponseDTO<T> success() {
        return ResponseDTO.response(1, "success", null);
    }

    public static <T> ResponseDTO<T> success(T content) {
        return ResponseDTO.response(1, "success", content);
    }

    public static <T> ResponseDTO<T> ofError(int state, String message, T content) {
        return ResponseDTO.response(state, message, content);
    }

    public static <T> ResponseDTO<T> ofError(int state, String message) {
        return ResponseDTO.response(state, message, null);
    }

    public boolean isSuccess() {
        return this.state == 1;
    }

    public static <T> ResponseDTO<T> ofError(String message) {
        return ResponseDTO.response(1002, message, null);
    }
}
