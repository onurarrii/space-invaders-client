package com.group14.termproject.client.service.http;

import lombok.Getter;

import java.util.function.Consumer;

/**
 * Used for handling any kind of HTTP responses. To get an instance, one should use {@link HttpResponseHandlerBuilder}
 * with same {@link T} type.
 * <br>
 * The builder can be used to set {@code onSuccess} and {@code onError} callbacks. Both of them are optional and if
 * they are not supplied the HTTP request will be sent but its response will be discarded.
 * Consequently, there is no way to decide whether response is successful or erroneous in this case.
 *
 * @param <T> indicates HTTP response body's type.
 */
@Getter
public class HttpResponseHandler<T> {

    private final Consumer<T> onSuccessCallback;
    private final Consumer<String> onErrorCallback;

    private HttpResponseHandler(Consumer<T> onSuccessCallback, Consumer<String> onErrorCallback) {
        this.onSuccessCallback = onSuccessCallback;
        this.onErrorCallback = onErrorCallback;
    }

    public static class HttpResponseHandlerBuilder<T> {
        Consumer<T> onSuccess = t -> {
        };
        Consumer<String> onError = t -> {
        };

        public HttpResponseHandlerBuilder<T> withOnSuccess(Consumer<T> callback) {
            this.onSuccess = callback;
            return this;
        }

        public HttpResponseHandlerBuilder<T> withOnError(Consumer<String> callback) {
            this.onError = callback;
            return this;
        }

        public HttpResponseHandler<T> build() {
            return new HttpResponseHandler<>(onSuccess, onError);
        }
    }

}
