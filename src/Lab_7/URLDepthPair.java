package Lab_7;

import java.net.MalformedURLException;
import java.net.URL;

public class URLDepthPair {
    private String url;
    private int depth;

    public URLDepthPair(String url, int depth){
        this.url = url;
        this.depth = depth;
    }

    @Override
    public String toString(){
        return url+" "+depth;
    }

    public String getHost(){
        try {
            URL url = new URL(this.url);
            return url.getHost();
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException" + e.getMessage());
            return null;
        }
    }

    public String getPath(){
        try {
            URL url = new URL(this.url);
            return url.getPath();
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException" + e.getMessage());
            return null;
        }
    }

    public int getDepth(){
        return depth;
    }

}
