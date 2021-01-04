package com.workafterworks.reactorexample;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Locale;
/*
* Mono: emits 0 or 1 element. Can be void also
*
* @author Eddy Bayonne 03.01.2021
* */
public class MonoExampleTests {
    @Test
    public void monoSubscriber(){
        var name = "Pascal";
        Mono<String> mono = Mono.just(name)
                .log();
        //without subscribing nothing will happen
        mono.subscribe();

        //assert with
        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumer(){
        var name = "Pascal";
        Mono<String> mono = Mono.just(name)
                .log();
        //without subscribing nothing will happen
        //in that order we have subscribed and consume the data and then we can apply any action on it
        mono.subscribe(s-> System.out.println(s.toUpperCase(Locale.ROOT)));

        //assert with
        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerError(){
        var name = "Pascal";
        Mono<String> mono = Mono.just(name)
                .map(s -> {
                    throw new RuntimeException("An error occurred");
                });
        //without subscribing nothing will happen
        //in that order we have subscribed and consume the data and then we can apply any action on it and in case of an exception of error we can react on it too
        mono.subscribe(data-> System.out.println(data.toUpperCase(Locale.ROOT)),
                error-> System.out.println(error.getMessage()));

        //assert with
        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoSubscriberConsumerCompleted(){
        var name = "Pascal";
        Mono<String> mono = Mono.just(name)
                .map(String::toUpperCase)
                .log();
        //without subscribing nothing will happen
        //in that order we have subscribed and consume the data and then we can apply any action on it
        // and in case of an exception of error we can react on it too. Once we are done we can use the Subscription method cancel which actually serves as a clean resources
        mono.subscribe(data-> System.out.println(data.toUpperCase(Locale.ROOT)),
                Throwable::printStackTrace,
                ()-> System.out.println("FINISHED"),
                Subscription::cancel);

        //assert with
        StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerSubscription(){
        var name = "Pascal";
        Mono<String> mono = Mono.just(name)
                .map(String::toUpperCase)
                .log();
        //without subscribing nothing will happen
        //in that order we have subscribed and consume the data and then we can apply any action on it
        // and in case of an exception of error we can react on it too.
        // Once we are done we can use the Subscription method cancel which actually serves as a clean resources
        //in this particular case, we have used the subscription with the number of requests (5) which makes this to not be unbounded
        mono.subscribe(data-> System.out.println(data.toUpperCase(Locale.ROOT)),
                Throwable::printStackTrace,
                ()-> System.out.println("FINISHED"),
                subscription -> subscription.request(5));

        //assert with
        StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }


    @Test
    public void monoDoOnMethods(){
        var name = "Pascal";
        Mono<Object> mono = Mono.just(name)
                .log()
                .doOnSubscribe(subscription -> System.out.println("Subscribed with subscription: "+subscription))
                .doOnRequest(value -> System.out.println("Request received... start doing something"))
                .doOnNext(value-> System.out.println("Doing onNext with value : "+value))
                .flatMap(value-> Mono.empty())
                .doOnNext(value-> System.out.println("Doing onNext with value : "+value))
                .doOnSuccess(value-> System.out.println("doOnSuccess executed with value: "+value));

        mono.subscribe(System.out::println);

    }

    @Test
    public void monoDoOError(){
        //In this example we are simulating a Mono with errors
        var name = "Pascal";
        Mono<Object> monoError = Mono.error(new IllegalArgumentException("Wrong argument has been passed and caused this exception"))
                .onErrorReturn("EMPTY")
                .onErrorResume(throwable -> {//nice method to resume with fallback
                    System.out.println("In Case of Error: ");
                    return Mono.empty();
                })
                .doOnError(exception -> System.out.println(exception.getMessage()))
                .log();

        StepVerifier.create(monoError)
                .expectNext("EMPTY").verifyComplete();
        // .verify();


    }
}
