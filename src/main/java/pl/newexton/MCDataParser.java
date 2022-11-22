package pl.newexton;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MCDataParser {

    public static ServerData downloadMCData() throws IOException {
        String sURL = "https://mcapi.us/server/status?ip=s32.titanaxe.com&port=40045";

        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; NewExtonAdminHelper/0.1;");
        request.connect();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.fromJson(new InputStreamReader((InputStream) request.getContent()), ServerData.class);
    }

}
