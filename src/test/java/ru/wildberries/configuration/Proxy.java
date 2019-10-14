package ru.wildberries.configuration;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Proxy {
    private static final Logger logger = LogManager.getLogger(Proxy.class);

    public void recordHarToFile(Har har) {
        try {
            File file = new File("logs\\har-logs.har");
            if (!file.exists()) {
                logger.info("Файл для har-логов не найден, создаю новый...");
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            try {
                har.writeTo(fos);
            }
            finally {
                logger.info("Лог прокси-сервера успешно записан");
                fos.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isUrlExistsInHar(Har har,String url){
        for (HarEntry entry:har.getLog().getEntries()){
            if (entry.getRequest().getUrl().contains(url)){
                return true;
            }
        }
        return false;
    }
    public HarRequest getHarRequestByUrl(Har har,String url){
        HarRequest request=null;
        for (HarEntry entry:har.getLog().getEntries()){
            if (entry.getRequest().getUrl().contains(url)){
                request = entry.getRequest();
            }
        }
        return request;
    }
    public HarResponse getHarResponseByUrl(Har har,String url){
        HarResponse response=null;
        for (HarEntry entry:har.getLog().getEntries()){
            if (entry.getRequest().getUrl().contains(url)){
                response = entry.getResponse();
            }
        }
        return response;
    }
}

