package com.github.abehsu.grpc.computeavg.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("gRPC server starting....");

        Server server = ServerBuilder.forPort(50052)
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Prepare to shutdown gRPC server");
            server.shutdown();
            System.out.println("Successfully shutdown gRPC server");
        } ));

        server.awaitTermination();
    }
}
