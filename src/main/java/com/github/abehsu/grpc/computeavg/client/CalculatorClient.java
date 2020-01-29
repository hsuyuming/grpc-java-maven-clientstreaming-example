package com.github.abehsu.grpc.computeavg.client;

import com.proto.computeaverage.CalculatorServiceGrpc;
import com.proto.computeaverage.ComputeAverageRequest;
import com.proto.computeaverage.ComputeAverageResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    public void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50052)
                .usePlaintext()
                .build();

        doClientStreamingCall(channel);

        channel.shutdown();
    }

    private void doClientStreamingCall(ManagedChannel channel) {

        CountDownLatch latch = new CountDownLatch(1);


        System.out.println("Start Stub");
        CalculatorServiceGrpc.CalculatorServiceStub ayncClient = CalculatorServiceGrpc.newStub(channel);

        StreamObserver<ComputeAverageRequest> requestObserver = ayncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse computeAverageResponse) {
                //we get a response from the server
                //onNext will be called only once
                System.out.println("Received a response from the server");
                System.out.println(computeAverageResponse.getAverage());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                //onCompleted will be called right after onNext()
                System.out.println("Server has completed sending us data");
                latch.countDown();

            }
        });

        for ( int i = 0 ; i< 10000 ; i++) {
            System.out.println("sending message " + i);
            requestObserver.onNext(ComputeAverageRequest.newBuilder()
                    .setNumber(i)
                    .build()
            );

        }

//        // sent message #1
//        System.out.println("sending message 1");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(1)
//                .build()
//        );
//
//        // sent message #2
//        System.out.println("sending message 2");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(2)
//                .build()
//        );
//
//        // sent message #3
//        System.out.println("sending message 3");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(3)
//                .build()
//        );
//
//        // sent message #4
//        System.out.println("sending message 4");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(4)
//                .build()
//        );

        requestObserver.onCompleted();


        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    public static void main(String[] args) {

        CalculatorClient main = new CalculatorClient();
        main.run();

    }
}
