package com.lagou.common.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMqDTO<T> implements Serializable {
    /**
     */
    private static final long serialVersionUID = -1762409052644257813L;

    private T data;//消息体数据

    private String messageId;
}
