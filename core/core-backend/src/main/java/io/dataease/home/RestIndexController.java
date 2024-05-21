package io.dataease.home;

import io.dataease.utils.ModelUtils;
import io.dataease.utils.RsaUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping
public class RestIndexController {

    @GetMapping("/dekey")
    @ResponseBody
    public String dekey() {
        return RsaUtils.publicKey();
    }

    @GetMapping("/model")
    @ResponseBody
    public boolean model() {
        return ModelUtils.isDesktop();
    }

    @GetMapping("/init.js")
    public ResponseEntity<String> getJavaScript() {
        // Construct the JavaScript string
        String jsScript = "console.log(\"init\"); \n";

        String viteCloudApiList = System.getenv("VITE_CLOUD_API_LIST");
        if (viteCloudApiList != null && !viteCloudApiList.isEmpty()) {
          jsScript += "window.VITE_CLOUD_API_LIST = \"" + viteCloudApiList + "\"; \n";
        }

        String viteCloudApiUrl = System.getenv("VITE_CLOUD_API_URL");
        if (viteCloudApiUrl != null && !viteCloudApiUrl.isEmpty()) {
          jsScript += "window.VITE_CLOUD_API_URL = \"" + viteCloudApiUrl + "\"; \n";
        }

        // Set the response headers and return the JavaScript
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/javascript");

        return new ResponseEntity<>(jsScript, headers, HttpStatus.OK);
    }

}
