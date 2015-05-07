package com.example.dwrd32.login_1;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import java.net.Socket;

/**
 * Created by dwrd32 on 07.05.2015.
 */
public class socket {

    socket = IO.socket("http://localhost");
    socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            socket.emit("foo", "hi");
            socket.disconnect();
        }

    }).on("event", new Emitter.Listener() {

        @Override
        public void call(Object... args) {}

    }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

        @Override
        public void call(Object... args) {}

    });
    socket.connect();
}
