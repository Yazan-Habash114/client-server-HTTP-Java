package httpClient;

import static httpClient.ClientInteractWindow.statusTextArea;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Yazan Habash
 */
public class DownloadUploadImage implements DownloadUpload {

    private String selectedImage;
    
    public DownloadUploadImage() {
        
    }

    public DownloadUploadImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    @Override
    public void downloadPHP(String url) {
        try {
            URL u = new URL(url);
            OutputStream os;
            InputStream is;

            HttpURLConnection myConn = (HttpURLConnection) u.openConnection();
            myConn.setDoOutput(true);
            myConn.setRequestMethod("POST");
            myConn.setDoInput(true);
            myConn.setRequestProperty("Content-Type", CONTENT_STR);
            myConn.setUseCaches(false);
            String dataStr = URLEncoder.encode("download_image", "UTF-8") + "=" + selectedImage;
            try (BufferedOutputStream out = new BufferedOutputStream(myConn.getOutputStream())) {
                out.write(dataStr.getBytes());
            }
            String SS = "";

            if (myConn.getResponseCode() == 404) {
                System.out.println("error!");
            }

            byte tmp[] = new byte[1024];
            is = myConn.getInputStream();
            ByteArrayOutputStream O = new ByteArrayOutputStream();

            int n = 0;
            int b = -1;
            FileOutputStream myWriter = new FileOutputStream("C:/Users/HP/" + selectedImage);
            if (myConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myConn.getInputStream()))) {
                    while ((n = is.read(tmp)) != -1) {
                        O.write(tmp, 0, n);
                    }

                    byte[] r = O.toByteArray();
                    myWriter.write(r);
                    myWriter.close();
                    is.close();
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void downloadServlet(String url) {
        try {
            URL u = new URL(url);
            OutputStream os;
            InputStream is;

            HttpURLConnection myConn = (HttpURLConnection) u.openConnection();
            myConn.setDoOutput(true);
            myConn.setRequestMethod("POST");
            myConn.setDoInput(true);
            myConn.setRequestProperty("Content-Type", CONTENT_STR);
            myConn.setUseCaches(false);
            String dataStr = URLEncoder.encode("download_image", "UTF-8") + "=" + selectedImage;
            try (BufferedOutputStream out = new BufferedOutputStream(myConn.getOutputStream())) {
                out.write(dataStr.getBytes());
            }
            String SS = "";

            if (myConn.getResponseCode() == 404) {
                System.out.println("error!");
            }

            int b = -1;
            File file = new File("C:/Users/HP/" + selectedImage);
            FileOutputStream myWriter = new FileOutputStream(file);
            if (myConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myConn.getInputStream()))) {
                    while ((b = bufferedReader.read()) != -1) {
                        SS = SS + (char) b;
                        myWriter.write(b);
                    }
                    myWriter.close();
                }
                ClientInteractWindow.statusTextArea.setText(SS);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void uploadImage() {
        try {
            OutputStream os;
            InputStream is;
            JFileChooser choose = new JFileChooser();
            choose.showOpenDialog(null);
            File f = choose.getSelectedFile();
            String filename = f.getAbsolutePath();
            ClientInteractWindow.imgTF.setText(filename);
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(filename);
            FileInputStream fileInputStream = new FileInputStream(sourceFile);

            URL url = null;
            if (ClientInteractWindow.urlTF.getText().compareTo("Servlet") == 0) {
                url = new URL("http://localhost:8081/network2_http_s/upload_image");
            } else {
                url = new URL(ClientInteractWindow.urlTF.getText());
            }

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_image", filename);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=uploaded_image;filename="
                    + filename + "" + lineEnd);

            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            int serverResponseCode = 0;
            // Send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            int b = -1;
            dos.close();
            String SS = "";
            if (conn.getResponseCode() == 404) {
                System.out.println("error!");
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    while ((b = bufferedReader.read()) != -1) {
                        SS = SS + (char) b;
                    }
                }
                BufferedImage image = ImageIO.read(new File(filename));
                ImageIcon icon = new ImageIcon(image);
                int h = icon.getIconHeight();
                int w = icon.getIconWidth();
                if (h > 220) {
                    h = 220;
                }
                if (w > 310) {
                    w = 310;
                }
                icon = new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
                ClientInteractWindow.iconImage.setIcon(icon);
            }
            statusTextArea.setText(SS);
            ClientInteractWindow.imageCombo.removeAllItems();
            ClientInteractWindow.addImageName();

            //  Desktop.getDesktop().open(new java.io.File("text_file/rahaf.txt"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientInteractWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
