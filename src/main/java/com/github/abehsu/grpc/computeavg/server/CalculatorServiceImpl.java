package com.github.abehsu.grpc.computeavg.server;

import com.proto.computeaverage.CalculatorServiceGrpc;
import com.proto.computeaverage.ComputeAverageRequest;
import com.proto.computeaverage.ComputeAverageResponse;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {

        StreamObserver<ComputeAverageRequest> requestObserve = new StreamObserver<ComputeAverageRequest>() {

            int count = 0;
            int sum = 0;

            @Override
            public void onNext(ComputeAverageRequest computeAverageRequest) {
                // when server receive client message

                // increase the count
                count += 1;
                // increase the sum
                sum += computeAverageRequest.getNumber();
            }

            @Override
            public void onError(Throwable throwable) {
                //when server receive client error msg

            }

            @Override
            public void onCompleted() {
                // when server recerive client talk us it finish
                double average = (double)sum / count;

                responseObserver.onNext(ComputeAverageResponse.newBuilder()
                        .setAverage(average)
                        .build()
                );

                // make sure request complete
                responseObserver.onCompleted();
            }
        };

        return requestObserve;

    }
}
