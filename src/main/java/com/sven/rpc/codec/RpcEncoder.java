package com.sven.rpc.codec;

import com.sven.rpc.serde.SvenSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> clz;
    private SvenSerialization serialization;

    public RpcEncoder(Class<?> clz, SvenSerialization serialization) {
        this.clz = clz;
        this.serialization = serialization;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (clz != null) {

            byte[] bytes = serialization.serialize(msg);

            out.writeInt(bytes.length);
            out.writeBytes(bytes);

        }
    }
}
