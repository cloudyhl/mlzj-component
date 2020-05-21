package com.mlzj.component.mq.server.demo.rpc;

import lombok.Data;

/**
 * @author yhl
 * @date 2020/5/21
 */
@Data
public class RpcResponse {
    private String requestId;

    private String response;
}
