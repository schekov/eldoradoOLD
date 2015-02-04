package part1.task4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimplePageParser {

    private final static String DEFAULT_DIRECTORY = "resources/";

    private final String CHARSET = "UTF8";
    private ExecutorService threadPoolExecutor;
    private String dir = DEFAULT_DIRECTORY;
    private String rootDirectory;
    private String pagePath;
    private URL url;


    public SimplePageParser(String urlString) throws MalformedURLException, UnsupportedEncodingException {
        this(DEFAULT_DIRECTORY, urlString);
    }

    public SimplePageParser(String dir, String urlString) throws MalformedURLException, UnsupportedEncodingException {
        this.dir = dir;
        url = new URL(urlString);
        rootDirectory = url.getHost();
        pagePath = URLDecoder.decode(url.getFile(), CHARSET);
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()
        );
    }

    public void parsePage() throws IOException {
        File mainPage = createDirsAndReturnFile(pagePath);
        mainPage.createNewFile();
        FileUtils.copyURLToFile(url, mainPage);
        Document doc = Jsoup.parse(mainPage, CHARSET);

        Elements links = doc.select("link[type=text/css]");
        save(links, TypeToSave.CSS);

        links = doc.select("img[src]");
        save(links, TypeToSave.IMG);
    }

    public enum TypeToSave {
        CSS,
        IMG
    }

    private void save(final Elements links, TypeToSave typeToSave) throws IOException {
        final boolean isCss = (typeToSave == TypeToSave.CSS);
        final boolean isImg = (typeToSave == TypeToSave.IMG);

        List<Runnable> runnables = new ArrayList<>();

        for (final Element link : links) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    String urlStringFromLink = "";
                    String path;
                    File file = null;

                    if (isCss) {
                        urlStringFromLink = link.attr("href");
                        path = getPathFromLink(urlStringFromLink, "css");
                        try {
                            file = createDirsAndReturnFile(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (isImg) {
                        urlStringFromLink = link.attr("src");
                        path = getPathFromLink(urlStringFromLink, null);
                        try {
                            file = createDirsAndReturnFile(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        if (file != null) {
                            file.createNewFile();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String sub = urlStringFromLink.substring(0, 4);
                    if (!(sub.equals("http"))) {
                        urlStringFromLink = "http:" + urlStringFromLink;
                    }
                    URL urlFromLink = null;
                    try {
                        urlFromLink = new URL(urlStringFromLink);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    if (isCss) {
                        try {
                            if (urlFromLink != null && file != null) {
                                FileUtils.copyURLToFile(urlFromLink, file);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (isImg) {
                        if (urlFromLink != null && file != null) {
                            try (
                                    InputStream is = urlFromLink.openStream();
                                    OutputStream os = new FileOutputStream(file)
                            ) {
                                byte[] b = new byte[2048];
                                int length;

                                while ((length = is.read(b)) != -1) {
                                    os.write(b, 0, length);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            runnables.add(task);
        }
        for (Runnable runnable : runnables) {
            threadPoolExecutor.execute(runnable);
        }
    }

    private String getPathFromLink(String link, String extension) {
        String resultString;
        String[] temp = link.split("/");
        int index = temp.length - 1;
        String elem = temp[index];
        if (extension != null) {
            int extIndex = elem.lastIndexOf(extension);
            int extSize = extension.length();
            resultString = "/" + elem.substring(0, extIndex + extSize);
        } else {
            resultString = elem;
        }

        while (index > 0) {
            index--;
            elem = temp[index];
            if (elem.equals(rootDirectory) || elem.equals("..")) {
                break;
            }
            resultString = "/" + elem + resultString;
        }
        return resultString;
    }


    private File createDirsAndReturnFile(String path) throws IOException {
        String filePath = dir + rootDirectory + path;
        File file = new File(filePath);

        int pos = filePath.lastIndexOf('/') + 1;
        String fileDirectories = filePath.substring(0, pos);
        File directories = new File(fileDirectories);
        directories.mkdirs();

        return file;
    }
}

