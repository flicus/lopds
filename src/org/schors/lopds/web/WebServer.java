package org.schors.lopds.web;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread {

    private final ServerSocket serverSocket;
    private final HttpParams httpParams;
    private final HttpService httpService;

    public WebServer(int port, String docRoot) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.httpParams = new SyncBasicHttpParams();
        this.httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);

        HttpProcessor httpProcessor = new ImmutableHttpProcessor(new HttpResponseInterceptor[]{
                new ResponseDate(),
                new ResponseServer(),
                new ResponseContent(),
                new ResponseConnControl()
        });

        HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register("/lib/*", new RootHandler(docRoot));
        registry.register("/opds/*", new OpdsHandler());
        //registry.register("/static/*", new StaticContentHandler(docRoot));
        //registry.register("/api/*", new ApiHandler());

        this.httpService = new HttpService(httpProcessor, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory(), registry, this.httpParams);

    }

    @Override
    public void run() {
        System.out.println(String.format("Listening on port: %d", this.serverSocket.getLocalPort()));
        while (!Thread.interrupted()) {
            try {
                Socket socket = this.serverSocket.accept();
                DefaultHttpServerConnection connection = new DefaultHttpServerConnection();
                System.out.println(String.format("Incoming connection from: %s", socket.getInetAddress().toString()));
                connection.bind(socket, this.httpParams);

                Thread worker = new Worker(this.httpService, connection);
                worker.setDaemon(true);
                worker.start();

            } catch (InterruptedIOException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                break;
            }
        }
    }


}
