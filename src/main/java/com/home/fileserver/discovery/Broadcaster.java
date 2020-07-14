package com.home.fileserver.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
public class Broadcaster {

    private final static Logger logger = LoggerFactory.getLogger(Broadcaster.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${broadcast.id}")
    private String broadcastId;

    @Value("${broadcast.port}")
    private int port;

    @Value("${broadcast.interval}")
    private long intervalInMillis;

    private DatagramSocket socket;
    private InetAddress broadcastAddress;

    @PostConstruct
    public void init() throws SocketException {
        initSocket();
        List<InetAddress> broadcastAddresses = getBroadcastAddresses();
        broadcastAddress = broadcastAddresses.get(0);
        logger.debug("Using broadcast address: " + broadcastAddress + " and port: " + port);
    }

    @Scheduled(fixedDelay = 5000)
    public void broadcast() {
        try {
            String message = getBroadcastMessage();
            byte[] buffer = message.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
            socket.send(packet);
//            logger.debug("Sending broadcast message: {}", message);
        } catch (Exception e) {
            logger.debug("Unable to send broadcast message. Error: " + e.getMessage(), e);
            try {
                initSocket();
            } catch (SocketException e1) {
                logger.debug("Unable to create broadcast socket. Error: " + e.getMessage(), e);
            }
        }
    }

    private void initSocket() throws SocketException {
        logger.debug("Creating socket");
        if (socket != null) {
            socket.close();
        }
        socket = new DatagramSocket();
        socket.setBroadcast(true);
    }

    private String getBroadcastMessage() {
        return applicationName + "::" + broadcastId + "::" + System.currentTimeMillis();
    }

    private static List<InetAddress> getBroadcastAddresses() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        List<InetAddress> broadcastAddresses = new ArrayList<>();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback()) {
                continue;
            }
            for (InterfaceAddress interfaceAddress :
                    networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null) {
                    broadcastAddresses.add(broadcast);
                }
            }
        }

        return broadcastAddresses;
    }
}
