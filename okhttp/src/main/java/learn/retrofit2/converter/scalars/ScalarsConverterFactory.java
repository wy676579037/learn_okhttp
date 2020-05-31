/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package learn.retrofit2.converter.scalars;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import learn.okhttp3.RequestBody;
import learn.okhttp3.ResponseBody;
import learn.retrofit2.Converter;
import learn.retrofit2.Retrofit;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.BooleanResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.ByteResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.CharacterResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.DoubleResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.FloatResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.IntegerResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.LongResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.ShortResponseBodyConverter;
import learn.retrofit2.converter.scalars.ScalarResponseBodyConverters.StringResponseBodyConverter;

/**
 * A {@linkplain Converter.Factory converter} for strings and both primitives and their boxed types
 * to {@code text/plain} bodies.
 */
public final class ScalarsConverterFactory extends Converter.Factory {
    public static ScalarsConverterFactory create() {
        return new ScalarsConverterFactory();
    }

    private ScalarsConverterFactory() {
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                                    Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type == String.class
                || type == boolean.class
                || type == Boolean.class
                || type == byte.class
                || type == Byte.class
                || type == char.class
                || type == Character.class
                || type == double.class
                || type == Double.class
                || type == float.class
                || type == Float.class
                || type == int.class
                || type == Integer.class
                || type == long.class
                || type == Long.class
                || type == short.class
                || type == Short.class) {
            return ScalarRequestBodyConverter.INSTANCE;
        }
        return null;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == String.class) {
            return StringResponseBodyConverter.INSTANCE;
        }
        if (type == Boolean.class || type == boolean.class) {
            return BooleanResponseBodyConverter.INSTANCE;
        }
        if (type == Byte.class || type == byte.class) {
            return ByteResponseBodyConverter.INSTANCE;
        }
        if (type == Character.class || type == char.class) {
            return CharacterResponseBodyConverter.INSTANCE;
        }
        if (type == Double.class || type == double.class) {
            return DoubleResponseBodyConverter.INSTANCE;
        }
        if (type == Float.class || type == float.class) {
            return FloatResponseBodyConverter.INSTANCE;
        }
        if (type == Integer.class || type == int.class) {
            return IntegerResponseBodyConverter.INSTANCE;
        }
        if (type == Long.class || type == long.class) {
            return LongResponseBodyConverter.INSTANCE;
        }
        if (type == Short.class || type == short.class) {
            return ShortResponseBodyConverter.INSTANCE;
        }
        return null;
    }
}
