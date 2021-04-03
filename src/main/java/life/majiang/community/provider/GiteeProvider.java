package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.deo.GiteeUser;
import life.majiang.community.deo.PrividerToken;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GiteeProvider {
    public String getAccessToken(PrividerToken prividerToken){
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(prividerToken));
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token?grant_type=authorization_code&code="
                        +prividerToken.getCode()
                        +"&client_id="+prividerToken.getClient_id()
                        +"&redirect_uri="+prividerToken.getRedirect_uri()
                        +"&client_secret="+prividerToken.getClient_secret())
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            String string = response.body().string();
            String token = string.split(":")[1].split("\"")[1];
            return token;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public GiteeUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GiteeUser githubUser = JSON.parseObject(string,GiteeUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
