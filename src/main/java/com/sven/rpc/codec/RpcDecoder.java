package com.sven.rpc.codec;

import com.sven.rpc.serde.SvenSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clz;

    private SvenSerialization serialization;

    public RpcDecoder(Class<?> clz, SvenSerialization serialization) {
        this.clz = clz;
        this.serialization = serialization;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //不是一个完整的frame，下次再读
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int dataLength = in.readInt();

        //同样，刚才读到的数字代表了后续内容的长度，如果后续的内容小于字段的长度，也需要放弃，下次再读
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        //前面读了之后可能移动了bytebuf的指针，后续处理可能有问题？


        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = serialization.deSerizlize(data, clz);
        out.add(obj);
    }
}
