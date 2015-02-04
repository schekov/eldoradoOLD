package part1.task1;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class CharsetRewriter {

    private static final long DEFAULT_LIMIT_FOR_GZIP = 10_000_000;
    private static final String DEFAULT_OUTPUT_CHARSET = "UTF8";

    private String filePath;
    private long limitForGZIP;
    private String charSet;

    public CharsetRewriter(){
        new CharsetRewriter("NONE");
    }

    public CharsetRewriter(String file) {
        this(file,DEFAULT_OUTPUT_CHARSET,DEFAULT_LIMIT_FOR_GZIP);
    }

    public CharsetRewriter(String file, String charSet, long limitForGZIPCompression) {
        this.filePath = file;
        this.charSet = charSet;
        this.limitForGZIP = limitForGZIPCompression;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getLimitForGZIP() {
        return limitForGZIP;
    }

    public void setLimitForGZIP(long limitForGZIP) {
        this.limitForGZIP = limitForGZIP;
    }

    public void changeCharset(String filePath) throws IOException {
        this.filePath = filePath;
        changeCharset();
    }

    public void changeCharset() throws IOException {
        File file = new File(filePath);
        final String fileNameWithExt = file.getName();
        final String fileDir = file.getParent();

        int pos = fileNameWithExt.lastIndexOf('.');
        final String fileNameWithoutExt = fileNameWithExt.substring(0, pos);
        final String fileExt = fileNameWithExt.substring(pos);

        File rewrittenFile = new File(fileDir, fileNameWithoutExt + charSet + fileExt);
        rewrittenFile.createNewFile();

        try (
                InputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = new FileOutputStream(rewrittenFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "CP1251"));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, charSet))

        ) {
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
            if( rewrittenFile.length() > limitForGZIP){
                File compressedFile = new File (fileDir, fileNameWithoutExt + charSet + ".gz");
                compressToGZIP(rewrittenFile,compressedFile);
            }
        }
    }

    public void compressToGZIP(File file, File fileForGZIP) throws IOException {
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(fileForGZIP))
        ){
            byte[] buffer = new byte[2048];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                gzipOutputStream.write(buffer, 0, len);
            }
            file.deleteOnExit();
        }

    }
}
