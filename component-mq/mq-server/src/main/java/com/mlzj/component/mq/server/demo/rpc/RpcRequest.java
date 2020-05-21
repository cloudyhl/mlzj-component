package com.mlzj.component.mq.server.demo.rpc;

import lombok.Data;

/**
 * @author yhl
 * @date 2020/5/21
 */
@Data
public class RpcRequest {

    private String requestId;

    private String methodName;

    private String param;
}
