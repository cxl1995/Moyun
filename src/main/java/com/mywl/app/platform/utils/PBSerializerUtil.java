package com.mywl.app.platform.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

import java.util.Optional;

/**
 * @author cxl
 * @date 2023/10/25 15:18
 */
public class PBSerializerUtil {
    public PBSerializerUtil() {
    }

    public static <T extends MessageOrBuilder> Optional<String> toJson(T t) {
        try {
            return Optional.of(JsonFormat.printer().print(t));
        } catch (InvalidProtocolBufferException var2) {
            return Optional.empty();
        }
    }

    public static <T extends Builder, V extends Message> Optional<V> toPBBean(String json, T t) {
        try {
            JsonFormat.parser().merge(json, t);
            V o = (V) t.build();
            return Optional.of(o);
        } catch (InvalidProtocolBufferException var3) {
            return Optional.empty();
        }
    }

}
