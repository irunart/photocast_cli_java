import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class tool {
    public static String host = "https://photo.hupili.net";
    public static String buildPhotographerUrl = host + "/build/photographer/";
    public static String buildIndexUrl = host + "/build/index/";
    // 上传照片
    public static String uploadUrl = host + "/upload";

    public static void main(String[] args) {
        // 图片所在文件夹的绝对路径
        String path = "/Users/Downloads/pics/";
        String token = "token";
        uploadPics(path, token);
        // 上传多张照片后，再更新索引，即可在portal中查看到上传的照片
        // buildPhotographer(token);
        // buildIndex(token);
    }

    public static void buildPhotographer(String token) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(buildPhotographerUrl);

        String value = "token=" + token + ";type=cli";
        try {
            httpPost.setHeader("Cookie",value);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                String res = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buildIndex(String token) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(buildIndexUrl);

        String value = "token=" + token + ";type=cli";
        try {
            httpPost.setHeader("Cookie",value);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                String res = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param path 绝对路径
     * @param token
     */
    public static void uploadPics(String path, String token) {


        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
            } else {
                if (fs.getName().startsWith(".")) continue;
                System.out.println(fs.getName());

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(uploadUrl);

                try {
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    // 读取本地照片文件
                    path = path.substring(path.length() - 1).equalsIgnoreCase("/") ? path : path + "/";
                    FileBody fileBody = new FileBody(new File(path + fs.getName()));
                    // 将照片文件加入到请求中
                    builder.addPart("file", fileBody);

                    HttpEntity multipart = builder.build();
                    httpPost.setEntity(multipart);
                    String value = "token=" + token + ";type=cli";
                    httpPost.setHeader("Cookie",value);
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    if (response != null) {
                        HttpEntity resEntity = response.getEntity();
                        if (resEntity != null) {
                            String res = EntityUtils.toString(resEntity, "UTF-8");
                            System.out.println(res);
                        }
                    }
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
