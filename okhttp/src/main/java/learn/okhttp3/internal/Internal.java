/*
 * Copyright (C) 2014 Square, Inc.
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
package learn.okhttp3.internal;

import learn.javax.annotation.Nullable;
import javax.net.ssl.SSLSocket;
import learn.okhttp3.Address;
import learn.okhttp3.Call;
import learn.okhttp3.ConnectionPool;
import learn.okhttp3.ConnectionSpec;
import learn.okhttp3.Headers;
import learn.okhttp3.OkHttpClient;
import learn.okhttp3.Request;
import learn.okhttp3.Response;
import learn.okhttp3.internal.connection.Exchange;
import learn.okhttp3.internal.connection.RealConnectionPool;

/**
 * Escalate internal APIs in {@code learn.okhttp3} so they can be used from OkHttp's implementation
 * packages. The only implementation of this interface is in {@link OkHttpClient}.
 */
public abstract class Internal {

  public static void initializeInstanceForTests() {
    // Needed in tests to ensure that the instance is actually pointing to something.
    new OkHttpClient();
  }

  public static Internal instance;

  public abstract void addLenient(Headers.Builder builder, String line);

  public abstract void addLenient(Headers.Builder builder, String name, String value);

  public abstract RealConnectionPool realConnectionPool(ConnectionPool connectionPool);

  public abstract boolean equalsNonHost(Address a, Address b);

  public abstract int code(Response.Builder responseBuilder);

  public abstract void apply(ConnectionSpec tlsConfiguration, SSLSocket sslSocket,
      boolean isFallback);

  public abstract Call newWebSocketCall(OkHttpClient client, Request request);

  public abstract void initExchange(
      Response.Builder responseBuilder, Exchange exchange);

  public abstract @Nullable Exchange exchange(Response response);
}
