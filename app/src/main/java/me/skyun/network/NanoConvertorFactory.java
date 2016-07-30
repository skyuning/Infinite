package me.skyun.network;

import com.google.protobuf.nano.MessageNano;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by linyun on 16/7/30.
 */
public class NanoConvertorFactory<T extends MessageNano> extends Converter.Factory {
    @Override
    public Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ResponseConvertor<>((Class<T>) type);
    }

    @Override
    public Converter<T, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestConvertor<>();
    }
}

class ResponseConvertor<T extends MessageNano> implements Converter<ResponseBody, T> {
    private Class<T> msgClz;

    public ResponseConvertor(Class<T> msgClz) {
        this.msgClz = msgClz;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            T msg = msgClz.newInstance();
            T.mergeFrom(msg, value.bytes());
            return msg;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class RequestConvertor<T extends MessageNano> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, T.toByteArray(value));
    }
}
