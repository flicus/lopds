package org.schors.lopds.web;

import com.google.gson.Gson;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.schors.lopds.Configuration;
import org.schors.lopds.indexer.IndexManager;
import org.schors.lopds.indexer.LibraryIndexer;
import org.schors.lopds.indexer.LibraryTraverser;
import org.schors.lopds.indexer.LibraryTraverserImpl;
import org.schors.lopds.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiHandler implements HttpRequestHandler {

    private static final Pattern pattern = Pattern.compile("/lib/api/(.+)/?");
    private final Gson gson = new Gson();
    //private LibraryTraverser traverser = new LibraryTraverserImpl();
    //private LibraryIndexer indexer;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String target = (String)context.getAttribute("base");//request.getRequestLine().getUri();
        String method = request.getRequestLine().getMethod().toUpperCase();
        String command = null;
        Matcher m = pattern.matcher(target);
        if (m.find())
            command = m.group(1);

        switch (method) {
            case "GET":
                serveRead(command == null ? null : command.toLowerCase(), request, response, context);
                break;
            case "POST":
                serveWrite(command == null ? null : command.toLowerCase(), request, response, context);
                break;
            default:
                response.setStatusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
                response.setEntity(Util.makeMessageBody("Unsupported HTTP method"));
        }
    }

    private void serveWrite(String command, HttpRequest request, HttpResponse response, HttpContext context) throws IOException {
        switch (command) {
            case "setconfig":
                if (request instanceof HttpEntityEnclosingRequest) {
                    HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                    String data = EntityUtils.toString(entity);
                    //SetConfigReq req = gson.fromJson(new InputStreamReader(entity.getContent()), SetConfigReq.class);
                    SetConfigReq req = gson.fromJson(data, SetConfigReq.class);
                    System.out.println("Incoming entity content: " + req);
                    Configuration cfg = Configuration.getInstance();
                    cfg.setArchiveDirectory(req.getArchiveDirectory());
                    cfg.setIndexDirectory(req.getIndexFile());
                    response.setStatusCode(HttpStatus.SC_OK);
                    StringEntity e = new StringEntity(gson.toJson("{}"), ContentType.create("application/json"));
                    response.setEntity(e);
                }
                break;
            default:
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                response.setEntity(Util.makeMessageBody("Unknown api request"));
        }
    }

    private void serveRead(String command, HttpRequest request, HttpResponse response, HttpContext context) {
        Configuration cfg = Configuration.getInstance();
        StringEntity entity;
        switch (command) {
            case "archiveinfo":
                ArchiveInfoRes archiveInfoRes = new ArchiveInfoRes();
                String ree = IndexManager.getInstanse().getProgress(archiveInfoRes);
                if (ree == null) {
                    archiveInfoRes.setResult("ok");
                } else {
                    archiveInfoRes.setResult("nok");
                    archiveInfoRes.setDetails(ree);
                }
                response.setStatusCode(HttpStatus.SC_OK);
                String aaa = gson.toJson(archiveInfoRes);
                System.out.println("update: " + aaa);
                entity = new StringEntity(aaa, ContentType.create("application/json"));
                response.setEntity(entity);
                break;
            case "archivelist":
                List list = new ArrayList<ArchiveRecord>();
                String res = IndexManager.getInstanse().openArchive(list);
                ArchiveListRes archiveListRes = new ArchiveListRes();
                if (res == null) {
                    archiveListRes.setArchiveList(list);
                    archiveListRes.setResult("ok");
                } else {
                    archiveListRes.setResult("nok");
                    archiveListRes.setDetails(res);
                }
                response.setStatusCode(HttpStatus.SC_OK);
                entity = new StringEntity(gson.toJson(archiveListRes), ContentType.create("application/json"));
                response.setEntity(entity);
                break;
            case "getconfig":
                GetConfigRes getConfigRes = new GetConfigRes(cfg.getArchiveDirectory(), cfg.getIndexDirectory());
                getConfigRes.setResult("ok");
                response.setStatusCode(HttpStatus.SC_OK);
                entity = new StringEntity(gson.toJson(getConfigRes), ContentType.create("application/json"));
                response.setEntity(entity);
                break;
            case "checkstatus": {
                CheckStatusRes checkStatusRes = new CheckStatusRes();
                checkStatusRes.setResult("ok");
                checkStatusRes.setStatus(IndexManager.getInstanse().getStatus());
                response.setStatusCode(HttpStatus.SC_OK);
                entity = new StringEntity(gson.toJson(checkStatusRes), ContentType.create("application/json"));
                response.setEntity(entity);
                break;
            }
            case "startindexing": {
                String rr = IndexManager.getInstanse().startIndexing();
                Response r = new Response();
                if (rr == null) {
                    r.setResult("ok");
                } else {
                    r.setResult("nok");
                    r.setDetails(rr);
                }
                response.setStatusCode(HttpStatus.SC_OK);
                entity = new StringEntity(gson.toJson(r), ContentType.create("application/json"));
                response.setEntity(entity);
                break;
            }
            default:
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                response.setEntity(Util.makeMessageBody("Unknown api request"));
                System.out.println("Unknown apo request: " + command);
        }
    }


}
