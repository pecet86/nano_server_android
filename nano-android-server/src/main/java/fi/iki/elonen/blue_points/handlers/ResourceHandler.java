package fi.iki.elonen.blue_points.handlers;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.function.Function;

import fi.iki.elonen.blue_points.Request;
import fi.iki.elonen.blue_points.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.getMimeTypeForFile;
import static fi.iki.elonen.NanoHTTPD.newChunkedResponse;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static fi.iki.elonen.blue_points.Utils.normalizeUri;

@SuppressWarnings("rawtypes")
public class ResourceHandler extends BaseHandler {

    protected ResourceHandler(@NonNull String mimeType, @NonNull Response.IStatus status,
                              @NonNull Function<Request, Response> process, @NonNull List<Object> parameters) {
        super(mimeType, status, process, parameters);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Builder<T extends Builder> extends BaseHandler.Builder<T> {
        @Getter
        private Context context;
        @Getter
        private String prefix;
        @Getter
        private String fileName;

        /*
            ss/dd/yy-zz.png => <prefix>_ss_dd_yy_zz.png || ss_dd_yy_zz.png
        */
        private String parseFilename(URL url) {
            return url.getPath()
                    .replace("/", "_")
                    .replace("-", "_")
                    .toLowerCase()
                    .substring(1);
        }

        private int getResourceId(String filename) {
            return context.getResources().getIdentifier(filename, "raw", context.getPackageName());
        }

        private String getMimeType(InputStream inputStream) throws IOException {
            String contentType = getMimeType();
            if (contentType != null) {
                return contentType;
            }
            contentType = URLConnection.guessContentTypeFromStream(inputStream);
            if (contentType != null) {
                return contentType;
            }
            return getMimeTypeForFile(fileName);
        }

        @NonNull
        private Response process(Request request) {
            String baseUri = request.getBluePoint().getUri();
            String realUri = normalizeUri(request.getSession().getUri());
            if (fileName == null) {
                try {
                    fileName = parseFilename(new URL(realUri));
                } catch (MalformedURLException ex) {
                    log("prosess", ex.getMessage(), ex);
                    return Utils.error404Handler();
                }
            }

            int resourceId = getResourceId(prefix + "_" + fileName);
            if (resourceId == 0) {
                resourceId = getResourceId(fileName);
                if (resourceId == 0) {
                    log("prosess", String.format("Could not find res/raw/%1$s_%2$s or res/raw/%2$s", prefix, fileName));
                    return Utils.error404Handler();
                }
            }

            try {
                InputStream inputStream = context.getResources().openRawResource(resourceId);
                return newChunkedResponse(getStatus(), getMimeType(inputStream), inputStream);
            } catch (Exception ex) {
                log("prosess", ex.getMessage(), ex);
                return newFixedLengthResponse(Response.Status.REQUEST_TIMEOUT, "text/plain", null);
            }
        }

        @Override
        protected void configure() {
            super.configure();
            if (prefix == null) {
                prefix = "mock";
            }
            if (context == null) {
                throw new IllegalArgumentException("context is null");
            }
            withProcess(this::process);
        }

    }
}
